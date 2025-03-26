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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GUI();
        }
    }

    @Composable
    fun GUI(){
        Column(
            Modifier.fillMaxSize().background(color = Color.DarkGray),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            var openDialog by rememberSaveable{ mutableStateOf(false)}
            var checked by rememberSaveable{ mutableStateOf(false) }
            //POP UP WINDOW OF THE GAME ABOUT
            if(openDialog){
                AlertDialog(
                    title={
                        Text("Name : Pathirahannahelage Fernando\nStudent ID : W2052712",fontSize = 20.sp)
                    },
                    text = {
                        Text("I confirm that understand what plagiarism is and have read and understood the section on Assessment Offenses in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged", lineHeight = 30.sp, fontSize = 20.sp,textAlign = TextAlign.Justify)
                    },
                    onDismissRequest = {},
                    confirmButton ={
                        TextButton(onClick = {
                            if(checked){
                                openDialog=false
                            }
                        }){
                            Text("I Confirm")
                            Checkbox(checked = checked, onCheckedChange = {checked=it})
                        }
                    }
                )
            }

            Row(
                Modifier.height(80.dp).background(color=Color.Gray).fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ){
                Text("GAME 101".toUpperCase(), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painterResource(id=R.drawable.twodice),
                    contentDescription = "Image of a dice",
                    modifier = Modifier.height(200.dp).width(200.dp)
                )
                Spacer(Modifier.width(20.dp))
                Row(){
                    Text("Welcome to game 101!".toUpperCase(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Cyan)
                }
                Spacer(Modifier.height(40.dp))
                Row(){
                    var context = LocalContext.current;
//              About Button
                    Button(
                        onClick={
                            //POPPING UP THE ALERT BOX
                            openDialog=true
                        },

                        //Setting a border and a border-radius to the button
                        Modifier.border(2.dp,Color.Cyan, RoundedCornerShape(30)),
                        //Setting a color to the text of the button, changing the default color of the button
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Cyan)
                    ){
                        Text("About Game",fontSize=26.sp)
                    }
                    Spacer(Modifier.width(20.dp))

//              New Game Button
                    Button(
                        onClick={
                            //Explicit Intent to the 'New Game' Activity
                            var i = Intent(context,NewGame::class.java)
                            context.startActivity(i)
                            humanWins=0
                            computerWins=0
                        },
                        //Setting a border and a border-radius to the button
                        Modifier.border(2.dp,Color.Cyan, RoundedCornerShape(30)),
                        //Setting a color to the text of the button, changing the default color of the button
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Cyan)
                    ){
                        Text("New Game",fontSize=26.sp)
                    }
                }
            }
        }
    }
}