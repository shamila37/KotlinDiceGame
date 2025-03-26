//Video link :- https://youtu.be/Kq3rPBkFDqg

package com.example.dicegamecw

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dicegamecw.ui.theme.DiceGameCWTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceGameCWTheme {
                HomePage()
            }
        }
    }
}

@Composable
fun HomePage() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Use both below ones for screen orientation part
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var winningScore by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.dice_background2),
            contentDescription = "Home screen background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            "🎲 Dice Game 🎲",
            style = TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .padding(top = 160.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        if (isPortrait){ // Device on portrait screen orientation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("If you want you can update Winning Score.", color = Color.Green)
                TextField(
                    value = winningScore,
                    onValueChange = { winningScore = it },
                    label = { Text("Default winning score 101", fontSize = 15.sp) },
                    modifier = Modifier.size(width = 350.dp, height = 75.dp).padding(all = 10.dp)
                )

                HomeScreenButtonsOrder(context, showDialog, onShowDialog = { showDialog = it }, winningScore)

            }
        }else{ // Device on landscape screen orientation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Text("If you want you can update Winning Score.", color = Color.Green)
                    TextField(
                        value = winningScore,
                        onValueChange = { winningScore = it },
                        label = { Text("Default winning score 101", fontSize = 15.sp) },
                        modifier = Modifier.size(width = 350.dp, height = 75.dp)
                            .padding(all = 10.dp)
                    )
                }

                HomeScreenButtonsOrder(context, showDialog, onShowDialog = { showDialog = it }, winningScore)
            }
        }

    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(16.dp),
            textContentColor = Color.White,
            title = {
                Text(
                    text = "About",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                LazyColumn {
                    item {
                        Text(
                            text = "Student ID : W1986643/ 20223147 \n" +
                                    "Student name : Shamila Ashan Gunarathna \n\n" +
                                    "I confirm that I understand what plagiarism is and have" +
                                    " read and understood the section on Assessment Offences in" +
                                    " the Essential Information for Students. The work that I have " +
                                    "submitted is entirely my own. Any work from other authors is" +
                                    " duly referenced and acknowledged.",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Go back to home")
                }
            }
        )
    }
}

// Home screen "new game" and "about" buttons
@Composable
fun HomeScreenButtonsOrder(context: Context, showDialog: Boolean, onShowDialog: (Boolean) -> Unit, winningScore: String = ""){
    Button(onClick = {
        val intent = Intent(context, NewGameActivity::class.java)
        intent.putExtra("winningScore", winningScore)
        context.startActivity(intent)
    },
        modifier = Modifier.size(width = 320.dp, height = 80.dp).padding(all = 10.dp)
    ) {
        Text(text = "New Game", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }

    Button(onClick = {
        onShowDialog(true)
    },
        modifier = Modifier.size(width = 320.dp, height = 80.dp).padding(all = 10.dp)
    ) {
        Text(text = "About", fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiceGameCWTheme {
        HomePage()
    }
}


//w1986643/20223147
//Shamila Ashan Gunarathna