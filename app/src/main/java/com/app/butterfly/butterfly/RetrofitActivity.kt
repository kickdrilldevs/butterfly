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

package com.app.butterfly.butterfly


import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import com.app.butterfly.db.Model
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

abstract class RetrofitActivity<R : Model> : ButterflyActivity(), Callback<R> {

    /**
     * base_url is main server domain url where actual server app is hosted;
     */
    var base_url: String? = null

    /**
     * it's used create web-client for typical webservice.
     */
    abstract protected var retrofit: Retrofit


    var okHttpClient: OkHttpClient? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRetrofit()


    }

    private fun initRetrofit() {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        }

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
    fun callRetrofitWebService(): Boolean {
        if (this!!.isConnectingToInternet!!) {
            var call: Call<R> = createWebService()
            call.enqueue(this)
            return true
        } else {
            atDeviceOffline()
            return false
        }
    }



    /**
     * it's method is for when create or call
     * api method in retrofit
     */
    abstract fun createWebService(): Call<R>

    /**
     * it's method is for when device is at offline or state.
     */
    protected abstract fun atDeviceOffline()


    override fun onResponse(call: Call<R>, response: Response<R>) {
        Log.e("request url : ", call.request().url().toString())
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
