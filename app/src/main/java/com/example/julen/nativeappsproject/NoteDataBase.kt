package com.example.julen.nativeappsproject

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.example.julen.nativeappsproject.model.Note


@Database(entities = [(Note::class)], version = 1)
@TypeConverters(Converter::class)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao() : NoteDao

    companion object {
        var INSTANCE: NoteDataBase? = null

        fun getAppDataBase(context: Context): NoteDataBase? {
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