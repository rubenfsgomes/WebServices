package com.example.webservices

import org.json.JSONArray
import org.json.JSONObject

/*class Post(var userId: String, var id: String, var title: String, var body: String) {
    /*fun readJson(json: String){
        val jsonObj = JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
        val postJson = jsonObj.getJSONArray("posts")
        for (i in 0 until postJson!!.length()) {
            val userid = postJson.getJSONObject(i).getString("userid")
            val post = Post()
        }
    }*/
    companion object{
        fun importFromJSON(jsonObject: JSONObject): Post {
            val id = jsonObject.getString("id")
            val userID = jsonObject.getString("userId")
            val title = jsonObject.getString("title")
            val body = jsonObject.getString("body")

            return Post(userID, id, title, body)
        }

        fun importFromArray(jsonArray: String): List<Post> {
            val array = JSONArray(jsonArray)
            val list = mutableListOf<Post>()

            for (i in 0 until array.length()){
                val json = array.getJSONObject(i)
                val post = Post.importFromJSON(json)
                list.add(post)
            }
            return list
        }


    }
}*/