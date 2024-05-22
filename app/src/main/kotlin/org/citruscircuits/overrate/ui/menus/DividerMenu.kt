package org.citruscircuits.overrate.ui.menus

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Menu for divider options.
 *
 * @param expanded Whether the menu is expanded.
 * @param onDismissRequest Callback for dismissing the menu.
 * @param actions Actions to run when items are selected.
 */
@Composable
fun DividerMenu(expanded: Boolean, onDismissRequest: () -> Unit, actions: DividerMenuActions) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(
            text = { Text("Rename") },
            onClick = {
                actions.rename()
                onDismissRequest()
            },
            leadingIcon = { Icon(Icons.Default.DriveFileRenameOutline, contentDescription = null) }
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            onClick = {
                actions.delete()
                onDismissRequest()
            },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
        )
    }
}

/**
 * Actions holder for the [DividerMenu].
 */
data class DividerMenuActions(val rename: () -> Unit, val delete: () -> Unit)
