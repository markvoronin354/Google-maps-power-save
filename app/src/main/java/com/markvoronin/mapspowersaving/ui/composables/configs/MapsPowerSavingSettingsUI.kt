package com.markvoronin.mapspowersaving.ui.composables.configs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.markvoronin.mapspowersaving.R
import com.markvoronin.mapspowersaving.shizuku.ShizukuStatus
import com.markvoronin.mapspowersaving.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsPowerSavingSettingsUI(viewModel: MainViewModel) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MainFeatureCard(viewModel)
            
            PermissionSection(viewModel)
            
            AboutSection()
        }
    }
}

@Composable
fun MainFeatureCard(viewModel: MainViewModel) {
    val context = LocalContext.current
    val isEnabled = viewModel.isMapsPowerSavingEnabled.value
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) 
                MaterialTheme.colorScheme.primaryContainer 
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (!isEnabled) CardDefaults.outlinedCardBorder() else null
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Power Saving Mode",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isEnabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isEnabled) "Active" else "Inactive",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isEnabled) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { viewModel.setMapsPowerSavingEnabled(it, context) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun PermissionSection(viewModel: MainViewModel) {
    val context = LocalContext.current
    
    Text(
        text = "System Permissions",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
    
    PermissionStatusItem(
        title = "Notification Listener",
        description = "Detects when Google Maps starts and stops navigation.",
        isGranted = viewModel.isNotificationListenerEnabled.value,
        onClick = { viewModel.requestNotificationListenerPermission(context) }
    )
    
    val shizukuStatus = viewModel.shizukuStatus.value
    PermissionStatusItem(
        title = "Shizuku / ADB",
        description = when (shizukuStatus) {
            ShizukuStatus.READY -> "Connected to system shell."
            ShizukuStatus.PERMISSION_NEEDED -> "Permission required to run shell commands."
            ShizukuStatus.NOT_RUNNING -> "Shizuku app is not running."
        },
        isGranted = shizukuStatus == ShizukuStatus.READY,
        onClick = { viewModel.requestShizukuPermission() }
    )
}

@Composable
fun PermissionStatusItem(
    title: String,
    description: String,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isGranted) MaterialTheme.colorScheme.surface 
                             else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.05f)
        ),
        border = CardDefaults.outlinedCardBorder(
            enabled = true
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title, 
                    fontWeight = FontWeight.SemiBold,
                    color = if (isGranted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                )
                Text(
                    text = description, 
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isGranted) {
                Text(
                    text = "OK",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Button(
                    onClick = onClick,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Grant", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun AboutSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "maps power save mode",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Pure Shell Implementation",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Text(
            text = "v16.5",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
    }
}
