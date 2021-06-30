package com.geekfreak.harrypottercharacters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geekfreak.harrypottercharacters.databinding.ListViewItemBinding
import com.geekfreak.harrypottercharacters.model.Character

class CharactersAdapter : RecyclerView.Adapter<CharactersViewHolder>() {
   private var characterList = mutableListOf<Character>()

   fun setCharactersList(characterList: List<Character>) {
      this.characterList = characterList.toMutableList()
      notifyDataSetChanged()
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
      val inflater = LayoutInflater.from( parent.context )
      val binding = ListViewItemBinding.inflate( inflater, parent, false )
      return CharactersViewHolder(binding)
   }

   override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
      val character = characterList[position]
      holder.binding.tvName.text = "${character.name}"

      Glide.with(holder.binding.ivPhoto.context)
         .load(character.image)
         .circleCrop()
         .into(holder.binding.ivPhoto)
   }

   override fun getItemCount() = characterList.size
}

class CharactersViewHolder(val binding: ListViewItemBinding) : RecyclerView.ViewHolder(binding.root)