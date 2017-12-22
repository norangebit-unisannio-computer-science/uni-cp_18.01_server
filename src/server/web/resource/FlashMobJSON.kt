package server.web.resource

import com.google.gson.Gson
import commons.Code
import commons.FlashMob
import commons.FlashMobException
import org.restlet.data.Status
import org.restlet.resource.*
import server.backend.wrapper.instance


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */

class FlashMobJSON: ServerResource(){

    @Get
    fun getFlashMob(): String?{
        try {
            return gson.toJson(instance.get(getAttribute("name").replace("%20", " ")), FlashMob::class.java)
        }catch (e: FlashMobException){
            status = Status(e.code, e.message)
            return null
        }
    }

    @Post
    fun post(payload: String){
        instance.update(gson.fromJson(payload, FlashMob::class.java))
        status = Status(Code.POST_OK)
    }

    @Put
    fun put(payload: String){
        try {
            instance.add(gson.fromJson(payload, FlashMob::class.java))
            status = Status(Code.PUT_OK)
        }catch (e: FlashMobException){ status = Status(e.code, e.message) }
    }

    @Delete
    fun deleteFlashMob(){
        try {
            instance.remove(getAttribute("name").replace("%20", " "))
            status = Status(Code.DELETE_OK)
        }catch (e: FlashMobException){ status = Status(e.code, e.message) }
    }
}

val gson = Gson()