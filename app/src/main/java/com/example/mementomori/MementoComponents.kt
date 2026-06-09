package com.example.mementomori

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import java.time.LocalDate
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneOffset
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.clickable
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun LifeStatCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HeroLifeCard(
    weeksLived: Long,
    totalWeeks: Long,
    percentLived: Double,
    quote: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Semana ${formatWholeNumber(weeksLived)} de ${formatWholeNumber(totalWeeks)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = quote,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (percentLived / 100).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

            Text(
                text = "${formatPercentage(percentLived)} de vida estimada completada",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LifeCalendarCard(
    weeksLived: Long,
    totalWeeks: Long
) {
    val columns = LIFE_CALENDAR_COLUMNS
    val totalWeeksInt = totalWeeks.toInt()
    val weeksLivedInt = weeksLived.toInt().coerceAtMost(totalWeeksInt)
    val rows = (totalWeeksInt + columns - 1) / columns

    val livedColor = MaterialTheme.colorScheme.primary
    val futureColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Calendario de vida",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Cada punto representa una semana.",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((rows * 8).dp)
            ) {
                val cellWidth = size.width / columns
                val cellHeight = 8.dp.toPx()
                val radius = 2.2.dp.toPx()

                for (week in 0 until totalWeeksInt) {
                    val row = week / columns
                    val column = week % columns

                    val x = column * cellWidth + cellWidth / 2
                    val y = row * cellHeight + cellHeight / 2

                    drawCircle(
                        color = if (week < weeksLivedInt) livedColor else futureColor,
                        radius = radius,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

data class SelectedLifeWeek(
    val weekIndex: Int,
    val ageYear: Int,
    val weekOfYear: Int,
    val status: String
)

@Composable
fun LifeCalendarLargeCard(
    weeksLived: Long,
    lifeExpectancyYears: Int,
    selectedWeek: SelectedLifeWeek?,
    onWeekSelected: (SelectedLifeWeek?) -> Unit
) {
    val columns = LIFE_CALENDAR_COLUMNS
    val rows = lifeExpectancyYears.coerceAtLeast(1)
    val totalVisualWeeks = rows * columns
    val weeksLivedInt = weeksLived.toInt().coerceIn(0, totalVisualWeeks)

    val livedColor = MaterialTheme.colorScheme.primary
    val futureColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
    val markerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
    val currentWeekColor = MaterialTheme.colorScheme.onSurface
    val selectedWeekColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.onSurface

    val rowHeight = 15.dp
    val extraGapEveryTenYears = 12.dp
    val decadeCount = rows / 10
    val totalHeight = (rows * rowHeight.value + decadeCount * extraGapEveryTenYears.value).dp

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${formatWholeNumber(weeksLived)} semanas vividas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Cada punto, una semana. Toca para saber más",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendDot(color = livedColor)
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Vividas", fontSize = 13.sp)

            Spacer(modifier = Modifier.size(18.dp))

            LegendDot(color = futureColor)
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Futuras", fontSize = 13.sp)

            Spacer(modifier = Modifier.size(18.dp))

            LegendCurrentWeekDot(color = currentWeekColor)
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Actual", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalHeight)
                .pointerInput(weeksLivedInt, rows) {
                    detectTapGestures { tapOffset ->
                        val labelWidth = 30.dp.toPx()
                        val availableWidth = size.width.toFloat() - labelWidth
                        val cellWidth = availableWidth / columns

                        val rowHeightPx = rowHeight.toPx()
                        val extraGapPx = extraGapEveryTenYears.toPx()

                        if (tapOffset.x < labelWidth) {
                            onWeekSelected(null)
                            return@detectTapGestures
                        }

                        val weekOfYear = ((tapOffset.x - labelWidth) / cellWidth)
                            .toInt()
                            .coerceIn(0, columns - 1)

                        var tappedYear: Int? = null

                        for (year in 0 until rows) {
                            val completedDecadesBefore = year / 10
                            val top = year * rowHeightPx + completedDecadesBefore * extraGapPx
                            val bottom = top + rowHeightPx

                            if (tapOffset.y in top..bottom) {
                                tappedYear = year
                                break
                            }
                        }

                        val year = tappedYear

                        if (year == null) {
                            onWeekSelected(null)
                            return@detectTapGestures
                        }

                        val weekIndex = year * columns + weekOfYear

                        if (weekIndex !in 0 until totalVisualWeeks) {
                            onWeekSelected(null)
                            return@detectTapGestures
                        }

                        val status = when {
                            weekIndex < weeksLivedInt -> "Vivida"
                            weekIndex == weeksLivedInt -> "Semana actual"
                            else -> "Por vivir"
                        }

                        onWeekSelected(
                            SelectedLifeWeek(
                                weekIndex = weekIndex,
                                ageYear = year,
                                weekOfYear = weekOfYear + 1,
                                status = status
                            )
                        )
                    }
                }
        ) {
            val labelWidth = 30.dp.toPx()
            val availableWidth = size.width - labelWidth
            val cellWidth = availableWidth / columns

            val rowHeightPx = rowHeight.toPx()
            val extraGapPx = extraGapEveryTenYears.toPx()
            val radius = 2.45.dp.toPx()
            val currentWeekRadius = 4.2.dp.toPx()
            val selectedWeekRadius = 5.1.dp.toPx()

            val textPaint = android.graphics.Paint().apply {
                isAntiAlias = true
                textSize = 11.sp.toPx()
                color = textColor.toArgb()
            }

            fun rowTop(yearIndex: Int): Float {
                val completedDecadesBefore = yearIndex / 10
                return yearIndex * rowHeightPx + completedDecadesBefore * extraGapPx
            }

            for (year in 0 until rows) {
                val top = rowTop(year)
                val centerY = top + rowHeightPx / 2

                for (weekOfYear in 0 until columns) {
                    val weekIndex = year * columns + weekOfYear
                    val x = labelWidth + weekOfYear * cellWidth + cellWidth / 2

                    drawCircle(
                        color = if (weekIndex < weeksLivedInt) livedColor else futureColor,
                        radius = radius,
                        center = Offset(x, centerY)
                    )

                    if (weekIndex == weeksLivedInt) {
                        drawCircle(
                            color = currentWeekColor,
                            radius = currentWeekRadius,
                            center = Offset(x, centerY),
                            style = Stroke(width = 1.6.dp.toPx())
                        )
                    }

                    if (selectedWeek?.weekIndex == weekIndex) {
                        drawCircle(
                            color = selectedWeekColor,
                            radius = selectedWeekRadius,
                            center = Offset(x, centerY),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }

            for (age in 10..rows step 10) {
                val y = rowTop(age)

                drawLine(
                    color = markerColor,
                    start = Offset(labelWidth, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.5.dp.toPx()
                )

                drawContext.canvas.nativeCanvas.drawText(
                    age.toString(),
                    0f,
                    y - 2.dp.toPx(),
                    textPaint
                )
            }
        }
    }
}

@Composable
fun SelectedLifeWeekBottomBar(
    week: SelectedLifeWeek,
    onClearClick: () -> Unit
) {
    Surface(
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Semana seleccionada",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Edad ${week.ageYear} · Semana ${week.weekOfYear} · ${week.status}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (week.status) {
                        "Semana actual" -> MaterialTheme.colorScheme.secondary
                        "Vivida" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            TextButton(onClick = onClearClick) {
                Text("Cerrar")
            }
        }
    }
}

@Composable
fun SelectedLifeWeekCard(
    week: SelectedLifeWeek
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Semana seleccionada",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Edad aproximada: ${week.ageYear} años",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Semana ${week.weekOfYear} del año",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Estado: ${week.status}",
                fontSize = 14.sp,
                color = when (week.status) {
                    "Semana actual" -> MaterialTheme.colorScheme.secondary
                    "Vivida" -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LegendDot(color: androidx.compose.ui.graphics.Color) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(10.dp)
            .background(color = color, shape = CircleShape)
    )
}

@Composable
fun LegendCurrentWeekDot(color: androidx.compose.ui.graphics.Color) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier.size(12.dp)
    ) {
        drawCircle(
            color = color,
            radius = 5.dp.toPx(),
            style = Stroke(width = 1.5.dp.toPx())
        )
    }
}

@Composable
fun MementoSettingsForm(
    draftBirthDate: LocalDate,
    draftLifeExpectancy: String,
    onBirthDateClick: () -> Unit,
    onLifeExpectancyChange: (String) -> Unit,
    onSaveClick: (LocalDate, Int) -> Unit,
    onClearClick: (() -> Unit)? = null
){
    val validation = validateMementoSettings(
        birthDate = draftBirthDate,
        lifeExpectancyText = draftLifeExpectancy
    )

    Text(
        text = "Tu información",
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = onBirthDateClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Fecha de nacimiento: ${formatDisplayDate(draftBirthDate)}")
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = draftLifeExpectancy,
        onValueChange = { newValue ->
            onLifeExpectancyChange(newValue.filter { char -> char.isDigit() })
        },
        label = { Text("Esperanza de vida en años") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (!validation.isValidLifeExpectancy) {
        Text(
            text = "Ingresa una esperanza de vida válida, entre $MIN_LIFE_EXPECTANCY_YEARS y $MAX_LIFE_EXPECTANCY_YEARS."
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (!validation.isValidBirthDate) {
        Text(text = "La fecha de nacimiento no puede estar en el futuro.")
        Spacer(modifier = Modifier.height(8.dp))
    }

    Button(
        onClick = {
            val lifeValue = validation.lifeExpectancyYears

            if (validation.canSave && lifeValue != null) {
                onSaveClick(draftBirthDate, lifeValue)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Guardar")
    }

    if (onClearClick != null) {
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Borrar datos locales",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MementoBirthDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis

                    if (selectedMillis != null) {
                        val selectedDate = Instant
                            .ofEpochMilli(selectedMillis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()

                        onDateSelected(selectedDate)
                    }

                    onDismiss()
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }

}

@Composable
fun ConfirmClearDataDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("¿Borrar datos locales?")
        },
        text = {
            Text(
                "Esto eliminará tu fecha de nacimiento y esperanza de vida guardadas en este dispositivo.\n\n" +
                        "La app no usa cuentas ni sincroniza datos, así que no se borrará nada fuera de este teléfono."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Borrar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun MementoIntroScreen(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Memento Mori",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Esta app es tuya.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tu fecha de nacimiento y esperanza de vida se guardan solo en este dispositivo.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay cuenta.\nNo hay internet.\nNo hay sincronización.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "“No es que tengamos poco tiempo, sino que desperdiciamos mucho.”",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "— Séneca",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comenzar")
        }
    }
}
@Composable
fun PrivacyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Privacidad",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Esta app no usa cuentas, internet ni sincronización.",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tu fecha de nacimiento, frases, y esperanza de vida se guardan solo en este dispositivo.",
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AppSignature() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Built for you and your finite time, with love.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "- Bourbaki",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CompactBottomBar(
    selectedTab: MementoTab,
    onTabSelected: (MementoTab) -> Unit
) {
    Surface(
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompactBottomBarItem(
                label = "Hoy",
                icon = Icons.Filled.Home,
                selected = selectedTab == MementoTab.Today,
                onClick = { onTabSelected(MementoTab.Today) }
            )

            CompactBottomBarItem(
                label = "Calendario",
                icon = Icons.Filled.DateRange,
                selected = selectedTab == MementoTab.Calendar,
                onClick = { onTabSelected(MementoTab.Calendar) }
            )

            CompactBottomBarItem(
                label = "Ajustes",
                icon = Icons.Filled.Settings,
                selected = selectedTab == MementoTab.Settings,
                onClick = { onTabSelected(MementoTab.Settings) }
            )
        }
    }
}

@Composable
fun CompactBottomBarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
    }

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color
        )

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
@Composable
fun CustomQuotesCard(
    customQuotes: List<String>,
    onAddQuote: (String) -> Unit,
    onDeleteQuote: (String) -> Unit
) {
    var newQuote by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Frases personalizadas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Agrega frases propias a la rotación diaria. Se guardan solo en este dispositivo.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newQuote,
                onValueChange = { newQuote = it },
                label = { Text("Nueva frase") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val cleanQuote = newQuote.trim()

                    if (cleanQuote.isNotBlank()) {
                        onAddQuote(cleanQuote)
                        newQuote = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar frase")
            }

            if (customQuotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tus frases",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                customQuotes.forEach { quote ->
                    CustomQuoteRow(
                        quote = quote,
                        onDeleteClick = {
                            onDeleteQuote(quote)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomQuoteRow(
    quote: String,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "“$quote”",
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        TextButton(onClick = onDeleteClick) {
            Text(
                text = "Borrar",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun MementoEasterEggDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Never forget")
        },
        text = {
            Text(
                "The beauty of it all is that everything you love has an expiration date.\n\n" +
                        "With love, Bourbaki."
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Live my life")
            }
        }
    )
}