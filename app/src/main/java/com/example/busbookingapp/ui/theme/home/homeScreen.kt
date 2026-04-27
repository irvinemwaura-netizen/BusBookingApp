package com.example.busbookingapp.ui.theme.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data Model (Defined at top level so it's resolved everywhere)
data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: String
)

// 2. Data List
val onboardingList = listOf(
    OnboardingPage("Real-time Tracking", "Never miss a bus again. Track your ride live on the map.", "📍"),
    OnboardingPage("Secure Payments", "Book with confidence using encrypted mobile money.", "💳"),
    OnboardingPage("Luxury Comfort", "Select from our fleet of VIP buses featuring Wi-Fi and AC.", "🛋️")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeOnboardingScreen(onGetStartedClick: () -> Unit) {
    // Specify the page count explicitly to fix "Cannot infer type"
    val pagerState = rememberPagerState(pageCount = { onboardingList.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // The Swiping Area
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(0.8f)
        ) { page ->
            OnboardingView(onboardingList[page])
        }

        // Page Indicator (Dots)
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardingList.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF6200EE) else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape) // This requires 'import androidx.compose.ui.draw.clip'
                        .background(color)
                )
            }
        }

        // Get Started Button
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Get Started", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun OnboardingView(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = page.icon, fontSize = 100.sp)
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            lineHeight = 24.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WelcomeOnboardingScreen(onGetStartedClick = { })
}