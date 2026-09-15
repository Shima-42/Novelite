package com.example.data

import com.example.R

object SampleData {

  val authors = listOf(
    UserProfile(
      id = "author_1",
      username = "amara_okafor",
      displayName = "Amara Okafor",
      email = "amara@novelite.app",
      bio = "Author of African romance & contemporary thrillers. Lover of spicy jollof and stormy rainy nights in Lagos.",
      avatarUrl = "",
      joinedDate = "Jan 2024",
      followersCount = 14200,
      followingCount = 184,
      readingStreak = 45,
      writingStreak = 18,
      longestStreak = 62,
      streakShields = 3,
      isWriter = true,
      favoriteGenres = listOf("African Stories", "Romance", "Thriller")
    ),
    UserProfile(
      id = "author_2",
      username = "elena_rostova",
      displayName = "Elena Rostova",
      email = "elena@novelite.app",
      bio = "High Fantasy worldbuilder, celestial mythologist, and coffee enthusiast. Building kingdoms one word at a time.",
      avatarUrl = "",
      joinedDate = "Mar 2024",
      followersCount = 28900,
      followingCount = 92,
      readingStreak = 88,
      writingStreak = 31,
      longestStreak = 90,
      streakShields = 4,
      isWriter = true,
      favoriteGenres = listOf("Fantasy", "Poetry", "Young Adult")
    ),
    UserProfile(
      id = "author_3",
      username = "zane_sterling",
      displayName = "Zane Sterling",
      email = "zane@novelite.app",
      bio = "Cyberpunk & speculative fiction writer. Exploring AI consciousness, neon rain, and retro synth futures.",
      avatarUrl = "",
      joinedDate = "Feb 2024",
      followersCount = 9300,
      followingCount = 120,
      readingStreak = 19,
      writingStreak = 9,
      longestStreak = 34,
      streakShields = 1,
      isWriter = true,
      favoriteGenres = listOf("Science Fiction", "Thriller", "Action")
    ),
    UserProfile(
      id = "author_4",
      username = "aisha_bello",
      displayName = "Aisha Bello",
      email = "aisha@novelite.app",
      bio = "Mythology researcher and novelist weaving folklore with urban fantasy. Storyteller by heart.",
      avatarUrl = "",
      joinedDate = "May 2024",
      followersCount = 16400,
      followingCount = 210,
      readingStreak = 33,
      writingStreak = 14,
      longestStreak = 42,
      streakShields = 2,
      isWriter = true,
      favoriteGenres = listOf("African Stories", "Fantasy", "Drama")
    ),
    UserProfile(
      id = "author_5",
      username = "liam_vance",
      displayName = "Liam Vance",
      email = "liam@novelite.app",
      bio = "YA contemporary and fantasy writer. Stargazer, indie musician, and champion of tender hearts.",
      avatarUrl = "",
      joinedDate = "Nov 2023",
      followersCount = 21500,
      followingCount = 155,
      readingStreak = 104,
      writingStreak = 41,
      longestStreak = 120,
      streakShields = 5,
      isWriter = true,
      favoriteGenres = listOf("Young Adult", "Fantasy", "Romance")
    ),
    UserProfile(
      id = "author_6",
      username = "chloe_dubois",
      displayName = "Chloe Dubois",
      email = "chloe@novelite.app",
      bio = "Writing cozy romances with witty banter, Parisian cafes, and awkward meet-cutes.",
      avatarUrl = "",
      joinedDate = "Jul 2024",
      followersCount = 8700,
      followingCount = 340,
      readingStreak = 15,
      writingStreak = 6,
      longestStreak = 25,
      streakShields = 2,
      isWriter = true,
      favoriteGenres = listOf("Romance", "Comedy", "Drama")
    ),
    UserProfile(
      id = "author_7",
      username = "brandon_cole",
      displayName = "Brandon Cole",
      email = "brandon@novelite.app",
      bio = "Psychological thrillers, mystery twists, and gothic horror. Nothing is as innocent as it seems.",
      avatarUrl = "",
      joinedDate = "Aug 2023",
      followersCount = 18900,
      followingCount = 80,
      readingStreak = 61,
      writingStreak = 22,
      longestStreak = 75,
      streakShields = 3,
      isWriter = true,
      favoriteGenres = listOf("Thriller", "Horror", "Mystery")
    ),
    UserProfile(
      id = "author_8",
      username = "tariq_almansoor",
      displayName = "Tariq Al-Mansoor",
      email = "tariq@novelite.app",
      bio = "Historical fiction enthusiast and courtroom mystery crafter. Unraveling secrets of the ancient Silk Road.",
      avatarUrl = "",
      joinedDate = "Apr 2024",
      followersCount = 7400,
      followingCount = 95,
      readingStreak = 27,
      writingStreak = 11,
      longestStreak = 30,
      streakShields = 1,
      isWriter = true,
      favoriteGenres = listOf("Historical Fiction", "Mystery", "Action")
    )
  )

