package com.ruviapps.newsapp.utility

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class NetworkUtility {

    companion object {
const val NETWORK_TAG = "NETWORK_TAG"
        fun createURL(stringUrl : String) : URL?{
            val url: URL?
            try {
                url = URL(stringUrl)
           }catch (ex : MalformedURLException){
                Log.e("NETWORK_TAG","${ex}, ${ex.stackTrace}")
                return null
            }
            return url
        }

/*
String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

myURLConnection.setRequestProperty ("Authorization", basicAuth);
myURLConnection.setRequestMethod("POST");
myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
myURLConnection.setRequestProperty("Content-Length", "" + postData.getBytes().length);
myURLConnection.setRequestProperty("Content-Language", "en-US");
myURLConnection.setUseCaches(false);
myURLConnection.setDoInput(true);
myURLConnection.setDoOutput(true);
 */
        fun makeHttpRequest(url: URL): String? {
    val apiKey = "2204d222009b4623a4e662dd932bb287"
            var jsonResponse = ""
            var httpURLConnection: HttpURLConnection? = null
            try {

                httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.readTimeout = 10000
                httpURLConnection.connectTimeout = 15000
                httpURLConnection.addRequestProperty("Authorization", "$apiKey")
                httpURLConnection.connect()
                val inputStream = httpURLConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            } catch (ex: IOException) {
                Log.e("NETWORK_TAG"," $ex ${ex.printStackTrace()}")
                return null
            } finally {
                httpURLConnection?.disconnect()
            }
            return jsonResponse
        }
        private fun readFromStream(inputStream: InputStream?) : String{
        val output = StringBuilder("")
            if(inputStream!=null){
                val streamReader = InputStreamReader(inputStream,Charset.forName("UTF-8"))
                val bufferedReader= BufferedReader(streamReader)
                var line = bufferedReader.readLine()
                while(line!=null){
                    output.append(line)
                    line = bufferedReader.readLine()
                }

            }
            return output.toString()
        }
    }

}