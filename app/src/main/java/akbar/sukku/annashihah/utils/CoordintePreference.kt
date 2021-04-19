package akbar.sukku.annashihah.utils

import android.content.Context

class CoordinatePreference constructor(context: Context) {

    companion object {
        private const val PREF_COORDINATE = "pref_coordinate"
        private const val LATITUDE = "latitude_data"
        private const val LONGITUDE = "longitude_data"
        private const val PREF_TIMEZONE = "time_zone_key"
        private const val DEFAULT_VALUE: Long = 0L

    }

    private val preference = context.getSharedPreferences(PREF_COORDINATE, Context.MODE_PRIVATE)
    private val preferenceLocation =
        context.getSharedPreferences(PREF_TIMEZONE, Context.MODE_PRIVATE)

    fun setCoordinate(coordinateData: CoordinateData) {
        val editor = preference.edit()
        editor.putLong(LATITUDE, java.lang.Double.doubleToRawLongBits(coordinateData.latitude))
        editor.putLong(LONGITUDE, java.lang.Double.doubleToRawLongBits(coordinateData.longitude))
        editor.apply()
    }

    fun getCoordinate(): CoordinateData {
        val lat = java.lang.Double.longBitsToDouble(preference.getLong(LATITUDE,
            DEFAULT_VALUE
        ))
        val longi = java.lang.Double.longBitsToDouble(preference.getLong(LONGITUDE,
            DEFAULT_VALUE
        ))
        return CoordinateData(lat, longi)
    }

    fun setAddressName(name: String?) {
        val editor = preferenceLocation.edit()
        editor.putString(PREF_TIMEZONE, name)
        editor.apply()
    }

    fun getAddressName(): String =
        preferenceLocation.getString(PREF_TIMEZONE, "Makassar") as String

}
data class CoordinateData(
    var latitude: Double,
    var longitude: Double,
)