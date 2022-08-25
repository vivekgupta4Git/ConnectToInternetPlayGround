package com.ruviapps.newsapp.entities

import androidx.lifecycle.Transformations.map
import com.ruviapps.newsapp.remote.dto.NewsFromNetwork
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId


open class NewsResponseRealm(
    var author : String? = "",
    @Required
    var title : String? = "",
    @Required
    var description : String? ="",
    @Required
    var url: String? = null,
    var urlToImage: String? = null,

    @Required
    var publishedAt: String? = "",

    @Required
    var content: String? = "",

    @PrimaryKey
    var id: String = ObjectId().toHexString()
): RealmObject()


/**
 * Mapper
 */
fun mapNewsFromDatabaseToUIObject(news : NewsResponseRealm): NewsFromNetwork{
    return NewsFromNetwork(news.author,
        news.title,
        news.description,
        news.url,
        news.urlToImage,
        news.publishedAt,
        news.content
    )
}