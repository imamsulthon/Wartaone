package com.imams.wartaone.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.imams.wartaone.databinding.ActivityMainBinding
import com.imams.wartaone.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initViewAndListener()
        observeViewModel()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchData()
    }

    private fun observeViewModel() {
        viewModel.switch.observe(this) {
            it?.let {
                binding.switchCompat.isChecked = it
            }
        }
    }

    private fun initViewAndListener() {
        with(binding) {
            switchCompat.setOnCheckedChangeListener { _, b ->
                viewModel.useApi(b)
            }

            btnGotoHome.setOnClickListener {
                toHome()
            }
        }
    }

    private fun toHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

}