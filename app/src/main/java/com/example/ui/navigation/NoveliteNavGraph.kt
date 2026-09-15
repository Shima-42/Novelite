package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.NoveliteViewModel
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.StoryDetailScreen
import com.example.ui.screens.StreakDashboardScreen
import com.example.ui.screens.WriterDraftEditorScreen
import com.example.ui.screens.WritingStudioScreen

object NoveliteDestinations {
  const val AUTH = "auth"
  const val ONBOARDING = "onboarding"
  const val HOME = "home"
  const val EXPLORE = "explore"
  const val LIBRARY = "library"
  const val WRITE = "write"
  const val PROFILE = "profile"
  const val STORY_DETAIL = "story_detail/{storyId}"
  const val READER = "reader/{storyId}/{chapterId}"
  const val WRITER_EDITOR = "writer_editor/{storyId}/{chapterId}"
  const val STREAK_DASHBOARD = "streak_dashboard"
  const val NOTIFICATIONS = "notifications"
  const val ADMIN = "admin"

  fun storyDetailRoute(storyId: String) = "story_detail/$storyId"
  fun readerRoute(storyId: String, chapterId: String = "default") = "reader/$storyId/$chapterId"
  fun writerEditorRoute(storyId: String, chapterId: String = "default") = "writer_editor/$storyId/$chapterId"
}

/**
 * Navigation Graph using androidx.navigation.compose to manage transitions between
 * a library view, reading mode, and writer editor.
 */
@Composable
fun NoveliteNavGraph(
  navController: NavHostController,
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier,
  startDestination: String = NoveliteDestinations.HOME
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier,
    enterTransition = { fadeIn(animationSpec = tween(220)) },
    exitTransition = { fadeOut(animationSpec = tween(220)) },
    popEnterTransition = { fadeIn(animationSpec = tween(220)) },
    popExitTransition = { fadeOut(animationSpec = tween(220)) }
  ) {
    composable(NoveliteDestinations.AUTH) {
      AuthScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.ONBOARDING) {
      OnboardingScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.HOME) {
      HomeScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.EXPLORE) {
      ExploreScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.LIBRARY) {
      LibraryScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.WRITE) {
      WritingStudioScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.PROFILE) {
      ProfileScreen(viewModel = viewModel)
    }

    composable(
      route = NoveliteDestinations.STORY_DETAIL,
      arguments = listOf(navArgument("storyId") { type = NavType.StringType })
    ) { backStackEntry ->
      val storyId = backStackEntry.arguments?.getString("storyId")
      if (storyId != null) {
        viewModel.openStory(storyId)
      }
      StoryDetailScreen(viewModel = viewModel)
    }

    composable(
      route = NoveliteDestinations.READER,
      arguments = listOf(
        navArgument("storyId") { type = NavType.StringType },
        navArgument("chapterId") { type = NavType.StringType; defaultValue = "default" }
      )
    ) { backStackEntry ->
      val storyId = backStackEntry.arguments?.getString("storyId")
      val chapterId = backStackEntry.arguments?.getString("chapterId")?.takeIf { it != "default" }
      if (storyId != null) {
        viewModel.openReader(storyId, chapterId)
      }
      ReaderScreen(viewModel = viewModel)
    }

    composable(
      route = NoveliteDestinations.WRITER_EDITOR,
      arguments = listOf(
        navArgument("storyId") { type = NavType.StringType },
        navArgument("chapterId") { type = NavType.StringType; defaultValue = "default" }
      )
    ) { backStackEntry ->
      val storyId = backStackEntry.arguments?.getString("storyId")
      val chapterId = backStackEntry.arguments?.getString("chapterId")?.takeIf { it != "default" }
      WriterDraftEditorScreen(
        viewModel = viewModel,
        storyId = storyId,
        chapterId = chapterId,
        onBackClick = { navController.popBackStack() }
      )
    }

    composable(NoveliteDestinations.STREAK_DASHBOARD) {
      StreakDashboardScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.NOTIFICATIONS) {
      NotificationsScreen(viewModel = viewModel)
    }

    composable(NoveliteDestinations.ADMIN) {
      AdminScreen(viewModel = viewModel)
    }
  }
}
