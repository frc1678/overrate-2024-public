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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.citruscircuits.overrate.MainActivityViewModel
import org.citruscircuits.overrate.data.MainListItem
import org.citruscircuits.overrate.ui.MainNavGraph

/**
 * Dialog for editing the label of a divider.
 *
 * @param id The ID of the divider to edit the label of.
 */
@MainNavGraph
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun EditDividerLabelDialog(id: String, navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    // get app state
    val appState by viewModel.appState.collectAsStateWithLifecycle()
    // current input
    var label by rememberSaveable {
        // initialize to existing label
        mutableStateOf((appState.list.find { it.id == id } as? MainListItem.Divider)?.label ?: "")
    }
    // container
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier =
            Modifier
                .width(IntrinsicSize.Max)
                .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // stack vertically
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    // scrollable
                    .verticalScroll(rememberScrollState())
        ) {
            // title
            Text("Edit divider label", style = MaterialTheme.typography.headlineMedium)
            // input field
            OutlinedTextField(value = label, onValueChange = { label = it })
            // cancel/done buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth()
            ) {
                // cancel button
                OutlinedButton(onClick = navigator::navigateUp) {
                    Text("Cancel")
                }
                // done button
                Button(
                    onClick = {
                        viewModel.appState.value =
                            appState.copy(
                                list =
                                    // look through items, edit item if ID matches
                                    appState.list.map {
                                        if (it.id == id) {
                                            (it as? MainListItem.Divider)?.copy(label = label) ?: it
                                        } else {
                                            it
                                        }
                                    }
                            )
                        navigator.navigateUp()
                    }
                ) {
                    Text("Done")
                }
            }
        }
    }
}
