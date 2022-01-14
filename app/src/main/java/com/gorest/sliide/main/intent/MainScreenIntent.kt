package com.gorest.sliide.main.intent

sealed class MainScreenIntent {
    class GetUsers() : MainScreenIntent()
    class AddUser(val name:String,val email:String,val gender:String,val status:String): MainScreenIntent()
    class RemoveUser(val id:Int): MainScreenIntent()
}