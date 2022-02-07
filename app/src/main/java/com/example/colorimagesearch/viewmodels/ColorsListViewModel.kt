package com.example.colorimagesearch.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.colorimagesearch.interfaces.NetworkResponseCallback
import com.example.colorimagesearch.model.Colors
import com.example.colorimagesearch.repository.ColorsRepository
import com.example.colorimagesearch.utils.NetworkHelper

class ColorsListViewModel(private val app: Application) : AndroidViewModel(app) {
    private var mList: MutableLiveData<List<Colors>> =
        MutableLiveData<List<Colors>>().apply { value = emptyList() }
    val mShowProgressBar = MutableLiveData(true)
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    val mShowApiError = MutableLiveData<String>()
    private var mRepository = ColorsRepository.getInstance()

    fun fetchColorsFromServer(forceFetch: Boolean): MutableLiveData<List<Colors>> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            mList = mRepository.getColors(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

                override fun onNetworkSuccess() {
                    mShowProgressBar.value = false
                }
            }, forceFetch)
        } else {
            mShowNetworkError.value = true
        }
        return mList
    }

    fun onRefreshClicked(view: View) {
        fetchColorsFromServer( true)
    }
}