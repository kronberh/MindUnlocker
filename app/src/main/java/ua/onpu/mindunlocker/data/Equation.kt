package ua.onpu.mindunlocker.data

import kotlin.math.pow
import kotlin.random.Random

data class Equation(val text: String, val answer: Boolean)

fun generateEquation(settings: EquationSettings): Equation {
    fun randomNumber(): Float {
        val scale = 10f.pow(settings.maxDecimals)
        val raw = Random.nextDouble(settings.minNumber.toDouble(), settings.maxNumber.toDouble())
        return (raw * scale).toInt() / scale
    }

    val a = randomNumber()
    val b = randomNumber()

    val operation = settings.allowedOperations.random()

    val actualResult = when (operation) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> if (b != 0f) a / b else 0f
        else -> 0f
    }

    val isCorrect = Random.nextBoolean()
    val displayedResult = if (isCorrect) {
        actualResult
    } else {
        val deviation = 1f / 10f.pow(settings.maxDecimals.coerceAtLeast(1))
        actualResult + listOf(-deviation, deviation).random()
    }

    val format = "%.${settings.maxDecimals}f"
    val equationText = "${format.format(a)} $operation ${format.format(b)} = ${format.format(displayedResult)}"

    return Equation(equationText, isCorrect)
}
