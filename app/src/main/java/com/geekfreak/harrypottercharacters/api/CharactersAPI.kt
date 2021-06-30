package com.geekfreak.harrypottercharacters.api

import com.geekfreak.harrypottercharacters.model.Character
import retrofit2.Call
import retrofit2.http.GET

interface CharactersAPI {
   @GET("api/characters")
   fun getCharacters( ): Call<List<Character>>
}