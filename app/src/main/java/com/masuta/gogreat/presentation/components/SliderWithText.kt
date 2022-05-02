package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SliderWithLabel(
    value: Float,
    selectedItem: MutableState<Int>,
    valueRange: ClosedFloatingPointRange<Float>,
    finiteEnd: Boolean,
    labelMinWidth: Dp = 24.dp,
    items: List<Int>
) {

    var sliderPosition by remember { mutableStateOf(value) }

    selectedItem.value = items[sliderPosition.toInt()]

    Column {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
            },
            steps = items.size - 2,
            valueRange = valueRange,
            modifier = Modifier.fillMaxWidth()
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val offset = getSliderOffset(
                value = sliderPosition,
                valueRange = valueRange,
                boxWidth = maxWidth,
                labelWidth = labelMinWidth + 8.dp
            )

            val endValueText = if (!finiteEnd && sliderPosition >= valueRange.endInclusive)
                "${sliderPosition.toInt()} +" else items[sliderPosition.toInt()]

            if (sliderPosition >= valueRange.start) {
                SliderLabel(
                    label = endValueText.toString(),
                    minWidth = labelMinWidth,
                    modifier = Modifier
                        .padding(start = if(sliderPosition.toInt() != items.size - 1) offset else offset - 16.dp)
                )
            }
        }
    }
}

@Composable
fun SliderLabel(
    label: String,
    minWidth: Dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        textAlign = TextAlign.Center,
        color = Color.White,
        softWrap = false,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .defaultMinSize(minWidth = minWidth)
    )
}

private fun getSliderOffset(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    boxWidth: Dp,
    labelWidth: Dp
): Dp {
    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val positionFraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)

    return (boxWidth - labelWidth) * positionFraction
}

private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f,1f)

@Composable
fun SliderWithLabelUserActivity(
    selectedItem: MutableState<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    finiteEnd: Boolean = true,
    enabled: Boolean = true,
    labelMinWidth: Dp = 24.dp,
    items: List<Any?>
) {
    Column {
        Slider(
            value = selectedItem.value,
            onValueChange = {
                selectedItem.value = it
            },
            steps = items.size - 2,
            valueRange = valueRange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val offset = getSliderOffset(
                value = selectedItem.value,
                valueRange = valueRange,
                boxWidth = maxWidth,
                labelWidth = labelMinWidth + 8.dp
            )

            val endValueText = if (!finiteEnd && selectedItem.value >= valueRange.endInclusive)
                "${selectedItem.value.toInt()} +" else items[selectedItem.value.toInt()]

            if (selectedItem.value >= valueRange.start) {
                SliderLabel(
                    label = endValueText.toString(),
                    minWidth = labelMinWidth,
                    modifier = Modifier
                        .padding(start = offset)
                )
            }
        }
    }
}