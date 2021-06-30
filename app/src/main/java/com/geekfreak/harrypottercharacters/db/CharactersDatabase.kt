package com.geekfreak.harrypottercharacters.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geekfreak.harrypottercharacters.model.Character

@Database(entities = [Character::class], version = 1)
@TypeConverters(Converters::class)
abstract class CharactersDatabase: RoomDatabase() {

   abstract fun getCharactersDao(): CharactersDao

   companion object {
      // Singleton prevents multiple instances of database opening at the
      // same time.
      @Volatile
      private var instance: CharactersDatabase? = null

      operator fun invoke(context: Context): CharactersDatabase {
         // if the INSTANCE is not null, then return it,
         // if it is, then create the database
         return instance?: synchronized(this) {
            instance = Room.databaseBuilder(
               context.applicationContext,
               CharactersDatabase::class.java,
            "room_database")
               .build()

            instance!!
         }
      }
   }
}