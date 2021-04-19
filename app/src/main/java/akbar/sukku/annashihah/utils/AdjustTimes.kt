package akbar.sukku.annashihah.utils

import akbar.sukku.annashihah.R
import android.content.Context
import android.content.SharedPreferences
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Madhab


class AdjustTimes(
        private val context: Context,
        private val sharedPreferences: SharedPreferences,
        private val methods: CalculationParameters,
) {

    fun getAdjustTimes() {
        val adjustFajr = sharedPreferences.getInt(context.getString(R.string.keyFajr), 0)
        val adjustSyuruq = sharedPreferences.getInt(context.getString(R.string.key_sunrise), 0)
        val adjustThur = sharedPreferences.getInt(context.getString(R.string.keyThur), 0)
        val adjustAsr = sharedPreferences.getInt(context.getString(R.string.keyAsr), 0)
        val adjustMaghrib = sharedPreferences.getInt(context.getString(R.string.keyMaghrib), 0)
        val adjustIsha = sharedPreferences.getInt(context.getString(R.string.keyIsya), 0)
        val methodsAsar = sharedPreferences.getString(
                context.getString(R.string.asar_methodKey),
                context.getString(R.string.shafii_method)
        ) as String
        val nameMethod = sharedPreferences.getString(context.getString(R.string.method_key), context.getString(R.string.default_method_key))
        val defaultMadhab = context.getString(R.string.shafii_method)
        if (nameMethod.equals("sihat_kemenag_indonesia")) {
            methods.apply {
                this.madhab = setMadhab(methodsAsar, defaultMadhab)
                this.adjustments.fajr = adjustFajr + 2
                this.adjustments.sunrise = adjustSyuruq - 3
                this.adjustments.dhuhr = adjustThur + 2
                this.adjustments.asr = adjustAsr + 2
                this.adjustments.maghrib = adjustMaghrib + 2
                this.adjustments.isha = adjustIsha + 2
            }
        } else {
            methods.apply {
                this.madhab = setMadhab(methodsAsar, defaultMadhab)
                this.adjustments.fajr = adjustFajr
                this.adjustments.sunrise = adjustSyuruq
                this.adjustments.dhuhr = adjustThur
                this.adjustments.asr = adjustAsr
                this.adjustments.maghrib = adjustMaghrib
                this.adjustments.isha = adjustIsha
            }
        }

    }

    private fun setMadhab(name: String, default: String): Madhab {
        return if (name == default) Madhab.SHAFI else Madhab.HANAFI
    }
}