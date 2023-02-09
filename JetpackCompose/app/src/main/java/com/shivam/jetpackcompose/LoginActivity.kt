package com.shivam.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shivam.jetpackcompose.ui.theme.JetpackComposeTheme

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

@Composable fun mainBody() {
    val input = remember { mutableStateOf(TextFieldValue()) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(1f), verticalArrangement = Arrangement.Center) {
            EnterMobile(input = input)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), horizontalArrangement = Arrangement.End) {
            NextButton(input = input)
        }
    }
}

@Composable fun EnterMobile(input: MutableState<TextFieldValue>) {
    Row(modifier = Modifier
        .padding(all = 20.dp)
        .fillMaxWidth()
        .height(IntrinsicSize.Max)
        .background(color = Color.Black, shape = MaterialTheme.shapes.small)) {
        Row(modifier = Modifier
            .align(Alignment.CenterVertically)
            .fillMaxHeight()
            .background(color = Color.White, shape = MaterialTheme.shapes.small)) {
            Text(text = "+91", color = Color.Black, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 10.dp))
        }
        Spacer(modifier = Modifier.width(2.dp))
        TextField(value = input.value, onValueChange = {
            input.value = it
        }, placeholder = {
            Text("Enter your number here")
        }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent), maxLines = 1, singleLine = true)
    }
}

@Composable fun NextButton(input: MutableState<TextFieldValue>) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        GreetingDialog(name = input.value.text) {
            showDialog.value = false
        }
    }
    IconButton(onClick = {
        if (input.value.text.length == 10) showDialog.value = true
    }, modifier = Modifier
        .size(40.dp)
        .background(color = Color.White, shape = MaterialTheme.shapes.small)) {
        Icon(painter = painterResource(R.drawable.ic_forward), contentDescription = "forward arrow", tint = Color.Black)
    }
}

@Composable fun GreetingDialog(name: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss.invoke() }, properties = DialogProperties(dismissOnBackPress = false)) {
        Greeting(name = name)
    }
}

@Composable fun Greeting(name: String) {
    Column(modifier = Modifier
        .padding(top = 100.dp, bottom = 100.dp)
        .background(color = Color.White, shape = MaterialTheme.shapes.small)) {
        Row(modifier = Modifier.padding(all = 20.dp)) {
            Image(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Contact profile picture", modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .paint(painter = painterResource(id = R.drawable.ic_launcher_background)))

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = "Hello $name!", color = MaterialTheme.colors.secondary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Welcome to JetpackCompose", color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true) @Composable fun DefaultPreview() {
    JetpackComposeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            mainBody()
        }
    }
}