package pobeda.server

import java.net.URL

object PathResolver {
    fun getResource(path: String): URL = this::class.java.classLoader.getResource(path)!!
}