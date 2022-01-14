package com.gorest.sliide.main.repository

import android.util.Log
import com.gorest.sliide.main.model.Users
import com.gorest.sliide.foundatiion.utilz.DataState
import com.gorest.sliide.main.model.Data
import com.gorest.sliide.main.model.addUser.AddUser
import com.gorest.sliide.main.network.MainApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

class MainRepo
@Inject
constructor(private val mainApi: MainApi) {

    fun getUsers(): Flow<DataState<Users>> = flow {
        emit(DataState.Loading)
        try {
            var lastPage = 1
            val responseForPage: Users = mainApi.getUsers(lastPage)
            lastPage =responseForPage.meta.pagination.page
            val response: Users = mainApi.getUsers(lastPage)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Excep",e.toString())
            emit(DataState.Error(e))
        }
    }

    fun addUsers(name:String, email:String,gender:String,status:String): Flow<DataState<AddUser>> = flow {
        emit(DataState.Loading)
        try {
            val token = "Bearer de0528924b9c8da4d9a66453c2f1ded35ae026a832ce1906cfa46607ae5be8d0"
            val response: AddUser = mainApi.addUser(token,name,email,gender,status)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Excep",e.toString())
            emit(DataState.Error(e))
        }
    }

    fun removeUsers(id:Int): Flow<DataState<retrofit2.Response<Unit>>> = flow {
        emit(DataState.Loading)
        try {
            val token = "Bearer de0528924b9c8da4d9a66453c2f1ded35ae026a832ce1906cfa46607ae5be8d0"
            val response: retrofit2.Response<Unit> = mainApi.removeUser(token,id)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Excep",e.toString())
            emit(DataState.Error(e))
        }
    }
}