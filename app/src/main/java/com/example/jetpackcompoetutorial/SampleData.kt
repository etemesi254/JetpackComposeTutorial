package com.example.jetpackcompoetutorial

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import java.time.LocalDateTime

enum class Direction {
    Left,
    Right,
}

data class Author(
    val author: String,
    val id: Int,
    val authorDrawable: Int = R.drawable.person,
    val direction: Direction,
)

data class Message(
    val author: Author,
    val body: String,
    val id: Int,
    val time: LocalDateTime? = null,
    val imageUrl: List<String> = listOf(),

    )

val lexi: Author = Author("Lexi", 0, R.drawable.woman_with_beer, Direction.Left);
val remi: Author = Author("Remi", 1, R.drawable.man_with_beer, Direction.Right);

/**
 * SampleData for Jetpack Compose Tutorial
 */
object SampleData {
    // Sample conversation data
    @RequiresApi(Build.VERSION_CODES.O)
    val conversationSample = mutableStateListOf(
        Message(
            lexi,
            "Test...Test...Test...",
            0,
            time = LocalDateTime.now(),
        ),
        Message(
            remi,
            "Hello Lexi",
            1,
            time = LocalDateTime.now().plusMinutes(10)

        ),
        Message(
            lexi,
            """What is your favourite programming language?""".trim(), 0,
            time = LocalDateTime.now().plusMinutes(20)

        ),
        Message(
            remi,
            "I think Kotlin is my favorite programming language.\n It's so much fun!".trim(), 1,
            time = LocalDateTime.now().plusMinutes(30)

        ),
        Message(
            lexi,
            "Searching for alternatives to XML layouts...", 0,
            time = LocalDateTime.now().plusMinutes(40)

        ),
        Message(
            remi,
            """Hey, take a look at Jetpack Compose, it's great!
            It's the Android's modern toolkit for building native UI.
            It simplifies and accelerates UI development on Android.
            Less code, powerful tools, and intuitive Kotlin APIs :)""".trim(),
            1,
            time = LocalDateTime.now().plusMinutes(50)

        ),
        Message(
            lexi,
            "It's available from API 21+ :)",
            0
        ),
        Message(
            remi,
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?",
            1,
        ),
        Message(
            remi,
            "Android Studio next version's name is Arctic Fox",
            0
        ),
        Message(
            lexi,
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^",
            1,
            time = LocalDateTime.now().plusMinutes(60)

        ),
        Message(
            lexi,
            "I didn't know you can now run the emulator directly from Android Studio",
            0
        ),
        Message(
            remi,
            "Compose Previews are great to check quickly how a composable layout looks like", 1
        ),
        Message(
            remi,
            "Previews are also interactive after enabling the experimental setting",
            1
        ),
        Message(
            remi,
            "Have you tried writing build.gradle with KTS?",
            0
        ),
        Message(
            lexi,
            "Do we support loading images?",
            0,
            imageUrl = listOf(
                "https://images.unsplash.com/photo-1695650911648-718c6ef64972?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzfHx8ZW58MHx8fHx8&auto=format&fit=crop&w=500&q=60"
            ),
        ),
    )
}
