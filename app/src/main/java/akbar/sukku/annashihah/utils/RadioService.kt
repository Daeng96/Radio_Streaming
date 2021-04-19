package akbar.sukku.annashihah.utils

import akbar.sukku.annashihah.media.MediaNotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.net.wifi.WifiManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.daeng96.radioan_nashihah.player.PlaybackStatus
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.NonNullApi
import com.google.android.exoplayer2.util.Util
import org.greenrobot.eventbus.EventBus

@Suppress("DEPRECATION")
class RadioService : Service(), Player.EventListener, OnAudioFocusChangeListener{

    private val iBinder: IBinder = LocalBinder()
    private lateinit var exoPlayer: SimpleExoPlayer
    lateinit var mediaSession: MediaSessionCompat
    private var transportControls: MediaControllerCompat.TransportControls? = null
    private var onGoingCall = false
    private var telephonyManager: TelephonyManager? = null
    private var wifiLock: WifiManager.WifiLock? = null
    private lateinit var audioManager: AudioManager
    private lateinit var notificationManager: MediaNotificationManager
    lateinit var status: String
    private lateinit var streamUrl: String
    private lateinit var mFocusRequest: AudioFocusRequest

    inner class LocalBinder : Binder() {
        fun getService(): RadioService {
            return this@RadioService
        }
    }

    private val becomingNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            pause()
        }
    }
    private val phoneStateListener: PhoneStateListener = object : PhoneStateListener() {

        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            if (state == TelephonyManager.CALL_STATE_OFFHOOK
                || state == TelephonyManager.CALL_STATE_RINGING
            ) {
                if (!isPlaying) return
                onGoingCall = true
                stop()
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                if (!onGoingCall) return
                onGoingCall = false
                resume()
            }
        }
    }
    private val mediasSessionCallback: MediaSessionCompat.Callback =
        object : MediaSessionCompat.Callback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPause() {
                super.onPause()
                pause()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStop() {
                super.onStop()
                stop()
                notificationManager.cancelNotify()
            }

            override fun onPlay() {
                super.onPlay()
                resume()
            }
        }

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }


    override fun onCreate() {
        super.onCreate()
        val strAppName = "Player"
        val strLiveBroadcast = "Live"
        streamUrl = ""
        onGoingCall = false
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        notificationManager = MediaNotificationManager(this)
        wifiLock = (applicationContext.getSystemService(WIFI_SERVICE) as WifiManager)
            .createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "mcScPAmpLock")
        mediaSession = MediaSessionCompat(this, javaClass.simpleName)
        transportControls = mediaSession.controller.transportControls
        mediaSession.isActive = true
        mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "...")
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, strAppName)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, strLiveBroadcast)
                        .build()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setUsage(AudioAttributes.USAGE_MEDIA)
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    build()
                })
                setAcceptsDelayedFocusGain(true)
                setOnAudioFocusChangeListener {
                    when (it) {
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            exoPlayer.volume = 0.8f
                            resume()
                        }
                        AudioManager.AUDIOFOCUS_LOSS -> stop()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> if (isPlaying) pause()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> if (isPlaying) exoPlayer.volume =
                                0.1f
                    }
                }
                build()
            }
        }
        mediaSession.setCallback(mediasSessionCallback)
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager!!.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

        val trackSelectionFactory = AdaptiveTrackSelection.Factory()
        val trackSelector = DefaultTrackSelector(this, trackSelectionFactory)
        exoPlayer = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        exoPlayer.addListener(this)
        registerReceiver(
                becomingNoisyReceiver,
                IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        )
        status = PlaybackStatus.IDLE
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (TextUtils.isEmpty(action)) return START_NOT_STICKY
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(mFocusRequest)
        } else {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            stop()
            return START_NOT_STICKY
        }
        when {
            action.equals(ACTION_PLAY, ignoreCase = true) -> {
                transportControls!!.play()
            }
            action.equals(ACTION_PAUSE, ignoreCase = true) -> {
                transportControls!!.pause()
            }
            action.equals(ACTION_STOP, ignoreCase = true) -> {
                transportControls!!.stop()
            }
        }
        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent): Boolean {
        if (status == PlaybackStatus.IDLE) stopSelf()
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {}


    override fun onDestroy() {
        pause()
        exoPlayer.release()
        exoPlayer.removeListener(this)
        if (telephonyManager != null) telephonyManager!!.listen(
                phoneStateListener,
                PhoneStateListener.LISTEN_NONE
        )
        notificationManager.cancelNotify()
        mediaSession.release()
        unregisterReceiver(becomingNoisyReceiver)
        super.onDestroy()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        status = if (playbackState == Player.STATE_BUFFERING) {
            PlaybackStatus.LOADING
        } else if (playbackState == Player.STATE_ENDED) {
            PlaybackStatus.STOPPED
        } else if (playbackState == Player.STATE_READY) {
            if (playWhenReady) PlaybackStatus.PLAYING else PlaybackStatus.PAUSED
        } else {
            PlaybackStatus.IDLE
        }
        if (status != PlaybackStatus.IDLE) notificationManager.startNotify(status)
        EventBus.getDefault().post(status)
    }

    @NonNullApi
    override fun onPlayerError(error: ExoPlaybackException) {
        EventBus.getDefault().post(PlaybackStatus.ERROR)
    }


    private fun play(streamUrl: String) {
        this.streamUrl = streamUrl
        if (wifiLock != null && !wifiLock!!.isHeld) {
            wifiLock!!.acquire()
        }

        val dataSourceFactory = DefaultDataSourceFactory(
                this,
                userAgent
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                MediaItem.fromUri(
                        streamUrl
                )
        )

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun resume() {
        play(streamUrl)
    }


    fun pause() {
        exoPlayer.playWhenReady = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(mFocusRequest)
        } else{
            audioManager.abandonAudioFocus(this)
        }
        wifiLockRelease()
    }


    fun stop() {
        exoPlayer.stop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(mFocusRequest)
        } else{
            audioManager.abandonAudioFocus(this)
        }
        wifiLockRelease()
    }


    fun playOrPause(url: String) {
        if (streamUrl == url) {
            if (!isPlaying) {
                play(streamUrl)
            } else {
                pause()
            }
        } else {
            if (isPlaying) {
                pause()
            }
            play(url)
        }
    }

    val isPlaying: Boolean
        get() = status == PlaybackStatus.PLAYING

    private fun wifiLockRelease() {
        if (wifiLock != null && wifiLock!!.isHeld) {
            wifiLock!!.release()
        }
    }

    private val userAgent: String
        get() = Util.getUserAgent(this, javaClass.simpleName)

    companion object {
        const val ACTION_PLAY = "com.daeng96.radioan_nashihah.player.ACTION_PLAY"
        const val ACTION_PAUSE = "com.daeng96.radioan_nashihah.player.ACTION_PAUSE"
        const val ACTION_STOP = "com.daeng96.radioan_nashihah.player.ACTION_STOP"
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange){
            AudioManager.AUDIOFOCUS_GAIN-> {
                exoPlayer.volume = 0.8f
                resume()
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                stop()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                if (isPlaying) pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                if (isPlaying) exoPlayer.volume = 0.1f
            }
        }
    }
}