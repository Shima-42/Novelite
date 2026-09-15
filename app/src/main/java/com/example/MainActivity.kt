package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.AnAuthorsReflectionIcon
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.LetTheQuillFlowIcon
import com.example.ui.components.OdysseyIcon
import com.example.ui.components.ReadingRoomIcon
import com.example.ui.components.VersesIHaveWovenIcon
import com.example.ui.components.WhereIWanderNowIcon
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DiagnosticScreen
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
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      NoveliteTheme {
        val viewModel: NoveliteViewModel = viewModel()
        NoveliteApp(viewModel = viewModel)
      }
    }
  }
}

data class NoveliteNavItem(
  val screen: Screen,
  val officialName: String,
  val testTag: String,
  val icon: @Composable (isSelected: Boolean) -> Unit
)

@Composable
fun NoveliteApp(viewModel: NoveliteViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val isLoggedIn by viewModel.isLoggedIn.collectAsState()
  val isOnboarded by viewModel.isOnboarded.collectAsState()
  val celebrationState by viewModel.celebration.collectAsState()

  BackHandler(enabled = currentScreen != Screen.HOME) {
    when (currentScreen) {
      Screen.READER -> {
        if (viewModel.selectedStoryId.value != null) {
          viewModel.navigateTo(Screen.STORY_DETAIL)
        } else {
          viewModel.navigateTo(Screen.HOME)
        }
      }
      Screen.STORY_DETAIL -> viewModel.navigateTo(Screen.HOME)
      Screen.WRITER_DRAFT -> viewModel.navigateTo(Screen.WRITE)
      Screen.STREAK_DASHBOARD -> viewModel.navigateTo(Screen.HOME)
      Screen.NOTIFICATIONS -> viewModel.navigateTo(Screen.HOME)
      Screen.ADMIN -> viewModel.navigateTo(Screen.PROFILE)
      Screen.PROFILE -> {
        if (viewModel.selectedAuthorId.value != null && viewModel.selectedAuthorId.value != viewModel.currentUser.value.id) {
          viewModel.openAuthorProfile(viewModel.currentUser.value.id)
        } else {
          viewModel.navigateTo(Screen.HOME)
        }
      }
      Screen.EXPLORE, Screen.LIBRARY, Screen.WRITE -> viewModel.navigateTo(Screen.HOME)
      else -> viewModel.navigateTo(Screen.HOME)
    }
  }

  val navItems = listOf(
    NoveliteNavItem(
      screen = Screen.HOME,
      officialName = "Home",
      testTag = "nav_the_reading_room",
      icon = { isSelected -> ReadingRoomIcon(isSelected = isSelected, size = 26.dp) }
    ),
    NoveliteNavItem(
      screen = Screen.EXPLORE,
      officialName = "Odyssey",
      testTag = "nav_odyssey",
      icon = { isSelected -> OdysseyIcon(isSelected = isSelected, size = 24.dp) }
    ),
    NoveliteNavItem(
      screen = Screen.LIBRARY,
      officialName = "Library",
      testTag = "nav_library",
      icon = { isSelected -> WhereIWanderNowIcon(isSelected = isSelected, size = 24.dp) }
    ),
    NoveliteNavItem(
      screen = Screen.WRITE,
      officialName = "Write",
      testTag = "nav_let_the_quill_flow",
      icon = { isSelected -> LetTheQuillFlowIcon(isSelected = isSelected, size = 24.dp) }
    ),
    NoveliteNavItem(
      screen = Screen.PROFILE,
      officialName = "Stories",
      testTag = "nav_verses_i_have_woven",
      icon = { isSelected -> VersesIHaveWovenIcon(isSelected = isSelected, size = 24.dp) }
    )
  )

  val showBottomBar = currentScreen in listOf(
    Screen.HOME,
    Screen.EXPLORE,
    Screen.LIBRARY,
    Screen.WRITE,
    Screen.PROFILE
  )

  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      bottomBar = {
        if (showBottomBar) {
          NavigationBar(
            containerColor = NoveliteCreamBg,
            tonalElevation = 2.dp,
            modifier = Modifier.testTag("bottom_nav_bar")
          ) {
            navItems.forEach { item ->
              val isSelected = currentScreen == item.screen
              NavigationBarItem(
                selected = isSelected,
                onClick = { viewModel.navigateTo(item.screen) },
                icon = {
                  item.icon(isSelected)
                },
                label = {
                  Text(
                    text = item.officialName,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 9.5.sp,
                      fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                      color = NoveliteDarkBrown
                    ),
                    maxLines = 1,
                    color = NoveliteDarkBrown
                  )
                },
                colors = NavigationBarItemDefaults.colors(
                  indicatorColor = NoveliteCardBeige,
                  selectedIconColor = NoveliteDarkBrown,
                  selectedTextColor = NoveliteDarkBrown,
                  unselectedIconColor = NoveliteDarkBrown,
                  unselectedTextColor = NoveliteDarkBrown
                ),
                modifier = Modifier.testTag(item.testTag)
              )
            }
          }
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
          when (screen) {
            Screen.AUTH -> AuthScreen(viewModel = viewModel)
            Screen.ONBOARDING -> OnboardingScreen(viewModel = viewModel)
            Screen.HOME -> HomeScreen(viewModel = viewModel)
            Screen.EXPLORE -> ExploreScreen(viewModel = viewModel)
            Screen.LIBRARY -> LibraryScreen(viewModel = viewModel)
            Screen.WRITE -> WritingStudioScreen(viewModel = viewModel)
            Screen.WRITER_DRAFT -> WriterDraftEditorScreen(
              viewModel = viewModel,
              storyId = viewModel.editingStoryId.value,
              chapterId = viewModel.editingChapterId.value,
              onBackClick = { viewModel.navigateTo(Screen.WRITE) }
            )
            Screen.PROFILE -> ProfileScreen(viewModel = viewModel)
            Screen.STORY_DETAIL -> StoryDetailScreen(viewModel = viewModel)
            Screen.READER -> ReaderScreen(viewModel = viewModel)
            Screen.STREAK_DASHBOARD -> StreakDashboardScreen(viewModel = viewModel)
            Screen.NOTIFICATIONS -> NotificationsScreen(viewModel = viewModel)
            Screen.ADMIN -> AdminScreen(viewModel = viewModel)
            Screen.DIAGNOSTIC -> DiagnosticScreen(viewModel = viewModel)
          }
        }

        // Celebration Dialog Overlay
        if (celebrationState.isVisible) {
          CelebrationDialog(
            streakDays = celebrationState.streakDays,
            goalMinutes = celebrationState.goalMinutes,
            badgeName = celebrationState.badgeName,
            onDismiss = { viewModel.dismissCelebration() }
          )
        }
      }
    }
  }
}
