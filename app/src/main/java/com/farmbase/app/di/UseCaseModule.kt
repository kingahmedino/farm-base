package com.farmbase.app.di

import com.farmbase.app.repositories.FormBuilderRepository
import com.farmbase.app.repositories.ProgramConfigRepository
import com.farmbase.app.useCase.FormBuilderUseCases
import com.farmbase.app.useCase.GetFormDataByIdUseCase
import com.farmbase.app.useCase.GetFormDataUseCase
import com.farmbase.app.useCase.GetProgramConfigByIDUseCase
import com.farmbase.app.useCase.GetProgramDataByRolesUseCase
import com.farmbase.app.useCase.InitializeFormStateUseCase
import com.farmbase.app.useCase.StreamlineFormDataUseCase
import com.farmbase.app.useCase.UpdateFormFieldUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetFormDataUseCase(repository: FormBuilderRepository): GetFormDataUseCase {
        return GetFormDataUseCase(repository)
    }

    @Provides
    fun provideInitializeFormStateUseCase(): InitializeFormStateUseCase {
        return InitializeFormStateUseCase()
    }

    @Provides
    fun provideUpdateFormFieldUseCase(): UpdateFormFieldUseCase {
        return UpdateFormFieldUseCase()
    }

    @Provides
    fun provideStreamlineFormDataUseCase(): StreamlineFormDataUseCase {
        return StreamlineFormDataUseCase()
    }

    @Provides
    fun provideGetFormDataByIdUseCase(repository: FormBuilderRepository): GetFormDataByIdUseCase {
        return GetFormDataByIdUseCase(repository)
    }

    @Provides
    fun provideFormBuilderUseCases(
        getFormDataUseCase: GetFormDataUseCase,
        initializeFormStateUseCase: InitializeFormStateUseCase,
        updateFormFieldUseCase: UpdateFormFieldUseCase,
        streamlineFormDataUseCase: StreamlineFormDataUseCase,
        getFormDataByIdUseCase: GetFormDataByIdUseCase
    ): FormBuilderUseCases {
        return FormBuilderUseCases(
            getFormDataUseCase,
            initializeFormStateUseCase,
            updateFormFieldUseCase,
            streamlineFormDataUseCase,
            getFormDataByIdUseCase
        )
    }

    @Provides
    fun provideGetProgramConfigByIDUseCase(repository: ProgramConfigRepository): GetProgramConfigByIDUseCase {
        return GetProgramConfigByIDUseCase(repository)
    }

    @Provides
    fun provideGetProgramDataByRoleUseCase(repository: ProgramConfigRepository): GetProgramDataByRolesUseCase {
        return GetProgramDataByRolesUseCase(repository)
    }
}