  fun getInitialStories(): List<Story> = listOf(
    Story(
      id = "story_1",
      title = "Whispers in Lagos",
      authorId = "author_1",
      authorName = "Amara Okafor",
      authorUsername = "amara_okafor",
      description = "When investigative journalist Kemi discovers a coded ledger linking Lagos's wealthiest tech mogul to an underground art heist, she teams up with an enigmatic art appraiser who holds secrets of his own. Between rooftop galas in Ikoyi and midnight meetings in Lekki, danger has never felt so captivating.",
      coverDrawableRes = R.drawable.cover_lagos,
      coverImageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
      teaserVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
      teaserVideoDurationSec = 15,
      coverColorHex = 0xFF5C2D91,
      genre = "African Stories",
      tags = listOf("Lagos", "Romance", "Mystery", "Urban Crime", "Slow Burn"),
      status = StoryStatus.ONGOING,
      readsCount = 38400,
      likesCount = 9420,
      commentsCount = 1240,
      chaptersCount = 3,
      publishedDate = "2 weeks ago",
      lastUpdated = "Yesterday",
      isLiked = true,
      isInLibrary = true,
      libraryCategory = LibraryTab.CURRENTLY_READING,
      readingProgressPercent = 0.65f,
      chapters = listOf(
        Chapter(
          id = "c1_1",
          storyId = "story_1",
          chapterNumber = 1,
          title = "The Rooftop at Midnight",
          content = """
The Atlantic breeze off Victoria Island swept over the glass balcony, carrying the scent of salt water and roasted suya from the streets below. Kemi adjusted the strap of her clutch, feeling the hard rectangular edge of the encrypted flash drive nestled against the satin lining.

Across the crowded terrace, jazz notes drifted from a brass saxophone. Men in tailored agbadas and women in shimmering silk gowns clinked champagne flutes beneath glowing chandeliers. But Kemi wasn't here for the champagne.

"You're looking at him too intently, Miss Balogun," a low, velvety voice spoke beside her.

She turned sharply. The man stood with effortless poise, holding a glass of sparkling water. His charcoal jacket was impeccably cut, his dark eyes observing her with an unnerving mixture of amusement and scrutiny.

"And you are?" Kemi asked, keeping her chin raised.

"Tunde Adeleke. I evaluate rare antiquities for private collections," he murmured, stepping closer to the balustrade. "And right now, you are holding something far more volatile than the 16th-century bronze mask being unveiled inside."

Kemi's pulse leaped against her ribs. She hadn't shown the drive to anyone. "I think you have me mistaken for someone else, Mr. Adeleke."

"Do I?" Tunde tilted his head, the warm city lights reflecting in his eyes. "Because the men standing by the elevator are not security guards, Kemi. And in approximately three minutes, they will realize the ledger is no longer in Chief Otunba's private safe."

The terrace suddenly felt ten degrees colder. She glanced toward the double glass doors. Two hulking men in dark suits were scanning the crowd with earpieces humming.

"If you want to leave this building with your story and your breath," Tunde whispered, offering his arm, "pretend you find my jokes exquisitely charming and walk with me to the service lift."
          """.trimIndent(),
          wordsCount = 412,
          readsCount = 18200,
          likesCount = 4320,
          commentsCount = 512,
          publishedDate = "2 weeks ago"
        ),
        Chapter(
          id = "c1_2",
          storyId = "story_1",
          chapterNumber = 2,
          title = "A Tangled Ledger",
          content = """
The service lift shuddered as it descended toward the underground parking garage. The fluorescent lights overhead hummed with an uneven flicker.

Kemi pulled her arm away the instant the heavy steel doors slid shut. "Who are you really, Tunde? How did you know what was in that safe?"

Tunde leaned back against the steel wall, crossing his arms with maddening calmness. "Let's just say Chief Otunba acquired a ceremonial staff two years ago that belonged to my grandfather's estate in Osun. I wanted proof of where he bought it. You wanted proof of his offshore shell companies."

"So you used me as a distraction."

"I provided you with an opportunity," he corrected smoothly. "And now we both possess pieces of the same puzzle."

The lift reached the basement with a heavy ding. The doors parted to reveal a row of luxury vehicles beneath dim halogen lamps. At the end of the aisle, a sleek black vintage Mercedes sat idling, headlights casting long amber beams across the damp concrete.

"After you," Tunde said, gesturing toward the passenger side. "Lekki traffic is ruthless at one in the morning, but Otunba's hunters are considerably faster."

Kemi hesitated for a fraction of a second. Trusting a stranger was against every rule she had lived by as an investigative journalist. But looking into his calm, steady gaze, she knew one thing for certain: tonight was only the beginning of a storm that would shake Lagos to its foundations.
          """.trimIndent(),
          wordsCount = 380,
          readsCount = 12100,
          likesCount = 3100,
          commentsCount = 380,
          publishedDate = "1 week ago"
        ),
        Chapter(
          id = "c1_3",
          storyId = "story_1",
          chapterNumber = 3,
          title = "Shadows in Lekki",
          content = """
The safehouse sat nestled at the end of a quiet cul-de-sac in Lekki Phase 1, cloaked behind bougainvillea vines and tall wrought-iron gates. Rain began to patter against the tin roof as Tunde unlocked the heavy mahogany door.

Inside, the room smelled of old paper, leather, and fresh cedarwood. Blueprint schematics and high-resolution photographs of ancient bronze castings were pinned across a magnetic whiteboard.

Kemi plugged the drive into a rugged laptop. Lines of green encrypted hex code scrolled across the monitor before resolving into bank wire transfers, container manifest numbers, and shipping routes along the Gulf of Guinea.

"Look at this," Kemi pointed to a highlighted shipment scheduled for Friday. "The destination isn't London or Zurich. It's an unregistered dock right here in Badagry."

Tunde leaned over her shoulder, his proximity sending a quiet shiver down her spine. "The Lost Scepter of Oduduwa," he murmured, his voice laced with reverence and sorrow. "He's auctioning it on the black market to foreign oligarchs."

Kemi looked up from the screen, meeting his gaze in the dim glow of the laptop. "Then we aren't just writing an expose, are we? We're taking it back."

A slow, dangerous smile curved on Tunde's lips. "I was hoping you'd say that."
          """.trimIndent(),
          wordsCount = 350,
          readsCount = 8100,
          likesCount = 2000,
          commentsCount = 348,
          publishedDate = "Yesterday"
        )
      )
    ),

    Story(
      id = "story_2",
      title = "The Starlight Heir",
      authorId = "author_2",
      authorName = "Elena Rostova",
      authorUsername = "elena_rostova",
      description = "In the floating celestial kingdom of Aethelgard, heirs are marked by the constellations at their birth. When runaway blacksmith apprentice Lyra accidentally ignites an extinct supernova flame from an ancient relic blade, the High Council declares her the prophesied Starlight Heir. Now she must survive the cutthroat court politics of the Solar Nobles.",
      coverDrawableRes = R.drawable.cover_fantasy,
      coverImageUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&q=80",
      teaserVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
      teaserVideoDurationSec = 20,
      coverColorHex = 0xFF1A237E,
      genre = "Fantasy",
      tags = listOf("High Fantasy", "Magic", "Royalty", "Enemies to Lovers", "Epic"),
      status = StoryStatus.ONGOING,
      readsCount = 52300,
      likesCount = 14200,
      commentsCount = 2150,
      chaptersCount = 2,
      publishedDate = "1 month ago",
      lastUpdated = "3 days ago",
      isLiked = true,
      isInLibrary = true,
      libraryCategory = LibraryTab.FAVORITES,
      readingProgressPercent = 0.40f,
      chapters = listOf(
        Chapter(
          id = "c2_1",
          storyId = "story_2",
          chapterNumber = 1,
          title = "The Ember in the Anvil",
          content = """
For seventeen winters, Lyra believed her life would begin and end in the soot of the Iron Ward. Her hands were scarred from molten copper, her apron stained with ash. While the nobles of Aethelgard walked on bridges woven of woven aurora light, she hammered plowshares for wheat farmers.

Until the night a dying knight stumbled through the forge door, collapsing against the anvil with an ebony scabbard strapped to his chest.

"Do not let the Eclipse Vanguard touch it," the knight wheezed, blood foaming on his cracked lips. "The sky... the sky is bleeding, child."

Before she could call for the apothecary, the knight drew his final breath. Hesitantly, Lyra reached out to unbuckle the blade. The moment her bare fingers brushed the hilt, a shockwave of violet fire detonated across the forge.

The ceiling timbers groaned. A column of celestial light pierced straight through the storm clouds above, illuminating the crest of the Seven-Pointed Nova burning brightly upon her palm.
          """.trimIndent(),
          wordsCount = 340,
          readsCount = 32100,
          likesCount = 8900,
          commentsCount = 1120,
          publishedDate = "1 month ago"
        ),
        Chapter(
          id = "c2_2",
          storyId = "story_2",
          chapterNumber = 2,
          title = "The Court of Whispering Mirrors",
          content = """
The palace of the Solar Court was constructed entirely of polished moonstone and suspended crystal waterfalls. Every whisper echoed across the grand atrium like chime bells in a gale.

Flanked by royal guards clad in silver mail, Lyra walked down the mirrored aisle. Her rough blacksmith linen had been replaced with deep indigo velvet embroidered with constellation threads that pulsed in rhythm with her heartbeat.

At the dais sat Prince Cassian. His eyes were the color of winter frost, unreadable and lethal. At his hip hung the twin blade to her own.

"They tell me an orphan from the lower rings holds the Flame of the First Star," Cassian said, descending the marble steps. His voice carried across the silent assembly. "Draw your blade, apprentice. Let us see if your starlight burns or merely flickers."
          """.trimIndent(),
          wordsCount = 310,
          readsCount = 20200,
          likesCount = 5300,
          commentsCount = 1030,
          publishedDate = "3 days ago"
        )
      )
    ),

    Story(
      id = "story_3",
      title = "Neon Paradox",
      authorId = "author_3",
      authorName = "Zane Sterling",
      authorUsername = "zane_sterling",
      description = "Neo-Shinjuku, 2094. Memory merchant Jax sells high-grade sensory memories to the elite. But when a dying synthetic agent uploads an encrypted memory file of a murder that hasn't happened yet, Jax becomes the prime target of both the MegaCorp Syndicate and an elusive rogue AI.",
      coverDrawableRes = R.drawable.cover_neon,
      coverColorHex = 0xFF0D47A1,
      genre = "Science Fiction",
      tags = listOf("Cyberpunk", "AI", "Noir", "Mystery", "Action"),
      status = StoryStatus.ONGOING,
      readsCount = 24100,
      likesCount = 6800,
      commentsCount = 760,
      chaptersCount = 2,
      publishedDate = "3 weeks ago",
      lastUpdated = "5 days ago",
      isLiked = false,
      isInLibrary = false,
      chapters = listOf(
        Chapter(
          id = "c3_1",
          storyId = "story_3",
          chapterNumber = 1,
          title = "Memory Leak 0x8F",
          content = """
Rain in Sector 4 always tasted like ozone and synthetic petroleum. Jax adjusted the neural visor over his cybernetic left eye, filtering out the blinding hologram advertisements for anti-aging stimulants and flying hover-cabs.

In the back alley behind the noodle bar, steam hissed from broken exhaust pipes.

"You're Jax?" The voice was distorted through an artificial vocoder. A figure wrapped in a thermal cloak pressed against the wet brick wall. Blue coolant dripped from a ragged puncture wound in their synthetic chest.

"Depends on who's asking and how many credits you're carrying," Jax replied, keeping his hand near the shock-pistol in his trench coat.

"No credits. Something priceless," the android gasped. A silver neural jack snapped out from their wrist, locking violently into the universal port on Jax's neck interface before he could pull away.

Data flooded his cortex like liquid nitrogen. In an instant, Jax saw a panoramic view of the Pinnacle Tower boardroom. The clock on the wall read: *Tomorrow, 11:42 PM*. And lying across the glass table in a pool of synthetic blood was the CEO of OmniCorp.
          """.trimIndent(),
          wordsCount = 330,
          readsCount = 14500,
          likesCount = 4100,
          commentsCount = 420,
          publishedDate = "3 weeks ago"
        ),
        Chapter(
          id = "c3_2",
          storyId = "story_3",
          chapterNumber = 2,
          title = "Ghosts in the Circuitry",
          content = """
Jax severed the neural uplink with a violent jerk, falling to his knees as nausea roiled through his gut. The android collapsed forward into the alley puddle, their optic lights dying into dull gray glass.

Sirens wailed in the distance. The sharp red beams of police drones cut through the smog above the skyline.

*Twenty-four hours,* Jax thought, scrambling down the fire escape toward the subway tunnel. *I have twenty-four hours before a murder I just witnessed comes to pass.*
          """.trimIndent(),
          wordsCount = 210,
          readsCount = 9600,
          likesCount = 2700,
          commentsCount = 340,
          publishedDate = "5 days ago"
        )
      )
    ),

    Story(
      id = "story_4",
      title = "Coffee & Stolen Glances",
      authorId = "author_6",
      authorName = "Chloe Dubois",
      authorUsername = "chloe_dubois",
      description = "Mila runs a cozy botanical book cafe in downtown Seattle. Every morning at precisely 7:45 AM, an architectural illustrator named Julian orders a double cortado and sketches the morning regulars in his moleskine notebook. When Julian accidentally leaves his sketchbook behind, Mila finds her own face filling every single page.",
      coverDrawableRes = null,
      coverColorHex = 0xFF6D4C41,
      genre = "Romance",
      tags = listOf("Contemporary", "Coffee Shop", "Sweet", "Slow Burn", "Artists"),
      status = StoryStatus.COMPLETED,
      readsCount = 48200,
      likesCount = 16800,
      commentsCount = 3200,
      chaptersCount = 2,
      publishedDate = "2 months ago",
      lastUpdated = "Completed",
      isLiked = true,
      isInLibrary = true,
      libraryCategory = LibraryTab.COMPLETED,
      readingProgressPercent = 1.0f,
      chapters = listOf(
        Chapter(
          id = "c4_1",
          storyId = "story_4",
          chapterNumber = 1,
          title = "The Cortado at 7:45",
          content = """
The hiss of the espresso machine was the rhythm of Mila's morning. The scent of roasted Ethiopian beans and cinnamon scones filled the corner of 4th and Pine as autumn rain tapped against the glass windows.

At 7:44 AM, the brass bell above the door chimed.

Mila didn't even have to look up. "Double cortado, oat milk, dash of nutmeg?"

Julian lowered his umbrella, his dark wool coat carrying the crisp morning air. A hesitant, dimpled smile touched his lips. "You're getting dangerously good at predicting my routine, Mila."

"You've ordered the exact same thing for ninety-four consecutive mornings, Julian. A barista notices."
          """.trimIndent(),
          wordsCount = 220,
          readsCount = 28000,
          likesCount = 9800,
          commentsCount = 1800,
          publishedDate = "2 months ago"
        ),
        Chapter(
          id = "c4_2",
          storyId = "story_4",
          chapterNumber = 2,
          title = "Page Forty-Two",
          content = """
When the evening rush died down and the cafe lights were dimmed to a warm amber glow, Mila wiped down the oak tables. Under the corner booth near the fern pot, a thick black leather journal lay forgotten.

Mila picked it up with the intention of placing it in the lost-and-found basket. But as she picked it up, the elastic band slipped, and the notebook fell open.

Charcoal sketches filled the textured cream paper.
Mila laughing while steaming milk. Mila tucking a strand of curly hair behind her ear while arranging paperbacks on the display shelf. Mila gazing out at the rain with a quiet, thoughtful smile.

In the margin of the final page, written in clean architectural print: *The most beautiful part of every morning.*
          """.trimIndent(),
          wordsCount = 260,
          readsCount = 20200,
          likesCount = 7000,
          commentsCount = 1400,
          publishedDate = "2 months ago"
        )
      )
    ),

    Story(
      id = "story_5",
      title = "Daughters of the Sun and River",
      authorId = "author_4",
      authorName = "Aisha Bello",
      authorUsername = "aisha_bello",
      description = "An atmospheric Yoruba mythological fantasy set in modern-day Ibadan. Twin sisters born under the blessing of Oshun and Oya discover their ancestral powers when ancient river spirits begin awakening across the Niger basin, demanding the return of a stolen bronze crown.",
      coverDrawableRes = null,
      coverColorHex = 0xFFD84315,
      genre = "African Stories",
      tags = listOf("Mythology", "Yoruba", "Sisters", "Magic", "Adventure"),
      status = StoryStatus.ONGOING,
      readsCount = 29500,
      likesCount = 8100,
      commentsCount = 990,
      chaptersCount = 1,
      publishedDate = "3 weeks ago",
      lastUpdated = "4 days ago",
      chapters = listOf(
        Chapter(
          id = "c5_1",
          storyId = "story_5",
          chapterNumber = 1,
          title = "When Waters Call",
          content = """
The river Osun did not flow like ordinary water. On the night of the crescent moon, it sang.

Yetunde stood barefoot on the red clay bank, watching the golden ripples reflect the canopy of ancient mahogany trees. In the palms of her hands, small droplets of water rose upward in defiance of gravity, dancing like suspended pearls.

From the tree line, her twin sister Taiwo stepped onto the grass, accompanied by a sudden gust of wind that rustled the dry palm fronds.

"You feel it too," Taiwo said softly, her eyes flashing with amber lightning. "The shrine keepers were wrong. The river didn't put the spirits to sleep. It was only waiting for us."
          """.trimIndent(),
          wordsCount = 240,
          readsCount = 29500,
          likesCount = 8100,
          commentsCount = 990,
          publishedDate = "3 weeks ago"
        )
      )
    ),

    Story(
      id = "story_6",
      title = "The Echoes of Blackwood Manor",
      authorId = "author_7",
      authorName = "Brandon Cole",
      authorUsername = "brandon_cole",
      description = "Hired to catalogue the vast antique library of an eccentric reclusive collector in the foggy hills of Vermont, archivist Noah realizes the portrait subjects in the hallway change their expressions whenever the clock strikes 3:00 AM.",
      coverDrawableRes = null,
      coverColorHex = 0xFF263238,
      genre = "Thriller",
      tags = listOf("Gothic", "Psychological", "Mystery", "Haunted Library", "Suspense"),
      status = StoryStatus.ONGOING,
      readsCount = 19800,
      likesCount = 5400,
      commentsCount = 610,
      chaptersCount = 1,
      publishedDate = "1 month ago",
      lastUpdated = "1 week ago",
      chapters = listOf(
        Chapter(
          id = "c6_1",
          storyId = "story_6",
          chapterNumber = 1,
          title = "The Midnight Catalogue",
          content = """
Blackwood Manor loomed against the stormy New England sky like a stone sentinel. Its gothic spires pierced the mist, and the tall arched windows were dark as obsidian.

Noah placed his briefcase on the heavy oak desk of the east wing library. Over forty thousand volumes of rare occult manuscripts and genealogical treaties surrounded him in spiraling floor-to-ceiling rotunda shelves.

As the grandfather clock chimed three resonant gongs, a whisper brushed past his neck. Noah turned around. The oil painting of Lady Eleanor Blackwood from 1892, which had depicted her with closed, solemn eyes when he entered, was now staring directly at him with a painted tear rolling down her cheek.
          """.trimIndent(),
          wordsCount = 260,
          readsCount = 19800,
          likesCount = 5400,
          commentsCount = 610,
          publishedDate = "1 month ago"
        )
      )
    ),

    Story(
      id = "story_7",
      title = "Chasing Supernovas",
      authorId = "author_5",
      authorName = "Liam Vance",
      authorUsername = "liam_vance",
      description = "High school astronomy nerd Ethan and rebellious track star Maya team up to win a national astrophysics observatory competition by mapping a rogue comet that only passes Earth once every eighty years.",
      coverDrawableRes = null,
      coverColorHex = 0xFF4A148C,
      genre = "Young Adult",
      tags = listOf("YA", "Astronomy", "Road Trip", "Friendship", "Coming of Age"),
      status = StoryStatus.COMPLETED,
      readsCount = 34100,
      likesCount = 11200,
      commentsCount = 1840,
      chaptersCount = 1,
      publishedDate = "3 months ago",
      lastUpdated = "Completed",
      chapters = listOf(
        Chapter(
          id = "c7_1",
          storyId = "story_7",
          chapterNumber = 1,
          title = "The Roof of Observatory Hill",
          content = """
The telescope lens clicked into focus. Through the eyepiece, the faint azure tail of Comet C/2026 glowed against the velvet void of space.

"Tell me again why we're breaking school curfew on a freezing Tuesday?" Maya whispered, shivering inside her oversized vintage denim jacket as she sat on the observatory railing.

"Because in seventy-two hours, Maya, that comet enters the sun's shadow and we won't see it again until we're ninety-six years old," Ethan replied without lifting his eye from the finder scope.

Maya looked up at the sea of stars above them. "Then we'd better not miss it."
          """.trimIndent(),
          wordsCount = 210,
          readsCount = 34100,
          likesCount = 11200,
          commentsCount = 1840,
          publishedDate = "3 months ago"
        )
      )
    ),

    Story(
      id = "story_8",
      title = "The Midnight Alibi",
      authorId = "author_8",
      authorName = "Tariq Al-Mansoor",
      authorUsername = "tariq_almansoor",
      description = "A high-stakes courtroom mystery. When a renowned defense attorney is framed for the murder of his own star witness in a locked penthouse suite, his junior associate has forty-eight hours to reconstruct the crime scene before the jury delivers a verdict.",
      coverDrawableRes = null,
      coverColorHex = 0xFF37474F,
      genre = "Mystery",
      tags = listOf("Courtroom", "Whodunit", "Legal Thriller", "Twists"),
      status = StoryStatus.ONGOING,
      readsCount = 15900,
      likesCount = 4200,
      commentsCount = 530,
      chaptersCount = 1,
      publishedDate = "2 weeks ago",
      lastUpdated = "6 days ago",
      chapters = listOf(
        Chapter(
          id = "c8_1",
          storyId = "story_8",
          chapterNumber = 1,
          title = "The Gavel Strikes",
          content = """
The mahogany courtroom was suffocatingly quiet. Every camera lens in the gallery was pointed at the defense table.

"Ladies and gentlemen of the jury," prosecutor Harrison began, leaning on the balustrade. "The victim was found inside a room with keycard access granted exclusively to one man."

Junior attorney Zoya leaned in toward her mentor. "He has the security logs, Tariq. But he doesn't know about the ventilation shaft behind the server room. Give me until tomorrow morning to prove it."
          """.trimIndent(),
          wordsCount = 190,
          readsCount = 15900,
          likesCount = 4200,
          commentsCount = 530,
          publishedDate = "2 weeks ago"
        )
      )
    ),

    Story(
      id = "story_9",
      title = "Constellations in My Bones",
      authorId = "author_2",
      authorName = "Elena Rostova",
      authorUsername = "elena_rostova",
      description = "A collection of poignant, luminous prose poetry about longing, starlight, resilience, and the quiet quiet beauty of healing.",
      coverDrawableRes = null,
      coverColorHex = 0xFF4527A0,
      genre = "Poetry",
      tags = listOf("Poetry", "Emotional", "Healing", "Cosmic"),
      status = StoryStatus.COMPLETED,
      readsCount = 18400,
      likesCount = 7600,
      commentsCount = 920,
      chaptersCount = 1,
      publishedDate = "4 months ago",
      lastUpdated = "Completed",
      chapters = listOf(
        Chapter(
          id = "c9_1",
          storyId = "story_9",
          chapterNumber = 1,
          title = "Canto I: Supernova",
          content = """
You are not made of fragile clay.
You are the remnants of collapsed stars
that refused to die in silence.

Carry your light with defiance;
even the deepest abyss of the universe
trembles before a single burning ember.
          """.trimIndent(),
          wordsCount = 120,
          readsCount = 18400,
          likesCount = 7600,
          commentsCount = 920,
          publishedDate = "4 months ago"
        )
      )
    ),

    Story(
      id = "story_10",
      title = "How Not to Summon a Demon Roommate",
      authorId = "author_6",
      authorName = "Chloe Dubois",
      authorUsername = "chloe_dubois",
      description = "When college freshman Toby tries to light a scented lavender candle during finals week, he accidentally mispronounces Latin vocabulary notes and summons Malakor, an infernal lord of shadows who now refuses to leave their dorm room unless Toby teaches him how to play Mario Kart.",
      coverDrawableRes = null,
      coverColorHex = 0xFF880E4F,
      genre = "Comedy",
      tags = listOf("Humor", "Urban Fantasy", "College", "Demons", "Witty"),
      status = StoryStatus.ONGOING,
      readsCount = 41200,
      likesCount = 15300,
      commentsCount = 2890,
      chaptersCount = 1,
      publishedDate = "1 month ago",
      lastUpdated = "Yesterday",
      chapters = listOf(
        Chapter(
          id = "c10_1",
          storyId = "story_10",
          chapterNumber = 1,
          title = "The Lavender Incident",
          content = """
"I specifically asked for the non-cursed vanilla bean candle from Target," Toby groaned, staring at the seven-foot demon currently eating his pizza rolls on the bunk bed.

Malakor adjusted his flaming crimson horns and wiped his clawed hands on a paper napkin. "Your pronunciation of 'quid pro quo' was atrocious, mortal. However, your microwave treats are surprisingly tolerable."
          """.trimIndent(),
          wordsCount = 160,
          readsCount = 41200,
          likesCount = 15300,
          commentsCount = 2890,
          publishedDate = "1 month ago"
        )
      )
    )
  )

