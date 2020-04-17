package pobeda.common

import kotlin.annotation.AnnotationTarget.PROPERTY

@Target(PROPERTY)
annotation class ModelField(
    val title: String,
    val longText: Boolean = false,
    val nullable: Boolean = false,
    val autoIncremented: Boolean = false,
    val isPrimaryKey: Boolean = false
)
