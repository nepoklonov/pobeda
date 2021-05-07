package pobeda.common

import kotlinx.serialization.Serializable
import pobeda.common.interpretation.YamlRef

enum class Method(val methodName: String) {
    GetYaml("api/yaml/get"),
    GetImages("api/images/get-src"),
    FileUpload("api/load/file"),
    LoadForm("api/join/load/form"),
    // TODO: create Participant
    GetImageInfo("api/images/get-info"),
    ParticipantsGetAll("api/admin/get-all")
//    GetImageVersion("api/images/get-version")
}

@Serializable
sealed class Request(val method: Method) {

    @Serializable
    class GetYaml(val yamlRef: YamlRef) : Request(Method.GetYaml)

    @Serializable
    class ImagesGetInfo(val src: String, val width: Int, val height: Int, val all: Boolean) : Request(Method.GetImageInfo)

    @Serializable
    class ImagesGetAll(val width: Int, val height: Int, val page: Int, val size: Int) : Request(Method.GetImages)

    @Serializable
    class AllImages(val images: List<String>, val amount: Int)

    @Serializable
    class FileUpload(val filesData: List<FileData>) : Request(Method.FileUpload) {
        constructor(fileData: FileData) : this(listOf(fileData))
    }

    @Serializable
    class ParticipantsGetAll(val password: String, val from: Int) : Request(Method.ParticipantsGetAll)

    @Serializable
    class FormSend(val participant: Participant) : Request(Method.LoadForm)
}

enum class AnswerType {
    OK, WRONG, EXCEPTION
}

@Serializable
data class ParticipantAdminDTO(
    val id: Int,
    val imageSrc: String,
    val original: String
)

@Serializable
data class Answer<T>(val answerType: AnswerType, val body: T)