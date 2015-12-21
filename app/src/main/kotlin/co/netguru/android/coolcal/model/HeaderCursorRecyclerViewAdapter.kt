package co.netguru.android.coolcal.model

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView.ViewHolder
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter

abstract class HeaderCursorRecyclerViewAdapter
            <VH : ViewHolder, HH : ViewHolder>(context: Context, cursor: Cursor?) :
        CursorRecyclerViewAdapter<VH>(context, cursor),
        StickyRecyclerHeadersAdapter<HH> {


    abstract fun onBindHeaderViewHolder(holder: HH, cursor: Cursor)

    override fun onBindHeaderViewHolder(holder: HH, position: Int) {
        val cursor = positionCursor(position)
        onBindHeaderViewHolder(holder, cursor)
    }

    abstract fun getHeaderId(cursor: Cursor): Long

    override fun getHeaderId(position: Int): Long {
        val cursor = positionCursor(position)
        return getHeaderId(cursor)
    }

    override fun positionCursor(position: Int): Cursor {
        return super.positionCursor(position)
    }
}
