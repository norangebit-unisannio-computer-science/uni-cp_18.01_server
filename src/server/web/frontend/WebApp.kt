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
import org.restlet.data.Protocol
import org.restlet.routing.Router
import server.backend.wrapper.instance
import server.web.resource.FlashMobJSON
import server.web.resource.ListJSON
import server.web.resource.SizeJSON

import java.io.File
import java.io.FileNotFoundException
import java.util.Arrays
import java.util.Scanner

class WebApp : Application() {

    override fun createInboundRoot(): Restlet {
        val router = Router()

        router.attach("/size", SizeJSON::class.java)
        router.attach("/list", ListJSON::class.java)
        router.attach("/{name}", FlashMobJSON::class.java)

        return router
    }

    private inner class Settings {
        var port: Int = 0
        var storage_base_dir: String? = null
        var storage_base_file: String? = null
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val gson = Gson()
            var settings: Settings? = null
            try {
                val sc = Scanner(File("settings.json"))
                //settings = gson.fromJson(sc.nextLine(), Settings::class.java)
                sc.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                //System.exit(1)
            }

            instance.setStorage(System.getProperty("user.dir") + "/" + "storage/backend", "save.dat")
            instance.restore()

            System.err.println(Arrays.toString(instance.list()))

            try {
                // Create a new Component.
                val component = Component()
                // Add a new HTTP server listening on port defined in the settings file.
                component.servers.add(Protocol.HTTP, 8182)
                // Add an handler for static files
                component.clients.add(Protocol.FILE)

                // Attach the CarRegistryWebApplication application.
                component.defaultHost.attach(WebApp())

                // Start the component.
                component.start()
            } catch (e: Exception) {      // Something is wrong.
                e.printStackTrace()
            }

        }
    }
}
