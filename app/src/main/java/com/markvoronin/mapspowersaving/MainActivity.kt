package com.markvoronin.mapspowersaving

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.markvoronin.mapspowersaving.ui.composables.configs.MapsPowerSavingSettingsUI
import com.markvoronin.mapspowersaving.ui.theme.EssentialsTheme
import com.markvoronin.mapspowersaving.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EssentialsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapsPowerSavingSettingsUI(
                        viewModel = mainViewModel
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.refreshPermissions(this)
    }
}
