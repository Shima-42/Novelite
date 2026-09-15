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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.widthIn
import com.example.ui.components.ResponsiveScreenContainer
import com.example.data.UserRole
import com.example.ui.NoveliteViewModel
import com.example.ui.components.NoveliteButton
import com.example.ui.components.NoveliteButtonStyle
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
fun AuthScreen(viewModel: NoveliteViewModel, initialTab: Int = 0) {
  var selectedTab by remember { mutableStateOf(initialTab) } // 0: Login, 1: Sign Up, 2: Forgot Password

  var fullName by remember { mutableStateOf("") }
  var username by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var rememberMe by remember { mutableStateOf(true) }
  var passwordVisible by remember { mutableStateOf(false) }
  var resetEmailSent by remember { mutableStateOf(false) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  ResponsiveScreenContainer(
    modifier = Modifier
      .fillMaxSize()
      .background(NoveliteCreamBg)
  ) { isWideScreen, horizontalPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = horizontalPadding, vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // Logo + Brand Header
      NoveliteLogoSymbol(size = 56.dp)

      Spacer(modifier = Modifier.height(12.dp))

      com.example.ui.components.LiteraryIllustration()

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "NOVELITE",
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.sp,
        color = NoveliteDarkBrown
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = when (selectedTab) {
          0 -> "Welcome back to your stories."
          1 -> "Begin your story."
          else -> "Password recovery."
        },
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.titleMedium,
        color = NoveliteDarkBrown,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = when (selectedTab) {
          0 -> "Pick up where you left off, discover something new, or continue writing your next chapter."
          1 -> "Create your Novelite account and find your place among stories, readers, and writers."
          else -> "Enter your registered email address to receive password reset instructions."
        },
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF6B7280),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Auth Card
      Card(
        modifier = Modifier
          .fillMaxWidth(if (isWideScreen) 0.85f else 1f)
          .widthIn(max = 520.dp)
          .testTag("auth_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, NoveliteBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(24.dp)) {
          // Tab Header / Selector
          TabRow(
            selectedTabIndex = if (selectedTab == 2) 0 else selectedTab,
            containerColor = Color(0xFFF5E1DA),
            contentColor = NoveliteDarkBrown,
            indicator = { tabPositions ->
              if (selectedTab < 2) {
                TabRowDefaults.SecondaryIndicator(
                  Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                  color = Color(0xFFC88577),
                  height = 3.dp
                )
              }
            },
            divider = {}
          ) {
            Tab(
              selected = selectedTab == 0,
              onClick = { selectedTab = 0; errorMessage = null },
              text = { Text("Log In", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) NoveliteDarkBrown else Color(0xFF6B7280)) }
            )
            Tab(
              selected = selectedTab == 1,
              onClick = { selectedTab = 1; errorMessage = null },
              text = { Text("Sign Up", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) NoveliteDarkBrown else Color(0xFF6B7280)) }
            )
          }

          Spacer(modifier = Modifier.height(24.dp))

          if (errorMessage != null) {
            Surface(
              color = Color(0xFFF5E1DA),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = errorMessage ?: "",
                color = NoveliteDarkBrown,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
          }

          when (selectedTab) {
            0 -> {
              // LOGIN FORM
              OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = null },
                label = { Text("Email or Username") },
                placeholder = { Text("e.g. aurora@novelite.app", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NoveliteDarkBrown) },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_username_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )
              if (username.isNotBlank() && username.contains("@") && !username.contains(".")) {
                Text(
                  text = "Please enter a valid email address.",
                  color = Color(0xFFC88577),
                  fontSize = 11.sp,
                  modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                placeholder = { Text("••••••••", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NoveliteDarkBrown) },
                trailingIcon = {
                  IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                      imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                      contentDescription = if (passwordVisible) "Hide password" else "Show password",
                      tint = Color(0xFF6B7280)
                    )
                  }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_password_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )
              if (password.isNotEmpty() && password.length < 6) {
                Text(
                  text = "Password should be at least 6 characters.",
                  color = Color(0xFFC88577),
                  fontSize = 11.sp,
                  modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.clickable { rememberMe = !rememberMe }
                ) {
                  androidx.compose.material3.Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = androidx.compose.material3.CheckboxDefaults.colors(
                      checkedColor = Color(0xFFC88577)
                    )
                  )
                  Text("Remember me", fontSize = 13.sp, color = Color(0xFF6B7280))
                }

                TextButton(onClick = { selectedTab = 2; errorMessage = null }) {
                  Text("Forgot password?", color = Color(0xFFC88577), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              NoveliteButton(
                text = "Log In",
                onClick = {
                  if (username.isBlank()) {
                    errorMessage = "Please enter your email or username."
                    return@NoveliteButton
                  }
                  if (password.isBlank()) {
                    errorMessage = "Please enter your password."
                    return@NoveliteButton
                  }
                  try {
                    isLoading = true
                    viewModel.login(username, password)
                  } finally {
                    isLoading = false
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("login_submit_button"),
                style = NoveliteButtonStyle.PRIMARY,
                isLoading = isLoading
              )

              Spacer(modifier = Modifier.height(12.dp))

              NoveliteButton(
                text = "Continue with Google",
                onClick = {
                  try {
                    isLoading = true
                    viewModel.login("google_user@novelite.app", "google_auth")
                  } finally {
                    isLoading = false
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("google_login_button"),
                style = NoveliteButtonStyle.SECONDARY,
                isLoading = isLoading
              )

              Spacer(modifier = Modifier.height(20.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Don't have an account? ", color = Color(0xFF6B7280), fontSize = 13.sp)
                TextButton(onClick = { selectedTab = 1; errorMessage = null }) {
                  Text("Create one", color = Color(0xFFC88577), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            1 -> {
              // SIGN UP FORM
              OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it; errorMessage = null },
                label = { Text("Full Name") },
                placeholder = { Text("Aurora Vance", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NoveliteDarkBrown) },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_name_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = null },
                label = { Text("Username") },
                placeholder = { Text("aurora_reads", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Create, contentDescription = null, tint = NoveliteDarkBrown) },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_username_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email Address") },
                placeholder = { Text("aurora@novelite.app", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NoveliteDarkBrown) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_email_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(12.dp))

              OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                placeholder = { Text("At least 6 characters", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NoveliteDarkBrown) },
                trailingIcon = {
                  IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                      imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                      contentDescription = null,
                      tint = Color(0xFF6B7280)
                    )
                  }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("signup_password_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(20.dp))

              NoveliteButton(
                text = "Create Account",
                onClick = {
                  if (fullName.isBlank()) {
                    errorMessage = "Please enter your full name."
                    return@NoveliteButton
                  }
                  if (!viewModel.validateUsernameUnique(username)) {
                    errorMessage = "Username '$username' is already taken. Please choose another username."
                    return@NoveliteButton
                  }
                  val pwdError = viewModel.validatePasswordStrength(password)
                  if (pwdError != null) {
                    errorMessage = pwdError
                    return@NoveliteButton
                  }
                  try {
                    isLoading = true
                    viewModel.signup(username, email, password)
                  } finally {
                    isLoading = false
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("signup_submit_button"),
                style = NoveliteButtonStyle.PRIMARY,
                isLoading = isLoading
              )

              Spacer(modifier = Modifier.height(12.dp))

              NoveliteButton(
                text = "Continue with Google",
                onClick = {
                  try {
                    isLoading = true
                    viewModel.signup("google_user", "google_user@novelite.app", "google_auth")
                  } finally {
                    isLoading = false
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("google_signup_button"),
                style = NoveliteButtonStyle.SECONDARY,
                isLoading = isLoading
              )

              Spacer(modifier = Modifier.height(20.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Already have an account? ", color = Color(0xFF6B7280), fontSize = 13.sp)
                TextButton(onClick = { selectedTab = 0; errorMessage = null }) {
                  Text("Log in", color = Color(0xFFC88577), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            2 -> {
              // FORGOT PASSWORD
              OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null; resetEmailSent = false },
                label = { Text("Registered Email Address") },
                placeholder = { Text("e.g. aurora@novelite.app", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NoveliteDarkBrown) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("reset_email_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFC88577),
                  unfocusedBorderColor = NoveliteBorder,
                  focusedTextColor = NoveliteDarkBrown,
                  unfocusedTextColor = NoveliteDarkBrown,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White
                ),
                singleLine = true
              )

              Spacer(modifier = Modifier.height(16.dp))

              if (resetEmailSent) {
                Surface(
                  color = Color(0xFFF5E1DA),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "✓ Password reset instructions successfully sent to $email.",
                    color = NoveliteDarkBrown,
                    modifier = Modifier.padding(14.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
                Spacer(modifier = Modifier.height(16.dp))
              }

              NoveliteButton(
                text = "Send Reset Link",
                onClick = {
                  if (email.isBlank() || !email.contains("@")) {
                    errorMessage = "Please enter a valid email address."
                    return@NoveliteButton
                  }
                  resetEmailSent = true
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("reset_submit_button"),
                style = NoveliteButtonStyle.PRIMARY
              )

              Spacer(modifier = Modifier.height(20.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
              ) {
                TextButton(onClick = { selectedTab = 0; errorMessage = null; resetEmailSent = false }) {
                  Text("Back to Log In", color = Color(0xFFC88577), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
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
        color = Color(0xFF6B7280),
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

@Composable
fun LoginScreen(
  viewModel: NoveliteViewModel,
  authViewModel: com.example.ui.AuthenticationViewModel? = null
) {
  AuthScreen(viewModel = viewModel, initialTab = 0)
}

@Composable
fun SignUpScreen(
  viewModel: NoveliteViewModel,
  authViewModel: com.example.ui.AuthenticationViewModel? = null
) {
  AuthScreen(viewModel = viewModel, initialTab = 1)
}
