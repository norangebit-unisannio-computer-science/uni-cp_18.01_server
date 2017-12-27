package server.web.resource

import org.restlet.resource.Get
import org.restlet.resource.ServerResource
import java.io.File


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 27/12/17
 */

class Photos: ServerResource() {

    @Get
    fun getPhoto(): String{
        val dir = File("storage/content/")
        if(!dir.exists())
            return "null"
        dir.listFiles().forEach { println(it.name) }
        val list = dir.listFiles().filter {
            it.name.startsWith(getAttribute("name").replace("%20", " "))
        }.map { it.name }
        return gson.toJson(list.toTypedArray(), Array<String>::class.java)
    }
}