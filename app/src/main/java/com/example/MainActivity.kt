package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppBottomNavigation
import com.example.ui.screens.AiDoubtSolverScreen
import com.example.ui.screens.BatchesScreen
import com.example.ui.screens.DppQuizScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.MockTestScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.theme.PwLearnTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PwLearnTheme {
                val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
                val selectedGoal by viewModel.selectedGoal.collectAsStateWithLifecycle()
                val userProgress by viewModel.userProgress.collectAsStateWithLifecycle()
                val selectedLecture by viewModel.selectedLecture.collectAsStateWithLifecycle()
                val quizState by viewModel.quizState.collectAsStateWithLifecycle()
                val doubtQuery by viewModel.doubtQuery.collectAsStateWithLifecycle()
                val selectedDoubtSubject by viewModel.selectedDoubtSubject.collectAsStateWithLifecycle()
                val aiDoubtResult by viewModel.aiDoubtResult.collectAsStateWithLifecycle()
                val isDoubtLoading by viewModel.isDoubtLoading.collectAsStateWithLifecycle()
                val savedNotes by viewModel.savedNotes.collectAsStateWithLifecycle()
                val testAttempts by viewModel.testAttempts.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Show bottom nav bar on standard tabs
                        if (currentTab != ScreenTab.VIDEO_PLAYER) {
                            AppBottomNavigation(
                                currentTab = currentTab,
                                onTabSelected = { tab -> viewModel.selectTab(tab) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            ScreenTab.HOME -> HomeScreen(
                                selectedGoal = selectedGoal,
                                onGoalSelected = { viewModel.selectGoal(it) },
                                userProgress = userProgress,
                                batches = viewModel.batches,
                                lectures = viewModel.lectures,
                                subjects = viewModel.subjects,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onOpenLecture = { viewModel.openLecture(it) }
                            )

                            ScreenTab.BATCHES -> BatchesScreen(
                                selectedGoal = selectedGoal,
                                onGoalSelected = { viewModel.selectGoal(it) },
                                batches = viewModel.batches,
                                subjects = viewModel.subjects,
                                lectures = viewModel.lectures,
                                onOpenLecture = { viewModel.openLecture(it) }
                            )

                            ScreenTab.VIDEO_PLAYER -> VideoPlayerScreen(
                                lecture = selectedLecture,
                                onBackClick = { viewModel.selectTab(ScreenTab.HOME) },
                                onStartDppQuiz = { subject ->
                                    viewModel.startQuizWithSubject(subject)
                                    viewModel.selectTab(ScreenTab.DPP_QUIZ)
                                },
                                onAskDoubtClick = {
                                    viewModel.selectTab(ScreenTab.AI_DOUBT)
                                }
                            )

                            ScreenTab.DPP_QUIZ -> DppQuizScreen(
                                quizState = quizState,
                                onSelectOption = { viewModel.selectQuizOption(it) },
                                onNextQuestion = { viewModel.nextQuizQuestion() },
                                onPreviousQuestion = { viewModel.previousQuizQuestion() },
                                onToggleSolution = { viewModel.toggleSolutionVisibility() },
                                onSubmitQuiz = { viewModel.submitQuiz() },
                                onSubjectChange = { viewModel.startQuizWithSubject(it) }
                            )

                            ScreenTab.MOCK_TEST -> MockTestScreen(
                                mockTests = viewModel.mockTests,
                                onStartTest = { mockTest ->
                                    viewModel.startQuizWithSubject(mockTest.subject)
                                    viewModel.selectTab(ScreenTab.DPP_QUIZ)
                                }
                            )

                            ScreenTab.AI_DOUBT -> AiDoubtSolverScreen(
                                doubtQuery = doubtQuery,
                                onQueryChange = { viewModel.setDoubtQuery(it) },
                                selectedSubject = selectedDoubtSubject,
                                onSubjectChange = { viewModel.setDoubtSubject(it) },
                                aiResult = aiDoubtResult,
                                isLoading = isDoubtLoading,
                                onAskDoubt = { viewModel.askDoubt() }
                            )

                            ScreenTab.LIBRARY -> LibraryScreen(
                                userProgress = userProgress,
                                savedNotes = savedNotes,
                                testAttempts = testAttempts,
                                onSaveNote = { title, subject, content ->
                                    viewModel.saveCustomNote(title, subject, content)
                                },
                                onDeleteNote = { id ->
                                    viewModel.deleteNote(id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
