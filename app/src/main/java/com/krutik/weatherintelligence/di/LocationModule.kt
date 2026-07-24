package com.krutik.weatherintelligence.di

import com.krutik.weatherintelligence.core.location.DefaultLocationTracker
import com.krutik.weatherintelligence.core.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        impl: DefaultLocationTracker
    ): LocationTracker
}
