package akbar.sukku.annashihah.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServicesModule {

    @ServiceScoped
    @Provides
    fun audioAttributes() =
        AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build()


    @ServiceScoped
    @Provides
    fun simpleExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ) =
        SimpleExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setTrackSelector(DefaultTrackSelector(context, AdaptiveTrackSelection.Factory()))
            .setHandleAudioBecomingNoisy(true)
            .build()


    @ServiceScoped
    @Provides
    fun dataSourceFactory(@ApplicationContext context: Context) =
        DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, javaClass.simpleName)
        )

}