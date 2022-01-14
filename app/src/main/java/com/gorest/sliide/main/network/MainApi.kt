package com.gorest.sliide.main.network

import com.gorest.sliide.main.model.Data
import com.gorest.sliide.main.model.Users
import com.gorest.sliide.main.model.addUser.AddUser
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

interface MainApi {
    @GET("public/v1/users")
    suspend fun getUsers(@Query("page") page: Int) : Users

    @POST("public/v1/users")
    suspend fun addUser(@Header("Authorization") token:String, @Query("name") name: String, @Query("email") email:String, @Query("gender" ) gender:String, @Query("status" ) status:String) : AddUser

    @DELETE("public/v1/users/{id}")
    suspend fun removeUser(@Header("Authorization") token:String, @Path("id") id:Int) :retrofit2.Response<Unit>
}