package org.citruscircuits.overrate

import kotlinx.serialization.Serializable
import org.citruscircuits.overrate.data.MainListItem

/**
 * Holder for the app's state.
 *
 * @param list The contents of the main list.
 * @param importedTeams The set of team numbers that have been imported.
 */
@Serializable
data class AppState(val list: List<MainListItem>, val importedTeams: Set<String>)
