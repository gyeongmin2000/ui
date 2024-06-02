package com.example.ui.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "Project")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val projectId: Long = 0L,
    @ColumnInfo
    val projectName: String,
    @ColumnInfo
    val projectImagePath: String
)

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project LIMIT 1000")
    fun getAll(): List<Project>

    @Query("SELECT * FROM Project WHERE projectId = :projectId")
    fun getById(projectId: Long): Project

    @Query("SELECT * FROM Project WHERE projectName LIKE :name LIMIT 1000")
    fun searchByName(name: String): List<Project>

    @Insert
    fun insert(vararg projects: Project)

    @Delete
    fun delete(project: Project)

    @Update
    fun update(project: Project)
}

@Database(entities = [Project::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun buildDatabase(context: Context) : AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "project"
            ).build()

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}