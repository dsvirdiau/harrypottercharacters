package com.geekfreak.harrypottercharacters.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geekfreak.harrypottercharacters.repository.MainRepository
import com.geekfreak.harrypottercharacters.viewmodel.MainViewModel
import java.lang.IllegalArgumentException

class MainViewModelFactory( val app: Application,
                            private val repository: MainRepository): ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
         MainViewModel(app, this.repository) as T
      }
      else {
         throw IllegalArgumentException("ViewModel not found")
      }
   }
}