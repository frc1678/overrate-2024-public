package org.citruscircuits.overrate.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Counter component with increment/decrement buttons.
 *
 * @param value The current value of the counter.
 * @param setValue Setter for the current value of the counter.
 */
@Composable
fun Counter(value: Int, setValue: (Int) -> Unit, modifier: Modifier = Modifier) {
    // stack items horizontally
    // centered vertically
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        // decrement button
        IconButton(onClick = { setValue(value - 1) }) {
            Icon(Icons.Default.Remove, contentDescription = null)
        }
        // animate value going up or down
        AnimatedContent(
            targetState = value,
            label = "",
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
                } else {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                }.using(SizeTransform(clip = false))
            }
        ) { target ->
            // current value
            Text(target.toString())
        }
        // increment button
        IconButton(onClick = { setValue(value + 1) }) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}
