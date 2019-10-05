package sample

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import sample.info.InputField

object Participants : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val time = varchar("time", 255)
    val surname = varchar("surname", 255)
    val name = varchar("name", 255)
    val age = integer("age")
    val city = varchar("city", 255)
    val school = varchar("school", 255)
    val email = varchar("email", 255)
    val title = varchar("title", 255)
    val fileName = varchar("oldFile", 255)
    val oldFileName = varchar("oldFileName", 255)
    val supervisor = bool("supervisor")
    val supervisorFIO = varchar("supervisorFIO", 255).nullable()
    val supervisorContacts = varchar("supervisorContacts", 255).nullable()
    val essay = bool("essay")
    val essayTitle = varchar("essayTitle", 255).nullable()
    val essayFileName = varchar("oldEssayFile", 255).nullable()
    val essayOldFileName = varchar("essayOldFileName", 255).nullable()
    val essayText = text("essayText").nullable()
}

fun addParticipant(map: Map<String, InputField>): String {
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
//    var s = ""
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Participants)
        Participants.insert {
            map.forEach { _, value ->
                InputField.let { f ->
                    if (value.willBeCollected) when (value) {
                        f.time -> it[time] = value.value
                        f.name -> it[name] = value.value
                        f.surname -> it[surname] = value.value
                        f.age -> it[age] = value.value.toInt()
                        f.city -> it[city] = value.value
                        f.school -> it[school] = value.value
                        f.email -> it[email] = value.value
                        f.title -> it[title] = value.value
                        f.file -> it[fileName] = value.value
                        f.oldFile -> it[oldFileName] = value.value
                        f.supervisor -> it[supervisor] = value.value == "on"
                        f.supervisorFIO -> it[supervisorFIO] = value.value
                        f.supervisorContacts -> it[supervisorContacts] = value.value
                        f.essay -> it[essay] = value.value == "on"
                        f.essayTitle -> it[essayTitle] = value.value
                        f.essayFile -> it[essayFileName] = value.value
                        f.oldEssayFile -> it[essayOldFileName] = value.value
                        f.essayTitle -> it[essayTitle] = value.value
                        f.essayText -> it[essayText] = value.value
                    }
                }
            }
        }
//        for (participant in Participants.selectAll()) {
//            Participants.columns.forEach { column -> s += "${participant[column].toString()} " }
//            s += "\n"
//        }
    }
    return "ok"
}

fun getAllImages(): MutableList<String> {
    val list = mutableListOf<String>()
    Database.connect("jdbc:h2:file:./data/main", driver = "org.h2.Driver")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Participants)
        for (participant in Participants.selectAll().orderBy(Participants.id)) {
            list.add(participant[Participants.fileName])
        }
    }
    return list
}