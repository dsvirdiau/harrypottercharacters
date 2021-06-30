package com.geekfreak.harrypottercharacters.db

import androidx.room.TypeConverter
import com.geekfreak.harrypottercharacters.model.Wand
import com.google.gson.Gson

class Converters {
   private val gson = Gson()

   @TypeConverter
   fun fromWand(wand: Wand): String {
      return gson.toJson(wand)
   }

   @TypeConverter
   fun toWand(wandString: String): Wand {
      return gson.fromJson(wandString, Wand::class.java )
   }
}