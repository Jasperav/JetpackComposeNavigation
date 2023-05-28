package com.beezleapp.navigationbottomitems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.beezleapp.navigationbottomitems.ui.theme.NavigationBottomItemsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationBottomItemsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen() {
    val items = listOf(
        Triple("a", Icons.Default.Person, Icons.Filled.Person),
        Triple("b", Icons.Default.Notifications, Icons.Filled.Notifications),
    )
    var selectedTab = items[0]
    val navHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                items.forEachIndexed { index, item ->
                    val isSelected = index == items.indexOf(selectedTab)

                    BottomNavigationItem(
                        icon = { Icon(if (isSelected) item.second else item.third, contentDescription = null) },
                        label = { Text(text = item.first) },
                        selected = isSelected,
                        onClick = {
                            navHostController.navigate(item.first) {
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navHostController,
            startDestination = items[0].first,
            Modifier.padding(it)
        ) {
            composable(items[0].first) {
                selectedTab = items[0]

                remember {
                    println("Recomposed first")

                    ""
                }

                Text("first")
            }
            composable(items[1].first) {
                selectedTab = items[1]

                remember {
                    println("Recomposed second")

                    ""
                }

                Text("Second")
            }
        }
    }
}