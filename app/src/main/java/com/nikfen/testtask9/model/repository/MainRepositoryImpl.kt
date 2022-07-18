package com.nikfen.testtask9.model.repository

import android.util.Log
import com.google.gson.Gson
import com.nikfen.testtask9.model.*
import com.nikfen.testtask9.other.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
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
    private val baseDtoReceived = BehaviorSubject.create<BaseDto>()
    private val connected = BehaviorSubject.create<Boolean>()

    override fun startReceivingUsers() {
        compositeDisposable.add(
            Observable
                .interval(1, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val getUsersDto = GetUsersDto(id)
                    val getUsersJson = gson.toJson(getUsersDto)
                    val getUsersBaseDto = BaseDto(BaseDto.Action.GET_USERS, getUsersJson)
                    val messageGetUsers = gson.toJson(getUsersBaseDto)
                    writer.println(messageGetUsers)
                    writer.flush()
                    Log.d("MyApp", "requesting users")
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun sendMassage(receiver: String, message: String) {
        compositeDisposable.add(
            Observable.just(message)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val sendMessageDto = SendMessageDto(id, receiver, it)
                    val sendMessageJson = gson.toJson(sendMessageDto)
                    val sendMessageBaseDto = BaseDto(BaseDto.Action.SEND_MESSAGE, sendMessageJson)
                    val messageSendMessage = gson.toJson(sendMessageBaseDto)
                    writer.println(messageSendMessage)
                    writer.flush()
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun connect(username: String) {
        this.username = username
        compositeDisposable.add(
            requestIp()
                .doOnNext {
                    socket = Socket(it, PORT_TCP)
                    socket.soTimeout = SERVER_RESPONSE_COUNTDOWN
                }
                .map {
                    socket
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    reader = BufferedReader(InputStreamReader(it.getInputStream()))
                    writer = PrintWriter(it.getOutputStream())
                }, {
                    it.printStackTrace()
                })
        )
        compositeDisposable.add(
            Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (socket.isConnected) {
                        try {
                            val line = reader.readLine()
                            val baseDto = gson.fromJson(line, BaseDto::class.java)
                            when (baseDto.action) {
                                BaseDto.Action.CONNECTED -> handleConnection(baseDto.payload)
                                BaseDto.Action.PONG -> handlePong(baseDto.payload)
                                else -> {
                                    baseDtoReceived.onNext(baseDto)
                                }
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

    override fun getConnection(): Observable<Boolean> {
        return connected
    }

    override fun newMessageReceived(): Observable<MessageDto> {
        return baseDtoReceived.filter {
            it.action == BaseDto.Action.NEW_MESSAGE
        }.map {
            gson.fromJson(it.payload, MessageDto::class.java)
        }
    }

    override fun usersReceived(): Observable<UsersReceivedDto> {
        return baseDtoReceived.filter {
            it.action == BaseDto.Action.USERS_RECEIVED
        }.map {
            gson.fromJson(it.payload, UsersReceivedDto::class.java)
        }
    }

    override fun stop() {
        compositeDisposable.dispose()
    }

    private fun requestIp(): Observable<String> {
        val socket = DatagramSocket()
        val request = REQUEST.toByteArray()
        val received = DatagramPacket("".toByteArray(), "".toByteArray().size)
        return Observable.fromCallable {
            socket.send(
                DatagramPacket(
                    request,
                    request.size,
                    InetAddress.getByName(UDP_ADRESS),
                    UDP_PORT
                )
            )
            socket.soTimeout = SERVER_RESPONSE_COUNTDOWN
            try {
                socket.receive(received)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            received.address.hostAddress
        }
    }

    private fun handleConnection(payload: String) {
        val connectedDto = gson.fromJson(payload, ConnectedDto::class.java)
        id = connectedDto.id
        val connectDto = ConnectDto(id, username)
        val connectJson = gson.toJson(connectDto)
        val connectBaseDto = BaseDto(BaseDto.Action.CONNECT, connectJson)
        val messageConnect = gson.toJson(connectBaseDto)
        writer.println(messageConnect)
        writer.flush()
        connected.onNext(true)
        compositeDisposable.add(
            Observable
                .interval(1, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val pingDto = PingDto(id)
                    val pingJson = gson.toJson(pingDto)
                    val pingBaseDto = BaseDto(BaseDto.Action.PING, pingJson)
                    val messagePing = gson.toJson(pingBaseDto)
                    writer.println(messagePing)
                    writer.flush()
                    Log.d("MyApp", "Ping sent")
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