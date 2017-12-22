package server.backend.wrapper.test

import commons.FlashMob
import commons.FlashMobException
import server.backend.wrapper.instance
import java.time.LocalTime


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */
 
fun main(args: Array<String>){
    instance.setStorage("storage/backend", "save.dat")
    instance.restore()
    instance.list().forEach { print(it+" - ") }
    println("\nsize ${instance.size()}")

    println("add titolo 5")
    try {
        instance.add(FlashMob("titolo 5", "path 5", LocalTime.of(21, 30), LocalTime.of(22, 0)))
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("add titolo 5")
    try {
        instance.add(FlashMob("titolo 5", "path 5", LocalTime.of(21, 30), LocalTime.of(22, 0)))
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("commit")
    instance.commit()

    println("update titolo 6")
    instance.update(FlashMob("titolo 6", "path 6", LocalTime.of(21, 30), LocalTime.of(22, 0)))
    instance.list().forEach { print(it+" - ") }

    println("\nrestore")
    instance.restore()
    println("size ${instance.size()}")
    instance.list().forEach { print(it+" - ") }

    println("\n"+instance.get("titolo 5").imagePath)

    println("remove 5")
    instance.remove("titolo 5")
    instance.list().forEach { print(it+" - ") }


}