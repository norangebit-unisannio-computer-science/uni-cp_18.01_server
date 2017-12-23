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
            val payload = FileRepresentation(File(System.getProperty("user.dir") + "/storage/content/" + fileName()),
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
        if (isInRole(Role.UNAUTHENTICATED)) {
            throw ResourceException(Status.CLIENT_ERROR_FORBIDDEN)
        }
        try {
            entity.write(FileOutputStream(System.getProperty("user.dir") + "/storage/content/" + fileName()))
        } catch (e: Exception) {
            throw ResourceException(e)
        }
        status = Status(Code.POST_OK)
    }

    fun fileName() = "${getAttribute("name")}_${getAttribute("id")}.jpg".replace("%20", " ")
}