  fun getInitialComments(): List<Comment> = listOf(
    Comment(
      id = "comm_1",
      storyId = "story_1",
      chapterId = "c1_1",
      userId = "user_reader_1",
      userName = "Chioma_Reads",
      text = "The tension between Kemi and Tunde on that rooftop balcony gave me absolute chills! 🔥 Amara's writing never misses!",
      timestamp = "2 hours ago",
      likes = 142,
      isLiked = true,
      inlineQuote = "The terrace suddenly felt ten degrees colder."
    ),
    Comment(
      id = "comm_2",
      storyId = "story_1",
      chapterId = "c1_1",
      userId = "user_reader_2",
      userName = "Kwame_Bookworm",
      text = "The Lagos nightlife atmosphere is so richly described. I could literally smell the suya and hear the sax! Need chapter 4 ASAP!",
      timestamp = "5 hours ago",
      likes = 89,
      replies = listOf(
        CommentReply(
          id = "rep_1",
          commentId = "comm_2",
          userId = "author_1",
          userName = "Amara Okafor",
          text = "Chapter 4 is dropping this Friday! Thank you so much for the love Kwame! ❤️",
          timestamp = "3 hours ago"
        )
      )
    ),
    Comment(
      id = "comm_3",
      storyId = "story_2",
      chapterId = "c2_1",
      userId = "user_reader_3",
      userName = "StarGazer99",
      text = "The worldbuilding in Aethelgard is top tier! Lyra's powers awakening on the anvil was pure cinematic bliss.",
      timestamp = "1 day ago",
      likes = 210,
      inlineQuote = "A column of celestial light pierced straight through the storm clouds"
    )
  )

