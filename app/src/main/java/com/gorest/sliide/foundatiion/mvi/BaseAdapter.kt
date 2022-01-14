package com.gorest.sliide.foundatiion.mvi

import android.app.Dialog
import android.content.Context
import android.text.format.DateUtils
import android.view.Window
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.gorest.sliide.R
import com.gorest.sliide.main.ui.MainActivity
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseAdapter(var context1: Context) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var loadingDialog:Dialog?=null
/*    fun showMessage(message:String){
        val dialog = Dialog(context1)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        val text = dialog.findViewById(R.id.text_dialog) as TextView
        text.text = message
        val dialogButton: Button = dialog.findViewById(R.id.btn_dialog) as Button
        dialogButton.setOnClickListener{ dialog.dismiss() }
        dialog.show()
    }

    fun showMessageSuccess(message:String){
        val toast = Toast(context1)
        toast.duration = Toast.LENGTH_LONG

        val inflater =
            context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.toast_message, null)
        view.message.text=message
        toast.view = view
        toast.show()
    }*/

    fun String.toExp():String{
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            val time: Long = sdf.parse(this)!!.time
            val now = System.currentTimeMillis()

            var ago: CharSequence =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            if (ago == "0 minutes ago")
                ago = context1.resources.getString(R.string.now)
            ago = ago.toString().replace("days", context1.resources.getString(R.string.days))
            ago = ago.toString().replace("hours", context1.resources.getString(R.string.hours))
            ago = ago.toString().replace("hour", context1.resources.getString(R.string.hour))
            ago = ago.toString().replace("minutes", context1.resources.getString(R.string.minutes))
            ago = ago.toString().replace("minute", context1.resources.getString(R.string.minutes))
            ago = ago.toString().replace("ago", "")
            ago = ago.toString().replace("Yesterday", context1.resources.getString(R.string.yesterday))
            return ago
        }catch (e: java.lang.Exception){
            return ""
        }
    }

    fun loading(){
        loadingDialog = Dialog(activityContext(context1)!!, R.style.Theme_Design_BottomSheetDialog)
        loadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.setCancelable(true)
        loadingDialog!!.setContentView(R.layout.loading)
        val window: Window = loadingDialog!!.window!!
        window.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        if (!(activityContext(context1) as MainActivity).isFinishing) {
            loadingDialog?.show()
        }

    }
    fun stopLoading(){
        if (loadingDialog!=null && !(activityContext(context1) as MainActivity).isFinishing) loadingDialog!!.dismiss()
    }

    fun activityContext(context1: Context): Context? {
        val context = context1
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }
}