package com.sergeimaleev.handywheelandroid.network

import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executors

class NetworkComponent {

    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null

    private fun setup(ip: String, port: Int) {
        socket = Socket(ip, port)
        printWriter = PrintWriter(socket!!.getOutputStream(), true)
    }

    fun setupBackground(ip: String, port: Int) {
        Thread {
            setup(ip, port)
            sendMsg("Hello from Client!")
        }.start()
    }

    fun sendMsg(msg: String) {
        printWriter?.println(msg)
    }

    fun terminate() {
        printWriter?.flush()
        socket?.close()
    }
}