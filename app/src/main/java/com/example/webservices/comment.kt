package com.example.webservices

import android.content.Context
import androidx.room.*

@Entity(tableName = "posts")
data class Post(@PrimaryKey(autoGenerate = true) val id: Int = 0, @ColumnInfo(name = "post") val
comment: String) {
    @Dao
    interface CommentDao {
        @Query("SELECT * FROM posts")
        fun getAll(): List<Post>
        @Query("SELECT * FROM posts WHERE id = :id")
        fun findById(id: Int): Post
        @Insert
        fun insertAll(vararg comment: Post)
        @Delete
        fun delete(comment: Post)
        @Update
        fun updateComment(vararg comment: Post)
    }
    @Database(entities = arrayOf(Post::class), version = 1)
    abstract class MyDatabase: RoomDatabase() {
        abstract fun commentDao(): CommentDao
        companion object {
            @Volatile private var instance: MyDatabase? = null
            private val LOCK = Any()
            operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
                instance ?: buildDatabase(context).also { instance = it}
            }
            private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                MyDatabase::class.java, "posts.db")
                .build()
        }
    }
}

