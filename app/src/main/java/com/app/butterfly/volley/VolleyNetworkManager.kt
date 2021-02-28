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

package com.app.butterfly.volley


import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.app.butterfly.butterfly.VolleyApplication
import org.json.JSONObject

class VolleyNetworkManager(
    internal var responser: Response.Listener<*>,
    internal var errorlistener: Response.ErrorListener,
    internal val url: String,
    internal val param: JSONObject
) {


    fun makeJsonWebRequest(method: Int) {
        val jsonwebservice =  JsonObjectRequest(method, url, param, responser as Response.Listener<JSONObject>, errorlistener)
        jsonwebservice.setRetryPolicy(DefaultRetryPolicy(500 * 1000, 200, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        addToRequestQueue(jsonwebservice)
    }

    private fun <T> addToRequestQueue(webservice: Request<T>) {
        VolleyApplication.instance!!.addToRequestQueue(webservice)
    }

    fun makeStringWebRequest(method: Int) {
        val stringwebservice = StringRequest(method, url, responser as Response.Listener<String>, errorlistener)
        stringwebservice.setRetryPolicy(DefaultRetryPolicy(500 * 1000, 200, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        addToRequestQueue(stringwebservice)
    }
}
