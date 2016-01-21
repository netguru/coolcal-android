package co.netguru.android.coolcal.model

import android.view.View
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.owm.api.Forecast
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder

class EventHolder(itemView: View) : ViewHolder(itemView) {

    val titleTextView: TextView by lazy {
        itemView.findViewById(R.id.event_title) as TextView
    }

    fun bind(obj: Event, forecast: Forecast?) {
        titleTextView.text = "${obj.title} : ${forecast?.weatherList?.get(0)?.description}"
    }

}