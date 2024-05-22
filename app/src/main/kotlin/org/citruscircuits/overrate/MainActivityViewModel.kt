package org.citruscircuits.overrate

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.citruscircuits.overrate.data.MainListItem
import org.citruscircuits.overrate.data.configFile

/**
 * [ViewModel] for [MainActivity].
 */
class MainActivityViewModel : ViewModel() {
    /**
     * The state of the app.
     */
    val appState = MutableStateFlow(AppState(list = emptyList(), importedTeams = emptySet()))

    /**
     * Loads the app state and starts watching for changes.
     */
    fun load(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // read state from file
            appState.value =
                if (context.configFile.exists()) {
                    Json.decodeFromString(context.configFile.readText())
                } else {
                    AppState(emptyList(), emptySet())
                }
            // write to file on every change in state
            viewModelScope.launch(Dispatchers.IO) {
                appState.collect { state ->
                    Log.i("MainActivityViewModel", "App state updated: $state")
                    context.configFile.writeText(Json.encodeToString(state))
                }
            }
        }
    }

    /**
     * Adds a team item with the given [number].
     *
     * @param number The team number.
     */
    fun addTeam(number: String, comment: String) {
        appState.value = appState.value.copy(
            list = appState.value.list + MainListItem.Team(number = number, rating = 0, comment = comment)
        )
    }

    /**
     * Adds team items from [teams] with rankings from [picklist] added as comments.
     */
    fun addPicklistTeams(teams: Collection<String>, picklist: Collection<String>) {
        appState.value = appState.value.copy(
            list = appState.value.list.toMutableList().apply {
                addAll(
                    teams.map { team ->
                        MainListItem.Team(
                            number = team,
                            rating = 0,
                            comment = picklist.indexOf(team).takeIf { it != -1 }?.let { "Rank ${it + 1}" } ?: ""
                        )
                    }
                )
            }
        )
    }

    /**
     * Adds a divider with the given [label].
     *
     * @param label The label of the divider.
     */
    fun addDivider(label: String = "Divider") {
        appState.value =
            appState.value.copy(list = appState.value.list + MainListItem.Divider(label = label))
    }

    /**
     * Adds a collection of team numbers to the set of imported teams.
     *
     * @param teams The team numbers to import.
     */
    fun importTeams(teams: Collection<String>) {
        appState.value =
            appState.value.copy(importedTeams = appState.value.importedTeams + teams)
    }
}
