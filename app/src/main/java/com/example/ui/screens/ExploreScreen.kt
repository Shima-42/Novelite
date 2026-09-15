package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StoryStatus
import com.example.ui.NoveliteViewModel
import com.example.ui.components.HorizontalStoryCard
import com.example.ui.components.OdysseyIcon
import com.example.ui.theme.NoveliteBorder
import com.example.ui.theme.NoveliteCardBeige
import com.example.ui.theme.NoveliteCaramel
import com.example.ui.theme.NoveliteCreamBg
import com.example.ui.theme.NoveliteDarkBrown
import com.example.ui.theme.NoveliteTextMuted
import com.example.ui.theme.NoveliteTextPrimary
import com.example.ui.theme.NoveliteWarmBrown
import com.example.ui.theme.NoveliteWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(viewModel: NoveliteViewModel) {
  val stories by viewModel.stories.collectAsState()
  val query by viewModel.searchQuery.collectAsState()
  val selectedGenre by viewModel.selectedGenreFilter.collectAsState()
  val isCompletedOnly by viewModel.isCompletedOnlyFilter.collectAsState()
  val sortBy by viewModel.sortByFilter.collectAsState()

  var showFilterSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()

  val categoryChips = listOf(
    "All", "Trending", "African Stories", "Fantasy", "Romance",
    "Mystery", "Thriller", "Science Fiction", "Young Adult", "Poetry", "Comedy"
  )

  // Filtered and Sorted list
  val filteredStories = stories.filter { story ->
    val matchesQuery = query.isBlank() ||
      story.title.contains(query, ignoreCase = true) ||
      story.authorName.contains(query, ignoreCase = true) ||
      story.authorUsername.contains(query, ignoreCase = true) ||
      story.genre.contains(query, ignoreCase = true) ||
      story.tags.any { it.contains(query, ignoreCase = true) }

    val matchesGenre = when (selectedGenre) {
      "All" -> true
      "Trending" -> story.readsCount > 20000
      else -> story.genre.equals(selectedGenre, ignoreCase = true)
    }

    val matchesStatus = if (isCompletedOnly) story.status == StoryStatus.COMPLETED else true

    matchesQuery && matchesGenre && matchesStatus
  }.let { list ->
    when (sortBy) {
      "Most Popular" -> list.sortedByDescending { it.readsCount }
      "Most Liked" -> list.sortedByDescending { it.likesCount }
      "Recently Updated" -> list.sortedByDescending { it.chaptersCount }
      else -> list.sortedByDescending { it.readsCount + it.likesCount }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
  ) {
    // Header & Search
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(NoveliteCreamBg)
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Odyssey",
              fontFamily = FontFamily.Serif,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = (-0.4).sp,
              color = NoveliteTextPrimary
            )
          }
          Text(
            text = "Discover stories waiting to be found.",
            fontSize = 12.sp,
            color = NoveliteTextMuted
          )
        }

        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NoveliteCardBeige),
          contentAlignment = Alignment.Center
        ) {
          OdysseyIcon(size = 28.dp, isSelected = true)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = query,
          onValueChange = { viewModel.searchQuery.value = it },
          modifier = Modifier
            .weight(1f)
            .testTag("explore_search_input"),
          placeholder = { Text("Search stories, authors, genres...", fontSize = 13.sp, color = NoveliteTextMuted) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NoveliteDarkBrown, modifier = Modifier.size(20.dp)) },
          trailingIcon = {
            if (query.isNotEmpty()) {
              IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = NoveliteDarkBrown)
              }
            }
          },
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NoveliteCardBeige,
            unfocusedContainerColor = NoveliteCardBeige,
            focusedBorderColor = NoveliteDarkBrown,
            unfocusedBorderColor = NoveliteBorder,
            focusedTextColor = NoveliteTextPrimary,
            unfocusedTextColor = NoveliteTextPrimary
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
          onClick = { showFilterSheet = true },
          modifier = Modifier.testTag("explore_filter_button")
        ) {
          Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Filters",
            tint = if (isCompletedOnly || sortBy != "Trending") NoveliteDarkBrown else NoveliteTextMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Category Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
      ) {
        items(categoryChips) { cat ->
          val isSelected = selectedGenre == cat
          FilterChip(
            selected = isSelected,
            onClick = { viewModel.selectedGenreFilter.value = cat },
            label = { Text(cat, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
            shape = RoundedCornerShape(16.dp),
            colors = FilterChipDefaults.filterChipColors(
              containerColor = NoveliteCardBeige,
              selectedContainerColor = NoveliteDarkBrown,
              selectedLabelColor = NoveliteWhite,
              labelColor = NoveliteTextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = NoveliteBorder,
              selectedBorderColor = NoveliteDarkBrown,
              borderWidth = 1.dp
            )
          )
        }
      }
    }

    // Results Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${filteredStories.size} stories",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = NoveliteTextMuted
      )

      Text(
        text = "Sort: $sortBy",
        fontSize = 12.sp,
        color = NoveliteWarmBrown,
        fontWeight = FontWeight.Bold
      )
    }

    // Stories List
    if (filteredStories.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "🔍", fontSize = 40.sp)
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No stories found",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
          Text(
            text = "Try adjusting your search terms or filters.",
            style = MaterialTheme.typography.bodySmall,
            color = NoveliteTextMuted
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .testTag("explore_stories_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(filteredStories) { story ->
          HorizontalStoryCard(
            story = story,
            onClick = { viewModel.openStory(story.id) }
          )
        }
      }
    }
  }

  // Filter Bottom Sheet
  if (showFilterSheet) {
    ModalBottomSheet(
      onDismissRequest = { showFilterSheet = false },
      sheetState = sheetState,
      containerColor = NoveliteCardBeige
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Filter & Sort",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
          TextButton(onClick = {
            viewModel.selectedGenreFilter.value = "All"
            viewModel.isCompletedOnlyFilter.value = false
            viewModel.sortByFilter.value = "Trending"
            showFilterSheet = false
          }) {
            Text("Reset All", color = NoveliteWarmBrown, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sort By", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NoveliteTextMuted)
        listOf("Trending", "Most Popular", "Most Liked", "Recently Updated").forEach { option ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = sortBy == option,
              onClick = { viewModel.sortByFilter.value = option },
              colors = RadioButtonDefaults.colors(selectedColor = NoveliteDarkBrown)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(option, fontSize = 14.sp, color = NoveliteTextPrimary)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Story Status", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NoveliteTextMuted)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = !isCompletedOnly,
            onClick = { viewModel.isCompletedOnlyFilter.value = false },
            colors = RadioButtonDefaults.colors(selectedColor = NoveliteDarkBrown)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("All Stories (Ongoing & Completed)", fontSize = 14.sp, color = NoveliteTextPrimary)
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = isCompletedOnly,
            onClick = { viewModel.isCompletedOnlyFilter.value = true },
            colors = RadioButtonDefaults.colors(selectedColor = NoveliteDarkBrown)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Completed Stories Only", fontSize = 14.sp, color = NoveliteTextPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
