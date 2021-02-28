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


import android.content.Context

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import java.lang.reflect.ParameterizedType


abstract open class ButterflyActivity : AppCompatActivity() {

    val isConnectingToInternet: Boolean?
        get() {
            val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info.indices)
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }

            }
            return false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
    }

    fun <V : ViewDataBinding> getActualTypeArgumentClassName(): String? {
        var geneClassName: String? = null
        val genericInterfaces = javaClass.genericInterfaces

        for (interfaceclass in genericInterfaces) {
            if (interfaceclass is ParameterizedType) {
                val type = interfaceclass.actualTypeArguments[0]
                try {
                    val geneClass = Class.forName((type as Class<*>).name) as Class<V>
                    geneClassName = geneClass.name
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }

            }
        }
        return geneClassName
    }




}
