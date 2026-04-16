package com.example.smartcampuscompanion.navigation

object Routes {
    const val LOGIN_REGISTER = "login_register"
    const val DASHBOARD = "dashboard"
    const val TASK_LIST = "task_list"
    const val ADD_TASK = "add_task"
    const val EDIT_TASK = "edit_task/{taskId}"
    const val CAMPUS_INFO = "campus_info"
    const val ANNOUNCEMENT_LIST = "announcement_list"
    const val ANNOUNCEMENT_DETAIL = "announcement_detail/{announcementId}"
    const val SETTINGS = "settings"
    const val ADD_ANNOUNCEMENT = "add_announcement"

    // Helper function to create the route path for editing a specific task
    fun editTask(taskId: Int) = "edit_task/$taskId"

    // Helper function for announcement detail route
    fun announcementDetail(announcementId: Int) = "announcement_detail/$announcementId"
}
