package org.citruscircuits.overrate.ui.pages

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.citruscircuits.overrate.MainActivityViewModel
import org.citruscircuits.overrate.data.MainListItem
import org.citruscircuits.overrate.ui.MainNavGraph
import org.citruscircuits.overrate.ui.animation.Transitions
import org.citruscircuits.overrate.ui.components.Counter
import org.citruscircuits.overrate.ui.destinations.AddTeamDialogDestination
import org.citruscircuits.overrate.ui.destinations.ClearListDialogDestination
import org.citruscircuits.overrate.ui.destinations.EditDividerLabelDialogDestination
import org.citruscircuits.overrate.ui.destinations.EditTeamDialogDestination
import org.citruscircuits.overrate.ui.destinations.ImportTeamsPageDestination
import org.citruscircuits.overrate.ui.destinations.MainPageDestination
import org.citruscircuits.overrate.ui.menus.AddMenu
import org.citruscircuits.overrate.ui.menus.AddMenuActions
import org.citruscircuits.overrate.ui.menus.DividerMenu
import org.citruscircuits.overrate.ui.menus.DividerMenuActions
import org.citruscircuits.overrate.ui.menus.TeamMenu
import org.citruscircuits.overrate.ui.menus.TeamMenuActions
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

/**
 * Page containing the main list.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@MainNavGraph(start = true)
@Destination(style = MainPageTransitions::class)
@Composable
fun MainPage(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    // get app state
    val appState by viewModel.appState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OverRate") },
                // actions at top right
                actions = {
                    Box {
                        // whether the menu is shown
                        var expanded by rememberSaveable { mutableStateOf(false) }
                        // add button
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                        // menu for add button
                        AddMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            actions =
                                AddMenuActions(
                                    addTeam = { navigator.navigate(AddTeamDialogDestination) },
                                    addDivider = { viewModel.addDivider() }
                                )
                        )
                    }
                    // import button
                    IconButton(onClick = { navigator.navigate(ImportTeamsPageDestination) }) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                    }
                    // clear button
                    IconButton(onClick = { navigator.navigate(ClearListDialogDestination) }) {
                        Icon(Icons.Default.ClearAll, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        // state of the main list
        val lazyListState = rememberLazyListState()
        // update app state when list is reordered
        val reorderableLazyColumnState =
            rememberReorderableLazyColumnState(lazyListState = lazyListState) { from, to ->
                viewModel.appState.value =
                    appState.copy(list = appState.list.toMutableList().apply { add(to.index, removeAt(from.index)) })
            }

        /**
         * Updates an existing item with a matching ID in the app state with [item].
         */
        fun updateItem(item: MainListItem) {
            viewModel.appState.value = appState.copy(list = appState.list.map { if (it.id == item.id) item else it })
        }

        /**
         * Deletes an item with a matching ID in the app state.
         */
        fun deleteItem(item: MainListItem) {
            viewModel.appState.value = appState.copy(list = appState.list.filterNot { it.id == item.id })
        }
        // main list
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
        ) {
            // show items
            items(appState.list, key = { it.id }) { item ->
                // allow reordering
                ReorderableItem(reorderableLazyListState = reorderableLazyColumnState, key = item.id) {
                    // check if the item is a team or a divider
                    when (item) {
                        is MainListItem.Team -> {
                            Box {
                                // whether the menu is expanded
                                var expanded by rememberSaveable { mutableStateOf(false) }
                                // team card
                                Card(
                                    colors =
                                        CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                ) {
                                    // stack horizontally
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier =
                                            Modifier
                                                .combinedClickable(onLongClick = { expanded = true }) {}
                                                .fillMaxWidth()
                                    ) {
                                        // drag handle
                                        IconButton(onClick = {}, modifier = Modifier.draggableHandle()) {
                                            Icon(Icons.Default.DragHandle, contentDescription = null)
                                        }
                                        // team number
                                        Text(item.number)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        // comment
                                        Text(item.comment, style = MaterialTheme.typography.labelSmall)
                                        // move items to opposite sides
                                        Spacer(modifier = Modifier.weight(1f))
                                        // team rating counter
                                        Counter(value = item.rating, setValue = { updateItem(item.copy(rating = it)) })
                                    }
                                }
                                // menu for team actions
                                TeamMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    actions =
                                        TeamMenuActions(
                                            edit = {
                                                navigator.navigate(EditTeamDialogDestination(id = item.id))
                                            },
                                            delete = { deleteItem(item) }
                                        )
                                )
                            }
                        }
                        is MainListItem.Divider -> {
                            Box {
                                // whether the menu is expanded
                                var expanded by rememberSaveable { mutableStateOf(false) }
                                // divider card
                                Card {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier =
                                            Modifier
                                                .combinedClickable(onLongClick = { expanded = true }) {}
                                                .fillMaxWidth()
                                    ) {
                                        // drag handle
                                        IconButton(onClick = {}, modifier = Modifier.draggableHandle()) {
                                            Icon(Icons.Default.DragHandle, contentDescription = null)
                                        }
                                        // label
                                        Text(item.label)
                                    }
                                }
                                // menu for divider actions
                                DividerMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    actions =
                                        DividerMenuActions(
                                            rename = {
                                                navigator.navigate(EditDividerLabelDialogDestination(id = item.id))
                                            },
                                            delete = { deleteItem(item) }
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
        // check if there are items
        if (appState.list.isEmpty()) {
            // no items, show placeholder
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp)
            ) {
                // help text
                Text("Nothing here yet! Add some teams to get started.", textAlign = TextAlign.Center)
                // button to add a team
                OutlinedButton(onClick = { navigator.navigate(AddTeamDialogDestination) }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add a team")
                }
            }
        }
    }
}

/**
 * Transitions for [MainPage].
 */
object MainPageTransitions : Transitions(destination = MainPageDestination, enter = { fadeIn() }, exit = { fadeOut() })
