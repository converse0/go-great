package com.masuta.gogreat.di

import com.masuta.gogreat.data.remote.Client
import com.masuta.gogreat.data.repository.AuthRepositoryImpl
import com.masuta.gogreat.data.repository.ProfileRepositoryImpl
import com.masuta.gogreat.domain.handlers.CreateUserParams
import com.masuta.gogreat.domain.handlers.GetUserParams
import com.masuta.gogreat.domain.handlers.Login
import com.masuta.gogreat.domain.handlers.SignUp
import com.masuta.gogreat.domain.repository.AuthRepository
import com.masuta.gogreat.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetUserParamsUseCase(repository: ProfileRepository): GetUserParams {
        return GetUserParams(repository)
    }

    @Provides
    @Singleton
    fun provideCreateUserParams(repository: ProfileRepository): CreateUserParams {
        return CreateUserParams(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): Login {
        return Login(repository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthRepository): SignUp {
        return SignUp(repository)
    }

    @Provides
    @Singleton
    fun provideClient(): Client {
        return Client()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(client: Client): AuthRepository {
        return AuthRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(client: Client): ProfileRepository {
        return ProfileRepositoryImpl(client)
    }

}
