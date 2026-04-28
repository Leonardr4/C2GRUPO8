package com.example.a6new

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class BaseDeDatos {
    @Database(entities = [Persona::class], version = 1)
    abstract class  BaseDeDatos : RoomDatabase(){
        abstract fun dao():PersonaDao

        companion object{
            @Volatile private var INSTANCE: BaseDeDatos?=null

            fun getDB(context: Context): BaseDeDatos=INSTANCE?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext, BaseDeDatos::class.java, "personas_db"
                ).build().also{
                    INSTANCE = it
                }
            }
        }
    }
}