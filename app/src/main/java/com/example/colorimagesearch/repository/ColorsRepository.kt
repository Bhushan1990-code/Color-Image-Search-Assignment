package com.example.colorimagesearch.repository

import androidx.lifecycle.MutableLiveData
import com.example.colorimagesearch.interfaces.NetworkResponseCallback
import com.example.colorimagesearch.model.Colors
import com.example.colorimagesearch.network.RestClient
import com.example.colorimagesearch.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ColorsRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback
    private var mColorsList: MutableLiveData<List<Colors>> =
        MutableLiveData<List<Colors>>().apply { value = emptyList() }

    companion object {
        private var mInstance: ColorsRepository? = null
        fun getInstance(): ColorsRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = ColorsRepository()
                }
            }
            return mInstance!!
        }
    }


    private lateinit var mColorsCall: Call<List<Colors>>

    fun getColors(callback: NetworkResponseCallback, forceFetch : Boolean): MutableLiveData<List<Colors>> {
        mCallback = callback
        if (mColorsList.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return mColorsList
        }
        
        var keywords= MainActivity.serach_keyword
        mColorsCall = RestClient.getInstance().getApiService().getColors(keywords,"json","20")
        mColorsCall.enqueue(object : Callback<List<Colors>> {

            override fun onResponse(call: Call<List<Colors>>, response: Response<List<Colors>>) {
                mColorsList.value = response.body()
                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<List<Colors>>, t: Throwable) {
                mColorsList.value = emptyList()
                mCallback.onNetworkFailure(t)
            }

        })
        return mColorsList
    }
}