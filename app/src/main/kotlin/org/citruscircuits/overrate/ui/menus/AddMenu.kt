package org.citruscircuits.overrate.ui.menus

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SafetyDivider
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Menu for adding an item to the main list.
 *
 * @param expanded Whether the menu is expanded.
 * @param onDismissRequest Callback for dismissing the menu.
 * @param actions Actions to run when items are selected.
 */
@Composable
fun AddMenu(expanded: Boolean, onDismissRequest: () -> Unit, actions: AddMenuActions) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(
            text = { Text("Add team") },
            onClick = {
                actions.addTeam()
                onDismissRequest()
            },
            leadingIcon = { Icon(Icons.Default.SmartToy, contentDescription = null) }
        )
        DropdownMenuItem(
            text = { Text("Add divider") },
            onClick = {
                actions.addDivider()
                onDismissRequest()
            },
            leadingIcon = { Icon(Icons.Default.SafetyDivider, contentDescription = null) }
        )
    }
}

/**
 * Actions holder for the [AddMenu].
 */
data class AddMenuActions(val addTeam: () -> Unit, val addDivider: () -> Unit)
