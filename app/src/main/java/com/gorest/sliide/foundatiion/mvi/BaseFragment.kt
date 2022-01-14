package com.gorest.sliide.foundatiion.mvi

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateUtils
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.gorest.sliide.R
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.time.Instant.now
import java.util.*

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
open class BaseFragment(layout:Int):Fragment(layout){
    var country = ""
    var city = ""
    var loadingDialog:Dialog?=null
    var onForground = false

    fun loading(){
        loadingDialog = Dialog(activityContext(), R.style.Theme_Design_BottomSheetDialog)
        loadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.setCancelable(true)
        loadingDialog!!.setContentView(R.layout.loading)
        val window: Window = loadingDialog!!.window!!
        window.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        if (!requireActivity().isFinishing) {
            loadingDialog?.show()
        }

    }
    fun stopLoading(){
        if (loadingDialog!=null && !requireActivity().isFinishing) loadingDialog!!.dismiss()
    }
    fun String.toExp():String{
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            val time: Long = sdf.parse(this)!!.time
            val now = System.currentTimeMillis()

            var ago: CharSequence =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            if (ago == "0 minutes ago")
                ago = requireContext().resources.getString(R.string.now)
            ago = ago.toString().replace("days", requireContext().resources.getString(R.string.days))
            ago = ago.toString().replace("hours", requireContext().resources.getString(R.string.hours))
            ago = ago.toString().replace("hour", requireContext().resources.getString(R.string.hour))
            ago = ago.toString().replace("minutes", requireContext().resources.getString(R.string.minutes))
            ago = ago.toString().replace("minute", requireContext().resources.getString(R.string.minute))
            ago = ago.toString().replace("ago", "")
            ago = ago.toString().replace("Yesterday", requireContext().resources.getString(R.string.yesterday))
            return ago
        }catch (e: java.lang.Exception){
            return ""
        }
    }
    fun activityContext(): Context {
        val context = context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context!!
    }


}
