package server.web.resource

import org.restlet.resource.Get
import org.restlet.resource.ServerResource
import server.backend.wrapper.instance


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */

class SizeJSON: ServerResource(){

    @Get
    fun getSize(): String = gson.toJson(instance.size(), Int::class.java)
}