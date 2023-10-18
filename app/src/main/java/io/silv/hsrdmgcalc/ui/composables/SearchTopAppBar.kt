package io.silv.hsrdmgcalc.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onSearchText: (String) -> Unit,
    color: Color = Color.Transparent,
    scrolledColor: Color = Color.Transparent,
    showTextField: Boolean,
    navigationIconLabel: String? = null,
    navigationIcon: ImageVector? = null,
    onNavigationIconClicked: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit),
    scrollBehavior: TopAppBarScrollBehavior,
    searchText: String,
    onForceSearch: () -> Unit = {},
    onSearchChanged: (active: Boolean) -> Unit,
) {
    var alreadyRequestedFocus by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        title = {
            if (!showTextField) {
                Text(title)
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (showTextField) {
                navigationIcon?.let {
                    IconButton(
                        onClick = onNavigationIconClicked,
                    ) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = navigationIconLabel
                        )
                    }
                }
            }
        },
        actions = {
            AnimatedVisibility(visible = showTextField, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 2.dp, bottom = 2.dp, start = 32.dp)
                        .focusRequester(focusRequester),
                    value = searchText,
                    placeholder = { Text("Search characters...") },
                    onValueChange = onSearchText,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = LocalContentColor.current,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    trailingIcon = {
                        AnimatedVisibility(visible = searchText.isNotBlank(), enter = fadeIn(), exit = fadeOut()) {
                            IconButton(
                                onClick = { onSearchText("") }
                            ) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                            }
                        }

                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onForceSearch()
                        },
                    ),
                )
                LaunchedEffect(Unit) {
                    if (!alreadyRequestedFocus) {
                        focusRequester.requestFocus()
                        alreadyRequestedFocus = true
                    }
                    if (searchText.isNotBlank()) {
                        onSearchText(searchText)
                    }
                }
            }
            val icon = when (showTextField) {
                true -> Icons.Filled.SearchOff
                false -> Icons.Filled.Search
            }
            IconButton(
                onClick = {
                    alreadyRequestedFocus = false
                    onSearchChanged(!showTextField)
                },
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
            actions()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = color,
            scrolledContainerColor = scrolledColor,
        ),
        scrollBehavior = scrollBehavior,
    )
}