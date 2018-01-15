package server.backend.wrapper

import commons.FlashMob
import server.backend.Registry
import java.io.File
import java.io.IOException






/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */
 
object instance{
    private var rg = Registry()
    private var root = ""
    private var fileBase = ""

    @Synchronized fun get(name: String) = rg.get(name)

    @Synchronized fun add(fm: FlashMob){
        rg.add(fm)
        commit()
    }

    @Synchronized fun update(fm: FlashMob){
        rg.update(fm)
        commit()
    }

    @Synchronized fun remove(name: String){
        rg.remove(name)
        commit()
    }

    @Synchronized fun list() = rg.list()

    @Synchronized fun size() = rg.size()

    fun setStorage(root: String, fileBase: String){
        this.root = root
        this.fileBase = fileBase
    }

    private fun buildStorageFileExtension(): Int {
        val folder = File(root)
        var c: Int
        var max = -1
        folder.listFiles().forEach {
            if (it.name.substring(0, fileBase.length).equals(fileBase, ignoreCase = true)){
                try {
                    c = Integer.parseInt(it.name.substring(fileBase.length))
                }catch(e: NumberFormatException){ c = -1 }catch(e: StringIndexOutOfBoundsException){ c = -1 }
                if (c > max) max = c
            }
        }
        return max
    }

    fun commit() {
        val extension = buildStorageFileExtension()
        val fileName = root + "/" + fileBase + (extension + 1)
        try {
            rg.save(fileName)
        } catch (e: IOException) { System.err.println("Commit filed") }
    }

    fun restore() {
        val extension = buildStorageFileExtension()
        if (extension == -1) {
            System.err.println("No data to load - starting a new registry")
        } else {
            val fileName = root + "/" + fileBase + extension
            try {
                rg.load(fileName)
            } catch (e: ClassNotFoundException) {
                System.err.println("Restore filed - starting a new registry")
                rg = Registry()
            } catch (e: IOException) {
                System.err.println("Restore filed - starting a new registry")
                rg = Registry()
            }
        }
    }
}