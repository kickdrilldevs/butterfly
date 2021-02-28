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

import android.util.Log
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.app.butterfly.butterfly.ButterflyActivity
import com.app.butterfly.volley.VolleyNetworkManager


import org.json.JSONObject


/**
 * Created by vyankatesh.jadhav on 3/8/2017.
 */

abstract class VolleyWebServiceCaller<R>(private val context: ButterflyActivity) : Response.Listener<R>, Response.ErrorListener {


    fun callJsonWebRequest(url: String, method: Int, param: JSONObject): Boolean {
        if (context.isConnectingToInternet!!) {
            val jsonrequest = VolleyNetworkManager(this, this, url, param)
            jsonrequest.makeJsonWebRequest(method)
            return true
        } else {
            atDeviceOffline()
            return false
        }
    }


    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     *
     * @param error
     */
    override fun onErrorResponse(error: VolleyError) {
        Log.e("error", error.message, error)
        if (error is TimeoutError) {
            onTimeout(error)
        } else {
            onNetworkError(error)
        }
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    override fun onResponse(response: R) {
        setResponse(response)
    }


    /**
     * it's method is for when device is at offline or state.
     */
    protected abstract fun atDeviceOffline()

    /**
     * it's method is used for when any webservice fail at any erro occured , except time out of web service
     *
     * @param error
     */

    protected abstract fun onNetworkError(error: VolleyError)

    /**
     * it's method is used for when any webservice is exceeds particular time
     *
     * @param error
     */
    protected abstract fun onTimeout(error: VolleyError)

    abstract fun setResponse(r: R)
}
