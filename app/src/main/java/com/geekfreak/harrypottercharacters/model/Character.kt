package com.geekfreak.harrypottercharacters.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class Character(
    val actor: String,
    val alive: Boolean,
    val ancestry: String,
    val dateOfBirth: String,
    val eyeColour: String,
    val gender: String,
    val hairColour: String,
    val hogwartsStaff: Boolean,
    val hogwartsStudent: Boolean,
    val house: String,
    val image: String,
    @PrimaryKey
    val name: String,
    val patronus: String,
    val species: String,
    val wand: Wand,
    val yearOfBirth: String
)