  fun getInitialAchievements(): List<Achievement> = listOf(
    Achievement(
      id = "ach_1",
      title = "Bookworm",
      description = "Complete 10 reading sessions",
      icon = "📖",
      category = AchievementCategory.READING,
      currentProgress = 10,
      maxProgress = 10,
      isUnlocked = true,
      unlockedDate = "Yesterday"
    ),
    Achievement(
      id = "ach_2",
      title = "Streak Master",
      description = "Reach a 14-day reading streak",
      icon = "🔥",
      category = AchievementCategory.READING,
      currentProgress = 12,
      maxProgress = 14,
      isUnlocked = false
    ),
    Achievement(
      id = "ach_3",
      title = "Night Reader",
      description = "Read a chapter between 10 PM and 3 AM",
      icon = "🌙",
      category = AchievementCategory.READING,
      currentProgress = 1,
      maxProgress = 1,
      isUnlocked = true,
      unlockedDate = "3 days ago"
    ),
    Achievement(
      id = "ach_4",
      title = "Early Reader",
      description = "Complete daily reading goal before 8 AM",
      icon = "🌅",
      category = AchievementCategory.READING,
      currentProgress = 0,
      maxProgress = 1,
      isUnlocked = false
    ),
    Achievement(
      id = "ach_5",
      title = "First Story",
      description = "Publish your very first story on Novelite",
      icon = "✍️",
      category = AchievementCategory.WRITING,
      currentProgress = 1,
      maxProgress = 1,
      isUnlocked = true,
      unlockedDate = "Last week"
    ),
    Achievement(
      id = "ach_6",
      title = "First Chapter",
      description = "Write and publish your first chapter",
      icon = "📝",
      category = AchievementCategory.WRITING,
      currentProgress = 1,
      maxProgress = 1,
      isUnlocked = true,
      unlockedDate = "Last week"
    ),
    Achievement(
      id = "ach_7",
      title = "Consistent Writer",
      description = "Maintain a 7-day writing streak",
      icon = "🔥",
      category = AchievementCategory.WRITING,
      currentProgress = 5,
      maxProgress = 7,
      isUnlocked = false
    ),
    Achievement(
      id = "ach_8",
      title = "1,000 Reads",
      description = "Accumulate 1,000 reads on your published stories",
      icon = "📈",
      category = AchievementCategory.WRITING,
      currentProgress = 720,
      maxProgress = 1000,
      isUnlocked = false
    ),
    Achievement(
      id = "ach_9",
      title = "Conversation Starter",
      description = "Leave 5 comments on chapters you read",
      icon = "💬",
      category = AchievementCategory.COMMUNITY,
      currentProgress = 5,
      maxProgress = 5,
      isUnlocked = true,
      unlockedDate = "2 days ago"
    ),
    Achievement(
      id = "ach_10",
      title = "Supportive Reader",
      description = "Like 20 chapters to encourage authors",
      icon = "❤️",
      category = AchievementCategory.COMMUNITY,
      currentProgress = 16,
      maxProgress = 20,
      isUnlocked = false
    )
  )

