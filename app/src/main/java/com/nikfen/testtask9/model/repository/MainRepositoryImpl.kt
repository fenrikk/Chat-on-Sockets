package com.nikfen.testtask9.model.repository

import android.util.Log
import com.google.gson.Gson
import com.nikfen.testtask9.model.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

class MainRepositoryImpl : MainRepository {

    private lateinit var username: String
    private lateinit var id: String
    private lateinit var socket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val gson = Gson()

    override fun getUsers() {
        TODO("Not yet implemented")
    }

    override fun sendMassage() {
        TODO("Not yet implemented")
    }

    override fun connect(username: String) {
        this.username = username
        compositeDisposable.add(
            requestSocket()
                .doOnNext {
                    socket = Socket(it.address.hostAddress, 6666)
                    socket.soTimeout = 1000
                }
                .map { socket }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    reader = BufferedReader(InputStreamReader(it.getInputStream()))
                    writer = PrintWriter(it.getOutputStream())
                    while (it.isConnected) {
                        try {
                            val line = reader.readLine()
                            val baseDto = gson.fromJson(line, BaseDto::class.java)
                            when (baseDto.action) {
                                BaseDto.Action.CONNECTED -> handleConnection(baseDto.payload)
                                BaseDto.Action.PONG -> handlePong(baseDto.payload)
                                else -> {}
                            }
                        } catch (e: Exception) {
                            Log.d("MyApp", "There are not messages from server")
                        }
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun requestSocket(): Observable<DatagramPacket> {
        val socket = DatagramSocket()
        val request = "request".toByteArray()
        val received = DatagramPacket("".toByteArray(), "".toByteArray().size)
        return Observable.fromCallable {
            socket.send(
                DatagramPacket(
                    request,
                    request.size,
                    InetAddress.getByName("255.255.255.255"),
                    8888
                )
            )
            socket.soTimeout = 5000
            try {
                socket.receive(received)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            received
        }
    }

    private fun handleConnection(payload: String) {
        val connectedDto = gson.fromJson(payload, ConnectedDto::class.java)
        id = connectedDto.id
        val connectDto = ConnectDto(id, username)
        val connectJson = gson.toJson(connectDto)
        val baseDto = BaseDto(BaseDto.Action.CONNECT, connectJson)
        val message = gson.toJson(baseDto)
        writer.println(message)
        writer.flush()
        compositeDisposable.add(
            Observable
                .interval(5, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val pingDto = PingDto(id)
                    val pingJson = gson.toJson(pingDto)
                    val baseDto = BaseDto(BaseDto.Action.PING, pingJson)
                    val message = gson.toJson(baseDto)
                    writer.println(message)
                    writer.flush()
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun handlePong(payload: String) {
        val pongDto = gson.fromJson(payload, PongDto::class.java)
        Log.d("MyApp", "Pong received with id: ${pongDto.id}")
    }
}