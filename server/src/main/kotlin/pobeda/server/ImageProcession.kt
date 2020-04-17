package pobeda.server

import pobeda.common.interpretation.PlanarSize
import pobeda.common.interpretation.ScaleType
import java.awt.RenderingHints
import java.awt.image.BufferedImage

inline fun getScaledImage(src: BufferedImage, w: Int = src.width, h: Int = src.height
                          , transform: (PlanarSize, PlanarSize) -> PlanarSize
                          = { _, rectSize -> rectSize }): BufferedImage {
    val size = transform(PlanarSize(src.width, src.height), PlanarSize(w, h))
    val result = BufferedImage(size.width, size.height, BufferedImage.TRANSLUCENT)
    val g2 = result.createGraphics()
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    g2.drawImage(src, 0, 0, size.width, size.height, null)
    g2.dispose()

    return result
}

//fun pobeda.server.getScaledImage(src: BufferedImage, coefficient: Double) =
//    pobeda.server.getScaledImage(src, (src.width * coefficient).toInt(), (src.height * coefficient).toInt())

fun scaleImageByRect(src: BufferedImage, size: PlanarSize, type: ScaleType) = getScaledImage(src, size.width, size.height) { srcSize, rectSize ->
    val factor = srcSize.width.toDouble() / srcSize.height.toDouble()
    var finalWidth = rectSize.width
    var finalHeight = rectSize.height
    if (type.flag xor (srcSize.run { width / height } > rectSize.run { width / height })) {
        finalHeight = (rectSize.width / factor).toInt()
    } else {
        finalWidth = (rectSize.height * factor).toInt()
    }
    PlanarSize(finalWidth, finalHeight)
}

//fun rotateClockwise90(src: BufferedImage): BufferedImage {
//    val width = src.width
//    val height = src.height
//
//    val result = BufferedImage(height, width, src.type)
//
//    val graphics2D = result.createGraphics()
//    graphics2D.translate((height - width) / 2, (height - width) / 2)
//    graphics2D.rotate(Math.PI / 2, (height / 2).toDouble(), (width / 2).toDouble())
//    graphics2D.drawRenderedImage(src, null)
//
//    return result
//}

//private fun cropImage(src: BufferedImage, w: Int, h: Int): BufferedImage {
//    val width = src.width.toDouble()
//    val height = src.height.toDouble()
//
//    return when {
//        w / width < h / height -> {
//            val finalW = ((height * w) / h).toInt()
//            val finalH = height.toInt()
//
//            val x = ((width - finalW) / 2).toInt()
//            val y = 0
//            src.getSubimage(x, y, finalW, finalH)
//        }
//        w / width > h / height -> {
//            val finalW = width.toInt()
//            val finalH = ((width * h) / w).toInt()
//
//            val x = 0
//            val y = ((height - finalH) / 2).toInt()
//            src.getSubimage(x, y, finalW, finalH)
//        }
//        else -> src
//    }
//}