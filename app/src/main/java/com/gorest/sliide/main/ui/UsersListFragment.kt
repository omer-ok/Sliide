package com.gorest.sliide.main.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gorest.sliide.R
import com.gorest.sliide.main.model.Data
import com.gorest.sliide.main.model.Users
import com.gorest.sliide.foundatiion.mvi.BaseFragment
import com.gorest.sliide.foundatiion.utilz.DataState
import com.gorest.sliide.main.adapters.UsersAdapter
import com.gorest.sliide.main.intent.MainScreenIntent
import com.gorest.sliide.main.model.addUser.AddUser
import com.gorest.sliide.main.repository.MainRepo
import com.gorest.sliide.main.vm.UsersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.users_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class UsersListFragment : BaseFragment(R.layout.users_list_fragment) {

    companion object {
        fun newInstance() = UsersListFragment()
    }

    @Inject
    lateinit var mainRepo: MainRepo
    lateinit var usersListViewModel: UsersListViewModel
    var usersList=ArrayList<Data>()
    lateinit var adapter: UsersAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersListViewModel = UsersListViewModel(mainRepo)
        loading()
        lifecycleScope.launch {
            usersListViewModel.userIntent.send(MainScreenIntent.GetUsers())
        }
        subscribeObservers()
        linearLayoutManager = LinearLayoutManager(context)
        users.layoutManager = linearLayoutManager
        adapter = UsersAdapter(activityContext(),users,viewLifecycleOwner,this@UsersListFragment,usersListViewModel,usersList)
        users.adapter = adapter
        addUserFab.setOnClickListener {
            loading()
            addUser()
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun subscribeObservers() {
        usersListViewModel.usersListResponse.observe(
            viewLifecycleOwner,
            Observer { usersListResponse ->
                when (usersListResponse) {
                    is DataState.Success<Users> -> {
                        stopLoading()
                        if (usersListResponse.data.data.isNotEmpty()) {
                            noItems.visibility= View.GONE
                            for (user in usersListResponse.data.data) {
                                usersList.add(user)
                                adapter.notifyItemInserted(usersList.size)
                            }
                        }
                        if (usersList.size==0)
                            noItems.visibility= View.VISIBLE
                    }
                    is DataState.Error -> {
                        stopLoading()
                        Toast.makeText(context, usersListResponse.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> {

                    }
                }
            })

        usersListViewModel.addUserResponse.observe(
            viewLifecycleOwner,
            Observer { addUserResponse ->
                when (addUserResponse) {
                    is DataState.Success<AddUser> -> {
                        stopLoading()
                        noItems.visibility= View.GONE
                        usersList.add(addUserResponse.data.data)
                        adapter.notifyItemInserted(usersList.size)

                        if (usersList.size==0)
                            noItems.visibility= View.VISIBLE
                    }
                    is DataState.Error -> {
                        stopLoading()
                        Toast.makeText(context, addUserResponse.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> {

                    }
                }
            })
        usersListViewModel.removeUserResponse.observe(
            viewLifecycleOwner,
            Observer { removeUserResponse ->
                when (removeUserResponse) {
                    is DataState.Success<retrofit2.Response<Unit>> -> {
                        adapter.stopLoading()
                        if(adapter.mItemRemovedStatus){
                            usersList.removeAt(adapter.mPosition)
                            adapter.notifyItemRemoved(adapter.mPosition)
                            adapter.notifyItemRangeChanged(adapter.mPosition,usersList.size)
                            adapter.mItemRemovedStatus =false
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is DataState.Error -> {
                        Toast.makeText(context, removeUserResponse.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> {

                    }
                }
            })
    }

    fun addUser() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_user_dialog)
        val name = dialog.findViewById(R.id.name) as EditText
        val email = dialog.findViewById(R.id.email) as EditText
        val gender = dialog.findViewById(R.id.gender) as EditText
        val addUser: Button = dialog.findViewById(R.id.add_user) as Button

        addUser.setOnClickListener{
            lifecycleScope.launch {
                usersListViewModel.userIntent.send(MainScreenIntent.AddUser(name.text.toString(),email.text.toString(),gender.text.toString(),"Active"))
            }
            dialog.hide()
        }
        dialog.show()
    }

}