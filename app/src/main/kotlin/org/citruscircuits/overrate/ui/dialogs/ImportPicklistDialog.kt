package org.citruscircuits.overrate.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.citruscircuits.overrate.MainActivityViewModel
import org.citruscircuits.overrate.ui.MainNavGraph

/**
 * Dialog for selecting teams to import from a picklist.
 *
 * @param importedList The list of teams imported from the picklist.
 */
@MainNavGraph
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun ImportPicklistDialog(
    importedList: ArrayList<String>,
    navigator: DestinationsNavigator,
    viewModel: MainActivityViewModel
) {
    var selectedTeams: Set<String> by rememberSaveable { mutableStateOf(emptySet()) }
    // container
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // stack vertically
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Import picklist", style = MaterialTheme.typography.headlineMedium)
            if (importedList.all { it in selectedTeams }) {
                OutlinedButton(onClick = { selectedTeams = emptySet() }) {
                    Icon(Icons.Default.DoneAll, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unselect all teams")
                }
            } else {
                OutlinedButton(onClick = { selectedTeams = importedList.toSet() }) {
                    Icon(Icons.Default.DoneAll, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select all teams")
                }
            }
            var search by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Search teams") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (search.isNotEmpty()) {
                        IconButton(onClick = { search = "" }) { Icon(Icons.Default.Cancel, contentDescription = null) }
                    }
                }
            )
            OutlinedCard(modifier = Modifier.weight(1f)) {
                LazyColumn {
                    items(importedList.filter { search in it }) { team ->
                        Column {
                            ListItem(
                                headlineContent = { Text(team) },
                                trailingContent = {
                                    if (team in selectedTeams) Icon(Icons.Default.Check, contentDescription = null)
                                },
                                modifier = Modifier.clickable {
                                    selectedTeams = selectedTeams.toMutableSet().apply {
                                        if (team in selectedTeams) remove(team) else add(team)
                                    }
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = { navigator.navigateUp() }) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        viewModel.addPicklistTeams(
                            teams = importedList.filter { it in selectedTeams },
                            picklist = importedList
                        )
                        navigator.navigateUp()
                    },
                    enabled = selectedTeams.isNotEmpty()
                ) {
                    Text("Add ${selectedTeams.size} team(s)")
                }
            }
        }
    }
}
