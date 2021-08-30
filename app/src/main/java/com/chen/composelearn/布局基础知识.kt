package com.chen.composelearn

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.chen.composelearn.ui.theme.ComposeLearnTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun ArtisCard() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Compose")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
                .clickable {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            "点击了",
                        )
                    }
                },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.katy),
                    contentDescription = null,
                    Modifier
                        .size(
                            size = 60.dp
                        )
                        .clip(
                            shape = RoundedCornerShape(
                                size = 30.dp
                            )
                        )
                )
                Column(
                    modifier = Modifier.padding(
                        start = 20.dp
                    )
                ) {
                    Text(
                        text = "Alfred Sisley", style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(text = "3 minutes ago")
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            Card(elevation = 5.dp, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.mipmap.background),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
            }
            Spacer(modifier = Modifier.padding(top = 10.dp))
            BoxWithConstraints {
                Text(text = "My minHeight is $minHeight while my maxWidth is $maxWidth")
            }

        }
    }
}

/**
 * 自定义Column布局
 */
@Composable
fun MyBasicColumn(
    modifier: Modifier = Modifier,
    content:@Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) {measurables,constraints ->
        val placeables = measurables.map {
            it.measure(constraints = constraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight){
            var pPotion = 0

            placeables.forEach {
                it.placeRelative(0, pPotion)
                pPotion += it.height
            }
        }
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(0, placeableY)
    }
}

val MaxChartValue = HorizontalAlignmentLine(merger = {old, new -> kotlin.math.min(old, new) })
val MinChartValue = HorizontalAlignmentLine(merger = {old, new -> kotlin.math.max(old, new) })

@Composable
fun BarChart(
    dataPoints:List<Int>,
    modifier: Modifier = Modifier
) {
    var maxYBaseline by remember { mutableStateOf(Float.MAX_VALUE) }
    var minYBaseline by remember { mutableStateOf(Float.MIN_VALUE) }

    val maxValue: Float = remember(dataPoints) { dataPoints.maxOrNull()!! * 1.2f }
    val maxDataPoint: Int = remember(dataPoints) { dataPoints.maxOrNull()!! }
    val minDataPoint: Int = remember(dataPoints) { dataPoints.minOrNull()!! }

    Layout(content = {
        BoxWithConstraints(propagateMinConstraints = true) {
            val density = LocalDensity.current
            with(density) {
                val yPositionRatio = remember(density, maxHeight, maxValue) {
                    maxHeight.toPx() / maxValue
                }
                val xPositionRatio = remember(density, maxWidth, dataPoints) {
                    maxWidth.toPx() / (dataPoints.size + 1)
                }
                val xOffset = remember(density) { // center points in the graph
                    xPositionRatio / dataPoints.size
                }

                Canvas(Modifier) {
                    dataPoints.forEachIndexed { index, dataPoint ->
                        val rectSize = Size(60f, dataPoint * yPositionRatio)
                        val topLeftOffset = Offset(
                            x = xPositionRatio * (index + 1) - xOffset,
                            y = (maxValue - dataPoint) * yPositionRatio
                        )
                        drawRect(Color(0xFF3DDC84), topLeftOffset, rectSize)

                        if (maxYBaseline == Float.MAX_VALUE && dataPoint == maxDataPoint) {
                            maxYBaseline = topLeftOffset.y
                        }
                        if (minYBaseline == Float.MIN_VALUE && dataPoint == minDataPoint) {
                            minYBaseline = topLeftOffset.y
                        }
                    }
                    drawLine(
                        Color(0xFF073042),
                        start = Offset(0f, 0f),
                        end = Offset(0f, maxHeight.toPx()),
                        strokeWidth = 6f
                    )
                    drawLine(
                        Color(0xFF073042),
                        start = Offset(0f, maxHeight.toPx()),
                        end = Offset(maxWidth.toPx(), maxHeight.toPx()),
                        strokeWidth = 6f
                    )
                }
            }
        }
    }, modifier = Modifier) {measurableas, constraints ->
        val placeable = measurableas[0].measure(constraints = constraints)
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
            alignmentLines = mapOf(
                MinChartValue to minYBaseline.roundToInt(),
                MaxChartValue to maxYBaseline.roundToInt()
            )
        ) {
            placeable.placeRelative(0,0)
        }
    }
}

@Composable
fun BarChartMinMax(
    dataPoints: List<Int>,
    maxText: @Composable () -> Unit,
    minText: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(content = {
                     maxText()
                     minText()
                     BarChart(dataPoints = dataPoints, modifier = Modifier.size(200.dp))

    }, modifier = modifier) {measurableas, constraints ->
        check(measurableas.size == 3)
        val placeables = measurableas.map {
            it.measure(constraints.copy(minWidth = 0,minHeight = 0))
        }

        val maxTextPlaceable = placeables[0]
        val minTextPlaceable = placeables[1]
        val barChartPlaceable = placeables[2]

        val minValueBaseline = barChartPlaceable[MinChartValue]
        val maxValueBaseline = barChartPlaceable[MaxChartValue]

        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
            maxTextPlaceable.placeRelative(
                x = 0,
                y = maxValueBaseline - (maxTextPlaceable.height / 2)
            )
            minTextPlaceable.placeRelative(
                x = 0,
                y = minValueBaseline - (minTextPlaceable.height / 2)
            )
            barChartPlaceable.placeRelative(
                x = max(maxTextPlaceable.width, minTextPlaceable.width) + 20,
                y = 0
            )
        }
    }
}

//@Composable
//fun BarChartMinMax(
//    dataPoints: List<Int>,
//    maxText: @Composable () -> Unit,
//    minText: @Composable () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Layout(
//        content = {
//            maxText()
//            minText()
//            // Set a fixed size to make the example easier to follow
//            BarChart(dataPoints, Modifier.size(200.dp))
//        },
//        modifier = modifier
//    ) { measurables, constraints ->
//        check(measurables.size == 3)
//        val placeables = measurables.map {
//            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
//        }
//
//        val maxTextPlaceable = placeables[0]
//        val minTextPlaceable = placeables[1]
//        val barChartPlaceable = placeables[2]
//
//        // Obtain the alignment lines from BarChart to position the Text
//        val minValueBaseline = barChartPlaceable[MinChartValue]
//        val maxValueBaseline = barChartPlaceable[MaxChartValue]
//        layout(constraints.maxWidth, constraints.maxHeight) {
//            maxTextPlaceable.placeRelative(
//                x = 0,
//                y = maxValueBaseline - (maxTextPlaceable.height / 2)
//            )
//            minTextPlaceable.placeRelative(
//                x = 0,
//                y = minValueBaseline - (minTextPlaceable.height / 2)
//            )
//            barChartPlaceable.placeRelative(
//                x = max(maxTextPlaceable.width, minTextPlaceable.width) + 20,
//                y = 0
//            )
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ComposeLearnTheme {
        ArtisCard()
    }
}