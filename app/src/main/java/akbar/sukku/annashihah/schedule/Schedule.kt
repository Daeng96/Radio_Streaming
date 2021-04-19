package akbar.sukku.annashihah.schedule

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Schedule(
    var name: String,
    var time: String
) : Parcelable
