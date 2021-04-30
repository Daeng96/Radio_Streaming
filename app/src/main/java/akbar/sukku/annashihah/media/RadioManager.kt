package akbar.sukku.annashihah.media

import akbar.sukku.annashihah.utils.RadioService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import org.greenrobot.eventbus.EventBus

class RadioManager(private val context: Context) {

    fun playOrPause(streamUrl: String) {
        service!!.playOrPause(streamUrl)
    }

    fun bind() {
        val intent = Intent(context, RadioService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        if (service != null) EventBus.getDefault().post(service!!.status)
    }

    fun unbind() {
        context.unbindService(serviceConnection)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, binder: IBinder) {
            service = (binder as RadioService.LocalBinder).getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i("OnServiceDisconnect", arg0.shortClassName)
        }
    }

    companion object {
        private var service: RadioService? = null
    }
}