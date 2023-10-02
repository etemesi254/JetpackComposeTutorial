package com.example.jetpackcompoetutorial

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
//import androidx.compose.material3.MaterialTheme

// ...
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


const val SCREEN_CONSTANT = 0.64F;

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleOutlinedTextFieldSample(message: SnapshotStateList<Message>) {
    var text by remember { mutableStateOf("") }

    Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it;
            },
            label = { Text("New Chat") }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Button(onClick = {
            var author = Message(lexi, text, 0, time = LocalDateTime.now())
            message.add(author)
            text = ""
        }) {
            Text("Send")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Conversation(messages: SnapshotStateList<Message>) {

    // enable scroll to bottom on new messages added

    val lazyColumnListState = rememberLazyListState()
    val corroutineScope = rememberCoroutineScope()


    Scaffold(
        content = {
            LazyColumn(modifier = Modifier.padding(it), state = lazyColumnListState) {
                corroutineScope.launch {
                    // scroll to the last item
                    lazyColumnListState.scrollToItem(messages.size - 1)

                }

                items(messages) { message ->
                    if (message.author.direction == Direction.Left)
                        LeftMessageCard(message) else RightMessageCard(message)
                }
            }
        },
        bottomBar = {
            SimpleOutlinedTextFieldSample(messages)
        }
    )
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Conversation(SampleData.conversationSample)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeftMessageCard(msg: Message) {
    val configuration = LocalConfiguration.current

    // We keep track if the message is expanded or not in this
    // variable
    var isExpanded by remember { mutableStateOf(false) }
    // surfaceColor will be updated gradually from one color to the other
    val surfaceColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        label = "surface_color",
    )
    // We toggle the isExpanded variable when we click on this Column
    Column(modifier = Modifier
        .padding(all = 4.dp)
        .clickable { isExpanded = !isExpanded }) {
        Row {
            Image(
                painter = painterResource(msg.author.authorDrawable),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = msg.author.author,
                        //color = authorColor,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    if (!msg.imageUrl.isEmpty()) {
                        loadImage(images = msg.imageUrl)
                    }
                    Text(
                        text = msg.body,
                        modifier = Modifier
                            .padding(start = 0.dp, top = 4.dp, bottom = 4.dp)
                            .widthIn(50.dp, (configuration.screenWidthDp * SCREEN_CONSTANT).dp),
                        // If the message is expanded, we display all its content
                        // otherwise we only display the first line
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (msg.time != null) {
                        formatDateAndTime(
                            timeString = msg.time.format(
                                DateTimeFormatter.ofPattern(
                                    "HH:mm"
                                )
                            ),
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun loadImage(images: List<String>) {
    val showDialog = remember { mutableStateOf(false) }
    val imageSelected = remember { mutableStateOf("") }
    if (showDialog.value) {
        // alert...
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                AsyncImage(
                    model = imageSelected,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    contentDescription = null,
                )
            }

        }
    }
    Column {
        images.forEach { image ->
            Box(modifier = Modifier.clickable {
                imageSelected.value = image;
                showDialog.value = true;
            }) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                )
            }
        }
    }


}

@Composable
fun formatDateAndTime(timeString: String) {
    Column(horizontalAlignment = Alignment.End) {
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = timeString,
            modifier = Modifier
                .padding(all = 0.dp)
                .align(Alignment.End),
            textAlign = TextAlign.End,
            fontSize = 10.sp,
            // If the message is expanded, we display all its content
            // otherwise we only display the first line
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RightMessageCard(msg: Message) {
    // We keep track if the message is expanded or not in this
    // variable
    var isExpanded by remember { mutableStateOf(false) }
    // surfaceColor will be updated gradually from one color to the other
    val surfaceColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface,
        label = "surface_color",
    )
    val authorColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface,
        label = "surface_color",
    )
    val configuration = LocalConfiguration.current

    // We toggle the isExpanded variable when we click on this Column
    Column(
        modifier = Modifier
            .fillMaxWidth() // for align to work
            .padding(10.dp)
            .clickable { isExpanded = !isExpanded },
        horizontalAlignment = Alignment.End,

        //horizontalArrangement = Arrangement.End

    ) {
        Row() {

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = msg.author.author,
                        //color = authorColor,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    if (!msg.imageUrl.isEmpty()) {
                        loadImage(images = msg.imageUrl)
                    }
                    Text(
                        text = msg.body,
                        textAlign = TextAlign.End,

                        modifier = Modifier
                            .padding(start = 0.dp, top = 4.dp, bottom = 4.dp)
                            .widthIn(50.dp, (configuration.screenWidthDp * SCREEN_CONSTANT).dp),
                        // If the message is expanded, we display all its content
                        // otherwise we only display the first line
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (msg.time != null) {
                        formatDateAndTime(
                            timeString = msg.time.format(
                                DateTimeFormatter.ofPattern(
                                    "HH:mm"
                                )
                            ),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(msg.author.authorDrawable),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
        }
    }
}

