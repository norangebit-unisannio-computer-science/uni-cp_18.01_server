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

package server.web.frontend

        /*
         *  Author: Raffaele Mignone
         *  Mat: 863/747
         *  Date: 13/12/17
         */

import com.google.gson.Gson
import org.restlet.Application
import org.restlet.Component
import org.restlet.Restlet
import org.restlet.data.ChallengeScheme
import org.restlet.data.Method
import org.restlet.data.Protocol
import org.restlet.routing.Router
import org.restlet.security.*
import org.restlet.security.Role
import server.backend.wrapper.instance
import server.web.frontend.Role.SU
import server.web.frontend.Role.USER
import server.web.resource.*
import java.io.File
import java.io.FileNotFoundException
import java.util.*

object Role{
    val USER = "authenticatated"
    val SU = "super user"
}

class WebApp : Application() {

    override fun createInboundRoot(): Restlet {
        val router = Router()

        val uploadProtectMethod = authorizeMethod(Method.GET)
        uploadProtectMethod.setNext(Photo::class.java)
        val uploadGuard = createAuthenticator()
        uploadGuard.next = uploadProtectMethod

        val editProtectMethod = authorizeMethod(Method.GET)
        editProtectMethod.setNext(FlashMobJSON::class.java)
        val editGuard = createAuthenticator()
        editGuard.next = editProtectMethod

        val loginGuard = createAuthenticator(false)
        loginGuard.setNext(Users::class.java)

        router.attach("/users", loginGuard)
        router.attach("/size", SizeJSON::class.java)
        router.attach("/list", ListJSON::class.java)
        router.attach("/{name}", editGuard)
        router.attach("/{name}/photo/{id}", uploadGuard)
        router.attach("/{name}/photos", Photos::class.java)

        return router
    }

    private fun createAuthenticator(optional: Boolean = true): ChallengeAuthenticator {
        val guard = ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "Realm")

        val realm = MemoryRealm()

        //admin users
        var sudoers: Array<User>? = null
        try {
            val sc = Scanner(File("sudoers.json"))
            sudoers = gson.fromJson(sc.nextLine(), Array<User>::class.java)
            sc.close()
        } catch (e: FileNotFoundException) { }
        sudoers?.forEach {
            realm.users.add(it)
            realm.map(it, Role.get(this, SU))
        }

        //normal users
        var users: Array<User>? = null
        try {
            val sc = Scanner(File("users.json"))
            users = gson.fromJson(sc.nextLine(), Array<User>::class.java)
            sc.close()
        } catch (e: FileNotFoundException) { }
        users?.forEach {
            realm.users.add(it)
            realm.map(it, Role.get(this, USER))
        }

        guard.verifier = realm.verifier
        guard.enroler = realm.enroler
        guard.isOptional = optional

        return guard
    }

    fun authorizeRole(vararg role: String): RoleAuthorizer{
        val roleAuth = RoleAuthorizer()
        role.forEach { roleAuth.authorizedRoles.add(Role.get(this, it)) }
        return roleAuth
    }

    fun authorizeMethod(vararg method: Method): MethodAuthorizer{
        val methodAuth = MethodAuthorizer()
        method.forEach { methodAuth.anonymousMethods.add(it) }
        methodAuth.authenticatedMethods.add(Method.GET)
        methodAuth.authenticatedMethods.add(Method.POST)
        methodAuth.authenticatedMethods.add(Method.PUT)
        methodAuth.authenticatedMethods.add(Method.DELETE)
        return methodAuth
    }

    private class Settings (var port: Int = 8182, var storage_base_dir: String = "storage/backend",
                                  var storage_base_file: String = "save.dat")

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val gson = Gson()
            var settings = Settings()
            try {
                val sc = Scanner(File("settings.json"))
                settings = gson.fromJson(sc.nextLine(), Settings::class.java)
                sc.close()
            } catch (e: FileNotFoundException) {
                System.err.println("Usate impostazioni di default")
            }

            instance.setStorage(System.getProperty("user.dir") + "/" + settings.storage_base_dir,
                    settings.storage_base_file)
            instance.restore()

            System.err.println(Arrays.toString(instance.list()))

            try {
                val component = Component()
                component.servers.add(Protocol.HTTP, settings.port)
                component.clients.add(Protocol.FILE)
                component.defaultHost.attach(WebApp())
                component.start()
            } catch (e: Exception) { e.printStackTrace() }

        }
    }
}
