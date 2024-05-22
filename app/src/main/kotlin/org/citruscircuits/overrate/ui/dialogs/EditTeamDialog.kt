package org.citruscircuits.overrate.ui.dialogs

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.citruscircuits.overrate.MainActivityViewModel
import org.citruscircuits.overrate.data.MainListItem
import org.citruscircuits.overrate.ui.MainNavGraph

/**
 * Dialog for editing a team item in the main list.
 *
 * @param id The ID of the team item to edit.
 */
@MainNavGraph
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun EditTeamDialog(id: String, navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    // get app state
    val appState by viewModel.appState.collectAsStateWithLifecycle()
    // current team number input
    var teamNumber by rememberSaveable {
        // initialize to existing team number
        mutableStateOf((appState.list.find { it.id == id } as? MainListItem.Team)?.number ?: "")
    }
    // current comment input
    var comment by rememberSaveable {
        // initialize to existing comment
        mutableStateOf((appState.list.find { it.id == id } as? MainListItem.Team)?.comment ?: "")
    }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                // scrollable
                .verticalScroll(rememberScrollState())
        ) {
            // title
            Text("Edit team", style = MaterialTheme.typography.headlineMedium)
            // input field + results
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // input field
                OutlinedTextField(
                    value = teamNumber,
                    onValueChange = { teamNumber = it },
                    label = { Text("Enter team number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                // show results if something is typed
                if (teamNumber.isNotEmpty()) {
                    // show results in card
                    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                        // sort/filter search results
                        appState.importedTeams
                            .filter { teamNumber in it }.filterNot { teamNumber == it }
                            .sortedBy { it.toIntOrNull() }.take(3).forEach { team ->
                                ListItem(
                                    headlineContent = { Text(team) },
                                    modifier = Modifier.clickable { teamNumber = team }
                                )
                                HorizontalDivider()
                            }
                    }
                }
            }
            // comment field
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Enter comment") }
            )
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
                                            (it as? MainListItem.Team)?.copy(number = teamNumber, comment = comment)
                                                ?: it
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