  fun getInitialChallenges(): List<Challenge> = listOf(
    Challenge(
      id = "chal_1",
      title = "7-Day Reading Streak Sprint",
      description = "Keep your reading habit alive every day this week to earn the Golden Flame Badge.",
      rewardBadge = "🔥 Golden Flame",
      icon = "🔥",
      targetValue = 7,
      currentProgress = 5,
      isCompleted = false,
      daysRemaining = 2
    ),
    Challenge(
      id = "chal_2",
      title = "Finish a Story Challenge",
      description = "Read all chapters of any completed novel in the library.",
      rewardBadge = "🏆 Avid Finisher",
      icon = "📚",
      targetValue = 1,
      currentProgress = 1,
      isCompleted = true,
      daysRemaining = 0
    ),
    Challenge(
      id = "chal_3",
      title = "30-Day Habit Mastery",
      description = "Reach 30 consecutive days of meeting your daily reading goal.",
      rewardBadge = "👑 Reading Royalty",
      icon = "👑",
      targetValue = 30,
      currentProgress = 12,
      isCompleted = false,
      daysRemaining = 18
    ),
    Challenge(
      id = "chal_4",
      title = "Write 5,000 Words",
      description = "Draft new words in the Writing Studio to boost your creative flow.",
      rewardBadge = "✍️ Quill Master",
      icon = "✍️",
      targetValue = 5000,
      currentProgress = 3200,
      isCompleted = false,
      daysRemaining = 6
    )
  )

