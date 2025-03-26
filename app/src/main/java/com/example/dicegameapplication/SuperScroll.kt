package com.example.dicegameapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class SuperScroll : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperScrolling()
        }
    }
    @Composable
    fun SuperScrolling(){
        Column(
            Modifier.fillMaxSize().background(Color.DarkGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            var context = LocalContext.current

            //HUMAN DRAWABLES TO BE DISPLAYED
            var humanDrawable1 by rememberSaveable{ mutableStateOf(R.drawable.dice1) }
            var humanDrawable2 by rememberSaveable{ mutableStateOf(R.drawable.dice2) }
            var humanDrawable3 by rememberSaveable{ mutableStateOf(R.drawable.dice3) }
            var humanDrawable4 by rememberSaveable{ mutableStateOf(R.drawable.dice4) }
            var humanDrawable5 by rememberSaveable{ mutableStateOf(R.drawable.dice5) }

            //COMPUTER DRAWABLES TO BE DISPLAYED
            var computerDrawable1 by rememberSaveable{ mutableStateOf(R.drawable.dice1) }
            var computerDrawable2 by rememberSaveable{ mutableStateOf(R.drawable.dice2) }
            var computerDrawable3 by rememberSaveable{ mutableStateOf(R.drawable.dice3) }
            var computerDrawable4 by rememberSaveable{ mutableStateOf(R.drawable.dice4) }
            var computerDrawable5 by rememberSaveable{ mutableStateOf(R.drawable.dice5) }

            //VARIABLES TO STORE THE SCORES OF THE TWO PLAYERS
            var computerScore by rememberSaveable{ mutableStateOf(0) }
            var humanScore by rememberSaveable { mutableStateOf(0) }

            //VARIABLES TO CONTROL THE NUMBER OF THROWS AND THE ALERT DIALOG BOX
            var noOfThrows by rememberSaveable{ mutableStateOf(0) }
            var popUpVisible by rememberSaveable{ mutableStateOf(false) }

            //VARIABLE TO CONTROL THE DISPLAYING OF DRAWABLES
            var drawablesVisibilty by rememberSaveable{ mutableStateOf(false) }

            if(popUpVisible){
                AlertDialog(
                    title={
                        if(computerScore>humanScore){
                        Text("You Lost The Super-Scroll",color=Color.Red, fontSize = 20.sp)
                        }
                        else{
                            Text("You Won The Super-Scroll",color=Color.Green, fontSize = 20.sp)
                        }},
                    onDismissRequest = {popUpVisible=false},
                    confirmButton = { TextButton(onClick={
                        var i = Intent(context,NewGame::class.java)
                        context.startActivity(i)
                        popUpVisible=false
                    }){
                        Text("Back to Game Screen", fontSize = 20.sp)
                    }},
                    dismissButton = { TextButton(onClick = {
                        var i = Intent(context,MainActivity::class.java)
                        context.startActivity(i)
                        popUpVisible=false
                    }){
                        Text("Main Menu", fontSize = 20.sp)
                    }}
                )
            }

            Row(
                Modifier.height(80.dp).background(color= Color.Gray).fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ){
                Text("Super Scroll".uppercase(), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Text("Human Score : ${humanScore}\nComputer Score : ${computerScore}",color=Color.White, fontSize = 20.sp)
                }
                Spacer(Modifier.height(50.dp))
                Column(){
                    Text("HUMAN PLAYER :", fontSize = 26.sp, color = Color.White)
                    //DRAWABLES OF THE HUMAN PLAYER
                    Row(){
                        if(drawablesVisibilty){
                            Column() {
                                Image(
                                    painterResource(id = humanDrawable1),
                                    contentDescription = "Dice Roll 1",
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(humanDrawable1) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = humanDrawable2),
                                    contentDescription = "Dice Roll 2"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(humanDrawable2) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = humanDrawable3),
                                    contentDescription = "Dice Roll 3"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(humanDrawable3) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = humanDrawable4),
                                    contentDescription = "Dice Roll 4"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(humanDrawable4) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = humanDrawable5),
                                    contentDescription = "Dice Roll 5"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(humanDrawable5) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    Row {
                        Text("COMPUTER : ", fontSize = 26.sp, color = Color.White)
                    }
                    Row(){
                        if(drawablesVisibilty) {
                            Column() {
                                Image(
                                    painterResource(id = computerDrawable1),
                                    contentDescription = "Dice Roll 1",
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(computerDrawable1) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = computerDrawable2),
                                    contentDescription = "Dice Roll 2"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(computerDrawable2) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = computerDrawable3),
                                    contentDescription = "Dice Roll 3"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(computerDrawable3) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = computerDrawable4),
                                    contentDescription = "Dice Roll 4"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(computerDrawable4) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Column() {
                                Image(
                                    painterResource(id = computerDrawable5),
                                    contentDescription = "Dice Roll 5"
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "" + (diceDrawables.indexOf(computerDrawable5) + 1),
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                }
                Row(
                    Modifier.paddingFromBaseline(0.dp,100.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Button(
                        onClick = {
                            if(noOfThrows<1){
                                drawablesVisibilty=true
                                humanScore=0
                                computerScore=0
                                ++noOfThrows
                                var humanRolls = generateDrawables()
                                var computerRolls = generateDrawables()

                                humanDrawable1 = humanRolls[0]
                                humanDrawable2 = humanRolls[1]
                                humanDrawable3 = humanRolls[2]
                                humanDrawable4 = humanRolls[3]
                                humanDrawable5 = humanRolls[4]

                                computerDrawable1 = computerRolls[0]
                                computerDrawable2 = computerRolls[1]
                                computerDrawable3 = computerRolls[2]
                                computerDrawable4 = computerRolls[3]
                                computerDrawable5 = computerRolls[4]

                                for(i in 0..humanRolls.size-1) {
                                    humanScore += diceDrawables.indexOf(humanRolls[i]) + 1
                                    computerScore += diceDrawables.indexOf(computerRolls[i]) + 1
                                }

                                //IF THE SUPER-SCROLL IS A TIE AGAIN
                                if(humanScore == computerScore){
                                    noOfThrows=0
                                }
                                //ALERT BOX APPEARS IF THE SCORES ARE NOT LEVEL
                                else{
                                    if(humanScore>computerScore){
                                        ++humanWins
                                    }
                                    else if(humanScore<computerScore){
                                        ++computerWins
                                    }
                                    popUpVisible=true
                                }
                            }
                        },
                        Modifier.border(2.dp,Color.Cyan, RoundedCornerShape(30)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor=Color.Cyan)
                    ){
                        Text("Throw", fontSize = 26.sp)
                    }
                }
            }
        }
    }
    fun generateDrawables():List<Int>{

        var list = mutableListOf<Int>()

        for(i in 1..5){
            var number = Random.nextInt(0,6)
            list.add(diceDrawables[number])
        }
        return list
    }
}