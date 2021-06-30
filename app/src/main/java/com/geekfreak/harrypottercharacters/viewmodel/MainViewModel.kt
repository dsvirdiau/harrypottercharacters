package com.geekfreak.harrypottercharacters.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.geekfreak.harrypottercharacters.HarryPotterApplication
import com.geekfreak.harrypottercharacters.model.Character
import com.geekfreak.harrypottercharacters.repository.MainRepository
import com.geekfreak.harrypottercharacters.util.Constants.Companion.CONVERSION_ERROR_MESSAGE
import com.geekfreak.harrypottercharacters.util.Constants.Companion.NETWORK_FAILURE_MESSAGE
import com.geekfreak.harrypottercharacters.util.Constants.Companion.NO_INTERNET_MESSAGE
import com.geekfreak.harrypottercharacters.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MainViewModel(app: Application, private val mainRepository: MainRepository) :
   AndroidViewModel(app) {
   val characters: MutableLiveData<Resource<List<Character>>> = MutableLiveData()

   init {
      getSavedCharacters()
      safeApiCall()
   }

   fun getSavedCharacters() = viewModelScope.launch {
      characters.postValue(Resource.Loading())
      characters.postValue(Resource.Success(mainRepository.getSavedCharacters()))
   }

   fun getStudentCharacters() = viewModelScope.launch {
      characters.postValue(Resource.Loading())
      characters.postValue(Resource.Success(mainRepository.getStudentCharacters()))
   }

   fun getStaffCharacters() = viewModelScope.launch {
      characters.postValue(Resource.Loading())
      characters.postValue(Resource.Success(mainRepository.getStaffCharacters()))
   }

   fun getCharactersByHouse(house: String?) = viewModelScope.launch {
      characters.postValue(Resource.Loading())
      characters.postValue(Resource.Success(mainRepository.getCharactersByHouse(house)))
   }

   fun refresh() {
      safeApiCall()
   }

   private fun saveAllCharacters(characters: List<Character>) = viewModelScope.launch {
      mainRepository.insertAll(characters)
   }

   private fun safeApiCall() {
      characters.postValue(Resource.Loading())
      try {
         if (hasInternetConnection()) {
            makeApiCall()
         } else {
            characters.postValue(Resource.Error(NO_INTERNET_MESSAGE))
         }
      } catch (t: Throwable) {
         when (t) {
            is IOException -> characters.postValue(Resource.Error(NETWORK_FAILURE_MESSAGE))
            else -> characters.postValue(Resource.Error(CONVERSION_ERROR_MESSAGE))
         }
      }
   }

   private fun makeApiCall() {
      mainRepository.getCharacters().enqueue(object : Callback<List<Character>> {
         override fun onResponse(call: Call<List<Character>>, response: Response<List<Character>>) {
            if (response.isSuccessful) {
               response.body()?.let { resultResponse ->
                  saveAllCharacters(resultResponse)
                  characters.postValue(Resource.Success(resultResponse.sortedBy { t ->
                     t.name
                  }))
               }
            }
         }

         override fun onFailure(call: Call<List<Character>>, t: Throwable) {
            return characters.postValue(t.message?.let { Resource.Error(it) })
         }
      })
   }

   private fun hasInternetConnection(): Boolean {
      val connectivityManager = getApplication<HarryPotterApplication>()
         .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         val activeNetwork = connectivityManager.activeNetwork ?: return false
         val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
         return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
         }
      }

      return false
   }
}