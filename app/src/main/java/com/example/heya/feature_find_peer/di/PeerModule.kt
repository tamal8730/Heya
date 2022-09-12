package com.example.heya.feature_find_peer.di

import com.example.heya.feature_find_peer.repository.peers.PeersRepository
import com.example.heya.feature_find_peer.repository.peers.impl.PeersRepositoryImpl
import com.example.heya.feature_find_peer.repository.users.UsersRepository
import com.example.heya.feature_find_peer.repository.users.impl.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

}