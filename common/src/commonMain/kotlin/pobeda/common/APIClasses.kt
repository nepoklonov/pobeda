package pobeda.common

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val fileType: String,
    val originalFileName: String,
    val namePrefix: String
)