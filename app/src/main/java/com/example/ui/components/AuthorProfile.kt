package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthorStatus
import com.example.data.SampleData
import com.example.data.Story
import com.example.data.StoryStatus
import com.example.data.UserProfile
import com.example.ui.NoveliteViewModel
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteSecondaryAccent
import com.example.ui.theme.NoveliteSoftAccentBg
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

/**
 * AuthorProfile composable
 * Displays an author's bio, their uploaded stories, and a 'Follow' button to track their activity.
 */
@Composable
fun AuthorProfile(
  authorId: String,
  viewModel: NoveliteViewModel,
  modifier: Modifier = Modifier,
  onBackClick: (() -> Unit)? = null
) {
  val currentUser by viewModel.currentUser.collectAsState()
  val allStories by viewModel.stories.collectAsState()
  val allStatuses by viewModel.authorStatuses.collectAsState()

  // Find author profile from SampleData or current user
  val author = if (authorId == currentUser.id) {
    currentUser
  } else {
    SampleData.authors.find { it.id == authorId } ?: currentUser
  }

  val authorStories = allStories.filter { it.authorId == author.id || it.authorName == author.displayName }
  val isFollowing = currentUser.followedAuthorIds.contains(author.id)
  val authorStatuses = allStatuses.filter { it.authorId == author.id }

  AuthorProfile(
    author = author,
    stories = authorStories,
    isFollowing = isFollowing,
    onFollowClick = { viewModel.followAuthor(author.id) },
    onStoryClick = { story -> viewModel.openStory(story.id) },
    modifier = modifier,
    authorStatuses = authorStatuses,
    onBackClick = onBackClick
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthorProfile(
  author: UserProfile,
  stories: List<Story>,
  isFollowing: Boolean,
  onFollowClick: () -> Unit,
  onStoryClick: (Story) -> Unit,
  modifier: Modifier = Modifier,
  authorStatuses: List<AuthorStatus> = emptyList(),
  onBackClick: (() -> Unit)? = null
) {
  var selectedStatusFilter by remember { mutableStateOf("All") }

  val filteredStories = when (selectedStatusFilter) {
    "Ongoing" -> stories.filter { it.status == StoryStatus.ONGOING }
    "Completed" -> stories.filter { it.status == StoryStatus.COMPLETED }
    else -> stories
  }

  val totalReads = stories.sumOf { it.readsCount }
  val totalLikes = stories.sumOf { it.likesCount }

  // Adjust display followers count dynamically based on following state
  val dynamicFollowersCount = if (isFollowing) {
    author.followersCount + 1
  } else {
    author.followersCount
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
      .testTag("author_profile_screen"),
    contentPadding = PaddingValues(bottom = 48.dp)
  ) {
    // Top Navigation Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(NoveliteCardBeige)
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (onBackClick != null) {
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { onBackClick() }
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = NoveliteDarkBrown,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Back",
              color = NoveliteDarkBrown,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        } else {
          Row(verticalAlignment = Alignment.CenterVertically) {
            AnAuthorsReflectionIcon(size = 22.dp, isSelected = true)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Author Profile",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = NoveliteDarkBrown
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = NoveliteSoftAccentBg
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "Verified Storyteller",
              tint = NoveliteWarmBrown,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Storyteller",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown
            )
          }
        }
      }
      HorizontalDivider(color = NoveliteBorder, thickness = 1.dp)
    }

    // Author Bio & Identity Card
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(NoveliteCardBeige)
          .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Avatar with verified badge overlay
        Box(
          contentAlignment = Alignment.BottomEnd,
          modifier = Modifier.size(86.dp)
        ) {
          Box(
            modifier = Modifier
              .size(86.dp)
              .clip(CircleShape)
              .background(NoveliteDarkBrown)
              .border(3.dp, NoveliteSoftAccentBg, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = author.displayName.take(1),
              color = NoveliteWhite,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 34.sp
            )
          }

          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(CircleShape)
              .background(NoveliteWarmBrown)
              .border(2.dp, NoveliteCardBeige, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = "Verified",
              tint = NoveliteWhite,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Author Name
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = author.displayName,
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Username & Join info
        Text(
          text = "@${author.username} • Joined ${author.joinedDate}",
          fontSize = 12.sp,
          color = NoveliteTextMuted
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Author Bio Box
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          color = NoveliteCreamBg,
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = NoveliteCaramel,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ABOUT THE AUTHOR",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NoveliteWarmBrown
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = author.bio.ifBlank { "Passionate storyteller weaving worlds on Novelite." },
              fontSize = 13.sp,
              color = NoveliteTextPrimary,
              lineHeight = 19.sp
            )

            // Favorite Genres Tags
            if (author.favoriteGenres.isNotEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                author.favoriteGenres.forEach { genre ->
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NoveliteSoftAccentBg
                  ) {
                    Text(
                      text = genre,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = NoveliteDarkBrown,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Author Statistics Bar
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = NoveliteCreamBg),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            AuthorStatItem(
              label = "Followers",
              value = formatCount(dynamicFollowersCount)
            )
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(NoveliteBorder))
            AuthorStatItem(
              label = "Stories",
              value = "${stories.size}"
            )
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(NoveliteBorder))
            AuthorStatItem(
              label = "Total Reads",
              value = formatCount(totalReads)
            )
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(NoveliteBorder))
            AuthorStatItem(
              label = "Streak",
              value = "${author.writingStreak}d 🔥"
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 'Follow' Button to Track Their Activity
        Button(
          onClick = onFollowClick,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("author_follow_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isFollowing) NoveliteSoftAccentBg else NoveliteDarkBrown,
            contentColor = if (isFollowing) NoveliteDarkBrown else NoveliteWhite
          ),
          border = if (isFollowing) androidx.compose.foundation.BorderStroke(1.dp, NoveliteWarmBrown) else null,
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = if (isFollowing) Icons.Default.Check else Icons.Default.Add,
              contentDescription = null,
              tint = if (isFollowing) NoveliteDarkBrown else NoveliteWhite,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isFollowing) "Following" else "Follow Author",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Activity Tracking Notice
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          color = if (isFollowing) NoveliteSoftAccentBg.copy(alpha = 0.5f) else NoveliteCreamBg,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isFollowing) NoveliteWarmBrown.copy(alpha = 0.3f) else NoveliteBorder
          )
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isFollowing) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
              contentDescription = "Activity Tracking",
              tint = if (isFollowing) NoveliteWarmBrown else NoveliteTextMuted,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isFollowing) {
                "Tracking activity • You'll receive alerts when ${author.displayName} drops chapters or status updates."
              } else {
                "Follow to track ${author.displayName}'s chapter releases and reader whispers."
              },
              fontSize = 11.sp,
              color = if (isFollowing) NoveliteDarkBrown else NoveliteTextMuted,
              lineHeight = 15.sp
            )
          }
        }
      }
      HorizontalDivider(color = NoveliteBorder, thickness = 1.dp)
    }

    // Recent Whispers / Status Updates (Activity Feed)
    if (authorStatuses.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = NoveliteWarmBrown,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Recent Activity & Whispers",
              fontFamily = FontFamily.Serif,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteTextPrimary
            )
          }
          Spacer(modifier = Modifier.height(10.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(authorStatuses) { status ->
              AuthorActivityCard(status = status)
            }
          }
        }
      }
    }

    // Uploaded Stories Section
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            VersesIHaveWovenIcon(size = 22.dp, isSelected = true)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Uploaded Stories (${stories.size})",
              fontFamily = FontFamily.Serif,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteTextPrimary
            )
          }

          Text(
            text = "$totalLikes total likes",
            fontSize = 12.sp,
            color = NoveliteWarmBrown,
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Status Filter Chips
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          listOf("All", "Ongoing", "Completed").forEach { filter ->
            val isSelected = selectedStatusFilter == filter
            FilterChip(
              selected = isSelected,
              onClick = { selectedStatusFilter = filter },
              label = {
                Text(
                  text = filter,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NoveliteDarkBrown,
                selectedLabelColor = NoveliteWhite,
                containerColor = NoveliteCardBeige,
                labelColor = NoveliteDarkBrown
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = NoveliteBorder,
                selectedBorderColor = NoveliteDarkBrown,
                enabled = true,
                selected = isSelected
              )
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Story Items
    if (filteredStories.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
          border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.MenuBook,
              contentDescription = null,
              tint = NoveliteCaramel,
              modifier = Modifier.size(38.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "No ${if (selectedStatusFilter != "All") selectedStatusFilter.lowercase() + " " else ""}stories uploaded yet",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = NoveliteTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Follow ${author.displayName} to be notified when they publish their next masterpiece.",
              fontSize = 12.sp,
              color = NoveliteTextMuted,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    } else {
      items(filteredStories) { story ->
        AuthorUploadedStoryCard(
          story = story,
          onClick = { onStoryClick(story) },
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )
      }
    }
  }
}

@Composable
private fun AuthorStatItem(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = NoveliteTextPrimary
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      fontSize = 10.sp,
      color = NoveliteTextMuted
    )
  }
}

@Composable
private fun AuthorUploadedStoryCard(
  story: Story,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("author_story_${story.id}")
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Cover thumbnail / Art
      Box(
        modifier = Modifier
          .width(72.dp)
          .height(96.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(Color(story.coverColorHex))
      ) {
        StoryCoverView(
          story = story,
          modifier = Modifier.matchParentSize(),
          shape = RoundedCornerShape(10.dp),
          showTeaserBadge = false
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Content Details
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Genre Pill
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = NoveliteSoftAccentBg
          ) {
            Text(
              text = story.genre,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteDarkBrown,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          // Status Badge
          Text(
            text = if (story.status == StoryStatus.COMPLETED) "Completed" else "Ongoing",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (story.status == StoryStatus.COMPLETED) NoveliteWarmBrown else NoveliteCaramel
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
          text = story.title,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = NoveliteTextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Description snippet
        Text(
          text = story.description,
          fontSize = 11.sp,
          color = NoveliteTextMuted,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Metrics Row & Read Action
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null,
                tint = NoveliteTextMuted,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = formatCount(story.readsCount),
                fontSize = 11.sp,
                color = NoveliteTextMuted
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = NoveliteCaramel,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = formatCount(story.likesCount),
                fontSize = 11.sp,
                color = NoveliteTextMuted
              )
            }

            Text(
              text = "${story.chapters.size} ch",
              fontSize = 11.sp,
              color = NoveliteTextMuted
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = NoveliteDarkBrown
          ) {
            Text(
              text = "Read",
              color = NoveliteWhite,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AuthorActivityCard(status: AuthorStatus) {
  Card(
    modifier = Modifier.width(240.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Author Whisper",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = NoveliteCaramel
        )
        Text(
          text = status.timestamp,
          fontSize = 9.sp,
          color = NoveliteTextMuted
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = status.statusText,
        fontSize = 11.sp,
        color = NoveliteTextPrimary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 15.sp
      )
      if (status.storyTitle != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "📖 ${status.storyTitle}",
          fontSize = 10.sp,
          color = NoveliteWarmBrown,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

private fun formatCount(count: Int): String {
  return when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
    count >= 1_000 -> String.format("%.1fk", count / 1_000f)
    else -> count.toString()
  }
}
