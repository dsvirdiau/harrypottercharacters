package com.geekfreak.harrypottercharacters.db

import androidx.room.*
import com.geekfreak.harrypottercharacters.model.Character

@Dao
interface CharactersDao {
   @Query("SELECT * FROM characters ORDER BY name ASC")
   suspend fun getCharacters(): List<Character>

   @Query("SELECT * FROM characters WHERE hogwartsStudent = 1 ORDER BY name ASC")
   suspend fun getStudentCharacters(): List<Character>

   @Query("SELECT * FROM characters WHERE hogwartsStaff = 1 ORDER BY name ASC")
   suspend fun getStaffCharacters(): List<Character>

   @Query("SELECT * FROM characters WHERE house LIKE :house ORDER BY name ASC")
   suspend fun getCharactersByHouse(house: String?): List<Character>

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun upsert(character: Character)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(characters: List<Character>)

   @Query("DELETE FROM characters")
   suspend fun deleteAll()
}