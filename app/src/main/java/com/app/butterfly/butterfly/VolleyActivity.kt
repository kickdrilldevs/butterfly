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

import android.os.Bundle

import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.app.butterfly.volley.VolleyNetworkManager

import org.json.JSONObject

abstract class VolleyActivity<T> : ButterflyActivity(), Response.Listener<T>, Response.ErrorListener {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResponse(response: T) {
        setResponse(response)
    }

    override fun onErrorResponse(error: VolleyError) {
        if (error is TimeoutError) {
            onTimeoutErrorOccured(error)
        } else {
            onNetworkProblemErrorOccured(error)
        }
    }

    abstract fun setResponse(response: T)

    abstract fun onNetworkProblemErrorOccured(error: VolleyError)

    abstract fun onTimeoutErrorOccured(error: VolleyError)

    fun makeJsonWebRequest(url: String, param: JSONObject, method: Int): Boolean {
        if (this!!.isConnectingToInternet!!) {
            val jsonrequest = VolleyNetworkManager(this, this, url, param)
            jsonrequest.makeJsonWebRequest(method)
            return true
        } else {
            return false
        }
    }

}
