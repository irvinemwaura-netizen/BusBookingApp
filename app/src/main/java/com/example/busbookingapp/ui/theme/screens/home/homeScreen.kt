package com.example.busbookingapp.ui.theme.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.busbookingapp.R
import com.example.busbookingapp.navigation.Routes

@Composable
fun HomeScreen(navController: NavController) {

    data class OnboardingPage(
        val title: String,
        val description: String,
        val image: Int
    )

    val onboardingList = listOf(
        OnboardingPage(
            "Welcome to Bus Booker!",
            "Book buses easily across the country.",
            R.drawable.welcome
        ),
        OnboardingPage(
            "Secure Payments",
            "Pay safely using mobile money.",
            R.drawable.pay
        ),
        OnboardingPage(
            "Luxury Comfort",
            "Travel in VIP buses with Wi-Fi and AC.",
            R.drawable.comfort
        )
    )

    val pagerState = rememberPagerState(pageCount = { onboardingList.size })

    Box(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            val item = onboardingList[page]

            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // 🔴 TOP TEXT
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val item = onboardingList[pagerState.currentPage]

                Text(
                    text = item.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = item.description,
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }

            // 🔵 INDICATORS (above button)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(onboardingList.size) { index ->
                    val color =
                        if (pagerState.currentPage == index) Color.White
                        else Color.Gray

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            // 🔴 BUTTON (fixed bottom)
            Button(
                onClick = {
                    navController.navigate(Routes.ROUTE_REGISTER) {
                        popUpTo(Routes.ROUTE_HOME) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Get Started", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}