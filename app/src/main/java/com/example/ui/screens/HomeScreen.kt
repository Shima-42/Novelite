package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AuthorStatus
import com.example.data.SampleData
import com.example.data.Story
import com.example.data.UserProfile
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.NoveliteLogo
import com.example.ui.components.ReadingRoomIcon
import com.example.ui.components.StoryCard
import com.example.ui.components.StoryStreak
import com.example.ui.components.StreakFlameBadge
import com.example.ui.components.TodayGoalCard
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteButtonBg
import com.example.ui.theme.NoveliteButtonText
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteSecondaryAccent
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

@Composable
fun HomeScreen(viewModel: NoveliteViewModel) {
  val currentUser by viewModel.currentUser.collectAsState()
  val stories by viewModel.stories.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val authorStatuses by viewModel.authorStatuses.collectAsState()
  val todayMinutes by viewModel.todayMinutesRead.collectAsState()

  val unreadNotifsCount = notifications.count { !it.isRead }
  val continueReadingList = stories.filter { it.readingProgressPercent > 0f }
  val trendingStories = stories.sortedByDescending { it.readsCount }
  val africanStories = stories.filter { it.genre == "African Stories" }
  val hiddenGems = stories.filter { it.likesCount > 4000 && it.readsCount < 30000 }
  val recommendedStories = stories.filter { currentUser.favoriteGenres.contains(it.genre) }.ifEmpty { stories }
  val featuredStory = continueReadingList.firstOrNull() ?: stories.firstOrNull()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
  ) {
    // Top Bar with Distinctive Novelite Logo
    HomeTopBar(
      userInitials = currentUser.displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "AS" },
      readingStreak = currentUser.readingStreak,
      unreadCount = unreadNotifsCount,
      onStreakClick = { viewModel.openStreakDashboard() },
      onSearchClick = { viewModel.navigateTo(Screen.EXPLORE) },
      onNotificationClick = { viewModel.navigateTo(Screen.NOTIFICATIONS) },
      onProfileClick = { viewModel.navigateTo(Screen.PROFILE) }
    )

    // Editorial Scrollable Feed
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 28.dp)
    ) {
      // Space Official Banner: The Reading Room
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = NoveliteCardBeige,
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "The Reading Room",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NoveliteDarkBrown
              )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "Your little corner where stories take you everywhere.",
              fontSize = 12.sp,
              color = NoveliteTextMuted,
              lineHeight = 16.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          // Child sitting on flying carpet reading open book (Official Concept)
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(NoveliteCreamBg)
              .border(1.dp, NoveliteBorder, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            ReadingRoomIcon(size = 38.dp, isSelected = true)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Today's Reading Goal Tracker Card
      TodayGoalCard(
        minutesRead = todayMinutes,
        goalMinutes = currentUser.readingGoalMinutes,
        streakDays = currentUser.readingStreak,
        onStartReading = {
          featuredStory?.let { viewModel.openReader(it.id, it.lastReadChapterId) }
        },
        onQuickAddMinute = { viewModel.addReadingMinute(1) },
        onOpenDashboard = { viewModel.openStreakDashboard() },
        modifier = Modifier.padding(horizontal = 20.dp)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Story Streak UI Component (Reading & Writing Streaks + Animated Fire Icon)
      StoryStreak(
        viewModel = viewModel,
        modifier = Modifier.padding(horizontal = 20.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Continue Reading / Featured Story Banner
      if (featuredStory != null) {
        FeaturedStoryHeroCard(
          story = featuredStory,
          onResumeClick = { viewModel.openReader(featuredStory.id, featuredStory.lastReadChapterId) },
          onSeeAllClick = { viewModel.navigateTo(Screen.LIBRARY) },
          modifier = Modifier.padding(horizontal = 20.dp)
        )
      }

      // Recommended for You
      Spacer(modifier = Modifier.height(26.dp))
      SectionHeader(
        title = "Recommended for You",
        subtitle = "Tailored to your reading taste",
        onSeeAll = { viewModel.navigateTo(Screen.EXPLORE) }
      )
      Spacer(modifier = Modifier.height(14.dp))
      LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(recommendedStories.take(6)) { story ->
          StoryCard(
            story = story,
            onClick = { viewModel.openStory(story.id) },
            onToggleLike = { viewModel.toggleLike(story.id) }
          )
        }
      }

      // Trending Stories
      Spacer(modifier = Modifier.height(26.dp))
      SectionHeader(
        title = "Trending Stories",
        subtitle = "Most engaged serials this week",
        onSeeAll = { viewModel.navigateTo(Screen.EXPLORE) }
      )
      Spacer(modifier = Modifier.height(14.dp))
      LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(trendingStories.take(6)) { story ->
          StoryCard(
            story = story,
            onClick = { viewModel.openStory(story.id) },
            onToggleLike = { viewModel.toggleLike(story.id) }
          )
        }
      }

      // African Stories & Folklore Spotlight
      if (africanStories.isNotEmpty()) {
        Spacer(modifier = Modifier.height(26.dp))
        SectionHeader(
          title = "African Stories & Folklore",
          subtitle = "Contemporary tales and rich mythologies",
          onSeeAll = { viewModel.navigateTo(Screen.EXPLORE) }
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(africanStories) { story ->
            StoryCard(
              story = story,
              onClick = { viewModel.openStory(story.id) },
              onToggleLike = { viewModel.toggleLike(story.id) }
            )
          }
        }
      }

      // Popular Storytellers / Writers Shelf
      Spacer(modifier = Modifier.height(26.dp))
      SectionHeader(
        title = "Popular Writers",
        subtitle = "Follow top authors to get chapter alerts",
        onSeeAll = { viewModel.navigateTo(Screen.EXPLORE) }
      )
      Spacer(modifier = Modifier.height(14.dp))
      LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(SampleData.authors) { author ->
          AuthorCard(
            author = author,
            isFollowing = currentUser.followedAuthorIds.contains(author.id),
            onFollowClick = { viewModel.followAuthor(author.id) },
            onClick = { viewModel.openAuthorProfile(author.id) }
          )
        }
      }

      // Author Whispers & Status Updates
      if (authorStatuses.isNotEmpty()) {
        Spacer(modifier = Modifier.height(26.dp))
        SectionHeader(
          title = "Author Whispers & Updates",
          subtitle = "Fresh notes and chapter alerts from storytellers",
          onSeeAll = { viewModel.navigateTo(Screen.EXPLORE) }
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(authorStatuses) { status ->
            AuthorStatusCard(
              status = status,
              onLikeClick = { viewModel.likeAuthorStatus(status.id) },
              onAuthorClick = { viewModel.openAuthorProfile(status.authorId) },
              onStoryClick = { status.storyId?.let { viewModel.openStory(it) } }
            )
          }
        }
      }

      // Hidden Gems
      if (hiddenGems.isNotEmpty()) {
        Spacer(modifier = Modifier.height(26.dp))
        SectionHeader(
          title = "Hidden Gems",
          subtitle = "Captivating underrated reads",
          onSeeAll = { viewModel.navigateTo(Screen.EXPLORE) }
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(hiddenGems) { story ->
            StoryCard(
              story = story,
              onClick = { viewModel.openStory(story.id) },
              onToggleLike = { viewModel.toggleLike(story.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun HomeTopBar(
  userInitials: String,
  readingStreak: Int,
  unreadCount: Int,
  onStreakClick: () -> Unit,
  onSearchClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onProfileClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("home_top_bar"),
    color = NoveliteCreamBg
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Official Novelite Brand Logo & Wordmark
      NoveliteLogo(
        symbolSize = 30.dp,
        textColor = NoveliteDarkBrown,
        showWordmark = true
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Streak Chip
        StreakFlameBadge(streakDays = readingStreak, onClick = onStreakClick)

        // Search Button
        IconButton(
          onClick = onSearchClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("home_search_button")
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = NoveliteDarkBrown,
            modifier = Modifier.size(20.dp)
          )
        }

        // Notification Bell
        IconButton(
          onClick = onNotificationClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("home_notifications_button")
        ) {
          if (unreadCount > 0) {
            BadgedBox(
              badge = {
                Badge(
                  containerColor = NoveliteCaramel,
                  contentColor = NoveliteWhite
                ) {
                  Text("$unreadCount", fontSize = 9.sp)
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = NoveliteDarkBrown,
                modifier = Modifier.size(20.dp)
              )
            }
          } else {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Notifications",
              tint = NoveliteDarkBrown,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        // User Avatar
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(NoveliteDarkBrown)
            .border(1.5.dp, NoveliteBorder, CircleShape)
            .clickable { onProfileClick() },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = userInitials,
            color = NoveliteCardBeige,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

@Composable
fun FeaturedStoryHeroCard(
  story: Story,
  onResumeClick: () -> Unit,
  onSeeAllClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Bottom
    ) {
      Text(
        text = "Continue Reading",
        fontFamily = FontFamily.Serif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.3).sp,
        color = NoveliteTextPrimary
      )
      Text(
        text = "See All",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = NoveliteWarmBrown,
        modifier = Modifier
          .clickable { onSeeAllClick() }
          .padding(4.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("hero_banner_card"),
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = NoveliteDarkBrown),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
      ) {
        val coverModel: Any? = story.coverImageUrl ?: story.coverDrawableRes
        if (coverModel != null) {
          AsyncImage(
            model = coverModel,
            contentDescription = story.title,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
          )
        }

        // Warm dark brown gradient overlay
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  NoveliteDarkBrown.copy(alpha = 0.25f),
                  NoveliteDarkBrown.copy(alpha = 0.88f),
                  NoveliteDarkBrown
                )
              )
            )
        )

        // Genre / Status Pill top right
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(NoveliteCaramel)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "FEATURED",
            color = NoveliteWhite,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          )
        }

        // Story details and CTA bottom
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = "${story.genre.uppercase()} • ${story.chaptersCount} CHAPTERS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = NoveliteCardBeige.copy(alpha = 0.85f)
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = story.title,
            fontFamily = FontFamily.Serif,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteWhite,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = story.description,
            fontSize = 12.sp,
            color = NoveliteCardBeige.copy(alpha = 0.85f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = onResumeClick,
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("hero_resume_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = NoveliteButtonBg,
              contentColor = NoveliteButtonText
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = NoveliteButtonText,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Continue Reading",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NoveliteButtonText
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  onSeeAll: (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = title,
        fontFamily = FontFamily.Serif,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = NoveliteTextPrimary
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = NoveliteTextMuted
        )
      }
    }

    if (onSeeAll != null) {
      Text(
        text = "See All",
        color = NoveliteWarmBrown,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .clickable { onSeeAll() }
          .padding(4.dp)
      )
    }
  }
}

@Composable
fun AuthorCard(
  author: UserProfile,
  isFollowing: Boolean,
  onFollowClick: () -> Unit,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(132.dp)
      .testTag("author_card_${author.username}")
      .clickable { onClick() },
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(NoveliteCreamBg)
          .border(1.dp, NoveliteBorder, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = author.displayName.take(1),
          color = NoveliteDarkBrown,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = author.displayName,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = NoveliteTextPrimary
      )

      Text(
        text = "@${author.username}",
        fontSize = 10.sp,
        color = NoveliteTextMuted,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "🔥", fontSize = 10.sp)
        Spacer(modifier = Modifier.width(2.dp))
        Text(
          text = "${author.readingStreak}d streak",
          fontSize = 10.sp,
          color = NoveliteCaramel,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Button(
        onClick = onFollowClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(34.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = NoveliteButtonBg,
          contentColor = NoveliteButtonText
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = if (isFollowing) "✓ Following" else "+ Follow",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteButtonText
          )
        }
      }
    }
  }
}

@Composable
fun AuthorStatusCard(
  status: AuthorStatus,
  onLikeClick: () -> Unit,
  onAuthorClick: () -> Unit,
  onStoryClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .width(260.dp)
      .testTag("author_status_card_${status.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // Author header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onAuthorClick() },
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(NoveliteDarkBrown),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = status.authorName.take(1),
            color = NoveliteCardBeige,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = status.authorName,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NoveliteDarkBrown,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "${status.authorUsername} • ${status.timestamp}",
            fontSize = 10.sp,
            color = NoveliteTextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Status Text (Editorial warm style)
      Text(
        text = status.statusText,
        fontSize = 12.sp,
        color = NoveliteTextPrimary,
        lineHeight = 17.sp,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
      )

      // Attached Media if present
      if (!status.mediaUrl.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(NoveliteDarkBrown)
        ) {
          AsyncImage(
            model = status.mediaUrl,
            contentDescription = "Status media",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
          )
          if (status.mediaType == "VIDEO") {
            Box(
              modifier = Modifier
                .align(Alignment.Center)
                .size(32.dp)
                .clip(CircleShape)
                .background(NoveliteDarkBrown.copy(alpha = 0.75f)),
              contentAlignment = Alignment.Center
            ) {
              Text("▶", color = NoveliteWhite, fontSize = 12.sp)
            }
          }
        }
      }

      // Linked story pill if present
      if (status.storyTitle != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onStoryClick() },
          shape = RoundedCornerShape(8.dp),
          color = NoveliteCreamBg,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("📖", fontSize = 11.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = status.storyTitle,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = NoveliteDarkBrown,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Interaction Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLikeClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (status.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like Status",
            tint = if (status.isLiked) NoveliteWarmBrown else NoveliteTextMuted,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${status.likesCount}",
            fontSize = 11.sp,
            color = if (status.isLiked) NoveliteWarmBrown else NoveliteTextMuted,
            fontWeight = FontWeight.Bold
          )
        }

        TextButton(
          onClick = { onAuthorClick() },
          contentPadding = PaddingValues(0.dp)
        ) {
          Text(
            text = "View Profile →",
            fontSize = 11.sp,
            color = NoveliteDarkBrown,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

