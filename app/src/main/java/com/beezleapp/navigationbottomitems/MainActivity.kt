package com.beezleapp.navigationbottomitems

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.beezleapp.navigationbottomitems.ui.theme.NavigationBottomItemsTheme
import java.util.*

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
            NavigationBar {
                items.forEachIndexed { index, item ->
                    selectedTab = item

                    val isSelected = index == items.indexOf(selectedTab)

                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (isSelected) item.second else item.third,
                                contentDescription = null
                            )
                        },
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
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Text",
                            )
                        },
                    )
                }) {
                    AndroidView(modifier = Modifier.padding(it).fillMaxSize(), factory = { context ->
                        val constraintLayout = ConstraintLayout(context)

                        constraintLayout.setBackgroundColor(context.getColor(android.R.color.holo_red_dark))
                        val editText = EditText(context)
                        editText.setText("Click here")

                        editText.id = View.generateViewId()

                        constraintLayout.addView(editText)

                        val constraintSet = ConstraintSet()

                        constraintSet.clone(constraintLayout)

                        constraintSet.connect(editText.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                        constraintSet.connect(editText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                        constraintSet.connect(editText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

                        constraintSet.applyTo(constraintLayout)

                        constraintLayout
                    })
                }
            }
            composable(items[1].first) {
                Column {
                    Text("Second")
                    Button(onClick = {
                        navHostController.navigate(
                            "nested/" + UUID.randomUUID().toString()
                        )
                    }) {
                        Text(text = "Go to nested")
                    }
                }
            }
            composable("nested/{id}") {
                Text("nested")
            }
        }
    }
}