  fun getInitialNotifications(): List<NoveliteNotification> = listOf(
    NoveliteNotification(
      id = "notif_1",
      title = "Keep Your Streak Alive! 🔥",
      message = "Don't lose your 12-day streak! Read for 2 more minutes today to complete your goal.",
      timeAgo = "1 hour ago",
      type = NotificationType.STREAK_REMINDER,
      isRead = false
    ),
    NoveliteNotification(
      id = "notif_2",
      title = "New Chapter from Amara Okafor 📖",
      message = "Whispers in Lagos just published Chapter 3: 'Shadows in Lekki'. Dive back into the mystery!",
      timeAgo = "3 hours ago",
      type = NotificationType.NEW_CHAPTER,
      isRead = false
    ),
    NoveliteNotification(
      id = "notif_3",
      title = "Elena Rostova liked your comment ❤️",
      message = "The author reacted to your review on 'The Starlight Heir'.",
      timeAgo = "1 day ago",
      type = NotificationType.LIKE,
      isRead = true
    ),
    NoveliteNotification(
      id = "notif_4",
      title = "Streak Shield Active 🛡️",
      message = "You earned a free Streak Shield for completing the 10-day milestone!",
      timeAgo = "2 days ago",
      type = NotificationType.SHIELD,
      isRead = true
    ),
    NoveliteNotification(
      id = "notif_5",
      title = "New Follower 🎉",
      message = "Kwame_Bookworm started following your writing profile.",
      timeAgo = "3 days ago",
      type = NotificationType.FOLLOWER,
      isRead = true
    )
  )

