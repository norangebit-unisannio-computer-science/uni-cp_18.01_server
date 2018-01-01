package server.web.resource

import com.google.gson.Gson
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
            return null
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

val gson = Gson()