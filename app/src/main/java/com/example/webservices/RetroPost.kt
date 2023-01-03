package com.example.webservices

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class RetroPost(@SerializedName("userId") val userId: String,
                     @SerializedName("id") val id: String,
                     @SerializedName("title") val title: String,
                     @SerializedName("body") val body: String) {
    companion object {
        fun importFromJSON(jsonObject: JSONObject): RetroPost {
            val id = jsonObject.getString("id")
            val userID = jsonObject.getString("userId")
            val title = jsonObject.getString("title")
            val body = jsonObject.getString("body")

            return RetroPost(userID, id, title, body)
        }

        fun importFromArray(jsonArray: String): List<RetroPost> {
            val array = JSONArray(jsonArray)
            val list = mutableListOf<RetroPost>()

            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val post = RetroPost.importFromJSON(json)
                list.add(post)
            }
            return list
        }

    }
}