  fun getInitialAuthorStatuses(): List<AuthorStatus> = listOf(
    AuthorStatus(
      id = "status_1",
      authorId = "author_1",
      authorName = "Amara Okafor",
      authorUsername = "amara_okafor",
      statusText = "Chapter 4 of Whispers in Lagos is finally drafted! Prepare yourselves for the most intense rooftop scene yet. Dropping Friday! 📖✨",
      storyId = "story_1",
      storyTitle = "Whispers in Lagos",
      mediaUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
      mediaType = "IMAGE",
      timestamp = "2 hours ago",
      likesCount = 84,
      isLiked = true
    ),
    AuthorStatus(
      id = "status_2",
      authorId = "author_2",
      authorName = "Elena Rostova",
      authorUsername = "elena_rostova",
      statusText = "Sneak peek of the celestial court teaser trailer! The Solar Nobles won't know what hit them in the next arc! ☕👑✨",
      storyId = "story_2",
      storyTitle = "The Starlight Heir",
      mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
      mediaType = "VIDEO",
      timestamp = "5 hours ago",
      likesCount = 142,
      isLiked = false
    ),
    AuthorStatus(
      id = "status_3",
      authorId = "author_4",
      authorName = "Aisha Bello",
      authorUsername = "aisha_bello",
      statusText = "Sending blessings from the river banks! Chapter 2 of Daughters of the Sun and River is in editing. What was your favorite folklore moment so far? 🌊✨",
      storyId = "story_5",
      storyTitle = "Daughters of the Sun and River",
      mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
      mediaType = "IMAGE",
      timestamp = "1 day ago",
      likesCount = 67,
      isLiked = true
    ),
    AuthorStatus(
      id = "status_4",
      authorId = "author_6",
      authorName = "Chloe Dubois",
      authorUsername = "chloe_dubois",
      statusText = "Coffee & Stolen Glances reached 48k reads! Thank you so much to every single reader leaving footprints on my pages! You make my day ❤️☕",
      storyId = "story_4",
      storyTitle = "Coffee & Stolen Glances",
      mediaUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&q=80",
      mediaType = "IMAGE",
      timestamp = "2 days ago",
      likesCount = 210,
      isLiked = false
    )
  )
}
