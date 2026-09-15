package com.example

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.example.data.NoveliteRepository
import com.example.data.StoryStatus
import com.example.ui.NoveliteViewModel
import com.example.ui.Screen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Novelite", appName)
  }

  @Test
  fun `activity launches without crash`() {
    val scenario = ActivityScenario.launch(MainActivity::class.java)
    assertNotNull(scenario)
    scenario.onActivity { activity ->
      assertNotNull(activity)
    }
    scenario.close()
  }

  @Test
  fun `repository and viewModel initialization`() {
    val repository = NoveliteRepository()
    val viewModel = NoveliteViewModel(repository)
    assertNotNull(viewModel.currentUser.value)
    assertEquals("Aurora Sterling", viewModel.currentUser.value.displayName)
    assertEquals(true, viewModel.stories.value.isNotEmpty())
  }

  @Test
  fun `test navigation through all screens`() {
    val repository = NoveliteRepository()
    val viewModel = NoveliteViewModel(repository)

    Screen.values().forEach { screen ->
      viewModel.navigateTo(screen)
      assertEquals(screen, viewModel.currentScreen.value)
    }
  }

  @Test
  fun `test reader and story interactions`() {
    val repository = NoveliteRepository()
    val viewModel = NoveliteViewModel(repository)

    val firstStory = viewModel.stories.value.first()
    viewModel.openStory(firstStory.id)
    assertEquals(Screen.STORY_DETAIL, viewModel.currentScreen.value)
    assertEquals(firstStory.id, viewModel.selectedStoryId.value)

    viewModel.openReader(firstStory.id)
    assertEquals(Screen.READER, viewModel.currentScreen.value)

    viewModel.addReadingMinute(5)
    assertTrue(viewModel.todayMinutesRead.value >= 5)

    viewModel.toggleLike(firstStory.id)
    viewModel.toggleLibrary(firstStory.id)
  }

  @Test
  fun `test writing studio interactions`() {
    val repository = NoveliteRepository()
    val viewModel = NoveliteViewModel(repository)

    val newStory = viewModel.createStory(
      title = "The Obsidian Veil",
      desc = "A thrilling fantasy quest.",
      genre = "Fantasy",
      tags = listOf("Fantasy", "Magic"),
      status = StoryStatus.ONGOING
    )
    assertNotNull(newStory)
    assertEquals("The Obsidian Veil", newStory.title)

    val chapter = viewModel.addChapter(
      storyId = newStory.id,
      title = "Prologue: Shadows Awake",
      content = "The night was quiet until the obsidian crystal cracked...",
      isDraft = false
    )
    assertNotNull(chapter)
    assertEquals("Prologue: Shadows Awake", chapter.title)
  }

  @Test
  fun `test chapter comments and reader feedback interactions`() {
    val repository = NoveliteRepository()
    val viewModel = NoveliteViewModel(repository)

    val initialCount = viewModel.comments.value.size

    // Post comment on chapter
    viewModel.postComment(
      storyId = "story_1",
      chapterId = "c1_1",
      text = "This chapter had breathtaking pacing and plot twists! 🔥",
      quote = "The destination isn't London or Zurich."
    )

    assertEquals(initialCount + 1, viewModel.comments.value.size)
    val latestComment = viewModel.comments.value.first()
    assertEquals("This chapter had breathtaking pacing and plot twists! 🔥", latestComment.text)
    assertEquals("The destination isn't London or Zurich.", latestComment.inlineQuote)
    assertEquals(0, latestComment.likes)

    // Like comment
    viewModel.likeComment(latestComment.id)
    val likedComment = viewModel.comments.value.first { it.id == latestComment.id }
    assertEquals(1, likedComment.likes)
    assertTrue(likedComment.isLiked)

    // Reply to comment
    viewModel.replyComment(latestComment.id, "Totally agree, that reveal had me stunned!")
    val repliedComment = viewModel.comments.value.first { it.id == latestComment.id }
    assertEquals(1, repliedComment.replies.size)
    assertEquals("Totally agree, that reveal had me stunned!", repliedComment.replies.first().text)

    // Delete comment
    viewModel.deleteComment(latestComment.id)
    assertEquals(initialCount, viewModel.comments.value.size)
  }

  @Test
  fun `test author profile navigation and follow activity tracking`() {
    val repository = NoveliteRepository()
    val viewModel = NoveliteViewModel(repository)

    val targetAuthorId = "author_1"
    viewModel.openAuthorProfile(targetAuthorId)
    assertEquals(Screen.PROFILE, viewModel.currentScreen.value)
    assertEquals(targetAuthorId, viewModel.selectedAuthorId.value)

    // Author's uploaded stories
    val authorStories = viewModel.stories.value.filter { it.authorId == targetAuthorId }
    assertTrue(authorStories.isNotEmpty())

    // Initial follow state: false
    assertEquals(false, viewModel.currentUser.value.followedAuthorIds.contains(targetAuthorId))

    // Follow author to track their activity
    viewModel.followAuthor(targetAuthorId)
    assertTrue(viewModel.currentUser.value.followedAuthorIds.contains(targetAuthorId))

    // Unfollow author
    viewModel.followAuthor(targetAuthorId)
    assertEquals(false, viewModel.currentUser.value.followedAuthorIds.contains(targetAuthorId))
  }
}
