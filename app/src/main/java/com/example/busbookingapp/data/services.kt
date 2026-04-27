package com.example.busbookingapp.data

class services {
    data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: String // In a real app, use a Drawable resource ID
)

    val onboardingList = listOf(
        OnboardingPage("Real-time Tracking", "Never miss a bus again. Track your ride live on the map.", "📍"),
        OnboardingPage("Secure Payments", "Book with confidence using encrypted mobile money.", "💳"),
        OnboardingPage("Luxury Comfort", "Select from our fleet of VIP buses featuring Wi-Fi and AC.", "🛋️")
    )
}