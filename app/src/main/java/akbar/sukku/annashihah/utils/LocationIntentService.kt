package akbar.sukku.annashihah.utils

import akbar.sukku.annashihah.utils.Constant.FAILURE_RESULT
import akbar.sukku.annashihah.utils.Constant.LOCATION_DATA_EXTRA
import akbar.sukku.annashihah.utils.Constant.RECEIVER
import akbar.sukku.annashihah.utils.Constant.RESULT_DATA_KEY
import akbar.sukku.annashihah.utils.Constant.RESULT_DATA_KEY_COOR
import akbar.sukku.annashihah.utils.Constant.SUCCESS_RESULT
import akbar.sukku.annashihah.utils.Constant.SUCCESS_RESULT_COORDINATE
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import androidx.core.app.JobIntentService
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*

class LocationIntentService : JobIntentService() {

    companion object{
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context, LocationIntentService::class.java, 1, intent)
        }
    }
    private lateinit var receiver: ResultReceiver

    private fun deliverResult(resultCode: Int, msg: String) {
        val bundle = Bundle().apply {
            this.putString(RESULT_DATA_KEY, msg)
        }
        receiver.send(resultCode, bundle)
    }
    private fun deliveryCoordinate(resultCode: Int, coordinate: DoubleArray){
        val bundle = Bundle().apply {
            this.putDoubleArray(RESULT_DATA_KEY_COOR, coordinate)
        }
        receiver.send(resultCode, bundle)
    }

    override fun onHandleWork(intent: Intent) {
        var errorMessage = ""
        receiver = intent.getParcelableExtra(RECEIVER)!!



        val location = intent.getParcelableExtra<Location>(LOCATION_DATA_EXTRA)
        if (location == null) {
            errorMessage = "No Location Data"
            deliverResult(FAILURE_RESULT, errorMessage)
            return
        }

        val geocode = Geocoder(this, Locale.getDefault())

        var address: List<Address> = emptyList()

        try {
            address = geocode.getFromLocation(location.latitude, location.longitude, 1)

        }catch (ioEx: IOException){
            errorMessage = "Layanan Lokasi Tidak Tersedia"
        }catch (e: IllegalArgumentException){
            errorMessage = "Invalid Latitude or Longitude"
        }

        if (address.isEmpty()){
            if (errorMessage.isEmpty()){
                errorMessage = "Maaf, tidak ditemukan lokasi"
            }
            deliverResult(FAILURE_RESULT, errorMessage)
        } else{
            val mAddress = address[0]

            deliveryCoordinate(SUCCESS_RESULT_COORDINATE, doubleArrayOf(mAddress.latitude, mAddress.longitude))
            deliverResult(SUCCESS_RESULT, mAddress.locality +" "+ mAddress.adminArea)
        }
    }
}