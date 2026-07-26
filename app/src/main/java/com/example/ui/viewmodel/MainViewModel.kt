package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiDoubtResponse
import com.example.data.ai.AiDoubtSolverService
import com.example.data.local.AppDatabase
import com.example.data.local.SavedNoteEntity
import com.example.data.local.TestAttemptEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.CourseBatch
import com.example.data.model.DppQuestion
import com.example.data.model.GoalType
import com.example.data.model.Lecture
import com.example.data.model.MockTest
import com.example.data.model.Subject
import com.example.data.repository.EducationalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME, BATCHES, DPP_QUIZ, MOCK_TEST, AI_DOUBT, LIBRARY, VIDEO_PLAYER
}

data class QuizState(
    val questions: List<DppQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val userAnswers: Map<Int, Int> = emptyMap(), // questionId -> selectedIndex
    val isSubmitted: Boolean = false,
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val isSolutionVisible: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = EducationalRepository(
        db.userProgressDao(),
        db.savedNotesDao(),
        db.testAttemptsDao()
    )
    private val doubtService = AiDoubtSolverService()

    val userProgress: StateFlow<UserProgressEntity?> = repository.userProgress.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProgressEntity()
    )

    val savedNotes: StateFlow<List<SavedNoteEntity>> = repository.savedNotes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val testAttempts: StateFlow<List<TestAttemptEntity>> = repository.testAttempts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _selectedGoal = MutableStateFlow(GoalType.JEE_MAIN)
    val selectedGoal: StateFlow<GoalType> = _selectedGoal.asStateFlow()

    private val _selectedLecture = MutableStateFlow<Lecture?>(null)
    val selectedLecture: StateFlow<Lecture?> = _selectedLecture.asStateFlow()

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    private val _doubtQuery = MutableStateFlow("")
    val doubtQuery: StateFlow<String> = _doubtQuery.asStateFlow()

    private val _selectedDoubtSubject = MutableStateFlow("Physics")
    val selectedDoubtSubject: StateFlow<String> = _selectedDoubtSubject.asStateFlow()

    private val _aiDoubtResult = MutableStateFlow<AiDoubtResponse?>(null)
    val aiDoubtResult: StateFlow<AiDoubtResponse?> = _aiDoubtResult.asStateFlow()

    private val _isDoubtLoading = MutableStateFlow(false)
    val isDoubtLoading: StateFlow<Boolean> = _isDoubtLoading.asStateFlow()

    val subjects: List<Subject> = repository.getSubjects()
    val batches: List<CourseBatch> = repository.getCourseBatches()
    val lectures: List<Lecture> = repository.getLectures()
    val mockTests: List<MockTest> = repository.getMockTests()

    init {
        startQuizWithSubject("Physics")
    }

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun selectGoal(goal: GoalType) {
        _selectedGoal.value = goal
        viewModelScope.launch {
            repository.saveGoal(goal.displayName)
        }
    }

    fun openLecture(lecture: Lecture) {
        _selectedLecture.value = lecture
        _currentTab.value = ScreenTab.VIDEO_PLAYER
    }

    fun startQuizWithSubject(subjectName: String) {
        val questions = repository.getDppQuestionsForSubject(subjectName)
        _quizState.value = QuizState(
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            userAnswers = emptyMap(),
            isSubmitted = false,
            score = 0,
            totalQuestions = questions.size,
            isSolutionVisible = false
        )
    }

    fun selectQuizOption(optionIndex: Int) {
        val current = _quizState.value
        if (current.isSubmitted) return
        val questionId = current.questions.getOrNull(current.currentIndex)?.id ?: return
        val updatedAnswers = current.userAnswers.toMutableMap().apply {
            put(questionId, optionIndex)
        }
        _quizState.value = current.copy(
            selectedOptionIndex = optionIndex,
            userAnswers = updatedAnswers
        )
    }

    fun nextQuizQuestion() {
        val current = _quizState.value
        if (current.currentIndex < current.questions.size - 1) {
            val nextIndex = current.currentIndex + 1
            val nextQId = current.questions[nextIndex].id
            _quizState.value = current.copy(
                currentIndex = nextIndex,
                selectedOptionIndex = current.userAnswers[nextQId],
                isSolutionVisible = false
            )
        }
    }

    fun previousQuizQuestion() {
        val current = _quizState.value
        if (current.currentIndex > 0) {
            val prevIndex = current.currentIndex - 1
            val prevQId = current.questions[prevIndex].id
            _quizState.value = current.copy(
                currentIndex = prevIndex,
                selectedOptionIndex = current.userAnswers[prevQId],
                isSolutionVisible = false
            )
        }
    }

    fun toggleSolutionVisibility() {
        _quizState.value = _quizState.value.copy(
            isSolutionVisible = !_quizState.value.isSolutionVisible
        )
    }

    fun submitQuiz() {
        val current = _quizState.value
        var score = 0
        current.questions.forEach { q ->
            if (current.userAnswers[q.id] == q.correctIndex) {
                score += 4 // JEE marking scheme +4
            } else if (current.userAnswers.containsKey(q.id)) {
                score -= 1 // -1 negative marking
            }
        }
        _quizState.value = current.copy(
            isSubmitted = true,
            score = score
        )

        // Save attempt to Room DB
        viewModelScope.launch {
            repository.saveTestAttempt(
                testTitle = "DPP Practice - ${current.questions.firstOrNull()?.subject ?: "Physics"}",
                subject = current.questions.firstOrNull()?.subject ?: "Physics",
                score = score,
                totalMarks = current.questions.size * 4,
                correctAnswers = current.questions.count { current.userAnswers[it.id] == it.correctIndex },
                totalQuestions = current.questions.size,
                timeTakenSeconds = 180
            )
        }
    }

    fun setDoubtQuery(query: String) {
        _doubtQuery.value = query
    }

    fun setDoubtSubject(subject: String) {
        _selectedDoubtSubject.value = subject
    }

    fun askDoubt() {
        val query = _doubtQuery.value.trim()
        if (query.isBlank()) return

        _isDoubtLoading.value = true
        viewModelScope.launch {
            val response = doubtService.solveDoubt(query, _selectedDoubtSubject.value)
            _aiDoubtResult.value = response
            _isDoubtLoading.value = false
        }
    }

    fun saveCustomNote(title: String, subject: String, content: String) {
        viewModelScope.launch {
            repository.saveNote(title, subject, content)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }
}
