package com.example.busbookingapp.ui.theme.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.busbookingapp.navigation.Routes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {

    // DATA CLASS (inside)
    data class OnboardingPage(
        val title: String,
        val description: String,
    )

    // DATA (inside + remembered)
    val onboardingList = remember {
        listOf(
            OnboardingPage(
                "Welcome to Bus Booker!",
                "Book buses easily across the country.",

            ),
            OnboardingPage(
                "Secure Payments",
                "Pay safely using mobile money.",

            ),
            OnboardingPage(
                "Luxury Comfort",
                "Travel in VIP buses with Wi-Fi and AC.",

            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { onboardingList.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // PAGER
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(0.8f)
        ) { page ->

            val item = onboardingList[page]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = item.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item.description,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }


        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardingList.size) { index ->
                val color =
                    if (pagerState.currentPage == index) Color(0xFF6200EE)
                    else Color.LightGray

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }


        Button(
            onClick = {
                navController.navigate(Routes.ROUTE_REGISTER) {
                    popUpTo(Routes.ROUTE_HOME) { inclusive = true }
                }
            },
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
@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen(navController = NavController(context = LocalContext.current))
}
