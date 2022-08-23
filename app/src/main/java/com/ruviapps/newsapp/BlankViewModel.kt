package com.ruviapps.newsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruviapps.newsapp.retrofit.NewsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlankViewModel : ViewModel() {
private var _response = MutableLiveData<String>()
    val response : LiveData<String>
    get() = _response

    init {
        getNewsApi()
    }
    private fun getNewsApi(){
        NewsApi.retrofitService.getNews("bitcoin","2204d222009b4623a4e662dd932bb287").enqueue(
            object  : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = response.body()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value= "Failure : " + t.message
                }
            }
        )

    }

}