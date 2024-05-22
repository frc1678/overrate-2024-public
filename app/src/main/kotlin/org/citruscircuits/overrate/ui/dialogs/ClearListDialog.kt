package org.citruscircuits.overrate.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.citruscircuits.overrate.MainActivityViewModel
import org.citruscircuits.overrate.ui.MainNavGraph

/**
 * Dialog for clearing the list.
 */
@MainNavGraph
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun ClearListDialog(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    // container
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // stack vertically
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            // scrollable
            .verticalScroll(rememberScrollState())
        ) {
            Text("Clear list", style = MaterialTheme.typography.headlineMedium)
            Text("Are you sure you want to clear all teams and dividers from the list?")
            // cancel/confirm buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = { navigator.navigateUp() }) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        viewModel.appState.value = viewModel.appState.value.copy(list = emptyList())
                        navigator.navigateUp()
                    }
                ) {
                    Text("Yes, clear list")
                }
            }
        }
    }
}
