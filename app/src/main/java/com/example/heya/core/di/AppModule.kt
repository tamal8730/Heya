package com.example.heya.core.di

import com.example.heya.core.message_listener.MessageListener
import com.example.heya.core.message_listener.impl.FakeMessageListenerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMessageListener(): MessageListener {
        return FakeMessageListenerImpl()
    }

}