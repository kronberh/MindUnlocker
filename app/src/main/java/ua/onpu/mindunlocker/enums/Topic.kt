package ua.onpu.mindunlocker.enums

import ua.onpu.mindunlocker.data.Equation
import ua.onpu.mindunlocker.data.EquationSettings
import kotlin.math.pow
import kotlin.random.Random

enum class Topic(
    val displayName: String,
    val defaultOperations: Set<Char>,
    val generateEquation: (EquationSettings) -> Equation
) {
    ALGEBRA("Algebra", setOf('+', '-', '*', '/'), { settings ->
        val scale = 10f.pow(settings.maxDecimals)
        fun rand() = ((Random.nextDouble(
            settings.minNumber.toDouble(),
            settings.maxNumber.toDouble()
        ) * scale).toInt()) / scale

        val a = rand()
        val b = rand()
        val op = settings.allowedOperations.random()
        val correctAnswer = when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0f) a / b else a
            else -> a + b
        }

        val missingLeft = Random.nextBoolean()
        val questionText = if (missingLeft)
            "? $op ${b.format(settings.maxDecimals)} = ${correctAnswer.format(settings.maxDecimals)}"
        else
            "${a.format(settings.maxDecimals)} $op ? = ${correctAnswer.format(settings.maxDecimals)}"

        val answer = if (missingLeft) a else b

        val options = generateOptions(answer)

        Equation(text = questionText, correctAnswer = answer, options = options)
    }),

    GEOMETRY("Geometry", setOf('◺', '▭'), { settings ->
        val op = settings.allowedOperations.random()
        when (op) {
            '◺' -> {
                val base = Random.nextInt(3, 15).toFloat()
                val height = Random.nextInt(3, 15).toFloat()
                val sides = listOf(base, height)
                val missing = listOf("base", "height").indices.random()
                val correct = sides[missing]

                val area = 0.5f * base * height

                val question = when (missing) {
                    0 -> "Area = ${area.toInt()}, height = ${height.toInt()}. Find base (X)."
                    1 -> "Area = ${area.toInt()}, base = ${base.toInt()}. Find height (X)."
                    else -> "?"
                }

                Equation(
                    text = question,
                    correctAnswer = correct,
                    options = generateOptions(correct),
                    shapeType = "triangle",
                    shapeParams = sides,
                    missingSideIndex = missing
                )
            }

            else -> {
                val w = Random.nextInt(3, 20).toFloat()
                val h = Random.nextInt(3, 20).toFloat()
                val sides = listOf(w, h)
                val missing = listOf("width", "height").indices.random()
                val correct = sides[missing]

                val area = w * h

                val question = when (missing) {
                    0 -> "Area = ${area.toInt()}, height = ${h.toInt()}. Find width (X)."
                    1 -> "Area = ${area.toInt()}, width = ${w.toInt()}. Find height (X)."
                    else -> "?"
                }

                Equation(
                    text = question,
                    correctAnswer = correct,
                    options = generateOptions(correct),
                    shapeType = "rectangle",
                    shapeParams = sides,
                    missingSideIndex = missing
                )
            }
        }
    })
}

private fun Float.format(decimals: Int): String = "%.${decimals}f".format(this)

private fun generateOptions(correct: Float): List<Float> {
    val delta = 1 + Random.nextInt(1, 5)
    val options = mutableSetOf(correct)
    while (options.size < 3) {
        val deviation = (Random.nextInt(-delta, delta + 1)).takeIf { it != 0 } ?: 1
        options.add(correct + deviation)
    }
    return options.shuffled()
}
