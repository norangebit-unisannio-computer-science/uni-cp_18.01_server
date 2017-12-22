package server.backend.test

import commons.FlashMob
import commons.FlashMobException
import server.backend.Registry
import java.io.IOException
import java.time.LocalTime


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */
 
fun main(args: Array<String>){
    val rg = Registry()
    println("print size ${rg.size()}")

    println("add titolo 1")
    rg.add(FlashMob("titolo 1", "path 1", LocalTime.of(21, 30), LocalTime.of(22, 0)))

    println("add titolo 1")
    try {
        rg.add(FlashMob("titolo 1", "path 1", LocalTime.of(21, 30), LocalTime.of(22, 0)))
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("update titolo 2")
    rg.update(FlashMob("titolo 2", "path 2", LocalTime.of(21, 30), LocalTime.of(22, 0)))
    println("update titolo 3")
    rg.update(FlashMob("titolo 3", "path 3", LocalTime.of(21, 30), LocalTime.of(22, 0)))


    rg.list().forEach { print(it+" - ") }

    println("\nremove titolo 1")
    rg.remove("titolo 1")

    println("print size ${rg.size()}")

    try {
        println("remove titolo 1")
        rg.remove("titolo 1")
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("save")
    rg.save("storage/backend/save.dat")

    val rg2 = Registry()

    println("print rg2 size ${rg2.size()}")
    try{ rg2.load("storage/backend/save.dat") }catch (e: IOException){ e.printStackTrace() }

    println("load rg1 into rg2\nprint size ${rg2.size()}")



}