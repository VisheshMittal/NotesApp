package com.vm.vishunotesapp.di

import com.vm.vishunotesapp.repository.NoteRepository
import com.vm.vishunotesapp.repository.RemoteNoteRepository
import com.vm.vishunotesapp.repository.UserRepository
import com.vm.vishunotesapp.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserRepositoryModule {
    @Binds
    abstract fun providesUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class NoteRepositoryModule {
    @Binds
    abstract fun providesNoteRepository(remoteNoteRepository: RemoteNoteRepository): NoteRepository
}