package ua.onpu.mindunlocker.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import ua.onpu.mindunlocker.data.Equation
import ua.onpu.mindunlocker.enums.Topic

@Composable
fun MathLockScreen(topic: Topic, equation: Equation, onAnswerSelected: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (topic == Topic.ALGEBRA) {
            Text(
                text = equation.text,
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            equation.shapeParams?.let {
                ShapeCanvas(equation.shapeType, it, equation.missingSideIndex)
            }
        }

        Spacer(Modifier.height(24.dp))

        val buttonStates = remember {
            mutableStateListOf<Boolean>().apply {
                repeat(equation.options.size) { add(true) }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            equation.options.forEachIndexed { index, option ->
                Button(
                    onClick = {
                        val isCorrect: Boolean = option == equation.correctAnswer
                        onAnswerSelected(isCorrect)
                        if (!isCorrect) {
                            buttonStates[index] = false
                        }
                    },
                    enabled = buttonStates[index]
                ) {
                    Text(
                        text = option.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Composable
fun ShapeCanvas(type: String?, params: List<Float>, missingIndex: Int? = null) {
    Canvas(
        modifier = Modifier
            .size(240.dp)
            .background(Color.White)
    ) {
        val canvas = drawContext.canvas.nativeCanvas
        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }

        when (type) {
            "rectangle" -> {
                val width = params.getOrNull(0) ?: 4f
                val height = params.getOrNull(1) ?: 3f
                val area = params.getOrNull(2) ?: (width * height)

                val scale = minOf(size.width / (width + 1f), size.height / (height + 1f))
                val w = width * scale
                val h = height * scale

                val left = (size.width - w) / 2f
                val top = (size.height - h) / 2f
                val cx = left + w / 2f
                val cy = top + h / 2f

                drawRect(
                    color = Color.Black,
                    topLeft = Offset(left, top),
                    size = Size(w, h),
                    style = Stroke(width = 4f)
                )

                canvas.drawText(
                    if (missingIndex == 0) "?" else "$width",
                    cx,
                    top - 20f,
                    paint
                )

                canvas.drawText(
                    if (missingIndex == 1) "?" else "$height",
                    left - 60f,
                    cy,
                    paint
                )

                canvas.drawText(
                    if (missingIndex == 2) "S=?" else "S=$area",
                    cx,
                    cy,
                    paint
                )
            }

            else -> {
                val base = params.getOrNull(0) ?: 4f
                val height = params.getOrNull(1) ?: 3f
                val area = params.getOrNull(2) ?: (base * height / 2)

                val scale = minOf(size.width / (base + 1f), size.height / (height + 1f))
                val b = base * scale
                val h = height * scale

                val cx = size.width / 2f
                val by = size.height * 0.9f

                val p1 = Offset(cx, by - h)
                val p2 = Offset(cx - b / 2f, by)
                val p3 = Offset(cx + b / 2f, by)

                drawPath(
                    path = Path().apply {
                        moveTo(p1.x, p1.y)
                        lineTo(p2.x, p2.y)
                        lineTo(p3.x, p3.y)
                        close()
                    },
                    color = Color.Black,
                    style = Stroke(width = 4f)
                )

                drawLine(
                    color = Color.Gray,
                    start = p1,
                    end = Offset(cx, by),
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                canvas.drawText(
                    if (missingIndex == 0) "?" else "$base",
                    cx,
                    by + 40f,
                    paint
                )
                canvas.drawText(
                    if (missingIndex == 1) "?" else "$height",
                    cx,
                    p1.y + h / 2f,
                    paint
                )
                canvas.drawText(
                    if (missingIndex == 2) "S=?" else "S=$area",
                    cx,
                    p1.y + h / 2f + 80f,
                    paint
                )
            }
        }
    }
}
