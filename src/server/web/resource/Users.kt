package server.web.resource

import commons.Code
import org.restlet.data.Status
import org.restlet.resource.Get
import org.restlet.resource.ServerResource


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */

class Users : ServerResource(){

    @Get
    fun testLogin(): String{
        status = Status(Code.GET_OK)
        return "login"
    }
}