package com.example.heya.feature_chat.di

import com.example.heya.core.util.TimestampFormatter
import com.example.heya.feature_auth.repository.auth.impl.AuthRepositoryImpl
import com.example.heya.feature_chat.repository.chat.ChatRepository
import com.example.heya.feature_chat.repository.chat.impl.ChatRepositoryImpl
import com.example.heya.feature_chat.util.chat_bubble_timestamp_formatter.DateTimeFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Singleton
    @Provides
    fun provideChatRepository(): ChatRepository {
        return ChatRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("chat_bubble_timestamp_formatter")
    fun provideChatBubbleTimestampFormatter(): TimestampFormatter {
        return DateTimeFormatter()
    }

}