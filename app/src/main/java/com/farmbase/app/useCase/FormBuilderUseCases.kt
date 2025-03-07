package com.farmbase.app.useCase

data class FormBuilderUseCases(
    val getFormDataUseCase: GetFormDataUseCase,
    val initializeFormStateUseCase: InitializeFormStateUseCase,
    val updateFormFieldUseCase: UpdateFormFieldUseCase,
    val streamlineFormDataUseCase: StreamlineFormDataUseCase,
    val getFormDataByIdUseCase: GetFormDataByIdUseCase
)