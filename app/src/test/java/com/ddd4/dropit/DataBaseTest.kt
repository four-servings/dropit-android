package com.ddd4.dropit

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ddd4.dropit.data.entity.DataEntity
import com.ddd4.dropit.data.repository.DropitRepositoryImpl
import com.ddd4.dropit.data.source.local.LocalDataSource
import com.ddd4.dropit.data.source.local.LocalDataSourceImpl
import com.ddd4.dropit.data.source.local.preferences.SharedPrefHelper
import com.ddd4.dropit.data.source.local.room.AppDataBase
import com.ddd4.dropit.data.source.local.room.DatabaseDao
import com.ddd4.dropit.domain.entity.DomainEntity
import com.ddd4.dropit.domain.getValue
import com.ddd4.dropit.domain.repository.DropitRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.*

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
class DataBaseTest {

    private lateinit var db: AppDataBase
    private lateinit var databaseDao: DatabaseDao

    private val folderId = 0L

    private val item = DataEntity.Item(
            null,
            folderId,
            0,
            0,
            0,
            FOLDER_NAME,
            "image",
            Date(),
            Date()
    )

    @Mock
    private lateinit var dropitRepository: DropitRepository
    private var sharedHelper = mock(SharedPrefHelper::class.java)
    private lateinit var dropitDataSource: LocalDataSource

    @Before
    fun initialize() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
                context, AppDataBase::class.java
        ).build()

        databaseDao = db.databaseDao()

        dropitDataSource = LocalDataSourceImpl(databaseDao, sharedHelper, context)
        dropitRepository = DropitRepositoryImpl(dropitDataSource)
    }

    @Test
    fun `Test Section In DataSource`() = runBlocking {
        dropitDataSource.setSectionFromJson()
        val result = dropitDataSource.getCategoryItems().getValue()[0].title

        Assert.assertEquals(result, FIRST_CATEGORY_NAME)
    }

    @Test
    @Throws(Exception::class)
    fun `아이템이 잘 불러와지는지 테스트`() = runBlocking {
        val result = dropitRepository.getItemsByFolder(folderId)
                .getValue()
                .get(0)
                .name

        Assert.assertEquals(result, FOLDER_NAME)
    }

    @After
    @Throws(IOException::class)
    fun destroy() {
        db.close()
    }


    companion object {
        private const val FOLDER_NAME = "folder test"
        private const val FIRST_CATEGORY_NAME = "스킨케어"
    }
}