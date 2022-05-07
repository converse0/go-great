package com.masuta.gogreat.di

import android.content.Context
import com.masuta.gogreat.data.http.Client
import com.masuta.gogreat.data.providers.AuthRepositoryImpl
import com.masuta.gogreat.data.providers.ProfileRepositoryImpl
import com.masuta.gogreat.data.providers.TrainRepositoryImpl
import com.masuta.gogreat.data.store.*
import com.masuta.gogreat.domain.handlers.auth_handlers.SignIn
import com.masuta.gogreat.domain.handlers.auth_handlers.SignUp
import com.masuta.gogreat.domain.handlers.profile_handlers.CreateParameters
import com.masuta.gogreat.domain.handlers.profile_handlers.GetParameters
import com.masuta.gogreat.domain.handlers.profile_handlers.UpdateParameters
import com.masuta.gogreat.domain.handlers.profile_handlers.UploadImage
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
    fun provideSignUp(repository: AuthRepository): SignUp {
        return SignUp(repository)
    }

    @Provides
    @Singleton
    fun provideSignIn(repository: AuthRepository, store: AuthStore): SignIn {
        return SignIn(repository, store)
    }

    @Provides
    @Singleton
    fun provideCreateParameters(repository: ProfileRepository): CreateParameters {
        return CreateParameters(repository)
    }

    @Provides
    @Singleton
    fun provideGetParameters(repository: ProfileRepository, store: ProfileStore): GetParameters {
        return GetParameters(repository, store)
    }

    @Provides
    @Singleton
    fun provideUpdateParameters(repository: ProfileRepository, store: ProfileStore): UpdateParameters {
        return UpdateParameters(repository, store)
    }

    @Provides
    @Singleton
    fun provideUploadImage(repository: ProfileRepository): UploadImage {
        return UploadImage(repository)
    }

    @Provides
    @Singleton
    fun provideGetWorkouts(repository: TrainRepository, store: TrainStore): GetWorkouts {
        return GetWorkouts(repository, store)
    }

    @Provides
    @Singleton
    fun provideGetCurrentWorkout(repository: TrainRepository, store: TrainStore): GetCurrentWorkout {
        return GetCurrentWorkout(repository, store)
    }

    @Provides
    @Singleton
    fun provideGetPastWorkouts(repository: TrainRepository, store: TrainStore): GetPastWorkouts {
        return GetPastWorkouts(repository, store)
    }

    @Provides
    @Singleton
    fun provideSaveWorkout(repository: TrainRepository): SaveWorkout {
        return SaveWorkout(repository)
    }

    @Provides
    @Singleton
    fun provideGerExerciseById(repository: TrainRepository): GetExercisesById {
        return GetExercisesById(repository)
    }

    @Provides
    @Singleton
    fun provideStartTraining(repository: TrainRepository, store: TrainStore): StartTraining {
        return StartTraining(repository, store)
    }

    @Provides
    @Singleton
    fun provideSetExerciseParameters(repository: TrainRepository, store: TrainStore): SetExerciseParameters {
        return SetExerciseParameters(repository, store)
    }

    @Provides
    @Singleton
    fun provideFinishTraining(repository: TrainRepository): FinishTraining {
        return FinishTraining(repository)
    }

    @Provides
    @Singleton
    fun provideEndTraining(repository: TrainRepository, store: TrainStore): EndTraining {
        return EndTraining(repository, store)
    }

}
