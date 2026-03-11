package com.example.smartcampuscompanion.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.ui.screens.campus.CampusInfoScreen
import com.example.smartcampuscompanion.ui.viewmodel.DepartmentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onNavigateToTasks: () -> Unit = {},
    onNavigateToAnnouncements: () -> Unit = {},
    viewModel: DepartmentViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf("main") }
    
    val departments by viewModel.departments.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(310.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerTonalElevation = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxHeight().padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(64.dp))
                    Text(
                        text = "Smart Campus",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                    DrawerItem("Announcements", Icons.Default.Announcement) {
                        scope.launch {
                            drawerState.close()
                            onNavigateToAnnouncements()
                        }
                    }

                    DrawerItem("Tasks", Icons.Default.Task) {
                        scope.launch {
                            drawerState.close()
                            onNavigateToTasks()
                        }
                    }

                    DrawerItem("Campus Info", Icons.Default.Info) {
                        scope.launch {
                            drawerState.close()
                            currentScreen = "campus_info"
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    DrawerItem("Sign Out", Icons.Default.ExitToApp, MaterialTheme.colorScheme.error) { onLogout() }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "DASHBOARD",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (currentScreen) {
                    "main" -> MainDashboardContent(departments, onNavigateToTasks, onNavigateToAnnouncements)
                    "campus_info" -> CampusInfoScreen(onBackClick = { currentScreen = "main" })
                }
            }
        }
    }
}

@Composable
fun MainDashboardContent(
    departments: List<Department>,
    onNavigateToTasks: () -> Unit,
    onNavigateToAnnouncements: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(24.dp)
    ) {
        item {
            Text(
                text = "Welcome,",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            Text(
                text = "Smart Campus",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(
                    onClick = onNavigateToAnnouncements,
                    modifier = Modifier.weight(1f).height(120.dp),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer).padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("Announcements", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                Card(
                    onClick = onNavigateToTasks,
                    modifier = Modifier.weight(1f).height(120.dp),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("Tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Departments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(departments) { dept ->
            DepartmentButton(
                name = dept.name,
                backgroundColor = Color(dept.bgColor)
            ) { 
                // Navigation for specific departments can be added here if needed
            }
        }
    }
}

@Composable
fun DepartmentButton(
    name: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(85.dp),
        shape = MaterialTheme.shapes.large,
        contentPadding = PaddingValues(horizontal = 24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color(0xFF1A1C1E)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.height(56.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = color,
            unselectedTextColor = color
        )
    )
}
