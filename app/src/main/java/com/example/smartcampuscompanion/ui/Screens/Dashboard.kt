package com.example.smartcampuscompanion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Dashboard Menu",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(16.dp) // increased rounding
                ) {
                    Text("Campus Information", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB)),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp) // increased rounding
                ) {
                    Text("Logout", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF1976D2))
                )
            },
            contentWindowInsets = WindowInsets(0,0,0,0)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp, vertical = 28.dp), // increased padding
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Welcome to the Dashboard",
                    fontSize = 30.sp, // slightly larger
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 36.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // taller card
                    shape = RoundedCornerShape(18.dp), // slightly rounder
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD6EAF8)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp) // slightly higher
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Main Dashboard Content Area",
                            fontSize = 22.sp, // slightly bigger
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
