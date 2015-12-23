package co.netguru.android.coolcal.model

import android.database.Cursor
import android.support.v7.widget.RecyclerView.ViewHolder
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter

abstract class HeaderCursorRecyclerViewAdapter
            <VH : ViewHolder, HH : ViewHolder>(cursor: Cursor?) :
        CursorRecyclerViewAdapter<VH>(cursor),
        StickyRecyclerHeadersAdapter<HH> {


    abstract fun onBindHeaderViewHolder(holder: HH, cursor: Cursor)

    override fun onBindHeaderViewHolder(holder: HH, position: Int) {
        val cursor = moveCursorToPosition(position)
        onBindHeaderViewHolder(holder, cursor)
    }

    abstract fun getHeaderId(cursor: Cursor): Long

    override fun getHeaderId(position: Int): Long {
        val cursor = moveCursorToPosition(position)
        return getHeaderId(cursor)
    }
}
