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

package server.backend.wrapper.test

import commons.FlashMob
import commons.FlashMobException
import server.backend.wrapper.instance
import java.util.*


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
        instance.add(FlashMob("titolo 5", Date(), Date()))
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("add titolo 5")
    try {
        instance.add(FlashMob("titolo 5", Date(), Date()))
    }catch (e: FlashMobException){ println("${e.code}: ${e.message}") }

    println("commit")
    instance.commit()

    println("update titolo 6")
    instance.update(FlashMob("titolo 6", Date(), Date()))
    instance.list().forEach { print(it+" - ") }

    println("\nrestore")
    instance.restore()
    println("size ${instance.size()}")
    instance.list().forEach { print(it+" - ") }

}