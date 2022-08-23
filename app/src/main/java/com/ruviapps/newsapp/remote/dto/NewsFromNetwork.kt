package com.ruviapps.newsapp.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class NewsFromNetwork(
    val author : String?,
    val title : String?,
    val description : String?,
    val url : String?,
    val urlToImage : String?,
    val publishedAt : String?,
    val content : String?
)
@Serializable
data class ResponseFromNetwork(
    val status : String,
    val totalResults : Int,
    @Serializable(with =ArticlesSerializer::class)
    val articles : List<NewsFromNetwork>
)

object ArticlesSerializer
    : JsonTransformingSerializer< List<NewsFromNetwork>>(
    ListSerializer(NewsFromNetwork.serializer())

    )