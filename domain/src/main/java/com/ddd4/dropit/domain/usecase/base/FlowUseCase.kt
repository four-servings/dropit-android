package com.ddd4.dropit.domain.usecase.base

import com.ddd4.dropit.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    operator fun invoke(param: P): Flow<Result<R>> {
        return execute(param)
            .catch { error ->
                emit(Result.Error(Exception(error)))
            }
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(param: P): Flow<Result<R>>
}