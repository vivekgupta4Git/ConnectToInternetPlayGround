package com.ruviapps.newsapp

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import com.ruviapps.newsapp.databinding.ActivityMainBinding
import com.ruviapps.newsapp.utility.NetworkUtility
import com.ruviapps.newsapp.utility.NetworkUtility.Companion.NETWORK_TAG
import java.io.IOException
import java.net.URI
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->

            val task = MyAsyncTask()
            task.execute()
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        }
    }

    /*
    String uri = Uri.parse("http://...")
                .buildUpon()
                .appendQueryParameter("key", "val")
                .build().toString();

     */

   private inner class MyAsyncTask : AsyncTask<URL, String, String>() {
       val apiKey = "2204d222009b4623a4e662dd932bb287"
        override fun doInBackground(vararg params: URL?) : String {
            val newApiString = Uri.parse("https://newsapi.org/v2/everything")
                .buildUpon()
                .appendQueryParameter("q","bitcoin")
                .appendQueryParameter("apiKey",apiKey)
                .build().toString()
            val url = NetworkUtility.createURL(newApiString)
            var response = ""
            try {
                if (url != null) {
                    response = NetworkUtility.makeHttpRequest(url).toString()
                    Log.d(NETWORK_TAG, "resonpse =$response")
                }
            } catch (ex: IOException) {

            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}



/*
method 1 :
GET https://newsapi.org/v2/everything?q=keyword&apiKey=2204d222009b4623a4e662dd932bb287



method 2 :
 retrofit Get request with AUTH HEADER

 @GET("api-shipping/Apps")
fun getApp(@Header("Authorization") auth: String) : retrofit2.Call<JsonObject>
call enqueue don't forget to add Bearer with a space in tokken

 val tokken =  "Bearer TOKKEN_Key"
 call.enqueue(object : Callback<JsonObject> {
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

        }

    })
}



 */