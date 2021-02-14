package com.ddd4.dropit.domain.usecase

import com.ddd4.dropit.domain.repository.DropitRepository
import com.ddd4.dropit.domain.Result
import com.ddd4.dropit.domain.entity.DomainEntity
import com.ddd4.dropit.domain.usecase.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Exception

class GetFoldersUseCase(
    private val repository: DropitRepository,
    coroutineDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, List<DomainEntity.Folder>>(coroutineDispatcher) {

    override suspend fun execute(param: Unit): List<DomainEntity.Folder> {
        return when(val response = repository.getFolderItems()) {
            is Result.Success -> response.data
            is Result.Error -> throw response.exception
        }
    }
}
