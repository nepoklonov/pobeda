package pobeda.common.interpretation

import kotlinx.serialization.enumMembers
import pobeda.common.info.General
import pobeda.common.interpretation.ScaleType.INSIDE
import pobeda.common.interpretation.ScaleType.OUTSIDE

data class PlanarSize(val width: Int, val height: Int) {
    constructor(w: Number, h: Number) : this(w.toInt(), h.toInt())

    override fun toString(): String {
        return "${width.toInt()}x${height.toInt()}"
    }
}

enum class ScaleType(val flag: Boolean) {
    INSIDE(false),
    OUTSIDE(true)
}

infix fun Number.x(other: Number) = PlanarSize(this, other)

fun scaleByWidth(width: Number) = width.toInt() x 1 put OUTSIDE
fun scaleByHeight(height: Number) = 1 x height.toInt() put OUTSIDE

class Scale(val size: PlanarSize, val type: ScaleType)

infix fun PlanarSize.put(type: ScaleType) = Scale(this, type)

interface ScaleContainer {
    val scale: Scale
}

enum class ParticipantScale(override val scale: Scale) : ScaleContainer {
    Big(500 x 500 put OUTSIDE),
    Normal(250 x 250 put OUTSIDE),
    VerySmall(100 x 100 put OUTSIDE)
}

class SingleScale(override val scale: Scale) : ScaleContainer

fun SingleScale.asList() = listOf(this)

class Image<out T : ScaleContainer>(val original: FileRef, val scales: List<T>) {
    constructor(originalPath: FileRef, vararg scales: T) : this(originalPath, scales.asList())

    companion object {
        operator fun invoke(originalPath: FileRef, vararg scales: Scale): Image<SingleScale> {
            return Image(originalPath, scales.map(::SingleScale))
        }

        inline operator fun <reified T> invoke(originalPath: FileRef): Image<T> where T : ScaleContainer, T : Enum<T> {
            return Image(originalPath, *T::class.enumMembers())
        }
    }
}

object Images {
    val mainBackground: Image<SingleScale> =
        Image(ImageDirs.design file "back.jpg", scaleByWidth(General.width))

    val mainLogo: Image<SingleScale> =
        Image(ImageDirs.design file "main-logo.png", 350 x 350 put OUTSIDE)

    val mapImage: Image<SingleScale> =
        Image(ImageDirs.design file "map.png", 0 x 0 put INSIDE)

    val mapWithBack: Image<SingleScale> =
        Image(ImageDirs.design file "map-with-back.png", scaleByWidth(1576 * General.scale))

    val defender: Image<SingleScale> =
        Image(ImageDirs.design file "defender.png", scaleByWidth(330 * General.scale))

    val lady: Image<SingleScale> =
        Image(ImageDirs.design file "arshinova.png", scaleByWidth(330 * General.scale))
}

