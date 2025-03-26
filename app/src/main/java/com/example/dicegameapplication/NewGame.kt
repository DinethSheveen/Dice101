package com.example.dicegameapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

//LIST OF DICE IMAGES AND THE NUMBERS CORRESPONDING TO THE IMAGES
var diceDrawables = listOf(R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6)
//VARIABLES TO STORE THE NUMBER OF WINS BY EACH PLAYER
var humanWins = 0
var computerWins = 0

class NewGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Gaming()
        }
    }
    @Composable
    fun Gaming(){
        Column(
            Modifier.fillMaxSize().background(color = Color.DarkGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //A VARIABLE TO SET THE TARGET
            var target by rememberSaveable{ mutableStateOf(101) }
            var targetInput by rememberSaveable{ mutableStateOf("") }
            Row(
                Modifier.height(80.dp).background(color= Color.Gray).fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ){
                Text("GAME 101".uppercase(), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
            Row(
                Modifier.fillMaxWidth()
            ){
                TextField(targetInput, onValueChange = {
                    targetInput=it
                    //CONVERTS THE INPUT OF TYPE STRING INTO AN INT, IF THE INPUT IS AN INVALID INTEGER, RETURNS A NULL VALUE
                    target = it.toIntOrNull()?:target       //THE DEFAULT TARGET OF 101 IF THE LEFT SIDE OF THE ELVIS OPERATOR IS NULL
                                                       },
                    label = {Text("SET YOUR TARGET HERE...", fontSize = 20.sp)},
                    modifier = Modifier.fillMaxWidth(),
                    //ENSURES THAT ONLY A NUMERIC KEYBOARD IS SHOWN TO THE USER
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
            }
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                val context = LocalContext.current
                //DEFAULT DRAWABLES VISIBLE ONCE THE PROGRAM RUNS
                var humanDrawable1 by rememberSaveable{ mutableStateOf(R.drawable.dice1) }
                var humanDrawable2 by rememberSaveable{ mutableStateOf(R.drawable.dice2) }
                var humanDrawable3 by rememberSaveable{ mutableStateOf(R.drawable.dice3) }
                var humanDrawable4 by rememberSaveable{ mutableStateOf(R.drawable.dice4) }
                var humanDrawable5 by rememberSaveable{ mutableStateOf(R.drawable.dice5) }

                var computerDrawable1 by rememberSaveable{ mutableStateOf(R.drawable.dice1) }
                var computerDrawable2 by rememberSaveable{ mutableStateOf(R.drawable.dice2) }
                var computerDrawable3 by rememberSaveable{ mutableStateOf(R.drawable.dice3) }
                var computerDrawable4 by rememberSaveable{ mutableStateOf(R.drawable.dice4) }
                var computerDrawable5 by rememberSaveable{ mutableStateOf(R.drawable.dice5) }

                var newHumanDrawables by rememberSaveable{ mutableStateOf(listOf(0,0,0,0,0,0)) }
                var newComputerDrawables by rememberSaveable { mutableStateOf(listOf(0,0,0,0,0,0)) }

                //A VARIABLE TO HANDLE THE GENERATION OF DICE IMAGES
                var drawableVisibility by rememberSaveable{ mutableStateOf(false) }

                //2 VARIABLES TO STORE THE SCORES
                var humanScore by rememberSaveable { mutableStateOf(0) }
                var computerScore by rememberSaveable{ mutableStateOf(0) }

                var humanThrows by rememberSaveable{ mutableStateOf(0)}     //VARIABLE TO STORE THE NUMBER OF CLICKS
                var scoreClick by rememberSaveable{ mutableStateOf(1) }     //VARIABLE TO CONTROL THE SCORE BUTTON

                //VARIABLES TO STORE THE RE-ROLLS AND THE SCORING OF COMPUTER
                var computerThrowDecision by rememberSaveable { mutableStateOf(1) }
                var computerThrows by rememberSaveable {mutableStateOf(0)}

                //VARIABLE TO CONTROL THE POP UP (ALERTDIALOG)
                var gameOverDialog by rememberSaveable{ mutableStateOf(false) }
                var gameTieDialog by rememberSaveable{ mutableStateOf(false) }

                val humanDiceLockedStatus by rememberSaveable{ mutableStateOf(mutableListOf(false,false,false,false,false))}
                var computerDiceLockedStatus by rememberSaveable{ mutableStateOf(mutableListOf(false,false,false,false,false)) }
                var numbers by rememberSaveable { mutableStateOf(mutableListOf(0,0,0,0,0)) }

                if(gameTieDialog) {
                    AlertDialog(
                        title = { Text("Game Tie".uppercase()) },
                        onDismissRequest = {},
                        confirmButton = {
                            TextButton(onClick = {
                                var i = Intent(context, SuperScroll::class.java)
                                context.startActivity(i)
                                gameTieDialog = false
                            }) {
                                Text("OK", fontSize = 20.sp)
                            }
                        }
                    )
                }
                else if((humanScore >=target || computerScore>=target) && (humanScore!=computerScore)){
                    if(gameOverDialog){
                        AlertDialog(
                            title= {
                                if (humanScore > computerScore) {
                                    Text("You win".uppercase(), color = Color.Green)
                                }
                                else if(computerScore > humanScore) {
                                    Text("You Lose".uppercase(), color = Color.Red)
                                }
                            },
                            onDismissRequest = {},
                            confirmButton = { TextButton(onClick={
                                if(humanScore>computerScore){
                                    ++humanWins
                                }
                                else if(humanScore<computerScore){
                                    ++computerWins
                                }
                                gameOverDialog=false
                                humanScore=0
                                computerScore=0
                                drawableVisibility=false
                            }){
                                Text("New Game", fontSize = 20.sp)
                            }},
                            dismissButton = { TextButton(
                                onClick = {
                                    var i = Intent(context,MainActivity::class.java)
                                    context.startActivity(i)
                                    gameOverDialog=false
                                }
                            ){
                                Text("Main Menu", fontSize = 20.sp)
                            } }
                        )
                    }
                }

                //COLUMN TO DISPLAY THE SCORES OF HUMAN AND COMPUTER BEGINS
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column(){
                        Text("Target : ${target}", fontSize = 20.sp,color=Color.White)
                        Text("H : ${humanWins}/ C : ${computerWins}", fontSize = 20.sp,color=Color.White)
                    }

                    Column(){
                        Text("Human Score : ${humanScore}", fontSize = 20.sp,color=Color.White)
                        Text("Computer Score : ${computerScore}",fontSize=20.sp,color=Color.White)
                    }
                }
                //COLUMN TO DISPLAY THE SCORES OF HUMAN AND COMPUTER ENDS

                Column(){
                    Text("HUMAN PLAYER :", fontSize = 26.sp, color = Color.White)
                    Row(){
                        if(drawableVisibility){
                            Column(){
                                Image(
                                    painterResource(id=humanDrawable1),
                                    contentDescription = "Dice Roll 1",
                                    modifier = Modifier.clickable(onClick={humanDiceLockedStatus[0]=true})
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(""+(diceDrawables.indexOf(humanDrawable1)+1),fontSize = 26.sp, color = Color.White)
                            }
                            Spacer(Modifier.width(20.dp))
                            Column(){
                                Image(
                                    painterResource(id=humanDrawable2),
                                    contentDescription = "Dice Roll 2",
                                    modifier = Modifier.clickable(onClick={humanDiceLockedStatus[1]=true})
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(""+ (diceDrawables.indexOf(humanDrawable2)+1), fontSize = 26.sp,color=Color.White)
                            }
                            Spacer(Modifier.width(20.dp))
                            Column(
                                Modifier.clickable(onClick={})
                            ){
                                Image(
                                    painterResource(id=humanDrawable3),
                                    contentDescription = "Dice Roll 3",
                                    modifier = Modifier.clickable(onClick={humanDiceLockedStatus[2]=true})
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(""+(diceDrawables.indexOf(humanDrawable3)+1), fontSize = 26.sp,color=Color.White)
                            }
                            Spacer(Modifier.width(20.dp))
                            Column(){
                                Image(
                                    painterResource(id=humanDrawable4),
                                    contentDescription = "Dice Roll 4",
                                    modifier = Modifier.clickable(onClick={humanDiceLockedStatus[3]=true})
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(""+ (diceDrawables.indexOf(humanDrawable4)+1), fontSize = 26.sp,color=Color.White)
                            }
                            Spacer(Modifier.width(20.dp))
                            Column(){
                                Image(
                                    painterResource(id=humanDrawable5),
                                    contentDescription = "Dice Roll 5",
                                    modifier = Modifier.clickable(onClick={humanDiceLockedStatus[4]=true})
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(""+(diceDrawables.indexOf(humanDrawable5)+1), fontSize = 26.sp,color=Color.White)
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Row {
                        Text("COMPUTER : ", fontSize = 26.sp, color = Color.White)
                    }
                    Row(){
                        if(drawableVisibility){
                            if(humanThrows==3 || scoreClick==1){
                                Column(){
                                    Image(
                                        painterResource(id=computerDrawable1),
                                        contentDescription = "Dice Roll 1",
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    Text(""+(diceDrawables.indexOf(computerDrawable1)+1),fontSize = 26.sp, color = Color.White)
                                }
                                Spacer(Modifier.width(20.dp))
                                Column(){
                                    Image(
                                        painterResource(id=computerDrawable2),
                                        contentDescription = "Dice Roll 2"
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    Text(""+ (diceDrawables.indexOf(computerDrawable2)+1), fontSize = 26.sp,color=Color.White)
                                }
                                Spacer(Modifier.width(20.dp))
                                Column(){
                                    Image(
                                        painterResource(id=computerDrawable3),
                                        contentDescription = "Dice Roll 3"
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    Text(""+(diceDrawables.indexOf(computerDrawable3)+1), fontSize = 26.sp,color=Color.White)
                                }
                                Spacer(Modifier.width(20.dp))
                                Column(){
                                    Image(
                                        painterResource(id=computerDrawable4),
                                        contentDescription = "Dice Roll 4"
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    Text(""+ (diceDrawables.indexOf(computerDrawable4)+1), fontSize = 26.sp,color=Color.White)
                                }
                                Spacer(Modifier.width(20.dp))
                                Column(){
                                    Image(
                                        painterResource(id=computerDrawable5),
                                        contentDescription = "Dice Roll 5"
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    Text(""+(diceDrawables.indexOf(computerDrawable5)+1), fontSize = 26.sp,color=Color.White)
                                }
                            }
                        }
                    }
                }
                Row(
                    Modifier.paddingFromBaseline(0.dp,100.dp)
                ){
                    Button(
                        onClick = {
                            newHumanDrawables = generateDrawables()
                            newComputerDrawables = generateDrawables()

                            if(humanScore < target && computerScore<target) {
                                ++computerThrows       //UPDATING THE NO OF THROWS BY THE COMPUTER
                                ++humanThrows
                                scoreClick = 0

                                //IF 'LockedStatus' IS TRUE
                                if (humanThrows <= 3 && scoreClick != 1) {
                                    //ASSIGNING THE ELEMENTS IN THE LIST TO INDEPENDENT VARIABLES
                                    for (i in 0..humanDiceLockedStatus.size - 1) {
                                        if (!humanDiceLockedStatus[i]) {
                                            when (i) {
                                                0 -> humanDrawable1 = newHumanDrawables[i]
                                                1 -> humanDrawable2 = newHumanDrawables[i]
                                                2 -> humanDrawable3 = newHumanDrawables[i]
                                                3 -> humanDrawable4 = newHumanDrawables[i]
                                                4 -> humanDrawable5 = newHumanDrawables[i]
                                            }
                                        }
                                    }
                                    drawableVisibility = true
                                }

                                //IF 'COMPUTER-THROW-DECISION' VARIABLE IS 0, IT DOESN'T RE-ROLL
                                //IF 'COMPUTER-THROW-DECISION' VARIABLE IS 1, IT RE-ROLLS
                                //FUNCTIONALITY - A LIST 'computerDiceLockedStatus' IS CREATED WITH 5 BOOLEAN ELEMENTS (INITIALLY 'false'). THE VARIABLE 'number' STORES A RANDOM NUMBER WHICH IS 0 OR 1 FOR 5 TIMES (FOR LOOP), IF THE RANDOM NUMBER GENERATED IS 1, THE COMPUTER KEEPS THE DICE ROLL BY CHANGING THE ELEMENT ON THE 'computerDiceLockedStatus' LIST TO 'true'. AND, IF THE NUMBER GENERATED IS 0, IT FOLLOWS THE NORMAL PROCEDURE OF RE-ROLLING
                                if (computerThrowDecision == 1 && computerThrows < 3) {
                                    for (i in 0..numbers.size - 1) {
                                        var number = Random.nextInt(0, 2)

                                        if (numbers[i] == 0) {
                                            numbers[i] = number
                                        } else {
                                            computerDiceLockedStatus[i] = true
                                        }
                                    }

                                    //IF 'LockedStatus' IS TRUE THE DRAWABLE IS ASSUMED CLICKED AND KEEPS THE SAME VALUE
                                    for (i in 0..computerDiceLockedStatus.size - 1) {
                                        if (!computerDiceLockedStatus[i]) {
                                            when (i) {
                                                0 -> computerDrawable1 = newComputerDrawables[i]
                                                1 -> computerDrawable2 = newComputerDrawables[i]
                                                2 -> computerDrawable3 = newComputerDrawables[i]
                                                3 -> computerDrawable4 = newComputerDrawables[i]
                                                4 -> computerDrawable5 = newComputerDrawables[i]
                                            }
                                        }
                                    }
                                }

                                computerThrowDecision = Random.nextInt(
                                    0,
                                    2
                                )        //GENERATING RANDOM NUMBERS TO DECIDE THE RE-ROLL

                                if (humanThrows == 3 || scoreClick == 1) {
                                    ++scoreClick

                                    //Explicitly casting the human and computer throws to be mutable
                                    for (i in 0..newHumanDrawables.size - 1) {
                                        (newHumanDrawables as MutableList<Int>)[i] = when (i) {
                                            0 -> humanDrawable1
                                            1 -> humanDrawable2
                                            2 -> humanDrawable3
                                            3 -> humanDrawable4
                                            else -> humanDrawable5
                                        }
                                        (newComputerDrawables as MutableList<Int>)[i] = when (i) {
                                            0 -> computerDrawable1
                                            1 -> computerDrawable2
                                            2 -> computerDrawable3
                                            3 -> computerDrawable4
                                            else -> computerDrawable5
                                        }
                                    }

                                    for (i in 0..newHumanDrawables.size - 1) {
                                        if (humanScore < target && computerScore < target) {
                                            humanScore += diceDrawables.indexOf(newHumanDrawables[i]) + 1
                                            computerScore += diceDrawables.indexOf(
                                                newComputerDrawables[i]
                                            ) + 1
                                        }
                                    }

                                    for (i in 0..humanDiceLockedStatus.size - 1) {
                                        humanDiceLockedStatus[i] = false
                                        computerDiceLockedStatus[i] = false
                                        numbers[i] = 0
                                    }
                                    computerThrowDecision =
                                        1           //UPDATING THE THROW DECISION IF THE 3 ROLLS ARE DONE
                                    humanThrows = 0
                                    computerThrows = 0
                                }
                            }

                            //POPPING UP THE ALERT DIALOG ONCE THE GAME FINISHES
                            if((humanScore>=target) || (computerScore>=target)){
                                gameOverDialog=true
                            }
                            //OPENS ANOTHER DIALOG BOX IF THE SCORES ARE LEVEL
                            if((humanScore==computerScore) && (computerScore>target || humanScore>target)){
                                gameTieDialog=true
                            }

                        },
                        Modifier.border(2.dp,Color.Cyan, RoundedCornerShape(30)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor=Color.Cyan)
                    ){
                        Text("Throw", fontSize = 26.sp)
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick={
                            humanThrows=0
                            ++scoreClick
                            computerThrows=0
                            computerThrowDecision=1

                            if(scoreClick==1){
                                if(humanScore<target && computerScore<target){
                                    //UPDATING THE HUMAN SCORE AND THE COMPUTER SCORE ONCE THE BUTTON IS CLICKED
                                    for(i in 0..newHumanDrawables.size-1){
                                        if((humanScore<target) && (computerScore<target)){
                                            (newHumanDrawables as MutableList<Int>)[0] = humanDrawable1
                                            (newHumanDrawables as MutableList<Int>)[1] = humanDrawable2
                                            (newHumanDrawables as MutableList<Int>)[2] = humanDrawable3
                                            (newHumanDrawables as MutableList<Int>)[3] = humanDrawable4
                                            (newHumanDrawables as MutableList<Int>)[4] = humanDrawable5

                                            (newComputerDrawables as MutableList<Int>)[0] = computerDrawable1
                                            (newComputerDrawables as MutableList<Int>)[1] = computerDrawable2
                                            (newComputerDrawables as MutableList<Int>)[2] = computerDrawable3
                                            (newComputerDrawables as MutableList<Int>)[3] = computerDrawable4
                                            (newComputerDrawables as MutableList<Int>)[4] = computerDrawable5

                                            humanScore+= diceDrawables.indexOf(newHumanDrawables[i])+1
                                            computerScore+= diceDrawables.indexOf(newComputerDrawables[i])+1
                                        }
                                    }
                                    for(i in 0..humanDiceLockedStatus.size-1){
                                        humanDiceLockedStatus[i]=false
                                        computerDiceLockedStatus[i]=false
                                        numbers[i]=0
                                    }
                                }

                                //POPPING UP THE ALERT DIALOG ONCE THE GAME FINISHES
                                if((humanScore>=target) || (computerScore>=target)){
                                    gameOverDialog=true
                                }
                                //OPENS ANOTHER DIALOG BOX IF THE SCORES ARE LEVEL
                                if((humanScore==computerScore) && (computerScore>=target || humanScore>=target)){
                                    gameTieDialog=true
                                }
                            }
                        },
                        //DESIGNING THE BUTTON
                        Modifier.border(2.dp,Color.Cyan, RoundedCornerShape(30)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Cyan)
                    ){
                        Text("Score",fontSize=26.sp)
                    }
                }
            }
        }
    }

    //GENERATING RANDOM NUMBERS TO GENERATE A RANDOM DRAWABLE
    fun generateDrawables():List<Int>{
        var newDrawables = mutableListOf<Int>()
        for(i in 1..5){                           //A FOR LOOP TO GENERATE RANDOM 5 NUMBERS AND TO ADD THE DRAWABLES TO A NEW LIST
            var number = Random.nextInt(0,6)
            newDrawables.add(diceDrawables[number])
        }
        return newDrawables
    }
}