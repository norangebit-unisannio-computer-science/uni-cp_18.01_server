package server.web.resource

import commons.Code
import org.restlet.data.MediaType
import org.restlet.data.Status
import org.restlet.resource.Get
import org.restlet.resource.ServerResource
import org.restlet.resource.ResourceException
import java.io.File
import java.io.FileOutputStream
import org.restlet.representation.Representation
import org.restlet.resource.Post
import server.web.frontend.Role
import org.restlet.representation.FileRepresentation
import java.io.IOException


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 23/12/17
 */

class Photo: ServerResource(){

    @Get
    fun getPhoto(): FileRepresentation?{
        try {
            val payload = FileRepresentation(File(System.getProperty("user.dir") + "/storage/content/" +
                    "${getAttribute("name").replace("%20", " ")}/${getAttribute("id")}"),
                    MediaType.IMAGE_ALL)
            return payload
        }catch (e: IOException){
            status = Status(Code.NOT_FOUND)
            return null
        }
        //TODO fix this
    }

    @Post
    @Throws(ResourceException::class)
    fun upload(entity: Representation){
        try {
            val f = File(System.getProperty("user.dir")+"/storage/content/"
                    +getAttribute("name").replace("%20", " "))
            f.mkdir()
            entity.write(FileOutputStream(f.absolutePath +"/"+getAttribute("id")+".jpg"))
        } catch (e: Exception) {
            throw ResourceException(e)
        }
        status = Status(Code.POST_OK)
    }

}