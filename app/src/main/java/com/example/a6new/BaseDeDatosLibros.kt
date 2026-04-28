package com.example.a6new

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class BaseDeDatosLibros {
    @Database(entities = [Libro::class], version = 1)
    abstract class  BasedeDatosLibros : RoomDatabase(){
        abstract fun dao(): LibroDao

        companion object{
            @Volatile private var INSTANCE: BasedeDatosLibros?=null

            fun getDB(context: Context): BasedeDatosLibros=INSTANCE?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext, BasedeDatosLibros::class.java, "libros_db"
                ).build().also{
                    INSTANCE = it
                }
            }
        }
    }
}