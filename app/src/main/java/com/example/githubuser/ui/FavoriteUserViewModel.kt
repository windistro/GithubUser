package com.example.githubuser.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.FavoriteRepository
import com.example.githubuser.data.local.FavoriteUser

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFav(): LiveData<List<FavoriteUser>> = mFavoriteRepository.read()
}