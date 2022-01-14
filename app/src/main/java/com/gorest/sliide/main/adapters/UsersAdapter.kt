package com.gorest.sliide.main.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.gorest.sliide.R
import com.gorest.sliide.main.model.Data
import com.gorest.sliide.foundatiion.mvi.BaseAdapter
import com.gorest.sliide.foundatiion.utilz.DataState
import com.gorest.sliide.main.intent.MainScreenIntent
import com.gorest.sliide.main.model.addUser.AddUser
import com.gorest.sliide.main.ui.UsersListFragment
import com.gorest.sliide.main.vm.UsersListViewModel
import kotlinx.android.synthetic.main.users_item.view.*
import kotlinx.android.synthetic.main.users_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class UsersAdapter(var context: Context,val recyclerView: RecyclerView,val viewLifecycleOwner: LifecycleOwner,var usersListFragment: UsersListFragment, var usersListViewModel: UsersListViewModel,var  users: ArrayList<Data>
) : BaseAdapter(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users_item, parent, false)
        return ViewHolder(view)
    }
    var mId :Int = 0
    var mPosition :Int = 0
    var mItemRemovedStatus :Boolean = false
    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        val holder=holder1 as ViewHolder

        holder.mName.text=users[position].name
        holder.mEmail.text=users[position].email
        holder.mGender.text=users[position].gender
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val currentDateandTime: String = sdf.format(Date())
        holder.mTime.text= currentDateandTime.toExp()

        holder.itemView.setOnLongClickListener{
            removeUser(users[position].id,position)
            true
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mName: TextView = itemView.name
        internal var mEmail: TextView = itemView.email
        internal var mGender: TextView = itemView.gender
        internal var mTime: TextView = itemView.time
    }

    fun removeUser(id:Int,position: Int) {
        val dialog = Dialog(activityContext(context)!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.remove_user_dialog)
        val ok: Button = dialog.findViewById(R.id.remove_user) as Button
        val cancel: Button = dialog.findViewById(R.id.cancel) as Button

        ok.setOnClickListener{
            loading()
            mId =id
            mPosition =position
            mItemRemovedStatus=true
            usersListFragment.lifecycleScope.launch {
                usersListViewModel.userIntent.send(MainScreenIntent.RemoveUser(id))
            }

            dialog.hide()
        }

        cancel.setOnClickListener{
            dialog.hide()
        }
        dialog.show()
    }
}