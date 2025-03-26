package com.example.dicegamecw

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class NewGameActivity:ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var winningScore = intent.getStringExtra("winningScore")?.toIntOrNull() ?: 101
        setContent {
            GameScreen(winningScore)
        }
    }
}

@Composable
fun GameScreen(winningScore :Int){

    // Use both below ones for screen orientation part
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var context = LocalContext.current
    var humanScore by rememberSaveable { mutableStateOf(0) }
    var computerScore by rememberSaveable { mutableStateOf(0) }
    var humanTieScore by rememberSaveable { mutableStateOf(0) }
    var computerTieScore by rememberSaveable { mutableStateOf(1) }
    var humanWinsCount by rememberSaveable { mutableStateOf(0) }
    var computerWinsCount by rememberSaveable { mutableStateOf(0) }
    var humanDiceRolls by rememberSaveable { mutableStateOf(List(5){Random.nextInt(1,6)}) }
    var computerDiceRolls by rememberSaveable { mutableStateOf(List(5){Random.nextInt(1,6)}) }
    var showGameOverAlertBox by rememberSaveable { mutableStateOf(false) }
    var gameOverText by rememberSaveable { mutableStateOf("") }
    var gameOverColor by remember { mutableStateOf(Color.Black) }
    var diceRollHistory by rememberSaveable { mutableStateOf<List<Pair<List<Int>,List<Int>>>>(emptyList()) }
    var showHumanHistoryAlertBox by rememberSaveable { mutableStateOf(false) }
    var showComputerHistoryAlertBox by rememberSaveable { mutableStateOf(false) }
    var HumanReRollCount by rememberSaveable { mutableStateOf(0) }
    var scoreButton by rememberSaveable { mutableStateOf(false) }
    var singleTimeUseThrowButton by rememberSaveable { mutableStateOf(false) }

    var selectedDice = rememberSaveable { mutableStateOf(mutableSetOf<Int>()) }

    Box (
        modifier = Modifier.fillMaxSize(),
    ){
//        Background image
        Image(
            painter = painterResource(R.drawable.dice_background4),
            contentDescription = "Game screen background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = Modifier.fillMaxWidth().padding(25.dp)

        ) {

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                    .background(Color(0xFF739e82))
                    .padding(10.dp)
            ){
                Row {
                    if (isPortrait){
                        Column {
                            Row {
                                Text("Hit $winningScore first and win the game!", Modifier.padding(10.dp))
                                Text("H: $humanWinsCount/C: $computerWinsCount", Modifier.padding(10.dp), fontWeight = FontWeight.Bold, color = Color.Yellow, fontSize = 18.sp)
                            }

                            Text(
                                "Human score: $humanScore | Computer score: $computerScore",
                                fontSize = 18.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }else{
                        Row {
                            Row {
                                Text("Hit $winningScore first and win the game!", Modifier.padding(10.dp))
                                Text("H: $humanWinsCount/C: $computerWinsCount", Modifier.padding(10.dp), fontWeight = FontWeight.Bold, color = Color.Yellow, fontSize = 18.sp)
                            }

                            Text(
                                "Human score: $humanScore | Computer score: $computerScore",
                                fontSize = 18.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            if (isPortrait){ // Human and Computer on portrait screen orientation
                Column {
//            Human dice data
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(19.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                            .background(Color.Gray)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                "Human Dice:",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

//                    Human dice image row
                            Row {
                                humanDiceRolls.forEachIndexed { index, value ->
                                    DiceImagesRow(value, index, selectedDice, isSelectable = true)
                                }
                            }

                            Text("You can re-roll the required dice/dices two times in a round. Select the dice/dices to keep.")

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                            ) {
                                Row(
                                    Modifier.align(Alignment.CenterHorizontally)
                                ) {
//                                Re-roll button
                                    Button(
                                        onClick = {
                                            if (HumanReRollCount < 2) {
                                                humanDiceRolls =
                                                    humanDiceRolls.mapIndexed { index, value ->
                                                        if (index !in selectedDice.value) (1..6).random() else value
                                                    }
                                                selectedDice.value =
                                                    mutableSetOf()   // Reset selections after re-roll
                                                HumanReRollCount++                    // Increase re-roll count

                                                if (HumanReRollCount == 2) {
                                                    val humanScoreThisRound = humanDiceRolls.sum()
                                                    val computerScoreThisRound = computerDiceRolls.sum()

                                                    humanScore += humanScoreThisRound
                                                    computerScore += computerScoreThisRound

                                                    diceRollHistory = diceRollHistory + listOf(
                                                        Pair(
                                                            humanDiceRolls.toList(),
                                                            computerDiceRolls.toList()
                                                        )
                                                    )

                                                    scoreButton =
                                                        true    // Disable score button after 2 re-rolls
                                                    singleTimeUseThrowButton =
                                                        false // Enable throw button after 2 re-rolls

//                                                Winning alert dialog logic
                                                    if (humanScore >= winningScore || computerScore >= winningScore) {
                                                        if (humanScore > computerScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You win!"
                                                            gameOverColor = Color.Green
                                                            humanWinsCount++
                                                        } else if (computerScore > humanScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You loose!"
                                                            gameOverColor = Color.Red
                                                            computerWinsCount++
                                                        }
                                                    }else{// In the tie round
                                                        while (humanTieScore == computerTieScore){
                                                            humanDiceRolls = List(5) { Random.nextInt(1, 6) }
                                                            computerDiceRolls = List(5) { Random.nextInt(1, 6) }

                                                            val humanLastScore = humanDiceRolls.sum()
                                                            val computerLastScore = computerDiceRolls.sum()

                                                            if (humanLastScore > computerLastScore) {
                                                                showGameOverAlertBox = true
                                                                gameOverText = "You win! The tie game round is over now."
                                                                gameOverColor = Color.Green
                                                                humanWinsCount++
                                                            } else if (computerLastScore > humanLastScore) {
                                                                showGameOverAlertBox = true
                                                                gameOverText = "You loose! The tie game round is over now."
                                                                gameOverColor = Color.Red
                                                                computerWinsCount++
                                                            }
                                                        }
                                                    }
                                                }

//                                    Computer player random re-roll strategy - This part runs when the human choose to re-roll
                                                computerDiceRolls =
                                                    computerDiceRolls.mapIndexed { index, value ->
                                                        if (index !in selectedDice.value) (1..6).random() else value
                                                    }

                                            }
                                        },
                                        enabled = HumanReRollCount < 2 && !scoreButton    // Disable button after 2 re-rolls
                                    ) {
                                        Text("Re-roll ($HumanReRollCount/2)", fontSize = 18.sp)
                                    }

//                        Score button
                                    if (!scoreButton) {
                                        Button(
                                            onClick = {
                                                val humanScoreThisRound = humanDiceRolls.sum()
                                                val computerScoreThisRound = computerDiceRolls.sum()

                                                humanScore += humanScoreThisRound
                                                computerScore += computerScoreThisRound

                                                diceRollHistory = diceRollHistory + listOf(
                                                    Pair(
                                                        humanDiceRolls.toList(),
                                                        computerDiceRolls.toList()
                                                    )
                                                )

                                                scoreButton =
                                                    true // Disable score button after click button single time
                                                singleTimeUseThrowButton =
                                                    false // Enable throw button after click score button

//                                            Winning alert dialog logic
                                                if (humanScore >= winningScore || computerScore >= winningScore) {
                                                    if (humanScore > computerScore) {
                                                        showGameOverAlertBox = true
                                                        gameOverText = "You win!"
                                                        gameOverColor = Color.Green
                                                        humanWinsCount++
                                                    } else if (computerScore > humanScore) {
                                                        showGameOverAlertBox = true
                                                        gameOverText = "You loose!"
                                                        gameOverColor = Color.Red
                                                        computerWinsCount++
                                                    }
                                                }else{// In the tie round
                                                    while (humanTieScore == computerTieScore){
                                                        humanDiceRolls = List(5) { Random.nextInt(1, 6) }
                                                        computerDiceRolls = List(5) { Random.nextInt(1, 6) }

                                                        val humanLastScore = humanDiceRolls.sum()
                                                        val computerLastScore = computerDiceRolls.sum()

                                                        if (humanLastScore > computerLastScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You win! The tie game round is over now."
                                                            gameOverColor = Color.Green
                                                            humanWinsCount++
                                                        } else if (computerLastScore > humanLastScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You loose! The tie game round is over now."
                                                            gameOverColor = Color.Red
                                                            computerWinsCount++
                                                        }
                                                    }
                                                }
                                            }
                                        ) {
                                            Text("Score", fontSize = 18.sp)
                                        }
                                    }
                                }

                                Row(
                                    Modifier.align(Alignment.CenterHorizontally)
                                ) {
//                                Human history button
                                    Button(
                                        onClick = {
                                            showHumanHistoryAlertBox = true
                                        },
                                        modifier = Modifier
                                            .size(width = 150.dp, height = 50.dp)
                                            .padding(all = 5.dp)
                                    ) {
                                        Text("History", fontSize = 18.sp)
                                    }
                                }
                            }

//                    Human history alert dialog
                            if (showHumanHistoryAlertBox) {
                                AlertDialog(
                                    onDismissRequest = { showHumanHistoryAlertBox = false },
                                    title = { Text("Human Dice Roll History") },
                                    text = {
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            if (diceRollHistory.isEmpty()) {
                                                Text("No history available", color = Color.Gray)
                                            } else {
                                                diceRollHistory.forEachIndexed { index, rolls ->
                                                    Text(
                                                        "Round ${index + 1} -\nHuman: ${rolls.first}",
                                                        color = Color.Gray
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showHumanHistoryAlertBox = false }) {
                                            Text("Close")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

//            Computer dice data
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(19.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                            .background(Color.Gray)
                            .padding(10.dp)
                    ) {
                        Column {

                            Text(
                                "Computer Dice:",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
//                    Computer dice image row
                            Row {
                                computerDiceRolls.forEachIndexed { index, value ->
                                    DiceImagesRow(value, index, selectedDice, isSelectable = false)
                                }
                            }

//                    Computer history button
                            Button(
                                onClick = {
                                    showComputerHistoryAlertBox = true
                                },
                                modifier = Modifier
                                    .size(width = 150.dp, height = 50.dp)
                                    .padding(all = 5.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text("History", fontSize = 18.sp)
                            }

//                    Computer history alert dialog
                            if (showComputerHistoryAlertBox) {
                                AlertDialog(
                                    onDismissRequest = { showComputerHistoryAlertBox = false },
                                    title = { Text("Computer Dice Roll History") },
                                    text = {
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            if (diceRollHistory.isEmpty()) {
                                                Text("No history available", color = Color.Gray)
                                            } else {
                                                diceRollHistory.forEachIndexed { index, rolls ->
                                                    Text(
                                                        "Round ${index + 1} -\nComputer: ${rolls.second}",
                                                        color = Color.Gray
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showComputerHistoryAlertBox = false }) {
                                            Text("Close")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }else{ // Human and Computer on landscape screen orientation
                Row {
//            Human dice data
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                            .background(Color.Gray)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                "Human Dice:",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

//                    Human dice image row
                            Row {
                                humanDiceRolls.forEachIndexed { index, value ->
                                    DiceImagesRow(value, index, selectedDice, isSelectable = true)
                                }
                            }

                            Text("You can re-roll the required dice/dices two times in a round. Select the dice/dices to keep.")

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    Modifier.align(Alignment.CenterHorizontally)
                                ) {
//                                Re-roll button
                                    Button(
                                        onClick = {
                                            if (HumanReRollCount < 2) {
                                                humanDiceRolls =
                                                    humanDiceRolls.mapIndexed { index, value ->
                                                        if (index !in selectedDice.value) (1..6).random() else value
                                                    }
                                                selectedDice.value =
                                                    mutableSetOf()   // Reset selections after re-roll
                                                HumanReRollCount++                    // Increase re-roll count

                                                if (HumanReRollCount == 2) {
                                                    val humanScoreThisRound = humanDiceRolls.sum()
                                                    val computerScoreThisRound = computerDiceRolls.sum()

                                                    humanScore += humanScoreThisRound
                                                    computerScore += computerScoreThisRound

                                                    diceRollHistory = diceRollHistory + listOf(
                                                        Pair(
                                                            humanDiceRolls.toList(),
                                                            computerDiceRolls.toList()
                                                        )
                                                    )

                                                    scoreButton =
                                                        true    // Disable score button after 2 re-rolls
                                                    singleTimeUseThrowButton =
                                                        false // Enable throw button after 2 re-rolls

//                                                Winning alert dialog logic
                                                    if (humanScore >= winningScore || computerScore >= winningScore) {
                                                        if (humanScore > computerScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You win!"
                                                            gameOverColor = Color.Green
                                                            humanWinsCount++
                                                        } else if (computerScore > humanScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You loose!"
                                                            gameOverColor = Color.Red
                                                            computerWinsCount++
                                                        }
                                                    }else{// In the tie round
                                                        while (humanTieScore == computerTieScore){
                                                            humanDiceRolls = List(5) { Random.nextInt(1, 6) }
                                                            computerDiceRolls = List(5) { Random.nextInt(1, 6) }

                                                            val humanLastScore = humanDiceRolls.sum()
                                                            val computerLastScore = computerDiceRolls.sum()

                                                            if (humanLastScore > computerLastScore) {
                                                                showGameOverAlertBox = true
                                                                gameOverText = "You win! The tie game round is over now."
                                                                gameOverColor = Color.Green
                                                                humanWinsCount++
                                                            } else if (computerLastScore > humanLastScore) {
                                                                showGameOverAlertBox = true
                                                                gameOverText = "You loose! The tie game round is over now."
                                                                gameOverColor = Color.Red
                                                                computerWinsCount++
                                                            }
                                                        }
                                                    }
                                                }

//                                    Computer player random re-roll strategy - This part runs when the human choose to re-roll
                                                computerDiceRolls =
                                                    computerDiceRolls.mapIndexed { index, value ->
                                                        if (index !in selectedDice.value) (1..6).random() else value
                                                    }

                                            }
                                        },
                                        enabled = HumanReRollCount < 2 && !scoreButton    // Disable button after 2 re-rolls
                                    ) {
                                        Text("Re-roll ($HumanReRollCount/2)", fontSize = 14.sp)
                                    }

//                        Score button
                                    if (!scoreButton) {
                                        Button(
                                            onClick = {
                                                val humanScoreThisRound = humanDiceRolls.sum()
                                                val computerScoreThisRound = computerDiceRolls.sum()

                                                humanScore += humanScoreThisRound
                                                computerScore += computerScoreThisRound

                                                diceRollHistory = diceRollHistory + listOf(
                                                    Pair(
                                                        humanDiceRolls.toList(),
                                                        computerDiceRolls.toList()
                                                    )
                                                )

                                                scoreButton =
                                                    true // Disable score button after click button single time
                                                singleTimeUseThrowButton =
                                                    false // Enable throw button after click score button

//                                            Winning alert dialog logic
                                                if (humanScore >= winningScore || computerScore >= winningScore) {
                                                    if (humanScore > computerScore) {
                                                        showGameOverAlertBox = true
                                                        gameOverText = "You win!"
                                                        gameOverColor = Color.Green
                                                        humanWinsCount++
                                                    } else if (computerScore > humanScore) {
                                                        showGameOverAlertBox = true
                                                        gameOverText = "You loose!"
                                                        gameOverColor = Color.Red
                                                        computerWinsCount++
                                                    }
                                                }else{// In the tie round
                                                    while (humanTieScore == computerTieScore){
                                                        humanDiceRolls = List(5) { Random.nextInt(1, 6) }
                                                        computerDiceRolls = List(5) { Random.nextInt(1, 6) }

                                                        val humanLastScore = humanDiceRolls.sum()
                                                        val computerLastScore = computerDiceRolls.sum()

                                                        if (humanLastScore > computerLastScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You win! The tie game round is over now."
                                                            gameOverColor = Color.Green
                                                            humanWinsCount++
                                                        } else if (computerLastScore > humanLastScore) {
                                                            showGameOverAlertBox = true
                                                            gameOverText = "You loose! The tie game round is over now."
                                                            gameOverColor = Color.Red
                                                            computerWinsCount++
                                                        }
                                                    }
                                                }
                                            }
                                        ) {
                                            Text("Score", fontSize = 14.sp)
                                        }
                                    }

//                                Human history button
                                    Button(
                                        onClick = {
                                            showHumanHistoryAlertBox = true
                                        }
                                    ) {
                                        Text("History", fontSize = 14.sp)
                                    }
                                }
                            }

//                    Human history alert dialog
                            if (showHumanHistoryAlertBox) {
                                AlertDialog(
                                    onDismissRequest = { showHumanHistoryAlertBox = false },
                                    title = { Text("Human Dice Roll History") },
                                    text = {
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            if (diceRollHistory.isEmpty()) {
                                                Text("No history available", color = Color.Gray)
                                            } else {
                                                diceRollHistory.forEachIndexed { index, rolls ->
                                                    Text(
                                                        "Round ${index + 1} -\nHuman: ${rolls.first}",
                                                        color = Color.Gray
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showHumanHistoryAlertBox = false }) {
                                            Text("Close")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    //            Main throw button
                    Button(
                        onClick = {
                            if (!singleTimeUseThrowButton) {
                                humanDiceRolls = List(5) { Random.nextInt(1, 6) }
                                computerDiceRolls = List(5) { Random.nextInt(1, 6) }

                                HumanReRollCount = 0 // Reset to 0 the re-roll count
                                scoreButton = false  // Enable score button after press throw

                                singleTimeUseThrowButton = true // Use to prevent multiple clicks
                            }
                        },
                        modifier = Modifier.size(width = 110.dp, height = 150.dp).padding(start = 10.dp).padding(top = 50.dp),
                        enabled = !singleTimeUseThrowButton // Disable throw button for second time pressing

                    ) {
                        Text("Throw", fontSize = 18.sp)
                    }

//            Computer dice data
                    Box(
                        modifier = Modifier
                            .width(400.dp)
                            .padding(5.dp)
                            .padding(start = 10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                            .background(Color.Gray)
                            .padding(10.dp)
                    ) {
                        Column {

                            Text(
                                "Computer Dice:",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
//                    Computer dice image row
                            Row {
                                computerDiceRolls.forEachIndexed { index, value ->
                                    DiceImagesRow(value, index, selectedDice, isSelectable = false)
                                }
                            }

//                    Computer history button
                            Button(
                                onClick = {
                                    showComputerHistoryAlertBox = true
                                },
                                modifier = Modifier
                                    .size(width = 150.dp, height = 50.dp)
                                    .padding(all = 5.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text("History", fontSize = 18.sp)
                            }

//                            Computer history alert dialog
                            if (showComputerHistoryAlertBox) {
                                AlertDialog(
                                    onDismissRequest = { showComputerHistoryAlertBox = false },
                                    title = { Text("Computer Dice Roll History") },
                                    text = {
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            if (diceRollHistory.isEmpty()) {
                                                Text("No history available", color = Color.Gray)
                                            } else {
                                                diceRollHistory.forEachIndexed { index, rolls ->
                                                    Text(
                                                        "Round ${index + 1} -\nComputer: ${rolls.second}",
                                                        color = Color.Gray
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = { showComputerHistoryAlertBox = false }) {
                                            Text("Close")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (isPortrait){ // Throw and back button on portrait screen orientation
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Main throw button
                    Button(
                        onClick = {
                            if (!singleTimeUseThrowButton) {
                                humanDiceRolls = List(5) { Random.nextInt(1, 6) }
                                computerDiceRolls = List(5) { Random.nextInt(1, 6) }

                                HumanReRollCount = 0 // Reset to 0 the re-roll count
                                scoreButton = false  // Enable score button after press throw

                                singleTimeUseThrowButton = true // Use to prevent multiple clicks
                            }
                        },
                        modifier = Modifier.size(width = 250.dp, height = 80.dp).padding(all = 5.dp),
                        enabled = !singleTimeUseThrowButton // Disable throw button for second time pressing

                    ) {
                        Text("Throw", fontSize = 25.sp)
                    }

//                    Game over alert dialog
                    if (showGameOverAlertBox) {
                        AlertDialog(
                            onDismissRequest = { showGameOverAlertBox = false },
                            title = {
                                Text(text = "Game over!")
                            },
                            text = {
                                LazyColumn { // Added lazy column to display the "The rules of the game" text
                                    item {
                                        Text(
                                            gameOverText,
                                            color = gameOverColor,
                                            fontSize = 25.sp,
                                            textAlign = TextAlign.Center
                                        )

                                        if (humanScore >= winningScore || computerScore >= winningScore) {

                                            Spacer(modifier = Modifier.height(15.dp))

                                            Text(
                                                text = "The rules of the game",
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )

                                            Spacer(modifier = Modifier.height(15.dp))

                                            Text(
                                                text = "1. Both players rolls 5 dice at the same time when the \"Throw\" button is pressed.\n" +
                                                        "2. After throw the human can press \"score\" button to update the current score. " +
                                                        "Or can do single re-roll button press and then score button press to update the current score option. " +
                                                        "Or double re-roll button press option then update the current score.\n" +
                                                        "3. In the re-roll option human can choose which dice/dices keep and which dice/dices re-roll/s.\n" +
                                                        "4. The game ends when the human or computer reaches $winningScore or more points.",
                                                fontSize = 15.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(
                                                    horizontal = 5.dp,
                                                    vertical = 1.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    showGameOverAlertBox = false
                                    // Reset the game data
                                    humanScore = 0
                                    computerScore = 0
                                    HumanReRollCount = 0
                                    scoreButton = false // Enable score button after press ok on game over alert dialog
                                    selectedDice.value = mutableSetOf()
                                    diceRollHistory = emptyList()

                                }) {
                                    Text("Ok")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

//                    Go back to home button
                    Button(
                        onClick = {
                            (context as Activity).finish()

                        },
                        modifier = Modifier.size(width = 250.dp, height = 60.dp).padding(all = 5.dp)
                    ) {
                        Text("Go back to home", fontSize = 18.sp)
                    }
                }
            }else{ //Throw and back button on landscape screen orientation
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
//                    Game over alert dialog
                    if (showGameOverAlertBox) {
                        AlertDialog(
                            onDismissRequest = { showGameOverAlertBox = false },
                            title = {
                                Text(text = "Game over!")
                            },
                            text = {
                                LazyColumn { // Added lazy column to display the "The rules of the game" text
                                    item {
                                        Text(
                                            gameOverText,
                                            color = gameOverColor,
                                            fontSize = 25.sp,
                                            textAlign = TextAlign.Center
                                        )

                                        if (humanScore >= winningScore || computerScore >= winningScore) {

                                            Spacer(modifier = Modifier.height(15.dp))

                                            Text(
                                                text = "The rules of the game",
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )

                                            Spacer(modifier = Modifier.height(15.dp))

                                            Text(
                                                text = "1. Both players rolls 5 dice at the same time when the \"Throw\" button is pressed.\n" +
                                                        "2. After throw the human can press \"score\" button to update the current score. " +
                                                        "Or can do single re-roll button press and then score button press to update the current score option. " +
                                                        "Or double re-roll button press option then update the current score.\n" +
                                                        "3. In the re-roll option human can choose which dice/dices keep and which dice/dices re-roll/s.\n" +
                                                        "4. The game ends when the human or computer reaches $winningScore or more points.",
                                                fontSize = 15.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(
                                                    horizontal = 5.dp,
                                                    vertical = 1.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    showGameOverAlertBox = false
                                    // Reset the game data
                                    humanScore = 0
                                    computerScore = 0
                                    HumanReRollCount = 0
                                    scoreButton = false // Enable score button after press ok on game over alert dialog
                                    selectedDice.value = mutableSetOf()
                                    diceRollHistory = emptyList()

                                }) {
                                    Text("Ok")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

//                    Go back to home button
                    Button(
                        onClick = {
                            (context as Activity).finish()
                        },
                        modifier = Modifier.size(width = 250.dp, height = 60.dp).padding(all = 5.dp)
                    ) {
                        Text("Go back to home", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

//Dice image functionality
@Composable
fun DiceImagesRow(value: Int, index: Int, selectedDice: MutableState<MutableSet<Int>>, isSelectable: Boolean) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(47.dp)
            .clickable(enabled = isSelectable) {  // Only allow clicks if selectable
                if (isSelectable) {
                    selectedDice.value = selectedDice.value.toMutableSet().apply {
                        if (contains(index)) remove(index) else add(index)
                    }
                }
            }
            .border(5.dp,
                if (index in selectedDice.value && isSelectable) Color.Gray else Color.Transparent, // Highlight selected dice
                shape = RoundedCornerShape(0.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(addingDiceImage(value)),
            contentDescription = "Dice $value",
            modifier = Modifier.fillMaxSize()
        )
    }
}

//Dice image relevant to value
fun addingDiceImage(value: Int): Int {
    return when (value) {
        1 -> R.drawable.die1
        2 -> R.drawable.die2
        3 -> R.drawable.die3
        4 -> R.drawable.die4
        5 -> R.drawable.die5
        else -> R.drawable.die6
    }
}


//w1986643/20223147
//Shamila Ashan Gunarathna