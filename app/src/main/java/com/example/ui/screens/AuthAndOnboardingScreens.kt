package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserRole
import com.example.ui.NoveliteViewModel
import com.example.ui.components.NoveliteLogo
import com.example.ui.components.NoveliteLogoSymbol
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
fun AuthScreen(viewModel: NoveliteViewModel) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Login, 1: Sign Up, 2: Forgot Password

  var username by remember { mutableStateOf("aurora_reads") }
  var email by remember { mutableStateOf("aurora@novelite.app") }
  var password by remember { mutableStateOf("password123") }
  var passwordVisible by remember { mutableStateOf(false) }
  var resetEmailSent by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 36.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // Logo + Brand Header
      NoveliteLogoSymbol(size = 56.dp)

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "NOVELITE",
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        color = NoveliteDarkBrown
      )

      Text(
        text = "Read. Write. Connect. Keep Your Streak Alive.",
        style = MaterialTheme.typography.bodySmall,
        color = NoveliteWarmBrown,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Auth Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("auth_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          // Tab Header
          TabRow(
            selectedTabIndex = selectedTab,
            containerColor = NoveliteCardBeige,
            contentColor = NoveliteDarkBrown,
            indicator = { tabPositions ->
              TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                color = NoveliteDarkBrown
              )
            }
          ) {
            Tab(
              selected = selectedTab == 0,
              onClick = { selectedTab = 0 },
              text = { Text("Log In", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) NoveliteDarkBrown else NoveliteTextMuted) }
            )
            Tab(
              selected = selectedTab == 1,
              onClick = { selectedTab = 1 },
              text = { Text("Sign Up", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) NoveliteDarkBrown else NoveliteTextMuted) }
            )
            Tab(
              selected = selectedTab == 2,
              onClick = { selectedTab = 2 },
              text = { Text("Reset", fontWeight = FontWeight.Bold, color = if (selectedTab == 2) NoveliteDarkBrown else NoveliteTextMuted) }
            )
          }

          Spacer(modifier = Modifier.height(24.dp))

          when (selectedTab) {
            0 -> {
              // LOGIN
              OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username or Email") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NoveliteDarkBrown) },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_username_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NoveliteDarkBrown,
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteTextPrimary,
                  unfocusedTextColor = NoveliteTextPrimary
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(14.dp))

              OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NoveliteDarkBrown) },
                trailingIcon = {
                  IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                      imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                      contentDescription = null,
                      tint = NoveliteTextMuted
                    )
                  }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_password_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NoveliteDarkBrown,
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteTextPrimary,
                  unfocusedTextColor = NoveliteTextPrimary
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                TextButton(onClick = { selectedTab = 2 }) {
                  Text("Forgot Password?", color = NoveliteWarmBrown, fontSize = 12.sp)
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("login_submit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = NoveliteDarkBrown,
                  contentColor = NoveliteWhite
                )
              ) {
                Text("Log In to Novelite", fontWeight = FontWeight.Bold, fontSize = 15.sp)
              }
            }

            1 -> {
              // SIGN UP
              OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Choose a Username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NoveliteDarkBrown) },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_username_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NoveliteDarkBrown,
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteTextPrimary,
                  unfocusedTextColor = NoveliteTextPrimary
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NoveliteDarkBrown) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_email_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NoveliteDarkBrown,
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteTextPrimary,
                  unfocusedTextColor = NoveliteTextPrimary
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Create Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NoveliteDarkBrown) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_password_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NoveliteDarkBrown,
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteTextPrimary,
                  unfocusedTextColor = NoveliteTextPrimary
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(20.dp))

              Button(
                onClick = { viewModel.signup(username, email, password) },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("signup_submit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = NoveliteDarkBrown,
                  contentColor = NoveliteWhite
                )
              ) {
                Text("Create Account & Start Streak 🔥", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }
            }

            2 -> {
              // FORGOT PASSWORD
              Text(
                text = "Enter your email to receive a password reset link.",
                style = MaterialTheme.typography.bodyMedium,
                color = NoveliteTextMuted
              )

              Spacer(modifier = Modifier.height(16.dp))

              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Your Registered Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NoveliteDarkBrown) },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("reset_email_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = NoveliteDarkBrown,
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteTextPrimary,
                  unfocusedTextColor = NoveliteTextPrimary
                )
              )

              Spacer(modifier = Modifier.height(16.dp))

              if (resetEmailSent) {
                Surface(
                  color = NoveliteCreamBg,
                  shape = RoundedCornerShape(12.dp),
                  border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "✅ Password reset instructions sent to $email!",
                    color = NoveliteDarkBrown,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Spacer(modifier = Modifier.height(16.dp))
              }

              Button(
                onClick = { resetEmailSent = true },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("reset_submit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = NoveliteDarkBrown,
                  contentColor = NoveliteWhite
                )
              ) {
                Text("Send Reset Link", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "Every story deserves to be lived.\nEvery day deserves a chapter.",
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.bodySmall,
        color = NoveliteTextMuted,
        textAlign = TextAlign.Center
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: NoveliteViewModel) {
  var selectedRole by remember { mutableStateOf(UserRole.BOTH) }
  val selectedGenres = remember {
    mutableStateListOf("Romance", "Fantasy", "African Stories", "Mystery")
  }
  var selectedGoalMinutes by remember { mutableStateOf(10) }

  val allGenres = listOf(
    "Romance", "Fantasy", "African Stories", "Mystery", "Thriller",
    "Science Fiction", "Drama", "Action", "Adventure", "Young Adult",
    "Historical Fiction", "Poetry", "Comedy", "Horror"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Welcome to Novelite",
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = NoveliteTextPrimary
      )

      Text(
        text = "Personalize your literary journey",
        style = MaterialTheme.typography.bodyMedium,
        color = NoveliteWarmBrown
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Step 1: Role Selection
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "1. How will you use Novelite?",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            RoleChoiceButton(
              title = "Read",
              icon = Icons.Default.AutoStories,
              selected = selectedRole == UserRole.READER,
              onClick = { selectedRole = UserRole.READER },
              modifier = Modifier.weight(1f)
            )
            RoleChoiceButton(
              title = "Write",
              icon = Icons.Default.Create,
              selected = selectedRole == UserRole.WRITER,
              onClick = { selectedRole = UserRole.WRITER },
              modifier = Modifier.weight(1f)
            )
            RoleChoiceButton(
              title = "Both 🔥",
              icon = Icons.Default.LocalFireDepartment,
              selected = selectedRole == UserRole.BOTH,
              onClick = { selectedRole = UserRole.BOTH },
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Step 2: Genres
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "2. Select your favorite genres",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
          Text(
            text = "Pick 3 or more for tailored recommendations",
            style = MaterialTheme.typography.bodySmall,
            color = NoveliteTextMuted
          )

          Spacer(modifier = Modifier.height(12.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            allGenres.forEach { genre ->
              val isSelected = selectedGenres.contains(genre)
              FilterChip(
                selected = isSelected,
                onClick = {
                  if (isSelected) selectedGenres.remove(genre) else selectedGenres.add(genre)
                },
                label = { Text(genre, fontSize = 12.sp) },
                leadingIcon = if (isSelected) {
                  { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = NoveliteDarkBrown,
                  selectedLabelColor = NoveliteWhite,
                  containerColor = NoveliteCreamBg,
                  labelColor = NoveliteTextPrimary
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Step 3: Daily Reading Goal
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NoveliteCardBeige),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "3. Choose your daily reading goal",
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NoveliteTextPrimary
          )
          Text(
            text = "Achieve this daily to maintain your reading streak! 🔥",
            style = MaterialTheme.typography.bodySmall,
            color = NoveliteWarmBrown
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(5, 10, 20, 30).forEach { mins ->
              val isSelected = selectedGoalMinutes == mins
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { selectedGoalMinutes = mins }
                  .border(
                    1.dp,
                    if (isSelected) NoveliteDarkBrown else NoveliteBorder,
                    RoundedCornerShape(12.dp)
                  ),
                color = if (isSelected) NoveliteDarkBrown else NoveliteCreamBg
              ) {
                Column(
                  modifier = Modifier.padding(vertical = 12.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(
                    text = "$mins",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isSelected) NoveliteWhite else NoveliteTextPrimary
                  )
                  Text(
                    text = "mins/day",
                    fontSize = 10.sp,
                    color = if (isSelected) NoveliteCaramel else NoveliteTextMuted
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      Button(
        onClick = {
          viewModel.completeOnboarding(selectedRole, selectedGenres.toList(), selectedGoalMinutes)
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("onboarding_finish_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = NoveliteDarkBrown,
          contentColor = NoveliteWhite
        )
      ) {
        Text("Begin My Journey 🔥", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }
    }
  }
}

@Composable
private fun RoleChoiceButton(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .border(
        1.dp,
        if (selected) NoveliteDarkBrown else NoveliteBorder,
        RoundedCornerShape(12.dp)
      ),
    color = if (selected) NoveliteDarkBrown else NoveliteCreamBg
  ) {
    Column(
      modifier = Modifier.padding(vertical = 10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (selected) NoveliteCaramel else NoveliteTextMuted,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = if (selected) NoveliteWhite else NoveliteTextPrimary
      )
    }
  }
}
