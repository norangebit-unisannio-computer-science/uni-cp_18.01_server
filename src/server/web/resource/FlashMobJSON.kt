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

import com.google.gson.GsonBuilder
import commons.Code
import commons.FlashMob
import commons.FlashMobException
import org.restlet.data.Status
import org.restlet.resource.*
import server.backend.wrapper.instance
import server.web.frontend.Role


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */

class FlashMobJSON: ServerResource(){

    @Get
    fun getFlashMob(): String?{
        try {
            return gson.toJson(instance.get(getAttribute("name").replace("%20", " ")),
                    FlashMob::class.java)
        }catch (e: FlashMobException){
            status = Status(e.code, e.message)
            return gson.toJson(e, FlashMobException::class.java)
        }
    }

    @Post
    fun post(payload: String){
      if (!isInRole(Role.SU))
          throw ResourceException(Status.CLIENT_ERROR_FORBIDDEN)
        instance.update(gson.fromJson(payload, FlashMob::class.java))
        status = Status(Code.POST_OK)
    }

    @Put
    fun put(payload: String){
        if (!isInRole(Role.SU))
            throw ResourceException(Status.CLIENT_ERROR_FORBIDDEN)
        try {
            instance.add(gson.fromJson(payload, FlashMob::class.java))
            status = Status(Code.PUT_OK)
        }catch (e: FlashMobException){ status = Status(e.code, e.message) }
    }

    @Delete
    fun deleteFlashMob(){
        if (!isInRole(Role.SU))
            throw ResourceException(Status.CLIENT_ERROR_FORBIDDEN)
        try {
            instance.remove(getAttribute("name").replace("%20", " "))
            status = Status(Code.DELETE_OK)
        }catch (e: FlashMobException){ status = Status(e.code, e.message) }
    }
}

val gson = GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").create()