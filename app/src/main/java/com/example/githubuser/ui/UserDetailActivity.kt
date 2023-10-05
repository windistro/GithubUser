package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.local.FavoriteUser
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.ui.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val userDetailViewModel = ViewModelProvider(this, ViewModelFactory(pref, application))[UserDetailViewModel::class.java]
        val detailUser = intent.getStringExtra("USERNAME").toString()
        username = detailUser

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        userDetailViewModel.getDetailUser(detailUser)
        userDetailViewModel.getFollowerData(detailUser)
        userDetailViewModel.getFollowingData(detailUser)

        userDetailViewModel.user.observe(this) { user ->
            setDetailUser(user)
            title = user.login

            val favUser = FavoriteUser(user.login, user.avatarUrl)
            userDetailViewModel.getUserByUsername(user.login).observe(this) {users ->
                binding.fab.apply {
                    if (users == null) {
                        setImageResource(R.drawable.baseline_favorite_border_24)
                        setOnClickListener { userDetailViewModel.insert(favUser) }
                    } else {
                        setImageResource(R.drawable.baseline_favorite_filled_24)
                        setOnClickListener { userDetailViewModel.delete(favUser) }
                    }
                }
            }
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_button -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val body = "User github link: https://github.com/$username"
                shareIntent.putExtra(Intent.EXTRA_TEXT, body)
                startActivity(Intent.createChooser(shareIntent, "Share using"))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setDetailUser(details: DetailUserResponse) {
        Glide.with(this).load(details.avatarUrl).into(binding.imgUserDetail)
        binding.tvName.text = details.name
        binding.tvUsername.text = details.login
        binding.followerCount.text = details.followers.toString()
        binding.followingCount.text = details.following.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}