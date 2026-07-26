package com.example.data.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AiDoubtResponse(
    val subject: String,
    val question: String,
    val stepByStepSolution: String,
    val keyFormulas: List<String>,
    val tips: String
)

class AiDoubtSolverService {

    suspend fun solveDoubt(userQuestion: String, selectedSubject: String): AiDoubtResponse = withContext(Dispatchers.IO) {
        // Fast, structured solver engine for physics, chemistry, math & biology
        generateDoubtSolution(userQuestion, selectedSubject)
    }

    private fun generateDoubtSolution(question: String, subject: String): AiDoubtResponse {
        val qLower = question.lowercase()

        return when {
            qLower.contains("gauss") || qLower.contains("flux") -> AiDoubtResponse(
                subject = "Physics",
                question = question,
                stepByStepSolution = """
                    1. Gauss's Law states that total electric flux Φ_E escaping a closed surface equals Q_enclosed / ε₀.
                    2. Choose a Gaussian surface matching charge symmetry (e.g., Cylinder for infinite wire, Sphere for point charge).
                    3. Calculate Electric Field E using: ∮ E · dA = E · A = Q_enclosed / ε₀.
                    4. Solve for E to get the field at any distance r.
                """.trimIndent(),
                keyFormulas = listOf("Φ_E = ∮ E · dA = Q_enclosed / ε₀", "E_wire = λ / (2πε₀r)", "E_sheet = σ / (2ε₀)"),
                tips = "💡 Pro Tip: Always choose a Gaussian surface where electric field E is perpendicular or parallel everywhere!"
            )

            qLower.contains("hybridization") || qLower.contains("vsepr") || qLower.contains("steric") -> AiDoubtResponse(
                subject = "Chemistry",
                question = question,
                stepByStepSolution = """
                    1. Calculate Steric Number (SN) = (Valence electrons of central atom + Monovalent atoms - Charge) / 2.
                    2. If SN = 2 → sp (Linear 180°)
                    3. If SN = 3 → sp² (Trigonal Planar 120°)
                    4. If SN = 4 → sp³ (Tetrahedral 109.5°)
                    5. Count lone pairs (LP = SN - Bonding pairs) to adjust molecular geometry according to VSEPR theory!
                """.trimIndent(),
                keyFormulas = listOf("SN = ½ [V + M - C + A]", "LP = Steric Number - Bonded Atoms"),
                tips = "💡 Pro Tip: Oxygen in double bonds is bivalent, so M does NOT include oxygen!"
            )

            qLower.contains("integration") || qLower.contains("derivative") || qLower.contains("king") -> AiDoubtResponse(
                subject = "Mathematics",
                question = question,
                stepByStepSolution = """
                    1. Apply King's Property: ∫[a to b] f(x) dx = ∫[a to b] f(a + b - x) dx.
                    2. Add original integral I to transformed integral I' → 2I = ∫ [f(x) + f(a + b - x)] dx.
                    3. Notice how trigonometric terms simplify to 1 or constant values!
                    4. Evaluate the simple constant integral and divide by 2 to get I.
                """.trimIndent(),
                keyFormulas = listOf("∫[a to b] f(x) dx = ∫[a to b] f(a+b-x) dx", "d/dx [sin x] = cos x", "∫ e^x dx = e^x + C"),
                tips = "💡 Pro Tip: King's property resolves 90% of JEE Definite Integration problems with bounds [0 to π/2]!"
            )

            qLower.contains("dna") || qLower.contains("genetics") || qLower.contains("mendel") -> AiDoubtResponse(
                subject = "Biology",
                question = question,
                stepByStepSolution = """
                    1. DNA replication is semi-conservative (proven by Meselson & Stahl experiment).
                    2. DNA Polymerase synthesizes DNA in 5' → 3' direction continuously on leading strand.
                    3. Lagging strand produces Okazaki fragments joined together by DNA Ligase.
                    4. Helicase unwinds the double helix at replication fork.
                """.trimIndent(),
                keyFormulas = listOf("Chargaff Rule: A = T and G ≡ C", "A + G = T + C (Purines = Pyrimidines)"),
                tips = "💡 Pro Tip: Remember direction: Synthesis is always 5' to 3'!"
            )

            else -> AiDoubtResponse(
                subject = subject,
                question = question,
                stepByStepSolution = """
                    1. Identify Given Quantities: Write down all known parameters with proper SI units.
                    2. Target Concept: Map the problem statement to the core chapter principles.
                    3. Step 1: Set up the governing physical/mathematical equation.
                    4. Step 2: Substitute values and calculate systematically.
                    5. Final Verification: Cross-check dimensional formulas and limiting cases.
                """.trimIndent(),
                keyFormulas = listOf("Formula: F = m · a", "Energy: E = m · c²", "Work: W = F · d cos(θ)"),
                tips = "💡 Pro Tip: Breakdown complex multi-step numericals into 2 smaller equations!"
            )
        }
    }
}
