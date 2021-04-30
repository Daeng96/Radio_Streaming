package akbar.sukku.annashihah.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("chanel")
    fun provideStreamUrl1() : String = "http://radio.an-nashihah.com/live"

    @Singleton
    @Provides
    @Named("chanel2")
    fun provideStreamUrl2() : String = "http://radio.an-nashihah.com/live2"
}