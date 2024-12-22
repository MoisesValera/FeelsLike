package com.mevalera.feelslike.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mevalera.feelslike.ui.composables.searchlocation.SearchLocationBar
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchLocationBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenSearchBarLoads_showsPlaceholder() {
        composeTestRule.setContent {
            MaterialTheme {
                SearchLocationBar(onSearchCity = {})
            }
        }

        composeTestRule.onNodeWithText("Search Location").assertIsDisplayed()
    }

    @Test
    fun whenTextEntered_callsOnSearchCity() {
        val onSearchCity = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            MaterialTheme {
                SearchLocationBar(onSearchCity = onSearchCity)
            }
        }

        composeTestRule.onNodeWithTag("searchBar").performTextInput("London")
        verify { onSearchCity("London") }
    }

    @Test
    fun whenCitySelected_clearsSearchText() {
        composeTestRule.setContent {
            MaterialTheme {
                SearchLocationBar(
                    onSearchCity = {},
                    cityWasSelected = true,
                )
            }
        }

        composeTestRule.onNodeWithTag("searchBar").assertTextEquals("")
    }

    @Test
    fun searchIcon_isDisplayed() {
        composeTestRule.setContent {
            MaterialTheme {
                SearchLocationBar(onSearchCity = {})
            }
        }

        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    @Test
    fun searchBar_maintainsTextDuringUpdates() {
        val onSearchCity = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            MaterialTheme {
                SearchLocationBar(
                    onSearchCity = onSearchCity,
                    cityWasSelected = false,
                )
            }
        }

        // Enter text
        composeTestRule.onNodeWithTag("searchBar").performTextInput("London")
        verify { onSearchCity("London") }

        // Verify placeholder is hidden when text is entered
        composeTestRule.onNodeWithText("Search Location").assertDoesNotExist()
    }
}
