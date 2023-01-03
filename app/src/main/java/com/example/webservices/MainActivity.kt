package com.example.webservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var posts = mutableListOf<RetroPost>()

        val BASE_URL = "http://jsonplaceholder.typicode.com"
        val recyclerView = findViewById<RecyclerView>(R.id.main_recyclerview)
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PostService::class.java)
        val call = service.getAllPosts().enqueue(object : Callback<List<RetroPost>> {
            override fun onResponse(call : Call<List<RetroPost>>, response: Response<List<RetroPost>>){
                if(response.code() == 200){
                    val retroFit2 = response.body()
                    recyclerView.adapter = retroFit2?.let { MyAdapterRec(it) }
                    val linearLayoutManager = LinearLayoutManager(this@MainActivity)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView.layoutManager = linearLayoutManager
                }
            }
            override fun onFailure(calll: Call<List<RetroPost>>, t: Throwable){
                print("error")
            }
        })
        recyclerView.adapter = MyAdapterRec(posts)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        PagerSnapHelper().attachToRecyclerView(recyclerView)

        val db = Room.databaseBuilder(
            applicationContext,
            Post.MyDatabase::class.java, "posts.db"
        ).build()
        GlobalScope.launch {
            db.commentDao().insertAll(Post(comment = "Teste"))
            val data = db.commentDao().getAll()
            data?.forEach {
                println(it)
            }
        }
    }
}

class MyAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_item, parent, false)) {
    private var tv: TextView? = itemView.findViewById(R.id.tv)
    private var tv1: TextView? = itemView.findViewById(R.id.tv1)
    private var tv2: TextView? = itemView.findViewById(R.id.tv2)
    private var tv3: TextView? = itemView.findViewById(R.id.tv3)
    fun bindData(text: String, colorResource: Int) {
        tv?.text = text
        tv1?.text = text
        tv2?.text = text
        tv3?.text = text

        itemView.setOnClickListener {
            Toast.makeText(parent.context,text, Toast.LENGTH_LONG).show()
        }
    }
}

class MyAdapterRec(private val mList: List<RetroPost>) : RecyclerView.Adapter<MyAdapterRecViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterRecViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyAdapterRecViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: MyAdapterRecViewHolder, position: Int) {
        val color = when(position % 2) {
            0 -> android.R.color.holo_red_dark
            1 -> android.R.color.holo_blue_dark
            else -> android.R.color.holo_orange_dark
        }
        val text1 = mList[position].userId
        val text2 = mList[position].id
        val text3 = mList[position].title
        val text4 = mList[position].body
        /*
        val text = mList.get(position).description
        val text1 = mList.get(position).title

        */
        holder.bindData(text1 + "\n" + text2 + "\n" + text3 + "\n" + text4,color)

    }
    override fun getItemCount(): Int {
        return mList.size
    }
}
/*

fun doRequest(): List<RetroPost>{
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url("https://jsonplaceholder.typicode.com/posts")
        .build()

    client.newCall(request).execute().use { response ->
        val arrayStr = response.body?.string()
        if (arrayStr != null) {
            var listOfPosts = Post.importFromArray(arrayStr)
        }
    }
}

*/

/**
 * @PrimaryKey identifica a chave primária (neste caso o ID)
 * @ColumnInfo permite dar um nome diferente da variável à coluna (entre outros aspectos)
 */
