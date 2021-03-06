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

package server.backend

import commons.Code
import commons.FlashMob
import commons.FlashMobException
import java.io.*
import java.util.*


/*
 *  Author: Raffaele Mignone
 *  Mat: 863/747
 *  Date: 22/12/17
 */

class Registry{
    var dict = TreeMap<String, FlashMob>()

    @Throws(FlashMobException::class)
    fun get(name: String): FlashMob{
        if(dict.containsKey(name))
            return dict[name] as FlashMob
        else
            throw FlashMobException(Code.NOT_FOUND)
    }

    @Throws(FlashMobException::class)
    fun add(fm: FlashMob){
        if (dict.containsKey(fm.name))
            throw FlashMobException(Code.CONFLICT)
        else
            dict[fm.name]=fm
    }

    @Throws(FlashMobException::class)
    fun remove(name: String){
        if(dict.containsKey(name))
            dict.remove(name)
        else
            throw FlashMobException(Code.NOT_FOUND)
    }

    fun update(fm: FlashMob){
        dict[fm.name]=fm
    }

    fun size() = dict.size

    fun list() = dict.keys.toTypedArray()

    @Throws(IOException::class)
    fun save(fileName: String){
        val out = FileOutputStream(fileName)
        val objOut = ObjectOutputStream(out)
        objOut.writeObject(dict)
        objOut.close()
        out.close()
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun load(fileName: String){
        val fileIn = FileInputStream(fileName)
        val objIn = ObjectInputStream(fileIn)
        dict = objIn.readObject() as TreeMap<String, FlashMob>
        objIn.close()
        fileIn.close()
    }
}