package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.ui.adapter.UserAdapter

class FavoriteUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Favorite User"

        val pref = SettingPreferences.getInstance(application.dataStore)
        val favoriteUserViewModel = ViewModelProvider(this, ViewModelFactory(pref, application))[FavoriteUserViewModel::class.java]
        favoriteUserViewModel.getAllFav().observe(this) { users ->
            val adapter = UserAdapter()
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            adapter.submitList(items)
            binding.rvFavUser.adapter = adapter
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavUser.addItemDecoration(itemDecoration)
    }
}