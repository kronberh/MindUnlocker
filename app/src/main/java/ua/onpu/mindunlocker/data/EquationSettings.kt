package ua.onpu.mindunlocker.data

data class EquationSettings(
    val minNumber: Float,
    val maxNumber: Float,
    val maxDecimals: Int,
    val allowedOperations: Set<Char>
)
