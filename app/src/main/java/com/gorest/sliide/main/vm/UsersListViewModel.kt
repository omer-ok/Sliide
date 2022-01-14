package com.gorest.sliide.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorest.sliide.main.model.Users
import com.gorest.sliide.foundatiion.utilz.DataState
import com.gorest.sliide.main.intent.MainScreenIntent
import com.gorest.sliide.main.model.Data
import com.gorest.sliide.main.model.addUser.AddUser
import com.gorest.sliide.main.repository.MainRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.ResponseBody

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class UsersListViewModel
constructor(
    private val mainRepo: MainRepo
): ViewModel() {
    val userIntent = Channel<MainScreenIntent>(Channel.UNLIMITED)

    private val _usersListResponse: MutableLiveData<DataState<Users>> = MutableLiveData()
    val usersListResponse: LiveData<DataState<Users>>
        get() = _usersListResponse

    private val _addUserResponse: MutableLiveData<DataState<AddUser>> = MutableLiveData()
    val addUserResponse: LiveData<DataState<AddUser>>
        get() = _addUserResponse

    private val _removeUserResponse: MutableLiveData<DataState<retrofit2.Response<Unit>>> = MutableLiveData()
    val removeUserResponse: LiveData<DataState<retrofit2.Response<Unit>>>
        get() = _removeUserResponse


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainScreenIntent.GetUsers -> getUsers()
                    is MainScreenIntent.AddUser -> addUser(it.name,it.email,it.gender,it.status)
                    is MainScreenIntent.RemoveUser -> removeUser(it.id)
                }
            }
        }
    }
    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    fun getUsers() {
        viewModelScope.launch {
            mainRepo.getUsers()
                .onEach { dataState ->
                    _usersListResponse.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    fun addUser(name:String,email:String,gender:String,status:String) {
        viewModelScope.launch {
            mainRepo.addUsers(name,email,gender,status)
                .onEach { dataState ->
                    _addUserResponse.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    fun removeUser(id:Int) {
        viewModelScope.launch {
            mainRepo.removeUsers(id)
                .onEach { dataState ->
                    _removeUserResponse.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}