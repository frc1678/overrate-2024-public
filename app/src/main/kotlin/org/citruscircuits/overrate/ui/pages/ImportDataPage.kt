package org.citruscircuits.overrate.ui.pages

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.serialization.json.Json
import org.citruscircuits.overrate.MainActivityViewModel
import org.citruscircuits.overrate.ui.MainNavGraph
import org.citruscircuits.overrate.ui.animation.Transitions
import org.citruscircuits.overrate.ui.destinations.ImportPicklistDialogDestination
import org.citruscircuits.overrate.ui.destinations.ImportTeamsPageDestination

/**
 * Page for importing teams from team list files.
 */
@OptIn(ExperimentalMaterial3Api::class)
@MainNavGraph
@Destination(style = ImportTeamsPageTransitions::class)
@Composable
fun ImportTeamsPage(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    // get app state
    val appState by viewModel.appState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import data") },
                navigationIcon = {
                    // back button
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        // stack vertically
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .padding(padding)
        ) {
            PicklistSection(
                onOpenPicklistDialog = { navigator.navigate(ImportPicklistDialogDestination(ArrayList(it))) }
            )
            ImportedTeamsSection(
                importedTeams = appState.importedTeams,
                onImportTeams = { viewModel.importTeams(it) },
                onClearImportedTeams = { viewModel.appState.value = appState.copy(importedTeams = emptySet()) }
            )
        }
    }
}

@Composable
private fun PicklistSection(onOpenPicklistDialog: (List<String>) -> Unit) {
    // content resolver for opening files
    val contentResolver = LocalContext.current.contentResolver
    // file picker launcher
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            contentResolver.openInputStream(it)?.use { stream ->
                try {
                    val importedList = csvReader().readAll(stream)
                        // get first column
                        .map { r -> r.first() }
                        // remove unnecessary rows
                        .let { l -> l.subList(4, l.size) }
                        // remove blanks
                        .filter { c -> c.isNotBlank() }
                    // navigate to dialog
                    if (importedList.isNotEmpty()) onOpenPicklistDialog(importedList)
                } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                    Log.e("ImportTeamsPage", "Error importing picklist", e)
                }
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // header
        Text("Picklist", style = MaterialTheme.typography.headlineMedium)
        Text("Import a picklist to add its teams to the list.")
        Button(onClick = { launcher.launch(arrayOf("*/*")) }) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open file picker")
        }
    }
}

@Composable
private fun ImportedTeamsSection(
    importedTeams: Set<String>,
    onImportTeams: (Collection<String>) -> Unit,
    onClearImportedTeams: () -> Unit
) {
    // content resolver for opening files
    val contentResolver = LocalContext.current.contentResolver
    // file picker launcher
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                contentResolver.openInputStream(it)?.use { stream ->
                    // read file and decode to list
                    val data: List<String> =
                        try {
                            Json.decodeFromString(stream.reader().readText())
                        } catch (e: IllegalArgumentException) {
                            Log.e("ImportTeamsPage", "Error importing teams", e)
                            return@use
                        }
                    // import from the list
                    onImportTeams(data)
                }
            }
        }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // header
        Text("Imported teams", style = MaterialTheme.typography.headlineMedium)
        // help text
        Text("Choose a team list to import teams. Imported teams are shown as suggestions when adding teams.")
        // file picker button
        Button(onClick = { launcher.launch(arrayOf("*/*")) }) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open file picker")
        }
        // show how many teams have been imported
        Text("You currently have ${importedTeams.size} team(s) imported.")
        // clear button
        OutlinedButton(onClick = onClearImportedTeams) {
            Icon(Icons.Default.ClearAll, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clear teams")
        }
    }
}

/**
 * Transitions for [ImportTeamsPage].
 */
object ImportTeamsPageTransitions : Transitions(
    destination = ImportTeamsPageDestination,
    enter = { slideInHorizontally { fullWidth -> fullWidth } },
    exit = { slideOutHorizontally { fullWidth -> fullWidth } }
)
