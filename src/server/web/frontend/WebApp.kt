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
import org.restlet.data.Protocol
import org.restlet.routing.Router
import org.restlet.security.ChallengeAuthenticator
import org.restlet.security.MemoryRealm
import org.restlet.security.Role
import org.restlet.security.User
import server.backend.wrapper.instance
import server.web.frontend.Role.SU
import server.web.frontend.Role.UNAUTHENTICATED
import server.web.frontend.Role.USER
import server.web.resource.*

import java.io.File
import java.io.FileNotFoundException
import java.util.Arrays
import java.util.Scanner

object Role{
    val UNAUTHENTICATED = "unauthenticated"
    val USER = "authenticatated"
    val SU = "super user"
}

class WebApp : Application() {

    override fun createInboundRoot(): Restlet {
        val router = Router()

        val editGuard = createAuthenticator()
        editGuard.setNext(FlashMobJSON::class.java)

        val uploadGuard = createAuthenticator()
        uploadGuard.setNext(Photo::class.java)

        router.attach("/size", SizeJSON::class.java)
        router.attach("/list", ListJSON::class.java)
        router.attach("/{name}", editGuard)
        router.attach("/{name}/photo/{id}", uploadGuard)
        router.attach("/{name}/photos", Photos::class.java)

        return router
    }

    private fun createAuthenticator(): ChallengeAuthenticator {
        val guard = ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "Realm")

        val realm = MemoryRealm()

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
        val user = User("debug", "debug".toCharArray())
        realm.users.add(user)
        realm.map(user, Role.get(this, UNAUTHENTICATED))

        guard.verifier = realm.verifier
        guard.enroler = realm.enroler

        return guard
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
            } catch (e: FileNotFoundException) { }

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
