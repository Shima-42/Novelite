package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LibraryTab
import com.example.data.Story
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import com.example.ui.components.PagesYetToBeTurnedIcon
import com.example.ui.components.ShelvesOfMyMakingIcon
import com.example.ui.components.StoriesThatStayedIcon
import com.example.ui.components.TreasuredBetweenTheLinesIcon
import com.example.ui.components.WhereIWanderNowIcon
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

data class NoveliteLibraryTabItem(
  val tabEnum: LibraryTab,
  val officialName: String,
  val shortLabel: String,
  val subtitle: String,
  val icon: @Composable (isSelected: Boolean) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(viewModel: NoveliteViewModel) {
  val stories by viewModel.stories.collectAsState()
  val customCollections by viewModel.customCollections.collectAsState()
  val librarySearchQuery by viewModel.librarySearchQuery.collectAsState()
  val recentlyReadStories by viewModel.recentlyReadStories.collectAsState()

  var selectedTabIndex by remember { mutableStateOf(0) }
  var showCreateCollectionDialog by remember { mutableStateOf(false) }
  var newCollectionName by remember { mutableStateOf("") }
  var newCollectionIcon by remember { mutableStateOf("📚") }
  var selectedCollectionId by remember { mutableStateOf<String?>(null) }

  val libraryTabs = listOf(
    NoveliteLibraryTabItem(
      tabEnum = LibraryTab.WHERE_I_WANDER_NOW,
      officialName = "Where I Wander Now",
      shortLabel = "Where I Wander Now",
      subtitle = "The stories I'm lost in right now.",
      icon = { isSelected -> WhereIWanderNowIcon(isSelected = isSelected, size = 20.dp) }
    ),
    NoveliteLibraryTabItem(
      tabEnum = LibraryTab.TREASURED_BETWEEN_THE_LINES,
      officialName = "Treasured Between the Lines",
      shortLabel = "Treasured Between the Lines",
      subtitle = "Stories that found a special place in my heart.",
      icon = { isSelected -> TreasuredBetweenTheLinesIcon(isSelected = isSelected, size = 20.dp) }
    ),
    NoveliteLibraryTabItem(
      tabEnum = LibraryTab.PAGES_YET_TO_BE_TURNED,
      officialName = "Pages Yet to Be Turned",
      shortLabel = "Pages Yet to Be Turned",
      subtitle = "Stories waiting for me to begin their journey.",
      icon = { isSelected -> PagesYetToBeTurnedIcon(isSelected = isSelected, size = 20.dp) }
    ),
    NoveliteLibraryTabItem(
      tabEnum = LibraryTab.STORIES_THAT_STAYED,
      officialName = "Stories That Stayed",
      shortLabel = "Stories That Stayed",
      subtitle = "The stories I finished, but never truly left behind.",
      icon = { isSelected -> StoriesThatStayedIcon(isSelected = isSelected, size = 20.dp) }
    ),
    NoveliteLibraryTabItem(
      tabEnum = LibraryTab.SHELVES_OF_MY_MAKING,
      officialName = "Shelves of My Making",
      shortLabel = "Shelves of My Making",
      subtitle = "Stories gathered into worlds of my own.",
      icon = { isSelected -> ShelvesOfMyMakingIcon(isSelected = isSelected, size = 20.dp) }
    )
  )

  val currentTab = libraryTabs[selectedTabIndex]

  val rawTabStories = when (selectedTabIndex) {
    0 -> stories.filter { it.isInLibrary && it.readingProgressPercent in 0.01f..0.98f }
    1 -> stories.filter { it.isLiked }
    2 -> stories.filter { it.isInLibrary && it.readingProgressPercent == 0f }
    3 -> stories.filter { it.isInLibrary && it.readingProgressPercent >= 0.98f }
    else -> emptyList()
  }

  val libraryStories = if (librarySearchQuery.isBlank()) {
    rawTabStories
  } else {
    rawTabStories.filter { story ->
      story.title.contains(librarySearchQuery, ignoreCase = true) ||
      story.authorName.contains(librarySearchQuery, ignoreCase = true)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
      .testTag("library_screen")
  ) {
    // Header
    Surface(
      color = NoveliteCardBeige,
      tonalElevation = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
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
              text = "Library",
              fontFamily = FontFamily.Serif,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = NoveliteTextPrimary
            )
            Text(
              text = currentTab.subtitle,
              fontSize = 12.sp,
              color = NoveliteTextMuted
            )
          }

          if (selectedTabIndex == 4) {
            Button(
              onClick = { showCreateCollectionDialog = true },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.testTag("create_collection_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = NoveliteWhite)
              Spacer(modifier = Modifier.width(4.dp))
              Text("New Shelf", fontSize = 11.sp, color = NoveliteWhite, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar (Title search filter)
        OutlinedTextField(
          value = librarySearchQuery,
          onValueChange = { viewModel.setLibrarySearchQuery(it) },
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .testTag("library_search_bar"),
          placeholder = { Text("Search library by title...", fontSize = 13.sp, color = NoveliteTextMuted) },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = NoveliteWarmBrown,
              modifier = Modifier.size(18.dp)
            )
          },
          trailingIcon = {
            if (librarySearchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.setLibrarySearchQuery("") }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Clear",
                  tint = NoveliteWarmBrown,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NoveliteWhite,
            unfocusedContainerColor = NoveliteWhite,
            focusedBorderColor = NoveliteDarkBrown,
            unfocusedBorderColor = NoveliteBorder,
            focusedTextColor = NoveliteTextPrimary,
            unfocusedTextColor = NoveliteTextPrimary
          )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Scrollable Tabs with official icons and names
        ScrollableTabRow(
          selectedTabIndex = selectedTabIndex,
          containerColor = Color.Transparent,
          contentColor = NoveliteDarkBrown,
          edgePadding = 16.dp,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
              color = NoveliteCaramel
            )
          }
        ) {
          libraryTabs.forEachIndexed { index, tabItem ->
            val isSelected = selectedTabIndex == index
            Tab(
              selected = isSelected,
              onClick = {
                selectedTabIndex = index
                selectedCollectionId = null
              },
              icon = {
                tabItem.icon(isSelected)
              },
              text = {
                Text(
                  text = tabItem.shortLabel,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) NoveliteDarkBrown else NoveliteTextMuted,
                  fontSize = 12.sp
                )
              }
            )
          }
        }
      }
    }

    // Recently Read Horizontal Section (when search query is blank)
    if (recentlyReadStories.isNotEmpty() && librarySearchQuery.isBlank()) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp, bottom = 4.dp)
          .testTag("recently_read_section")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoStories,
              contentDescription = null,
              tint = NoveliteWarmBrown,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Recently Read",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = NoveliteDarkBrown
            )
          }

          Text(
            text = "${recentlyReadStories.size} stories",
            fontSize = 11.sp,
            color = NoveliteTextMuted
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 20.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(recentlyReadStories) { story ->
            RecentlyReadStoryCard(
              story = story,
              onClick = { viewModel.openReader(story.id, story.lastReadChapterId) }
            )
          }
        }
      }
    }

    // Active Category Subtitle Card
    Surface(
      color = NoveliteCreamBg,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        currentTab.icon(true)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = currentTab.officialName,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NoveliteDarkBrown
          )
          Text(
            text = currentTab.subtitle,
            fontSize = 10.sp,
            color = NoveliteTextMuted
          )
        }
      }
    }

    if (selectedTabIndex == 4) {
      // Custom Collections View
      if (selectedCollectionId == null) {
        // Shelf lists
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(customCollections) { col ->
            val colStories = stories.filter { col.storyIds.contains(it.id) }
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedCollectionId = col.id },
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
              border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = col.icon, fontSize = 26.sp)
                  Spacer(modifier = Modifier.width(14.dp))
                  Column {
                    Text(
                      text = col.name,
                      fontFamily = FontFamily.Serif,
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp,
                      color = NoveliteTextPrimary
                    )
                    Text(
                      text = "${colStories.size} Stories",
                      fontSize = 12.sp,
                      color = NoveliteTextMuted
                    )
                  }
                }

                Icon(
                  imageVector = Icons.Default.Folder,
                  contentDescription = null,
                  tint = NoveliteCaramel
                )
              }
            }
          }
        }
      } else {
        // Active Shelf Details
        val activeCol = customCollections.find { it.id == selectedCollectionId }
        val colStories = stories.filter { activeCol?.storyIds?.contains(it.id) == true }

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "← All Shelves",
                color = NoveliteWarmBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { selectedCollectionId = null }
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "${activeCol?.icon} ${activeCol?.name}",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = NoveliteTextPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          if (colStories.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No stories in this shelf yet. Add stories from Explore or Story Details!",
                color = NoveliteTextMuted,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          } else {
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              items(colStories) { story ->
                LibraryStoryItemCard(
                  story = story,
                  onRead = { viewModel.openReader(story.id, story.lastReadChapterId) },
                  onRemove = { viewModel.removeStoryFromCustomCollection(activeCol!!.id, story.id) }
                )
              }
            }
          }
        }
      }
    } else {
      // Standard Tabs View
      if (libraryStories.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "📖", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "No stories in this section",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = NoveliteTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Browse Explore to discover stories and add them to your shelf.",
              fontSize = 12.sp,
              color = NoveliteTextMuted,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = { viewModel.navigateTo(Screen.EXPLORE) },
              colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Discover Stories", fontWeight = FontWeight.Bold, color = NoveliteWhite)
            }
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(libraryStories) { story ->
            LibraryStoryItemCard(
              story = story,
              onRead = { viewModel.openReader(story.id, story.lastReadChapterId) },
              onRemove = { viewModel.toggleLibrary(story.id) }
            )
          }
        }
      }
    }
  }

  // Create Collection Dialog
  if (showCreateCollectionDialog) {
    AlertDialog(
      onDismissRequest = { showCreateCollectionDialog = false },
      containerColor = NoveliteCardBeige,
      title = {
        Text(
          text = "Create Custom Collection",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          color = NoveliteTextPrimary
        )
      },
      text = {
        Column {
          OutlinedTextField(
            value = newCollectionName,
            onValueChange = { newCollectionName = it },
            label = { Text("Shelf Name", color = NoveliteTextMuted) },
            placeholder = { Text("e.g. Bedtime Stories", color = NoveliteTextMuted) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("collection_name_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = NoveliteDarkBrown,
              unfocusedBorderColor = NoveliteBorder,
              focusedTextColor = NoveliteTextPrimary,
              unfocusedTextColor = NoveliteTextPrimary
            ),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text("Choose Icon Emoji", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NoveliteTextPrimary)
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            listOf("📚", "❤️", "😭", "🌙", "🔥", "✨").forEach { emoji ->
              Surface(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { newCollectionIcon = emoji },
                color = if (newCollectionIcon == emoji) NoveliteCreamBg else Color.Transparent,
                border = if (newCollectionIcon == emoji) androidx.compose.foundation.BorderStroke(1.dp, NoveliteDarkBrown) else null
              ) {
                Text(text = emoji, fontSize = 24.sp, modifier = Modifier.padding(6.dp))
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newCollectionName.isNotBlank()) {
              viewModel.createCustomCollection(newCollectionName, newCollectionIcon)
              newCollectionName = ""
              showCreateCollectionDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = NoveliteDarkBrown),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Create Shelf", color = NoveliteWhite, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCreateCollectionDialog = false }) {
          Text("Cancel", color = NoveliteTextMuted)
        }
      }
    )
  }
}

@Composable
fun LibraryStoryItemCard(
  story: Story,
  onRead: () -> Unit,
  onRemove: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onRead() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(modifier = Modifier.padding(12.dp)) {
      Box(
        modifier = Modifier
          .width(70.dp)
          .height(95.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(Color(story.coverColorHex))
      ) {
        if (story.coverDrawableRes != null) {
          AsyncImage(
            model = story.coverDrawableRes,
            contentDescription = "Cover",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(
        modifier = Modifier
          .weight(1f)
          .height(95.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = story.title,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = NoveliteTextPrimary
          )
          Text(
            text = "by ${story.authorName}",
            style = MaterialTheme.typography.bodySmall,
            color = NoveliteTextMuted
          )
        }

        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (story.readingProgressPercent > 0f) "${(story.readingProgressPercent * 100).toInt()}% completed" else "Unread",
              fontSize = 11.sp,
              color = if (story.readingProgressPercent >= 0.98f) NoveliteDarkBrown else NoveliteCaramel,
              fontWeight = FontWeight.Bold
            )
            IconButton(
              onClick = onRemove,
              modifier = Modifier.size(24.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Remove", tint = NoveliteTextMuted, modifier = Modifier.size(16.dp))
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          LinearProgressIndicator(
            progress = { story.readingProgressPercent },
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp)),
            color = NoveliteCaramel,
            trackColor = NoveliteCreamBg
          )
        }
      }
    }
  }
}

@Composable
fun RecentlyReadStoryCard(
  story: Story,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(135.dp)
      .clickable { onClick() }
      .testTag("recently_read_card_${story.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
    border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      // Book Cover
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(115.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(story.coverColorHex))
      ) {
        if (story.coverDrawableRes != null) {
          AsyncImage(
            model = story.coverDrawableRes,
            contentDescription = "Cover",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
          )
        } else {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = story.title.take(2).uppercase(),
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = NoveliteWhite
            )
          }
        }

        // Mini Badge
        Surface(
          shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 8.dp),
          color = NoveliteDarkBrown,
          modifier = Modifier.align(Alignment.TopStart)
        ) {
          Text(
            text = "${(story.readingProgressPercent * 100).toInt()}%",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteWhite,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = story.title,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = NoveliteTextPrimary
      )

      Text(
        text = story.authorName,
        fontSize = 10.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = NoveliteTextMuted
      )

      Spacer(modifier = Modifier.height(6.dp))

      LinearProgressIndicator(
        progress = { story.readingProgressPercent },
        modifier = Modifier
          .fillMaxWidth()
          .height(3.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = NoveliteCaramel,
        trackColor = NoveliteCreamBg
      )
    }
  }
}
