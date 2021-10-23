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

package com.app.butterfly.adapters

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.butterfly.adaptermaker.ItemLayoutBinder
import com.app.butterfly.butterfly.ButterflyActivity

import com.app.butterfly.db.Model

import java.util.*



class DataBindingBaseAdapter<V : ViewDataBinding, M : Model>(
    var pojos: ArrayList<M>,
    internal var context: ButterflyActivity
) : BaseAdapter() {
    public var layoutid = -1
    public var inflater: LayoutInflater

    var adapterItemLayoutBinder: ItemLayoutBinder<V, M>? = null
        set(value) {
            field = value
        }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return pojos.size
    }

    override fun getItem(position: Int): Any {
        return pojos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder: V? = null
        val Model: M = pojos[position]
        if (layoutid == -1) {
            try {
                throw Exception("Must provide layoutid ")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (convertView == null) {
            convertView = inflater.inflate(layoutid, parent, false)
            viewHolder = DataBindingUtil.bind(convertView)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as V
        }
        if (adapterItemLayoutBinder != null) {
            if (viewHolder != null) {
                adapterItemLayoutBinder!!.onBindViewHolder(viewHolder, Model)
            }
        }
        return convertView
    }


}
