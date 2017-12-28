package server.web.resource

import commons.Code
import org.restlet.data.Status
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
        val dir = File("storage/content/${getAttribute("name").replace("%20", " ")}")
        if(!dir.exists()){
            status = Status(Code.NOT_FOUND)
            return ""
        }
        val list = dir.listFiles().map { it.name }
        return gson.toJson(list.toTypedArray(), Array<String>::class.java)
    }
}