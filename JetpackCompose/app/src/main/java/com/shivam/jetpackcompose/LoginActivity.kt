package com.shivam.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shivam.jetpackcompose.ui.theme.JetpackComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    mainBody()
                }
            }
        }
    }
}

@Composable
fun mainBody() {
    val input = remember { mutableStateOf(TextFieldValue()) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), verticalArrangement = Arrangement.Center
        ) {
            EnterMobile(input = input)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.End
        ) {
            NextButton(input = input)
        }
    }
}

@Composable
fun EnterMobile(input: MutableState<TextFieldValue>) {
    Row(
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(color = Color.Black, shape = MaterialTheme.shapes.small)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .background(color = Color.White, shape = MaterialTheme.shapes.small)
        ) {
            Text(
                text = "+91", color = Color.Black, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        TextField(
            value = input.value,
            onValueChange = {
                input.value = it
            },
            placeholder = {
                Text("Enter your number here")
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent),
            maxLines = 1,
            singleLine = true
        )
    }
}

@Composable
fun NextButton(input: MutableState<TextFieldValue>) {
    var buttonState by remember { mutableStateOf(ButtonState.NEXT) }
    val showDialog = remember { mutableStateOf(false) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    if (showDialog.value) {
        GreetingDialog(name = input.value.text) {
            showDialog.value = false
        }
    }
    IconButton(
        onClick = {
            coroutineScope.launch {
                buttonState = ButtonState.LOADING
                delay(500)
                if (input.value.text.length == 10) showDialog.value = true
                buttonState = ButtonState.NEXT
            }
        }, modifier = Modifier
            .size(40.dp)
            .background(color = Color.White, shape = MaterialTheme.shapes.small)
    ) {
        if (buttonState == ButtonState.NEXT) {
            Icon(
                painter = painterResource(R.drawable.ic_forward),
                contentDescription = "forward arrow",
                tint = Color.Black
            )
        } else if (buttonState == ButtonState.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .graphicsLayer(alpha = 5F)
                    .size(30.dp),
                color = MaterialTheme.colors.secondary,
                strokeWidth = 3.dp
            )
        }
    }
}

enum class ButtonState {
    NEXT, LOADING
}

@Composable
fun CloseButton(helper: AnimatedTransitionDialogHelper) {
    val mainButtonColor = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colors.secondary
    )

    Button(
        onClick = { helper.triggerAnimatedDismiss() }, elevation = null, colors = mainButtonColor,
        modifier = Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(5.dp)
        )
    ) {
        Text(text = "Thank you!!")
    }
}

@Composable
fun GreetingDialog(name: String, onDismiss: () -> Unit) {
    AnimatedTransitionDialog(onDismissRequest = onDismiss) {
        Greeting(name = name, it)
    }
}

@Composable
fun Greeting(name: String, helper: AnimatedTransitionDialogHelper) {
    Column(
        modifier = Modifier
            .padding(top = 100.dp, bottom = 100.dp)
            .background(color = Color.White, shape = MaterialTheme.shapes.small)
    ) {
        Row(modifier = Modifier.padding(all = 20.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .paint(painter = painterResource(id = R.drawable.ic_launcher_background))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = "Hello $name!", color = MaterialTheme.colors.secondary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Welcome to JetpackCompose", color = Color.Black)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CloseButton(helper = helper)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            mainBody()
        }
    }
}