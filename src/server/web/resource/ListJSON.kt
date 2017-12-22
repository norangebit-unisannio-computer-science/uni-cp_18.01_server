package server.web.resource

import org.restlet.resource.Get
import org.restlet.resource.ServerResource
import server.backend.wrapper.instance


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */

class ListJSON : ServerResource(){

    @Get
    fun list(): String = gson.toJson(instance.list(), Array<String>::class.java)
}