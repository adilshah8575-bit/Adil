package com.example.data.repository

import com.example.data.local.SavedNoteEntity
import com.example.data.local.SavedNotesDao
import com.example.data.local.TestAttemptEntity
import com.example.data.local.TestAttemptsDao
import com.example.data.local.UserProgressDao
import com.example.data.local.UserProgressEntity
import com.example.data.model.CourseBatch
import com.example.data.model.DppQuestion
import com.example.data.model.GoalType
import com.example.data.model.Lecture
import com.example.data.model.MockTest
import com.example.data.model.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EducationalRepository(
    private val progressDao: UserProgressDao,
    private val notesDao: SavedNotesDao,
    private val testDao: TestAttemptsDao
) {

    val userProgress: Flow<UserProgressEntity?> = progressDao.getUserProgress()
    val savedNotes: Flow<List<SavedNoteEntity>> = notesDao.getAllNotes()
    val testAttempts: Flow<List<TestAttemptEntity>> = testDao.getAllTestAttempts()

    suspend fun saveGoal(goalName: String) {
        val current = UserProgressEntity(selectedGoal = goalName)
        progressDao.insertOrUpdateProgress(current)
    }

    suspend fun addStudyMinutes(minutes: Int) {
        val current = UserProgressEntity(totalStudyMinutes = minutes)
        progressDao.insertOrUpdateProgress(current)
    }

    suspend fun saveNote(title: String, subject: String, content: String) {
        notesDao.insertNote(
            SavedNoteEntity(title = title, subject = subject, content = content)
        )
    }

    suspend fun deleteNote(id: Long) {
        notesDao.deleteNote(id)
    }

    suspend fun saveTestAttempt(
        testTitle: String,
        subject: String,
        score: Int,
        totalMarks: Int,
        correctAnswers: Int,
        totalQuestions: Int,
        timeTakenSeconds: Int
    ) {
        testDao.insertAttempt(
            TestAttemptEntity(
                testTitle = testTitle,
                subject = subject,
                score = score,
                totalMarks = totalMarks,
                correctAnswers = correctAnswers,
                totalQuestions = totalQuestions,
                timeTakenSeconds = timeTakenSeconds
            )
        )
    }

    fun getSubjects(): List<Subject> = listOf(
        Subject("phy", "Physics", "PHY101", "atom", 28, 0xFF0284C7),
        Subject("chem", "Chemistry", "CHM101", "flask", 24, 0xFFD97706),
        Subject("math", "Mathematics", "MTH101", "calculator", 30, 0xFF312E81),
        Subject("bio", "Biology", "BIO101", "dna", 22, 0xFF059669)
    )

    fun getCourseBatches(): List<CourseBatch> = listOf(
        CourseBatch(
            id = "lakshya_jee_2026",
            title = "Lakshya JEE 2026 Batch",
            goal = GoalType.JEE_MAIN,
            priceText = "₹4,199",
            originalPriceText = "₹8,999",
            rating = 4.9f,
            enrolledCount = "142,000+ Students",
            startDateText = "Started 15th July",
            features = listOf(
                "Live + Recorded Daily Classes",
                "Daily Practice Problems (DPP) with Video Solutions",
                "24x7 AI Doubt Engine & Faculty Support",
                "All India Test Series (AITS)"
            ),
            facultyList = listOf("Alakh Sir (Physics)", "Saleem Sir (Physics)", "Amit Sir (Maths)", "Pankaj Sir (Chemistry)"),
            badgeText = "BESTSELLER"
        ),
        CourseBatch(
            id = "yakeen_neet_2026",
            title = "Yakeen NEET Dropper 2026",
            goal = GoalType.NEET_UG,
            priceText = "₹4,499",
            originalPriceText = "₹9,999",
            rating = 4.95f,
            enrolledCount = "185,000+ Students",
            startDateText = "Started 1st August",
            features = listOf(
                "NCERT Line-by-Line Micro Lectures",
                "Daily 100+ MCQ Question Banks",
                "Weekly OMR Based Mock Tests",
                "Dedicated Mentorship & Strategy Calls"
            ),
            facultyList = listOf("Tarun Sir (Botany)", "MD Sir (Zoology)", "MR Sir (Physics)", "Mohit Sir (Chemistry)"),
            badgeText = "TOP RATED"
        ),
        CourseBatch(
            id = "parishram_boards_2026",
            title = "Parishram Class 12th Board Booster",
            goal = GoalType.BOARD_12,
            priceText = "₹2,999",
            originalPriceText = "₹5,999",
            rating = 4.8f,
            enrolledCount = "98,000+ Students",
            startDateText = "Starts 20th August",
            features = listOf(
                "Complete Board Syllabus Coverage",
                "Sample Papers & Previous 10-Year Question Bank",
                "Handwritten Topper Answer Notes PDF",
                "Practical Lab Experiment Demonstrations"
            ),
            facultyList = listOf("Shipra Ma'am (English)", "Sunil Sir (Physics)", "Rahul Sir (Chemistry)"),
            badgeText = "BOARDS BATCH"
        )
    )

    fun getLectures(): List<Lecture> = listOf(
        Lecture(
            id = "lec_phy_1",
            subjectId = "phy",
            chapterName = "Electrostatics & Capacitance",
            title = "Lecture 04: Gauss's Law & Electric Flux Applications",
            durationText = "1h 24m",
            facultyName = "Alakh Sir",
            facultyTitle = "Head Physics Faculty",
            isLiveNow = true,
            hasDpp = true,
            viewsCount = "89K watching"
        ),
        Lecture(
            id = "lec_chem_1",
            subjectId = "chem",
            chapterName = "Chemical Bonding",
            title = "Lecture 02: VSEPR Theory & Hybridization Shortcuts",
            durationText = "1h 12m",
            facultyName = "Pankaj Sir",
            facultyTitle = "Senior Organic Faculty",
            isLiveNow = false,
            scheduledTime = "Today, 6:00 PM",
            hasDpp = true
        ),
        Lecture(
            id = "lec_math_1",
            subjectId = "math",
            chapterName = "Definite Integration",
            title = "Lecture 03: King's Property & Limit of Sums Tricks",
            durationText = "1h 45m",
            facultyName = "Amit Sir",
            facultyTitle = "Mathematics Expert",
            isLiveNow = false,
            scheduledTime = "Tomorrow, 10:00 AM",
            hasDpp = true
        ),
        Lecture(
            id = "lec_phy_2",
            subjectId = "phy",
            chapterName = "Rotational Motion",
            title = "Lecture 01: Moment of Inertia & Parallel Axis Theorem",
            durationText = "1h 18m",
            facultyName = "MR Sir",
            facultyTitle = "Physics Maestro",
            isLiveNow = false,
            viewsCount = "156K views"
        )
    )

    fun getDppQuestionsForSubject(subjectName: String): List<DppQuestion> = listOf(
        DppQuestion(
            id = 101,
            subject = "Physics",
            chapter = "Electrostatics",
            questionText = "An electric dipole of moment p is placed in a uniform electric field E. What is the maximum torque acting on the dipole?",
            options = listOf("p · E", "p × E", "p / E", "Zero"),
            correctIndex = 1,
            explanation = "Torque τ = p × E = pE sin(θ). Maximum torque occurs when θ = 90°, so τ_max = pE.",
            formula = "τ = pE sin(θ)"
        ),
        DppQuestion(
            id = 102,
            subject = "Physics",
            chapter = "Electrostatics",
            questionText = "What is the electric flux through a closed spherical surface of radius R containing a charge Q at its center according to Gauss's Law?",
            options = listOf("Q / ε₀", "Q / 4πε₀R", "Q · 4πR²", "Zero"),
            correctIndex = 0,
            explanation = "Gauss's Law states that total electric flux Φ_E = Q_enclosed / ε₀, independent of the radius R.",
            formula = "Φ_E = ∮ E · dA = Q_enclosed / ε₀"
        ),
        DppQuestion(
            id = 103,
            subject = "Chemistry",
            chapter = "Chemical Kinetics",
            questionText = "For a zero-order reaction A → Products, what is the half-life t_1/2 in terms of initial concentration [A]₀ and rate constant k?",
            options = listOf("[A]₀ / (2k)", "ln(2) / k", "1 / (k[A]₀)", "k / [A]₀"),
            correctIndex = 0,
            explanation = "For zero-order reactions, rate = k. Concentration at time t is [A] = [A]₀ - kt. When [A] = [A]₀/2, t_1/2 = [A]₀ / (2k).",
            formula = "t_1/2 = [A]₀ / (2k)"
        ),
        DppQuestion(
            id = 104,
            subject = "Mathematics",
            chapter = "Calculus",
            questionText = "Evaluate the derivative d/dx [ x · e^(2x) ] at x = 1.",
            options = listOf("3e²", "2e²", "e²", "4e²"),
            correctIndex = 0,
            explanation = "Using product rule: d/dx [x · e^(2x)] = 1 · e^(2x) + x · (2e^(2x)) = e^(2x) (1 + 2x). At x=1, e²(1+2) = 3e².",
            formula = "d/dx [u · v] = u'v + uv'"
        ),
        DppQuestion(
            id = 105,
            subject = "Biology",
            chapter = "Genetics",
            questionText = "In Mendelian monohybrid cross of F2 generation, what is the phenotypic ratio?",
            options = listOf("3 : 1", "1 : 2 : 1", "9 : 3 : 3 : 1", "2 : 1"),
            correctIndex = 0,
            explanation = "In a monohybrid cross, F2 phenotype ratio is 3 Dominant : 1 Recessive, while genotype ratio is 1:2:1.",
            formula = "F2 Phenotypic Ratio = 3 : 1"
        )
    )

    fun getMockTests(): List<MockTest> = listOf(
        MockTest(
            id = "mock_jee_full_1",
            title = "All India JEE Main Full Syllabus Test 01",
            subject = "Physics, Chemistry & Maths",
            durationMinutes = 60,
            totalMarks = 300,
            totalQuestions = 5,
            isLive = true,
            questions = getDppQuestionsForSubject("All")
        ),
        MockTest(
            id = "mock_phy_chapter_1",
            title = "Physics Chapterwise: Electrostatics & Magnetism",
            subject = "Physics",
            durationMinutes = 30,
            totalMarks = 100,
            totalQuestions = 3,
            isLive = false,
            questions = getDppQuestionsForSubject("Physics")
        )
    )
}
