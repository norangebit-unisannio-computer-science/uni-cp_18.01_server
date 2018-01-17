/*
 *       copyright (c)  2018 Raffaele Mignone
 *
 *        This file is part of  Server
 *
 *        Server is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *
 *        Server is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *
 *        You should have received a copy of the GNU General Public License
 *        along with Server.  If not, see <http://www.gnu.org/licenses/>.
 */

package server.web.resource

import commons.Code
import org.restlet.data.MediaType
import org.restlet.data.Status
import org.restlet.representation.FileRepresentation
import org.restlet.representation.Representation
import org.restlet.resource.Get
import org.restlet.resource.Post
import org.restlet.resource.ResourceException
import org.restlet.resource.ServerResource
import java.io.File
import java.io.FileOutputStream
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