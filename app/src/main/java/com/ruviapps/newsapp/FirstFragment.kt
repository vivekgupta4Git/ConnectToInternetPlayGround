package com.ruviapps.newsapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ruviapps.newsapp.databinding.FragmentFirstBinding
import com.ruviapps.newsapp.entities.NewsResponseRealm
import com.ruviapps.newsapp.entities.mapNewsFromDatabaseToUIObject
import com.ruviapps.newsapp.remote.dto.NewsFromNetwork
import com.ruviapps.newsapp.remote.dto.ResponseFromNetwork
import com.ruviapps.newsapp.utility.NetworkUtility.Companion.NETWORK_TAG
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import okhttp3.OkHttpClient
import okhttp3.Request
import org.bson.types.ObjectId
import retrofit2.HttpException

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
    private lateinit var realmConfiguration: RealmConfiguration
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val app = context.applicationContext as NewsApplication
       realmConfiguration= app.provideRealmConfig()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {

                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }

                )
            }
            engine {
                connectTimeout = 150_00
                socketTimeout = 100_00
                // parseAuthorizationHeader(apikey)
            }
        }
        lifecycleScope.launchWhenResumed {


            val responseFromNetwork: ResponseFromNetwork = client.get(BASE_URL) {
                contentType(ContentType.Application.Json)
                url {
                    path("/v2/everything")

                    parameters.append("q", "keyword")
                 //   parameters.append("searchIn", "description,title")
                    parameters.append("language", "en")
                    parameters.append("sortBy", "publishedAt")
                }
                headers {
                    append(HttpHeaders.Authorization, apikey)
                }
            }




            binding.textviewFirst.text = ""
            responseFromNetwork.articles.forEach { news ->
                insertNews(news)
            }

            binding.buttonFirst.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                retrieveNews().forEach {
                    binding.textviewFirst.append(it.title + "\n\n")
                }
            }
     //           findNavController().navigate(R.id.action_FirstFragment_to_blankFragment)
            }
        }
    }

    private suspend fun insertNews(news : NewsFromNetwork) {
        val realm = Realm.getInstance(realmConfiguration)
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            NewsResponseRealm()
            val newsToInsert = NewsResponseRealm(news.author,
                news.title,
                news.description,
                news.url,
                news.urlToImage,
                news.publishedAt,
                news.content)
            realmTransaction.insert(newsToInsert)

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private suspend fun retrieveNews() : List<NewsFromNetwork>{
        val realm = Realm.getInstance(realmConfiguration)
        val newsToGet = mutableListOf<NewsFromNetwork>()

        realm.executeTransactionAwait(Dispatchers.IO){
            realmTransaction->
            newsToGet.addAll(
                realmTransaction
                    .where(NewsResponseRealm::class.java)
                    .contains("title","science")
                    .distinct("title")
                    .findAll()
                    .map {
                        mapNewsFromDatabaseToUIObject(it)
                    }
            )
        }
        return newsToGet
    }

    /* val responseFromSources : ResponseFromSources = client.get(BASE_URL ){
            url{
                path("/v2/top-headlines/sources")
                parameters.append("category","science")
            }
            headers{
                append(HttpHeaders.Authorization,apikey)
            }
        }

        responseFromSources.sources.forEach {
            binding.textviewFirst.append( it.toString() + "\n\n")
        }*/

    /*
            val response: HttpResponse = client.get(BASE_URL) {
                contentType(ContentType.Application.Json)
                url {
                    path("/v2/everything")
                    parameters.append("q", "science")
                    // parameters.append("apiKey",apikey)
                }
                headers {
                    append(HttpHeaders.Authorization, apikey)
                }
            }
*/


/*
            val topHeadlines : ResponseFromNetwork = client.get(BASE_URL){
                contentType(ContentType.Application.Json)
                url{
                    path("v2/top-headlines/")
                    parameters.append("country","in")
                }
                headers {
                    append(HttpHeaders.Authorization,apikey)
                }
            }
*/


    /*


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
            //Log.i(NETWORK_TAG,"${responseFromNetwork.articles}")

        val topHeadlines : ResponseFromNetwork = client.get(BASE_URL){
            contentType(ContentType.Application.Json)
            url{
                path("v2/top-headlines/")
               parameters.append("country","in")
            }
            headers {
                append(HttpHeaders.Authorization,apikey)
            }
        }
        Log.i(NETWORK_TAG,"source ${topHeadlines.totalResults}")
        }
*/

}