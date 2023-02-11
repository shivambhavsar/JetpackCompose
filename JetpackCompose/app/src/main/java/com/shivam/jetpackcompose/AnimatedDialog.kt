package com.shivam.jetpackcompose

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val ANIMATION_TIME = 500L
const val DIALOG_BUILD_TIME = 300L

enum class AnimationType {
    SlideInSlideOut, ScaleInScaleOut, SlideInSlideOutVertically, CurvedEntryExit
}

@Composable
fun AnimatedTransitionDialog(
    animationType: AnimationType,
    onDismissRequest: () -> Unit,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable (AnimatedTransitionDialogHelper) -> Unit
) {
    val onDismissSharedFlow: MutableSharedFlow<Any> = remember { MutableSharedFlow() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val animateTrigger = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animateTrigger.value = true
        }
        launch {
            onDismissSharedFlow.asSharedFlow().collectLatest {
                startDismissWithExitAnimation(animateTrigger, onDismissRequest)
            }
        }
    }
    Dialog(onDismissRequest = {
        coroutineScope.launch {
            startDismissWithExitAnimation(animateTrigger, onDismissRequest)
        }
    }) {
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedScaleInTransition(
                animationType = animationType,
                visible = animateTrigger.value
            ) {
                content(AnimatedTransitionDialogHelper(coroutineScope, onDismissSharedFlow))
            }
        }
    }
}

@Composable
internal fun AnimatedScaleInTransition(
    animationType: AnimationType,
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    when (animationType) {
        AnimationType.ScaleInScaleOut -> ScaleInScaleOut(visible = visible, content = content)
        AnimationType.CurvedEntryExit -> CurvedEntryExit(visible = visible, content = content)
        AnimationType.SlideInSlideOutVertically -> SlideInSlideOutVertically(
            visible = visible,
            content = content
        )
        AnimationType.SlideInSlideOut -> SlideInSlideOut(visible = visible, content = content)
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalTransitionApi::class)
@Composable
internal fun CurvedEntryExit(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (visible) 0F else 30F,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    AnimatedVisibility(
        visible = visible,
        enter = slideIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            initialOffset = { IntOffset(it.width, -it.height) }
        ),
        exit = slideOut(
            animationSpec = tween(ANIMATION_TIME.toInt()),
            targetOffset = { IntOffset(it.width, -it.height) }
        ),
        content = {
            Column(modifier = Modifier.rotate(rotationAngle)) {
                content()
            }
        }
    )
}

@Composable
internal fun SlideInSlideOut(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            initialOffset = { IntOffset(it.width, -it.height) }
        ),
        exit = slideOut(
            animationSpec = tween(ANIMATION_TIME.toInt()),
            targetOffset = { IntOffset(it.width, -it.height) }
        ),
        content = content
    )
}

@Composable
internal fun SlideInSlideOutVertically(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(ANIMATION_TIME.toInt()),
            initialOffsetY = { -it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(ANIMATION_TIME.toInt()),
            targetOffsetY = { -it }
        ),
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScaleInScaleOut(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(ANIMATION_TIME.toInt())
        ),
        exit = scaleOut(
            animationSpec = tween(ANIMATION_TIME.toInt())
        ),
        content = content
    )
}

suspend fun startDismissWithExitAnimation(
    animateTrigger: MutableState<Boolean>,
    onDismissRequest: () -> Unit
) {
    animateTrigger.value = false
    delay(ANIMATION_TIME)
    onDismissRequest()
}

class AnimatedTransitionDialogHelper(
    private val coroutineScope: CoroutineScope,
    private val onDismissFlow: MutableSharedFlow<Any>
) {

    fun triggerAnimatedDismiss() {
        coroutineScope.launch {
            onDismissFlow.emit(Any())
        }
    }
}