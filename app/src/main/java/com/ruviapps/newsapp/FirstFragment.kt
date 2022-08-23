package com.ruviapps.newsapp

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ruviapps.newsapp.databinding.FragmentFirstBinding
import com.ruviapps.newsapp.remote.dto.ResponseFromNetwork
import com.ruviapps.newsapp.utility.NetworkUtility.Companion.NETWORK_TAG
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

import io.ktor.http.*
import io.ktor.http.auth.*

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    fun getNews() : String{
        val apiKey = "2204d222009b4623a4e662dd932bb287"
        val client = OkHttpClient()
        var output = ""
        val newApiString = Uri.parse("https://newsapi.org/v2/everything")
            .buildUpon()
            .appendQueryParameter("q","bitcoin")
            .appendQueryParameter("apiKey",apiKey)
            .build().toString()
        val request = Request.Builder()
            .url(newApiString).build()
        try {
                val response = client.newCall(request).execute()
            output = response.body.toString()

        }catch (ex :HttpException){
            Log.e(NETWORK_TAG,"Error :$ex")
        }
          return output
    }
    private val BASE_URL = "https://newsapi.org"
    private val apikey = "2204d222009b4623a4e662dd932bb287"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient(Android){
            install(Logging){
                level = LogLevel.ALL
            }
            install(JsonFeature){

                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        prettyPrint= true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }

                )
            }
             engine {
                connectTimeout = 150_00
                socketTimeout = 100_00
                parseAuthorizationHeader(apikey)
            }
        }
        lifecycleScope.launchWhenResumed {
              val response : HttpResponse = client.get(BASE_URL){
                  contentType(ContentType.Application.Json)
                  url {
                      path("/v2/everything")
                    parameters.append("q","bitcoin")
                   // parameters.append("apiKey",apikey)
                }
                    headers{
                        append(HttpHeaders.Authorization,apikey)
                    }
            }

            binding.textviewFirst.text = response.readText()

            val responseFromNetwork : ResponseFromNetwork = client.get(BASE_URL){
                contentType(ContentType.Application.Json)
                url {
                    path("/v2/everything")
                    parameters.append("q","bitcoin")
                    parameters.append("apiKey",apikey)
                }
                headers{
                    append(HttpHeaders.Authorization,apikey)
                }
            }
            Log.i(NETWORK_TAG,"${responseFromNetwork.articles}")
        }


        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_blankFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}