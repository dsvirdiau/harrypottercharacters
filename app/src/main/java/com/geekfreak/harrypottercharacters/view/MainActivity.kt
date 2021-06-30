package com.geekfreak.harrypottercharacters.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekfreak.harrypottercharacters.R
import com.geekfreak.harrypottercharacters.adapter.CharactersAdapter
import com.geekfreak.harrypottercharacters.databinding.ActivityMainBinding
import com.geekfreak.harrypottercharacters.db.CharactersDatabase
import com.geekfreak.harrypottercharacters.factory.MainViewModelFactory
import com.geekfreak.harrypottercharacters.repository.MainRepository
import com.geekfreak.harrypottercharacters.util.Constants.Companion.MAIN_ACTIVITY_TAG
import com.geekfreak.harrypottercharacters.util.Constants.Companion.SPAN_COUNT
import com.geekfreak.harrypottercharacters.util.Constants.Companion.SWIPE_REFRESH_DELAY
import com.geekfreak.harrypottercharacters.util.Resource
import com.geekfreak.harrypottercharacters.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var viewModel: MainViewModel
	private val charactersAdapter = CharactersAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val mainRepository = MainRepository(
			CharactersDatabase(this)
				.getCharactersDao()
		)

		viewModel = ViewModelProvider(this, MainViewModelFactory(application, mainRepository))
			.get(MainViewModel::class.java)

		binding.recyclerView.apply {
			layoutManager = GridLayoutManager(this@MainActivity, SPAN_COUNT,
				RecyclerView.VERTICAL, false)
			adapter = charactersAdapter
		}

		viewModel.characters.observe(this, {
			when (it) {
				is Resource.Success -> {
					it.data?.let { charactersList ->
						charactersAdapter.setCharactersList(charactersList)
					}
				}
				is Resource.Error -> {
					it.message?.let { message ->
						Log.e(MAIN_ACTIVITY_TAG, "onCreate: $message")
						Toast.makeText(this, message, Toast.LENGTH_LONG).show()
					}
				}
				is Resource.Loading -> {
				}
			}
		})

		binding.chipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
			when (val textOrNull = chipGroup.findViewById<Chip>(checkedId)?.text) {
				null -> viewModel.getSavedCharacters()
				getString(R.string.students) -> viewModel.getStudentCharacters()
				getString(R.string.staff) -> viewModel.getStaffCharacters()
				else -> viewModel.getCharactersByHouse(textOrNull.toString())
			}
		}

		binding.swipeRefreshRecyclerView.setOnRefreshListener {
			binding.chipGroup.clearCheck()
			viewModel.refresh()
			lifecycleScope.launch {
				delay(SWIPE_REFRESH_DELAY)
				binding.swipeRefreshRecyclerView.isRefreshing = false

			}
		}
	}
}