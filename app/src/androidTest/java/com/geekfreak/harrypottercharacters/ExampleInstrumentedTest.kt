package com.geekfreak.harrypottercharacters

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekfreak.harrypottercharacters.db.CharactersDao
import com.geekfreak.harrypottercharacters.db.CharactersDatabase
import com.geekfreak.harrypottercharacters.model.Character
import com.geekfreak.harrypottercharacters.model.Wand
import com.geekfreak.harrypottercharacters.view.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
	private lateinit var charactersDao: CharactersDao
	private lateinit var db: CharactersDatabase

	@get:Rule
	var activityRule: ActivityScenarioRule<MainActivity> =
		ActivityScenarioRule( MainActivity::class.java)

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(
			context, CharactersDatabase::class.java).build()
		charactersDao = db.getCharactersDao()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	fun useAppContext() {
		// Context of the app under test.
		val appContext = InstrumentationRegistry.getInstrumentation().targetContext
		assertEquals("com.example.harrypottercaracters", appContext.packageName)
	}

	@Test
	@Throws(Exception::class)
	fun insertAndGetCharacter() = runBlocking{
		val charactersList = mutableListOf<Character>()
		charactersList.add( Character( "Alan Rickman",
			false,"half-blood",
			"09-01-1960","black",
			"male","black",true,
			false,"Slytherin",
			"http://hp-api.herokuapp.com/images/snape.jpg",
			"Severus Snape","doe","human",
			Wand("","",""),"1960")
		)
		charactersDao.insertAll(charactersList)
		val characters = charactersDao.getCharacters()
		Log.d(TAG, "insertAndGetCharacter: $characters")
		assertEquals(characters.count()==1, true)
	}
}