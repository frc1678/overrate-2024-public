package org.citruscircuits.overrate.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Base interface for items that can be in the app's main list.
 */
@Serializable
sealed interface MainListItem {
    /**
     * A unique ID for this item.
     */
    val id: String

    /**
     * Class representing a team in the main list.
     *
     * @param number The team number.
     * @param rating The team's current rating.
     * @param comment A comment shown next to the team number.
     */
    @Serializable
    @SerialName("team")
    data class Team(
        val number: String,
        val rating: Int,
        val comment: String,
        override val id: String = UUID.randomUUID().toString()
    ) : MainListItem

    /**
     * Class representing a divider in the main list.
     *
     * @param label The label of the divider.
     */
    @Serializable
    @SerialName("divider")
    data class Divider(val label: String, override val id: String = UUID.randomUUID().toString()) : MainListItem
}
