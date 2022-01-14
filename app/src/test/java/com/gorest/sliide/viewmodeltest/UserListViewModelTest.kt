package com.gorest.sliide.viewmodeltest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import com.gorest.sliide.main.vm.UsersListViewModel
import com.gorest.sliide.viewmodeltest.rules.MainCoroutinesRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.gorest.sliide.foundatiion.utilz.DataState
import com.gorest.sliide.main.model.Users
import com.gorest.sliide.main.network.MainApi

import org.mockito.Mock

import androidx.lifecycle.LifecycleRegistry
import com.gorest.sliide.main.model.addUser.AddUser
import com.gorest.sliide.main.repository.MainRepo
import io.reactivex.Single
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.mockito.Mockito
import org.mockito.Mockito.*

import org.mockito.MockitoAnnotations
import java.lang.Exception


@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
@RunWith(JUnit4::class)
class UserListViewModelTest {

    private lateinit var usersListViewModel: UsersListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Mock
    var mainApi: MainApi? = null

    @Mock
    var observer: Observer<DataState<Users>>? = null

    @Mock
    var users:Users?=null

    @Mock
    var lifecycleOwner: LifecycleOwner? = null
    var lifecycle: Lifecycle? = null

    @Mock
    private lateinit var mainRepo:MainRepo

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner!!)
        usersListViewModel = UsersListViewModel(mainRepo)
        usersListViewModel.usersListResponse.observeForever(observer!!)
    }

    @Test
    suspend fun testNull() {
        `when`(mainApi?.getUsers(1)).thenReturn(null)
        assertNotNull(usersListViewModel.getUsers())
        assertTrue(usersListViewModel.usersListResponse.hasObservers())
    }

    @Test
    suspend fun testApiFetchDataSuccess() {
        // Mock API response
        `when`(mainApi?.getUsers(1)).thenReturn(users)
        usersListViewModel.getUsers()
        verify(observer)?.onChanged(DataState.Loading)
        //verify(observer)?.onChanged(DataState.Success(users))
    }

    @Test
    suspend fun testApiFetchDataError() {
        //`when`(mainApi?.getUsers(1)).thenReturn(Single.error(Throwable("Api error")))
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mainApi = null
    }
}