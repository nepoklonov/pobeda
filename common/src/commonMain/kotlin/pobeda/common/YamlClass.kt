package pobeda.common

import kotlinx.serialization.Serializable

@Serializable
data class Resource(
    val logo: String,
    val link: String,
    val name: String,
    val text: String
)

@Serializable
data class TeamPerson(
    val position: Int,
    val fullName: String,
    val picture: String,
    val description: String,
    val email: String,
    val phone: String?,
    val area: String?
)

@Serializable
data class Team(
    val positions: List<String>,
    val team: List<TeamPerson>
)

@Serializable
data class Smi(
    var files: List<String>,
    val photos: List<String>
)

@Serializable
data class Contact(
    val name: String,
    val logo: String,
    val link: String,
    val text: String
)

@Serializable
data class Logo(
    val name: String,
    val logo: String,
    val link: String
)
