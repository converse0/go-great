package com.masuta.gogreat.di

import android.content.Context
import com.masuta.gogreat.core.http.Client
import com.masuta.gogreat.core.providers.AuthRepositoryImpl
import com.masuta.gogreat.core.providers.ProfileRepositoryImpl
import com.masuta.gogreat.core.providers.TrainRepositoryImpl
import com.masuta.gogreat.core.service.auth_service.AuthService
import com.masuta.gogreat.core.service.auth_service.AuthServiceImpl
import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.core.service.profile_service.ProfileServiceImpl
import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.service.train_service.TrainServiceImpl
import com.masuta.gogreat.core.store.*
import com.masuta.gogreat.domain.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.domain.handlers.auth_handlers.GetToken
import com.masuta.gogreat.domain.handlers.auth_handlers.SignIn
import com.masuta.gogreat.domain.handlers.auth_handlers.SignUp
import com.masuta.gogreat.domain.handlers.profile_handlers.*
import com.masuta.gogreat.domain.handlers.train_handlers.*
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
    fun provideAuthStore(@ApplicationContext context: Context): AuthStore {
        return AuthStoreImpl(context)
    }

    @Provides
    @Singleton
    fun provideTrainStore(@ApplicationContext context: Context): TrainStore {
        return TrainStoreImpl(context)
    }

    @Provides
    @Singleton
    fun provideProfileStore(): ProfileStore {
        return ProfileStoreImpl()
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
    fun provideTrainRepository(client: Client, @ApplicationContext context: Context): TrainRepository {
        return TrainRepositoryImpl(client, context)
    }

    @Provides
    @Singleton
    fun provideListValuesForSliders(@ApplicationContext context: Context): ListsValuesForSliders {
        return ListsValuesForSliders(context)
    }

    @Provides
    @Singleton
    fun provideGetToken(store: AuthStore): GetToken {
        return GetToken(store)
    }

    @Provides
    @Singleton
    fun provideProfileService(repository: ProfileRepository, store: ProfileStore): ProfileService {
        return ProfileServiceImpl(repository, store)
    }

    @Provides
    @Singleton
    fun provideAuthService(repository: AuthRepository, store: AuthStore): AuthService {
        return AuthServiceImpl(repository, store)
    }

    @Provides
    @Singleton
    fun provideTrainService(repository: TrainRepository, store: TrainStore): TrainService {
        return TrainServiceImpl(repository, store)
    }

    @Provides
    @Singleton
    fun provideAuthHandlers(authService: AuthService, store: AuthStore): AuthHandlers {
        return AuthHandlers(
            getToken = GetToken(store),
            signup = SignUp(authService),
            signin = SignIn(authService)
        )
    }

    @Provides
    @Singleton
    fun provideProfileHandlers(profileService: ProfileService): ProfileHandlers {
        return ProfileHandlers(
            createParameters = CreateParameters(profileService),
            updateParameters = UpdateParameters(profileService),
            getParameters = GetParameters(profileService),
            uploadImage = UploadImage(profileService)
        )
    }

    @Provides
    @Singleton
    fun provideTrainHandlers(trainService: TrainService): TrainHandlers {
        return TrainHandlers(
            endTraining = EndTraining(trainService),
            finishTraining = FinishTraining(trainService),
            startTraining = StartTraining(trainService),
            getCurrentWorkout = GetCurrentWorkout(trainService),
            getPastWorkouts = GetPastWorkouts(trainService),
            getWorkouts = GetWorkouts(trainService),
            getTraining = GetTraining(trainService),
            getExercisesById = GetExercisesById(trainService),
            saveWorkout = SaveWorkout(trainService),
            setExerciseParameters = SetExerciseParameters(trainService)
        )
    }

}
