/*
 * Copyright 2018-2019 the original author or authors.
 *
 *   Author:  Vyankatesh Narayan Jadhav
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   Contributors:
 *         Vyankatesh Narayan Jadhav
 *         ...
 *
 */

package com.app.butterfly.webservice

import android.app.ProgressDialog
import android.util.Log
import com.app.butterfly.db.Model
import com.app.butterfly.butterfly.ButterflyActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


abstract class RetroFitWeb<R : Model>(
    /**
     * base_url is main server domain url where actual server app is hosted;
     */
    var base_url: String, var context: ButterflyActivity
) : Callback<R> {

    /**
     * it's used create web-client for typical webservice.
     */
    protected var retrofit: Retrofit



    abstract var dialog: ProgressDialog

    init {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    /**
     * it's method used for calling Web Services using
     * Retrofit api with Taking Response.
     */
    fun callRetrofitWebService(vararg webparams: Any): Boolean {
        try {

            if (context.isConnectingToInternet!!) {
                dialog = ProgressDialog(context)
                dialog.setMessage("Please Wait...")
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
                val call = callWebService(*webparams)
                call.enqueue(this)
                return true
            } else {
                atDeviceOffline()
                return false
            }
        } catch (e: Exception) {
            dialog.dismiss()
            return false
        }

    }

    /**
     * it's method is for when device is at offline or state.
     */
    protected abstract fun atDeviceOffline()

    /**
     * it's method used calling webseevices
     * and return webservice call object;
     *
     * @return
     */
    abstract fun callWebService(vararg webparams: Any): Call<R>

    override fun onResponse(call: Call<R>, response: Response<R>) {
        Log.e("request url : ", call.request().url().toString())
        try {
            dialog.dismiss()
        } catch (ex: Exception) {
            Log.e("error", ex.message, ex)
        }

        setResponse(call, response)
    }

    /**
     * it's method used for to set the response of any web service
     *
     * @param call
     * @param response
     */
    protected abstract fun setResponse(call: Call<R>, response: Response<R>)


    override fun onFailure(call: Call<R>, t: Throwable) {
        dialog.dismiss()
        Log.e("request url : ", call.request().url().toString())
        if (t is SocketTimeoutException) {
            onTimeout(call, t)
        } else {
            onNetworkError(call, t)
        }
    }

    /**
     * it's method is used for when any webservice fail at any erro occured , except time out of web service
     *
     * @param call
     * @param t
     */

    protected abstract fun onNetworkError(call: Call<R>, t: Throwable)

    /**
     * it's method is used for when any webservice is exceeds particular time
     *
     * @param call
     * @param t
     */
    protected abstract fun onTimeout(call: Call<R>, t: Throwable)
}
