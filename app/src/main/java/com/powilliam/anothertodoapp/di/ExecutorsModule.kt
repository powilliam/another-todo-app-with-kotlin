package com.powilliam.anothertodoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
@InstallIn(ActivityComponent::class)
object ExecutorsModule {
    @Provides
    fun provideSingleThreadExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }
}