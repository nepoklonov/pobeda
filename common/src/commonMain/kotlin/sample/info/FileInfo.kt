package sample.info

class FileInfo {
    companion object {
        private operator fun String.div(other: String): String {
            return this + "/" + other
        }
        const val yamlDir = "yaml"
        val yamlList = setOf(
                "resources",
                "smi",
                "symbols",
                "team",
                "contacts",
                "logos"
        )
        const val documentsDir = "/documents"
        val officialPDF = documentsDir / "official.pdf"
    }

    class Image(val folder: String, val filename: String, val extension : String, val width: Int? = null, val height: Int? = null) {
        val imageList = mutableListOf<Image>()
        val src: String
            get() = folder / filename + "." + extension
        companion object {
            val imagesDir = "/images"
            val designImagesDir = imagesDir / "design"
            val contactsImagesDir = imagesDir / "contacts"
            val resourcesImagesDir = imagesDir / "resources"
            val symbolsImagesDir = imagesDir / "symbols"
            val smiImagesDir = imagesDir / "smi"
            val teamImagesDir = imagesDir / "team"
            val partnersImagesDir = imagesDir / "partners"

            val mainBackgroundImage = Image(designImagesDir, "back", "jpg", width = General.width)
            val mainLogo = Image(designImagesDir, "main-logo", "png", width = (353 * General.scale).toInt())
            val mapImage = Image(designImagesDir, "map", "png")
            val mapWithBackImage = Image(designImagesDir, "map-with-back", "png", width = (1576 * General.scale).toInt())
            val defenderImage = Image(designImagesDir, "defender", "png", width = (330 * General.scale).toInt())
            val arshinovaMainImage = Image(designImagesDir, "arshinova", "png", width = (330 * General.scale).toInt())
        }
        init {
            imageList.add(this)
        }
    }
}