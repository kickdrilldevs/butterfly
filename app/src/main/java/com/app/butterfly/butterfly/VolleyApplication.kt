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

import android.text.TextUtils

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley



class VolleyApplication : butterflyApplication() {

    private var mRequestQueue: RequestQueue? = null
        get() {
            return mRequestQueue ?: Volley.newRequestQueue(applicationContext)
        }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.setTag(if (TextUtils.isEmpty(tag)) TAG else tag)
        mRequestQueue?.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.setTag(TAG)
        mRequestQueue?.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {

        val TAG = butterflyApplication::class.java.simpleName

        @get:Synchronized
        var instance: VolleyApplication? = null
            private set
    }
}
