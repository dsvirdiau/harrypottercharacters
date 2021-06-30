package com.geekfreak.harrypottercharacters.repository

import com.geekfreak.harrypottercharacters.api.RetrofitInstance
import com.geekfreak.harrypottercharacters.model.Character
import com.geekfreak.harrypottercharacters.db.CharactersDao

class MainRepository (private val characterDao: CharactersDao) {

   fun getCharacters() = RetrofitInstance.api.getCharacters()

   suspend fun getSavedCharacters() = characterDao.getCharacters()

   suspend fun getStudentCharacters() = characterDao.getStudentCharacters()

   suspend fun getStaffCharacters() = characterDao.getStaffCharacters()

   suspend fun getCharactersByHouse(house: String?) = characterDao.getCharactersByHouse(house)

   suspend fun insertAll(characters: List<Character>) = characterDao.insertAll(characters)
}