package com.sergeimaleev.handywheelandroid.network

import android.util.Log
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class NetworkComponent {

    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null
    //private var outputStreamWriter: OutputStreamWriter? = null

    @Synchronized
    private fun setup(ip: String, port: Int) {
        socket = Socket(ip, port)
        printWriter = PrintWriter(socket!!.getOutputStream(), true)
        //outputStreamWriter = OutputStreamWriter(socket!!.getOutputStream())
    }

    @Synchronized
    fun setupBackground(ip: String, port: Int) {
        Thread {
            setup(ip, port)
            sendMsg("Hello from Client!")
        }.start()
    }

    fun sendMsg(msg: String) {
        printWriter!!.println(msg)
        Log.d("sendMSG", "sendMsg: " + msg)
        //outputStreamWriter?.write(msg)
        //outputStreamWriter?.flush()
    }

    fun terminate() {
        try {
            printWriter?.flush()
            socket?.close()
        } catch (_: IOException) {

        }
    }
}