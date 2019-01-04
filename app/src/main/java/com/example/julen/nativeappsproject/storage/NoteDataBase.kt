package com.example.julen.nativeappsproject.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.example.julen.nativeappsproject.Converter
import com.example.julen.nativeappsproject.model.Note

/**
 * The database to store [Note]
 */
@Database(entities = [(Note::class)], version = 1)
@TypeConverters(Converter::class)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao() : NoteDao

    companion object {
        var INSTANCE: NoteDataBase? = null

        fun getNoteDataBase(context: Context): NoteDataBase? {
            if (INSTANCE == null){
                synchronized(NoteDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, NoteDataBase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }

}