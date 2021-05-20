package com.tekkr.organics.location

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class LocationSocket {

    var socket: Socket? = null

    fun createSocket() {
        try {
            socket = IO.socket("http://165.22.214.54:3000/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        socket?.connect()
        Log.e("Is Socket Connected: ", socket?.connected().toString())
    }

    fun sendSocket(eventName: String, lat: Double?, lng: Double?, riderId: String) {
        val obj = JSONObject()
        try {
            obj.put("lat", lat)
            obj.put("lng", lng)
            obj.put("id", riderId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        socket?.emit(eventName, obj)
        Log.e("Send $eventName: ", " $riderId ($lat - $lng)")
    }

    fun sendPickupSocket(eventName: String, pickup_id: String) {
        val obj = JSONObject()
        try {
            obj.put("pickup_id", pickup_id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        socket?.emit(eventName, obj)
        Log.e("Is Socket Connected: ", socket?.connected().toString())
        Log.e("Send Socket $eventName: ", " $pickup_id")
    }

    fun joinRoom(riderId: String) {
        val obj = JSONObject()
        try {
            obj.put("id", riderId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        socket?.emit("join_room", obj)
        Log.e("Is Socket Connected: ", socket?.connected().toString())
        Log.e("JOIN ROOM: ", " $riderId")
    }
}

