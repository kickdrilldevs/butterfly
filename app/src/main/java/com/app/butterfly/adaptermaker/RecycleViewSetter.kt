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

package com.app.butterfly.adaptermaker


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.butterfly.db.Model

interface RecycleViewSetter<V : RecyclerView.ViewHolder, M : Model> {

    fun <V : RecyclerView.ViewHolder> onCreateViewHolder(parent: Int, view: View): V

    fun onBindViewHolder(holder: V, pojo: M, position: Int)


}
