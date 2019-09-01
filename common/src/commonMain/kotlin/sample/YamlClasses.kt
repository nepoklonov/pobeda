package sample

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
    val files: MutableList<String>,
    val photos: MutableList<String>
)

@Serializable
data class Contacts(
    val name: String,
    val logo: String,
    val link: String,
    val text: String
)

@Serializable
data class Logos(
    val name: String,
    val logo: String,
    val link: String
)
