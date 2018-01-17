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