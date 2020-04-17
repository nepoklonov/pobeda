package pobeda.server

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import pobeda.common.ModelField
import pobeda.common.Participant
import pobeda.common.div
import pobeda.common.dot
import pobeda.common.interpretation.PlanarSize
import pobeda.common.interpretation.ScaleType.INSIDE
import pobeda.common.interpretation.ScaleType.OUTSIDE
import pobeda.common.interpretation.x
import pobeda.server.ModelFieldType.*
import java.io.File
import javax.imageio.ImageIO
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

enum class ModelFieldType {
    INT, BOOLEAN, STRING, TEXT
}

class ModelFieldData(val name: String, val type: ModelFieldType, val title: String,
                     val nullable: Boolean, val isPrimaryKey: Boolean, val autoIncremented: Boolean)

class Model<T : Any>(val fields: List<ModelFieldData>, val kClass: KClass<T>)

fun <T : Any> Model<T>.toMap(instance: T): Map<String, String> {
    return fields.associate { field ->
        field.name to kClass.memberProperties.first {
            it.name == field.name
        }.get(instance).toString()
    }
}

fun <T : Any> KClass<T>.createModel() = Model(memberProperties.asSequence().mapNotNull {
    it.findAnnotation<ModelField>()?.to(it)
}.map { (annotation, prop) ->
    ModelFieldData(prop.name,
        when (prop.returnType.classifier) {
            Int::class -> INT
            String::class -> if (annotation.longText) TEXT else STRING
            Boolean::class -> BOOLEAN
            else -> error("Incorrect type")
        }, annotation.title, annotation.nullable, annotation.isPrimaryKey, annotation.autoIncremented
    )
}.toList(), this)

