package co.netguru.android.coolcal.calendar

data class TimelineData(val events: List<Event>,
                        val dtStart: Long,
                        val timeSpan: Long)