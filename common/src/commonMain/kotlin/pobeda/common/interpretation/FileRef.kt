package pobeda.common.interpretation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import pobeda.common.*
import pobeda.common.interpretation.DirRef.Companion.images
import pobeda.common.interpretation.DirRef.Companion.yaml

class DirRef private constructor(private val parent: DirRef? = null, val name: String) {
    val path: String
        get() = parent?.run { path / name } ?: name

    companion object {
        val root = DirRef(null, "")
        val yaml = root / "yaml"
        val images = root / "images"
    }

    operator fun div(other: String) = DirRef(this.takeIf { it != root }, other)
    infix fun file(other: String) = FileRef(this, other)
}

fun YamlRef.getFileRefByName() = yaml file yamlName dot "yaml"

fun getYamlRefByName(name: String): YamlRef {
    YamlRef.values().forEach {
        if (it.name == name) {
            return it
        }
    }
    throw IllegalArgumentException()
}

enum class YamlRef(val yamlName: String, val serializer: KSerializer<out Any>) {
    ResourcesYaml("resources", Resource.serializer().list),
    SymbolsYaml("symbols", String.serializer().list),
    TeamYaml("team", Team.serializer()),
    SmiYaml("smi", Smi.serializer()),
    ContactsYaml("contacts", Contact.serializer().list),
    LogosYaml("logos", Logo.serializer().list);
}

object ImageDirs {
    val design = images / "design"
    val contacts = images / "contacts"
    val partners = images / "partners"
    val resources = images / "resources"
    val smi = images / "smi"
    val symbols = images / "symbols"
    val team = images / "team"
}

data class FileRef(val dir: DirRef, val fileName: String) {
    val path: String
        get() = dir.path / fileName
}

infix fun FileRef.dot(ext: String) = copy(fileName = "$fileName.$ext")