class ModelTable<T : Any>(private val model: Model<T>) : Table(model.kClass.simpleName!!) {
    init {
        model.fields.forEach {
            when (it.type) {
                INT -> integer(it.name)
                BOOLEAN -> bool(it.name)
                STRING -> varchar(it.name, 255)
                TEXT -> text(it.name)
            }.run {
                if (it.isPrimaryKey) primaryKey() else this
            }.run {
                if (it.autoIncremented) autoIncrement() else this
            }.run {
                if (it.nullable) nullable() else this
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun insert(modelInstance: T) {
        val fieldValues = model.toMap(modelInstance)
        insert { op ->
            model.fields.forEach { field ->
                val column = columns.first { it.name == field.name }
                if (!field.autoIncremented) {
                    when (field.type) {
                        INT -> {
                            op[column as Column<Int>] = fieldValues.getValue(field.name).toInt()
                        }
                        BOOLEAN -> {
                            op[column as Column<Boolean>] = fieldValues.getValue(field.name).toBoolean()
                        }
                        STRING, TEXT -> {
                            op[column as Column<String>] = fieldValues.getValue(field.name)
                        }
                    }
                }
            }
        }
    }
}

val participantModel = Participant::class.createModel()

val participantTable = ModelTable(participantModel)


object ImageVersions : Table() {
    val originalSrc = varchar("originalSrc", 255)
    val src = varchar("src", 255)
    val width = integer("width")
    val height = integer("height")
    val isOriginal = bool("isOriginal")
}

@Suppress("UNCHECKED_CAST")
fun addParticipant(participant: Participant): MutableList<String> {
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
    val okList = mutableListOf<String>()
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(participantTable)
        participantTable.insert(participant)
        okList += "ok"
        okList += participantTable.select {
            participantTable.columns.first { it.name == "fileName" } as Column<String> eq
                participant.fileName
        }.first()[participantTable.columns.first { it.name == "id" }].toString()
    }
    return okList
}

@Suppress("UNCHECKED_CAST")
fun getAllImages(width: Int, height: Int): List<String> {
    val list = mutableListOf<String>()
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(participantTable)
        for (participant in participantTable.selectAll().orderBy(participantTable.columns.first { it.name == "id" }, SortOrder.DESC)) {
            var resultSize = PlanarSize(-1, -1)
            var resultSrc = ""
            for (version in ImageVersions.select { ImageVersions.originalSrc eq participant[participantTable.columns.first { it.name == "fileName" } as Column<String>] }) {
                val src = version[ImageVersions.src]
                val w = version[ImageVersions.width]
                val h = version[ImageVersions.height]
                if (resultSize.width < width && resultSize.height < height &&
                    w > resultSize.width && h > resultSize.height) {
                    resultSize = PlanarSize(w, h)
                    resultSrc = src
                } else if (resultSize.width >= w && resultSize.height >= h &&
                    w >= width && h >= height) {
                    resultSize = PlanarSize(w, h)
                    resultSrc = src
                }
                println("$resultSrc $w $h $resultSize")
            }
            list.add(resultSrc)
        }
    }
    return list
}

suspend fun addImageVersions(originalPath: String) {
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(ImageVersions)
        val originalImageFile = File(originalPath)
        val pureName = originalImageFile.nameWithoutExtension
//        val ext = originalImageFile.extension
        val sizes = mutableListOf(
            500 x 500 to OUTSIDE,
            400 x 400 to OUTSIDE,
            200 x 200 to OUTSIDE,
            100 x 100 to OUTSIDE)
        val originalImage = ImageIO.read(originalImageFile)
        ImageVersions.insert {
            it[originalSrc] = originalPath
            it[src] = originalPath
            it[width] = originalImage.width
            it[height] = originalImage.height
            it[isOriginal] = true
        }
        sizes.forEach { tr ->
            if ((tr.second == INSIDE && (originalImage.width > tr.first.width || originalImage.height > tr.first.height)) ||
                (tr.second == OUTSIDE && (originalImage.width > tr.first.width && originalImage.height > tr.first.height))) {
                val newImage = scaleImageByRect(originalImage, tr.first, tr.second)
                val newDir = "uploads/images/copies" / pureName
                File(newDir).let { d -> if (!d.exists()) d.mkdirs() }
                val newPath = newDir / newImage.run { PlanarSize(width, height).toString() } dot "png"
                val newFile = File(newPath)

                ImageIO.write(newImage, "PNG", newFile)
                ImageVersions.insert {
                    it[originalSrc] = originalPath
                    it[src] = newPath
                    it[width] = newImage.width
                    it[height] = newImage.height
                    it[isOriginal] = false
                }
            }
            println("added iv")
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun getOpenParticipantInfo(src: String, width: Int, height: Int, all: Boolean): List<String> {
    val list = mutableListOf<String>()
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(participantTable)
        val originalSrc = ImageVersions.select { ImageVersions.src eq src }.first()[ImageVersions.originalSrc]
        var resultSrc = ""
        var resultSize = PlanarSize(-1, -1)
        for (version in ImageVersions.select { ImageVersions.originalSrc eq originalSrc }) {
            val s = version[ImageVersions.src]
            val w = version[ImageVersions.width]
            val h = version[ImageVersions.height]
            if (resultSize.width < width && resultSize.height < height &&
                w > resultSize.width && h > resultSize.height) {
                resultSize = PlanarSize(w, h)
                resultSrc = s
            } else if (resultSize.width >= w && resultSize.height >= h &&
                w >= width && h >= height) {
                resultSize = PlanarSize(w, h)
                resultSrc = s
            }
            println("-- $resultSrc $w $h $resultSize $width $height")
        }
        list.add(resultSrc)
        if (all) {
            val participant = participantTable.select { participantTable.columns.first { it.name == "fileName" } as Column<String> eq originalSrc }.first()
            list += participant[participantTable.columns.first { it.name == "surname" } as Column<String>]
            list += participant[participantTable.columns.first { it.name == "name" } as Column<String>]
            list += participant[participantTable.columns.first { it.name == "age" } as Column<Int>].toString()
            list += participant[participantTable.columns.first { it.name == "city" } as Column<String>]
            list += participant[participantTable.columns.first { it.name == "title" } as Column<String>]
            list += originalSrc
        }
    }
    println(list.toString())
    return list
}