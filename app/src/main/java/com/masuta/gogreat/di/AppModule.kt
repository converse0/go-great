package com.masuta.gogreat.di

import android.content.Context
import com.masuta.gogreat.data.http.Client
import com.masuta.gogreat.data.providers.AuthRepositoryImpl
import com.masuta.gogreat.data.providers.ProfileRepositoryImpl
import com.masuta.gogreat.data.providers.TrainRepositoryImpl
import com.masuta.gogreat.data.store.Store
import com.masuta.gogreat.data.store.StoreImpl
import com.masuta.gogreat.domain.repository.AuthRepository
import com.masuta.gogreat.domain.repository.ProfileRepository
import com.masuta.gogreat.domain.repository.TrainRepository
import com.masuta.gogreat.utils.ListsValuesForSliders
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStore(@ApplicationContext context: Context): Store {
        return StoreImpl(context)
    }

    @Provides
    @Singleton
    fun provideClient(): Client {
        return Client()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(client: Client, @ApplicationContext context: Context): AuthRepository {
        return AuthRepositoryImpl(client, context)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(client: Client, @ApplicationContext context: Context): ProfileRepository {
        return ProfileRepositoryImpl(client, context)
    }

    @Provides
    @Singleton
    fun provideTrainRepository(client: Client, @ApplicationContext context: Context, store: Store): TrainRepository {
        return TrainRepositoryImpl(client, context, store)
    }

    @Provides
    @Singleton
    fun provideListValuesForSliders(@ApplicationContext context: Context): ListsValuesForSliders {
        return ListsValuesForSliders(context)
    }

}
