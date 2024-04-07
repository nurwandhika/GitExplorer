package com.example.fundamentalgithub.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.fundamentalgithub.R
import com.example.fundamentalgithub.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModelFactory(
            this.baseContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = ContextCompat.getString(this@SettingsActivity, R.string.settings)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel.getDarkMode().observe(this) {
            binding.darkModeSwitch.isChecked = it
            if (it)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.apply {
            darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setDarkMode(isChecked)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}