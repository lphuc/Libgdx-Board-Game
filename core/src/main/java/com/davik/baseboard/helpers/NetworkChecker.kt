package com.davik.baseboard.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.Net.HttpResponseListener
import com.badlogic.gdx.net.HttpRequestBuilder

/**
 * Created on 1/19/2019.
 * Check if the game is currently connected to internet
 */
interface NetworkListener {
    fun onResult(connected: Boolean)
}

const val WEB_SOCKET_SERVER = ""
object NetworkChecker {
    var connected = false
    fun checkServerConnection(networkListener: NetworkListener) {
        try {
            val requestBuilder = HttpRequestBuilder()
            val httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(WEB_SOCKET_SERVER).build()
            val responseListener: HttpResponseListener = object : HttpResponseListener {
                override fun handleHttpResponse(httpResponse: Net.HttpResponse) {
                    val status = httpResponse.status
                    connected = status.statusCode == 200
                    networkListener.onResult(true)
                }

                override fun failed(t: Throwable) {
                    connected = false
                    networkListener.onResult(false)
                }

                override fun cancelled() {
                    networkListener.onResult(false)
                }
            }
            Gdx.net.sendHttpRequest(httpRequest, responseListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}