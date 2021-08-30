package com.chen.composelearn.ui.theme

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


@Composable
fun BackHandler(backDispatcher: OnBackPressedDispatcher, onBack: () -> Unit) {

    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)

    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        // Always intercept back events. See the SideEffect for
        // a more complete version
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    // If `backDispatcher` changes, dispose and reset the effect
    DisposableEffect(backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(backCallback)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}