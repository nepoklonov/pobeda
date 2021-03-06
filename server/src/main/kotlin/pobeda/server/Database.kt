package pobeda.server

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import pobeda.common.ModelField
import pobeda.common.Participant
import pobeda.common.div
import pobeda.common.dot
import pobeda.common.interpretation.FileRef
import pobeda.common.interpretation.PlanarSize
import pobeda.common.interpretation.Scale
import pobeda.common.interpretation.ScaleType.INSIDE
import pobeda.common.interpretation.ScaleType.OUTSIDE
import pobeda.common.interpretation.x
import pobeda.server.ModelFieldType.*
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

enum class ModelFieldType {
    INT, BOOLEAN, STRING, TEXT
}

class ModelFieldData(
    val name: String, val type: ModelFieldType, val title: String,
    val nullable: Boolean, val isPrimaryKey: Boolean, val autoIncremented: Boolean
)

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
    ModelFieldData(
        prop.name,
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


    fun getColumn(s: String) = columns.first { it.name == s }

    fun ResultRow.toMap(): Map<String, Any> {
        return model.fields.associate {
            it.name to this[getColumn(it.name)]!!
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

@Serializable
data class IV(
    val originalSrc: String,
    val src: String,
    val width: Int,
    val height: Int,
    val isOriginal: Boolean,
    val url: String
)

object ImageVersions : IntIdTable() {
    val originalSrc = varchar("originalSrc", 1024)
    val src = varchar("src", 1024)
    val width = integer("width")
    val height = integer("height")
    val isOriginal = bool("isOriginal")
    val storage = varchar("storage", 256).default("server")
    val url = text("url")
}

fun readLocalProperties(): Properties {
    val localPropertiesFile = PathResolver.getResource("local.properties")
    return Properties().apply {
        load(localPropertiesFile.openStream())
    }
}

object YandexCredentials {
    private val properties = readLocalProperties()
    val token = properties["yandex_token"] as String
}

object AdminCredentials {
    private val properties = readLocalProperties()
    val secret = properties["admin_secret"] as String
}

object EmailCredentials {
    private val properties = readLocalProperties()
    val email = properties["email"] as String
    val password = properties["email_password"] as String
}

object DB {
    private val properties = readLocalProperties()
    private val ip = properties["db_url"] as String
    val url: String = "jdbc:postgresql://$ip/pobeda"
    val user: String = properties["db_user"] as String
    val password: String = properties["db_password"] as String
}

inline fun <T> database(crossinline block: Transaction.() -> T): T {
    Database.connect(
        DB.url,
        driver = "org.postgresql.Driver",
        user = DB.user,
        password = DB.password
    )
    return transaction {
        addLogger(StdOutSqlLogger)
        block()
    }
}


fun initDB() = database {
    SchemaUtils.createMissingTablesAndColumns(ImageVersions)
    SchemaUtils.createMissingTablesAndColumns(participantTable)
}

@Suppress("UNCHECKED_CAST")
fun addParticipant(participant: Participant): MutableList<String> = database {
    val okList = mutableListOf<String>()
    participantTable.insert(participant)
    okList += "ok"
    okList += participantTable.select {
        participantTable.columns.first { it.name == "fileName" } as Column<String> eq
                participant.fileName
    }.first()[participantTable.columns.first { it.name == "id" }].toString()
    okList
}

fun chooseBestImageVersionSize(id: Int, info: List<Pair<PlanarSize, String>>, width: Int, height: Int): String {
    var resultSize = PlanarSize(-1, -1)
    var resultUrl = ""

    for (version in info) {
        val url = version.second
        val w = version.first.width
        val h = version.first.height
        if (resultSize.width < width && resultSize.height < height &&
            w > resultSize.width && h > resultSize.height
        ) {
            resultSize = w x h
            resultUrl = url
        } else if (resultSize.width >= w && resultSize.height >= h &&
            w >= width && h >= height
        ) {
            resultSize = PlanarSize(w, h)
            resultUrl = url
        }
    }
    return resultUrl
}

@Suppress("UNCHECKED_CAST")
fun getAllImages(width: Int, height: Int, page: Int, size: Int): List<String> = database {
    participantTable.innerJoin(ImageVersions,
        { participantTable.getColumn("fileName") as Column<String> },
        { originalSrc })
        .slice(
            participantTable.getColumn("id"),
            ImageVersions.url,
            ImageVersions.width,
            ImageVersions.height,
        ).select { ImageVersions.originalSrc eq participantTable.getColumn("fileName") }
        .orderBy(participantTable.getColumn("id"), SortOrder.DESC)
        .limit(size, page * size)
        .groupBy(
            keySelector = {
                it[participantTable.getColumn("id") as Column<Int>]
            },
            valueTransform = {
                it[ImageVersions.width] x it[ImageVersions.height] to it[ImageVersions.url]
            }
        ).map { (id, info) ->
            chooseBestImageVersionSize(id, info, width, height)
        }
}

fun addImageVersions(originalPath: String) {
    database {
        SchemaUtils.create(ImageVersions)
        val originalImageFile = File(originalPath)
        val pureName = originalImageFile.nameWithoutExtension
//        val ext = originalImageFile.extension
        val sizes = mutableListOf(
            500 x 500 to OUTSIDE,
            400 x 400 to OUTSIDE,
            200 x 200 to OUTSIDE,
            100 x 100 to OUTSIDE
        )
        val originalImage = ImageIO.read(originalImageFile)


        val sendResult = sendToYandex(originalImageFile, originalPath)
        val originalStorage = sendResult.storage.also {
            if (it == "yandex") originalImageFile.delete()
        }

        ImageVersions.insert {
            it[originalSrc] = originalPath
            it[src] = originalPath
            it[width] = originalImage.width
            it[height] = originalImage.height
            it[isOriginal] = true
            it[storage] = originalStorage
            it[url] = sendResult.url
        }

        sizes.forEach { tr ->
            if ((tr.second == INSIDE && (originalImage.width > tr.first.width || originalImage.height > tr.first.height)) ||
                (tr.second == OUTSIDE && (originalImage.width > tr.first.width && originalImage.height > tr.first.height))
            ) {
                val newImage = scaleImageByRect(originalImage, tr.first, tr.second)
                val newDir = "uploads/images/copies" / pureName
                File(newDir).let { d -> if (!d.exists()) d.mkdirs() }
                val newPath = newDir / newImage.run { PlanarSize(width, height).toString() } dot "png"
                val newFile = File(newPath)

                ImageIO.write(newImage, "PNG", newFile)

                val sendResultVersion = sendToYandex(newFile, newPath)
                val imageStorage = sendResultVersion.storage.also {
                    if (it == "yandex") {
                        newFile.delete()
                        if (newFile.parentFile.listFiles()?.toList()?.isEmpty() == true) {
                            newFile.parentFile.delete()
                        }
                    }
                }

                ImageVersions.insert {
                    it[originalSrc] = originalPath
                    it[src] = newPath
                    it[width] = newImage.width
                    it[height] = newImage.height
                    it[isOriginal] = false
                    it[storage] = imageStorage
                    it[url] = sendResultVersion.url
                }
            }
//            println("added iv")
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun getOpenParticipantInfo(src: String, width: Int, height: Int, all: Boolean): List<String> {
    val list = mutableListOf<String>()
    database {
        var originalUrl = ""
        val originalSrc = ImageVersions.select { ImageVersions.src eq src or (ImageVersions.url eq src) }.first().let {
            originalUrl = it[ImageVersions.url]
            it[ImageVersions.originalSrc]
        }
        var resultSrc = ""
        var resultUrl = ""
        var resultSize = PlanarSize(-1, -1)
        for (version in ImageVersions.select { ImageVersions.originalSrc eq originalSrc }) {
            val s = version[ImageVersions.src]
            val u = version[ImageVersions.url]
            val w = version[ImageVersions.width]
            val h = version[ImageVersions.height]
            if (resultSize.width < width && resultSize.height < height &&
                w > resultSize.width && h > resultSize.height
            ) {
                resultSize = PlanarSize(w, h)
                resultUrl = u
                resultSrc = s
            } else if (resultSize.width >= w && resultSize.height >= h &&
                w >= width && h >= height
            ) {
                resultSize = PlanarSize(w, h)
                resultUrl = u
                resultSrc = s
            }
            println("-- $resultSrc $resultUrl $w $h $resultSize $width $height")
        }
        list.add(resultUrl)
        if (all) {
            val participant =
                participantTable.select { participantTable.columns.first { it.name == "fileName" } as Column<String> eq originalSrc }
                    .first()
            list += participant[participantTable.columns.first { it.name == "surname" } as Column<String>]
            list += participant[participantTable.columns.first { it.name == "name" } as Column<String>]
            list += participant[participantTable.columns.first { it.name == "age" } as Column<Int>].toString()
            list += participant[participantTable.columns.first { it.name == "city" } as Column<String>]
            list += participant[participantTable.columns.first { it.name == "title" } as Column<String>]
            list += originalUrl
        }
    }
    println(list.toString())
    return list
}