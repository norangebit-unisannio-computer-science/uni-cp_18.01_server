package server.backend.test

import commons.FlashMob
import commons.FlashMobException
import server.backend.Registry
import java.io.IOException
import java.time.LocalTime
import java.util.*


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */
 
fun main(args: Array<String>){
    val rg = Registry()
    println("print size ${rg.size()}")

    println("add titolo 1")
    rg.add(FlashMob("titolo 1",  Date(), Date()))

    println("add titolo 1")
    try {
        rg.add(FlashMob("titolo 1",  Date(), Date()))
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("update titolo 2")
    rg.update(FlashMob("titolo 2",  Date(), Date()))
    println("update titolo 3")
    rg.update(FlashMob("titolo 3",  Date(), Date()))


    rg.list().forEach { print(it+" - ") }

    println("\nremove titolo 1")
    rg.remove("titolo 1")

    println("print size ${rg.size()}")

    try {
        println("remove titolo 1")
        rg.remove("titolo 1")
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("save")
    rg.save("storage/backend/save.dat0")

    val rg2 = Registry()

    println("print rg2 size ${rg2.size()}")
    try{ rg2.load("storage/backend/save.dat0") }catch (e: IOException){ e.printStackTrace() }

    println("load rg1 into rg2\nprint size ${rg2.size()}")



}