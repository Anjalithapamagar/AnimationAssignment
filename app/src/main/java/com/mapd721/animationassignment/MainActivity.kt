package com.mapd721.animationassignment

import android.os.Bundle
import android.view.VelocityTracker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mapd721.animationassignment.ui.theme.AnimationAssignmentTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationAssignmentTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("screen1") { Screen1() }
                    composable("screen2") { Screen2() }
                    composable("screen3") { Screen3() }
                    composable("screen4") { Screen4() }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("screen1") }) {
            Text(text = "Animation 1 Demo")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("screen2") }) {
            Text(text = "Animation 2 Demo")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("screen3") }) {
            Text(text = "Animation 3 Demo")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("screen4") }) {
            Text(text = "Animation 4 Demo")
        }
        Spacer(modifier = Modifier.height(200.dp))

        Card(
            modifier = Modifier
                .width(300.dp)
                .heightIn(min = 56.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Anjali Thapa Magar", fontWeight = FontWeight.Bold)
                Text(text = "301372527", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun Screen1() {
    AnimatedContent()
}

@Composable
fun Screen2() {
    AnimatedVisibility()
}

@Composable
fun Screen3() {
    RememberInfiniteTransition()
}

@Composable
fun Screen4() {
    GestureBasedAnimation()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContent(){
    var count by remember { mutableStateOf(0) }
    var screenBackgroundColor by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        count++
                        screenBackgroundColor = if (count % 2 == 0) Color.Blue else Color.Gray
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }, label = ""
            ) { targetCount ->
                Text(
                    text = "Count: $targetCount",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibility() {
    var selected by remember { mutableStateOf(false) }
    val transition = updateTransition(selected, label = "selected state")
    val borderColor by transition.animateColor(label = "border color") { isSelected ->
        if (isSelected) Color.Magenta else Color.Blue
    }
    val elevation by transition.animateDp(label = "elevation") { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Animation 2 Demo",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Surface(
            onClick = { selected = !selected },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, borderColor),
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = elevation
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                transition.AnimatedVisibility(
                    visible = { targetSelected -> targetSelected },
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Text(text = "Welcome to MAPD721 course.")
                }
                transition.AnimatedContent { targetState ->
                    if (targetState) {
                        Text(text = "It is good day to read this course.")
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person",
                            modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            onClick = { selected = !selected },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, borderColor),
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = elevation
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                transition.AnimatedVisibility(
                    visible = { targetSelected -> targetSelected },
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Text(
                        text = stringResource(id = R.string.student),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                transition.AnimatedContent { targetState ->
                    if (targetState) {
                        Text(text = "This is Anjali Thapa Magar", modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RememberInfiniteTransition() {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val colors = listOf(Color.Red, Color.Magenta, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Gray, Color.DarkGray, Color.Black)

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        repeat(3) { rowIndex ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                repeat(3) { colIndex ->
                    val colorIndex = rowIndex * 3 + colIndex
                    val color = colors[colorIndex % colors.size]
                    val animatedColor by infiniteTransition.animateColor(
                        initialValue = color,
                        targetValue = color.copy(alpha = 0.2f),
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000),
                            repeatMode = RepeatMode.Reverse
                        ), label = ""
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(animatedColor)
                    )
                }
            }
        }
    }
}

@Composable
fun GestureBasedAnimation() {
    var offsetX by remember { mutableStateOf(0f) }

    val onDismissed = {
        println("Item dismissed")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeToDismiss(onDismissed)
            .background(Color.Gray)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Swipe me to dismiss",
            modifier = Modifier.padding(16.dp)
        )
    }
}

fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                val velocityTracker = androidx.compose.ui.input.pointer.util.VelocityTracker()
                offsetX.stop()
                awaitPointerEventScope {
                    val pointerId = awaitFirstDown().id

                    horizontalDrag(pointerId) { change ->
                        launch {
                            offsetX.snapTo(
                                offsetX.value + change.positionChange().x
                            )
                        }
                        velocityTracker.addPosition(
                            change.uptimeMillis,
                            change.position
                        )
                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(
                    offsetX.value,
                    velocity
                )
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        offsetX.animateTo(
                            targetValue = 0f,
                            initialVelocity = velocity
                        )
                    } else {
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

/*
@Composable
fun GestureBasedAnimation() {
    var sizeState by remember { mutableStateOf(SizeState.Small) }

    val size by animateDpAsState(
        targetValue = if (sizeState == SizeState.Small) 100.dp else 200.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )

    Box(
        modifier = Modifier
            .size(size)
            .background(Color.Blue)
            .clickable {
                sizeState = if (sizeState == SizeState.Small) SizeState.Big else SizeState.Small
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Tap to Resize", color = Color.White)
    }
}

enum class SizeState { Small, Big }
 */

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("screen1") { Screen1() }
        composable("screen2") { Screen2() }
        composable("screen3") { Screen3() }
        composable("screen4") { Screen4() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}
