package com.mevalera.feelslike.ui.composables.searchlocation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mevalera.feelslike.R

@Composable
fun SearchLocationBar(
    onSearchCity: (String) -> Unit,
    cityWasSelected: Boolean = false,
) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    fun clearSearchFocus() {
        keyboardManager?.hide()
        focusManager.clearFocus()
    }

    LaunchedEffect(cityWasSelected) {
        if (cityWasSelected) {
            searchText = ""
        }
    }

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.search_bar_height))
                .padding(horizontal = dimensionResource(R.dimen.search_bar_horizontal_padding)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.search_bar_corner_radius)),
        color = Color(0xFFF2F2F2),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.search_bar_content_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Box(
                modifier = Modifier.weight(0.9f),
            ) {
                if (searchText.isEmpty()) {
                    Text(
                        text = stringResource(R.string.search_location_hint),
                        color = Color(0xFFC4C4C4),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                BasicTextField(
                    value = searchText,
                    onValueChange = { newText ->
                        searchText = newText
                        onSearchCity(newText)
                    },
                    textStyle =
                        TextStyle(
                            color = Color.Black,
                            fontSize = dimensionResource(R.dimen.search_bar_text_size).value.sp,
                        ),
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Search,
                        ),
                    keyboardActions =
                        KeyboardActions(
                            onSearch = {
                                clearSearchFocus()
                            },
                        ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag("searchBar"),
                )
            }

            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    clearSearchFocus()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon_description),
                    tint = Color(0xFFC4C4C4),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchLocationBarPreview() {
    SearchLocationBar(
        onSearchCity = {},
    )
}
