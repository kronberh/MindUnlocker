package ua.onpu.mindunlocker.data

data class Equation(
    val text: String,
    val correctAnswer: Float,
    val options: List<Float>,
    val shapeType: String? = null,
    val shapeParams: List<Float>? = null,
    val missingSideIndex: Int? = null
)
