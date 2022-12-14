package com.masuta.gogreat.di

import android.content.Context
import com.masuta.gogreat.core.handlers.ErrorHandler
import com.masuta.gogreat.core.http.Client
import com.masuta.gogreat.core.providers.AuthImpl
import com.masuta.gogreat.core.providers.ProfileImpl
import com.masuta.gogreat.core.providers.TrainImpl
import com.masuta.gogreat.core.service.auth_service.AuthService
import com.masuta.gogreat.core.service.auth_service.AuthServiceImpl
import com.masuta.gogreat.core.service.profile_service.ProfileService
import com.masuta.gogreat.core.service.profile_service.ProfileServiceImpl
import com.masuta.gogreat.core.service.train_service.TrainService
import com.masuta.gogreat.core.service.train_service.TrainServiceImpl
import com.masuta.gogreat.core.store.*
import com.masuta.gogreat.core.handlers.auth_handlers.AuthHandlers
import com.masuta.gogreat.core.handlers.auth_handlers.GetToken
import com.masuta.gogreat.core.handlers.auth_handlers.SignIn
import com.masuta.gogreat.core.handlers.auth_handlers.SignUp
import com.masuta.gogreat.core.handlers.profile_handlers.*
import com.masuta.gogreat.core.handlers.train_handlers.*
import com.masuta.gogreat.core.providers.Auth
import com.masuta.gogreat.core.providers.Profile
import com.masuta.gogreat.core.providers.Train
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
    fun provideAuthRepository(client: Client, @ApplicationContext context: Context): Auth {
        return AuthImpl(client, context)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(client: Client, @ApplicationContext context: Context): Profile {
        return ProfileImpl(client, context)
    }

    @Provides
    @Singleton
    fun provideTrainRepository(client: Client, @ApplicationContext context: Context): Train {
        return TrainImpl(client, context)
    }

    @Provides
    @Singleton
    fun provideListValuesForSliders(@ApplicationContext context: Context): ListsValuesForSliders {
        return ListsValuesForSliders(context)
    }

    @Provides
    @Singleton
    fun provideProfileService(repository: Profile, store: ProfileStore): ProfileService {
        return ProfileServiceImpl(repository, store)
    }

    @Provides
    @Singleton
    fun provideAuthService(repository: Auth, store: AuthStore): AuthService {
        return AuthServiceImpl(repository, store)
    }

    @Provides
    @Singleton
    fun provideTrainService(repository: Train, store: TrainStore): TrainService {
        return TrainServiceImpl(repository, store)
    }

    @Provides
    @Singleton
    fun provideAuthHandlers(authService: AuthService, @ApplicationContext context: Context): AuthHandlers {
        return AuthHandlers(
            getToken = GetToken(authService),
            signup = SignUp(authService),
            signin = SignIn(authService),
            errorHandler = ErrorHandler(context)
        )
    }

    @Provides
    @Singleton
    fun provideProfileHandlers(profileService: ProfileService, @ApplicationContext context: Context): ProfileHandlers {
        return ProfileHandlers(
            createParameters = CreateParameters(profileService),
            updateParameters = UpdateParameters(profileService),
            getParameters = GetParameters(profileService),
            uploadImage = UploadImage(profileService),
            errorHandler = ErrorHandler(context)
        )
    }

    @Provides
    @Singleton
    fun provideTrainHandlers(trainService: TrainService, @ApplicationContext context: Context): TrainHandlers {
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
            setExerciseParameters = SetExerciseParameters(trainService),
            clearExerciseData = ClearExerciseData(trainService),
            saveExercise = SaveExercise(trainService),
            getAllExercise = GetAllExercise(trainService),
            getTrainingByUid = GetTrainingByUid(trainService),
            setCurrentExerciseAndSets = SetCurrentExerciseAndSets(trainService),
            errorHandler = ErrorHandler(context)
        )
    }
}
