package com.ddd4.dropit.domain.usecase.base

import com.ddd4.dropit.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(param: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(param).let {
                    Result.Success(it)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    protected abstract suspend fun execute(param: P): R
}