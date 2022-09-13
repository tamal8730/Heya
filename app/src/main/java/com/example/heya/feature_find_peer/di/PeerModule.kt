package com.example.heya.feature_find_peer.di

import com.example.heya.core.util.TimestampFormatter
import com.example.heya.feature_find_peer.repository.peers.PeersRepository
import com.example.heya.feature_find_peer.repository.peers.impl.PeersRepositoryImpl
import com.example.heya.feature_find_peer.repository.users.UsersRepository
import com.example.heya.feature_find_peer.repository.users.impl.UsersRepositoryImpl
import com.example.heya.feature_find_peer.util.last_message_timestamp_formatter.TimeOrDateTimestampFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PeerModule {

    @Singleton
    @Provides
    fun providePeersRepository(): PeersRepository {
        return PeersRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideUsersRepository(): UsersRepository {
        return UsersRepositoryImpl()
    }

    @Singleton
    @Provides
    @Named("last_message_timestamp_formatter")
    fun provideLastMessageTimestampFormatter(): TimestampFormatter {
        return TimeOrDateTimestampFormatter()
    }

}