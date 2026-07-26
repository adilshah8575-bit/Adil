package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val selectedGoal: String = "JEE Main & Advanced 2026",
    val studyStreakDays: Int = 5,
    val totalStudyMinutes: Int = 340,
    val completedLecturesCount: Int = 18,
    val solvedDppsCount: Int = 42,
    val averageScorePercent: Int = 86
)

@Entity(tableName = "saved_notes")
data class SavedNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val content: String,
    val isBookmarked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_attempts")
data class TestAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testTitle: String,
    val subject: String,
    val score: Int,
    val totalMarks: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)
