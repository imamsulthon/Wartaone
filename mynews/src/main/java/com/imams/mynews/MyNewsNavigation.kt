package com.imams.mynews

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.imams.mynews.ui.screen.HomeNewsScreen
import com.imams.mynews.ui.screen.NewsDetailScreen
import com.imams.mynews.ui.screen.SeeAllNewsScreen

@Composable
fun MyNewsNavigation(
    navController: NavHostController = rememberNavController(),
) {

    NavHost(navController = navController, startDestination = "home_screen") {

        composable("home_screen") {
            HomeNewsScreen(
                seeAll = {
                    navController.navigate("see_all_list")
                },
                onClickItem = {
                    navController.navigate("news_detail/$it")
                }
            )
        }
        composable("see_all_list") {
            SeeAllNewsScreen(onClick = {
                navController.navigate("news_detail/$it")
            })
        }
        composable(
            route = "news_detail/{uuid}",
            arguments = listOf(navArgument("uuid") {defaultValue = ""})
        ) {
            val id = it.arguments?.getString("uuid") ?: ""
            NewsDetailScreen(uuid = id)
        }
        
    }

}