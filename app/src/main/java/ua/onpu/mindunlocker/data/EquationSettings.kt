package ua.onpu.mindunlocker.data

data class EquationSettings(
    val minNumber: Float = 1f,
    val maxNumber: Float = 10f,
    val maxDecimals: Int = 0,
    val allowedOperations: Set<Char>
)
