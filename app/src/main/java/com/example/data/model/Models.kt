package com.example.data.model

enum class GoalType(val displayName: String, val tag: String) {
    JEE_MAIN("JEE Main & Advanced 2026", "Engineering"),
    NEET_UG("NEET UG Super 2026", "Medical"),
    BOARD_12("Class 12 Boards", "School"),
    FOUNDATION("Class 10 Foundation", "Junior")
}

data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val iconName: String,
    val chapterCount: Int,
    val colorHex: Long
)

data class Lecture(
    val id: String,
    val subjectId: String,
    val chapterName: String,
    val title: String,
    val durationText: String,
    val facultyName: String,
    val facultyTitle: String,
    val videoUrl: String = "",
    val isLiveNow: Boolean = false,
    val scheduledTime: String = "",
    val hasDpp: Boolean = true,
    val notesPdfUrl: String = "",
    val viewsCount: String = "124K views"
)

data class CourseBatch(
    val id: String,
    val title: String,
    val goal: GoalType,
    val priceText: String,
    val originalPriceText: String,
    val rating: Float,
    val enrolledCount: String,
    val startDateText: String,
    val features: List<String>,
    val facultyList: List<String>,
    val badgeText: String = "POPULAR"
)

data class DppQuestion(
    val id: Int,
    val subject: String,
    val chapter: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val formula: String = ""
)

data class MockTest(
    val id: String,
    val title: String,
    val subject: String,
    val durationMinutes: Int,
    val totalMarks: Int,
    val totalQuestions: Int,
    val isLive: Boolean = false,
    val questions: List<DppQuestion>
)

data class AiDoubt(
    val id: String,
    val question: String,
    val subject: String,
    val answer: String,
    val timestamp: Long = System.currentTimeMillis()
)
