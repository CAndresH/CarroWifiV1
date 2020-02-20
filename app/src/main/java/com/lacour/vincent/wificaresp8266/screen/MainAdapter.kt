package com.lacour.vincent.wificaresp8266.screen

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lacour.vincent.wificaresp8266.R
import kotlinx.android.synthetic.main.video_row.view.*

/**
 * Created by brianvoong on 12/18/17.
 */

 class MainAdapter(val homeFeed: HomeFeed): RecyclerView.Adapter<CustomViewHolder>() {



    // numberOfItems
    override fun getItemCount(): Int {
        println( homeFeed.temp[0])
        return homeFeed.temp.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.video_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        val videoTitle = videoTitles.get(position)
        val video = homeFeed.temp.get(position)
        holder?.view?.textView_video_title?.text = video.valor //medicion
        holder?.view?.fecha?.text = video.medicion
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
