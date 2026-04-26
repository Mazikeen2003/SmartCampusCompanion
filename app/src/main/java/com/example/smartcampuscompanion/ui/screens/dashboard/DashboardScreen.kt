package com.example.smartcampuscompanion.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAddAnnouncement: () -> Unit = {},
    viewModel: DepartmentViewModel,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    userRole: String = "student"
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf("main") }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }
    
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

                    DrawerItem(
                        if (userRole == "admin") "Broadcasts" else "Announcements", 
                        Icons.AutoMirrored.Filled.Announcement
                    ) {
                        scope.launch {
                            drawerState.close()
                            onNavigateToAnnouncements()
                        }
                    }

                    DrawerItem(
                        if (userRole == "admin") "Task Audit" else "Tasks", 
                        Icons.Default.Task
                    ) {
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

                    DrawerItem("Settings", Icons.Default.Settings) {
                        scope.launch {
                            drawerState.close()
                            onNavigateToSettings()
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    DrawerItem("Sign Out", Icons.AutoMirrored.Filled.ExitToApp, MaterialTheme.colorScheme.error) { onLogout() }
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
                            "SMART CAMPUS COMPANION",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = onThemeToggle) {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            floatingActionButton = {
                if (userRole == "admin") {
                    FloatingActionButton(
                        onClick = onNavigateToAddAnnouncement,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Announcement")
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (currentScreen) {
                    "main" -> MainDashboardContent(
                        departments = departments, 
                        onNavigateToTasks = onNavigateToTasks, 
                        onNavigateToAnnouncements = onNavigateToAnnouncements,
                        onDepartmentClick = { selectedDepartment = it },
                        userRole = userRole
                    )
                    "campus_info" -> CampusInfoScreen(onBackClick = { currentScreen = "main" })
                }

                // Department Detail Dialog
                selectedDepartment?.let { dept ->
                    AlertDialog(
                        onDismissRequest = { selectedDepartment = null },
                        confirmButton = {
                            TextButton(onClick = { selectedDepartment = null }) {
                                Text("CLOSE")
                            }
                        },
                        title = { Text(dept.name, fontWeight = FontWeight.Bold, color = Color(dept.bgColor)) },
                        text = {
                            Column {
                                Text(
                                    text = dept.description.ifEmpty { "Welcome to the ${dept.name}." },
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Campus Central Hub", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.extraLarge
                    )
                }
            }
        }
    }
}

@Composable
fun MainDashboardContent(
    departments: List<Department>,
    onNavigateToTasks: () -> Unit,
    onNavigateToAnnouncements: () -> Unit,
    onDepartmentClick: (Department) -> Unit,
    userRole: String
) {
    val isAdmin = userRole == "admin"
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isAdmin) 260.dp else 220.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = if (isAdmin) {
                                listOf(
                                    MaterialTheme.colorScheme.errorContainer,
                                    MaterialTheme.colorScheme.surface
                                )
                            } else {
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.surface
                                )
                            }
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Surface(
                        color = if (isAdmin) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.School,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                if (isAdmin) "ADMINSTRATOR CONSOLE" else "STUDENT PORTAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = if (isAdmin) "System Manager," else "Welcome,",
                        style = MaterialTheme.typography.titleLarge,
                        color = (if (isAdmin) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary).copy(alpha = 0.8f)
                    )
                    Text(
                        text = if (isAdmin) "Campus Control" else "Campus Companion",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (isAdmin) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "You have full access to broadcast announcements and manage campus data.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Admin specific Quick Actions Row
            if (isAdmin) {
                Text(
                    text = "Management Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(), 
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    title = if (isAdmin) "Broadcasts" else "Announcements",
                    subtitle = if (isAdmin) "Manage Updates" else "Latest News",
                    icon = if (isAdmin) Icons.Default.Campaign else Icons.Default.Campaign,
                    containerColor = if (isAdmin) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.secondaryContainer,
                    onClick = onNavigateToAnnouncements,
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    title = if (isAdmin) "Task Audit" else "Tasks",
                    subtitle = if (isAdmin) "Review System" else "Be Productive",
                    icon = if (isAdmin) Icons.Default.SettingsSuggest else Icons.AutoMirrored.Filled.PlaylistAddCheck,
                    containerColor = if (isAdmin) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = onNavigateToTasks,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isAdmin) "Campus Infrastructure" else "Colleges & Departments",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isAdmin) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (departments.isEmpty()) {
             item {
                 Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                     Column(horizontalAlignment = Alignment.CenterHorizontally) {
                         Icon(
                             Icons.Default.DomainDisabled, 
                             contentDescription = null, 
                             modifier = Modifier.size(48.dp),
                             tint = MaterialTheme.colorScheme.outlineVariant
                         )
                         Spacer(Modifier.height(16.dp))
                         Text(
                             "No departments available", 
                             style = MaterialTheme.typography.bodyMedium,
                             color = MaterialTheme.colorScheme.onSurfaceVariant
                         )
                     }
                 }
             }
        } else {
            items(departments) { dept ->
                DepartmentButton(
                    name = dept.name,
                    backgroundColor = Color(dept.bgColor)
                ) { 
                    onDepartmentClick(dept)
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(140.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = CircleShape,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp))
                }
            }
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height(80.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, backgroundColor.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(backgroundColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Academic Unit",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
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
