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
import com.app.butterfly.adaptermaker.ViewSetter
import com.app.butterfly.butterfly.ButterflyActivity

import com.app.butterfly.db.Model

import com.app.butterfly.viewholders.ViewHolder
import java.util.*


class BaseAdapter<V : ViewHolder, M : Model>(
    internal var layoutid: Int,
    var pojos: ArrayList<M>,
    internal var context: ButterflyActivity
) : BaseAdapter() {

    internal var inflater: LayoutInflater
    internal var getviewmaker: ViewSetter<V, M>? = null
        set(value) {
            field = value
        }


    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (getviewmaker is ButterflyActivity) {
            getviewmaker = context as ViewSetter<V, M>
        }
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        if (layoutid == -1) {
            try {
                throw Exception("You Must provide layoutid ")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        var view =  getViewMakerNotNull(convertView, position, parent) ?: throw Exception("Must provide AddViewMaker Interface")
        return view
    }

    private fun getViewMakerNotNull(convertView: View?, position: Int, parent: ViewGroup): View? {

        var view = setTag(convertView) ?: initView(parent)
        val Model = pojos[position]
        getviewmaker!!.AtInitView(convertView?.tag as V, Model, position)
        return view
    }

    private fun setTag(convertView: View?): View? {
        convertView?.setTag(convertView.tag as V)
        return convertView
    }


    private fun initView(parent: ViewGroup): View? {
        val viewHolder = ViewHolder::class.java.constructors.get(0).newInstance(inflater.inflate(layoutid, parent, false)) as V?
        viewHolder?.view!!.tag = viewHolder
        return viewHolder?.view
    }


}
