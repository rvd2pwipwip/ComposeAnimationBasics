package com.hdesrosiers.composeanimationbasics

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hdesrosiers.composeanimationbasics.ui.theme.ComposeAnimationBasicsTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposeAnimationBasicsTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
        ) {
          JoeBirchDemo()
//          Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//          ) {
//            AnimateContentSizeDemo()
//            Spacer(modifier = Modifier.height(30.dp))
//            LazyColumn(
//              horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//              item {
//                AnimateAsStateDemo()
//                Spacer(modifier = Modifier.height(30.dp))
//                UpdateTransitionDemo()
//                Spacer(modifier = Modifier.height(30.dp))
//                AnimatedVisibilityDemo()
//                Spacer(modifier = Modifier.height(30.dp))
//                CrossFadeDemo()
//                Spacer(modifier = Modifier.height(30.dp))
//              }
//            }
//          }
        }
      }
    }
  }
}

val squareSize = 64.dp

private enum class BoxState {
  Small,
  Large
}

private enum class DemoScene {
  Text,
  Icon
}

@Composable
fun AnimateAsStateDemo() {
  //animate boolean value by wrapping it in animate*AsState
  var blue by remember { mutableStateOf(true) }
//  val color = if (blue) Blue else Red
  val color by animateColorAsState(
    targetValue = if (blue) Blue else Red,
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      onClick = { blue = !blue },
      shape = CircleShape
    ) {
      Text(text = "Change Color")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Box(
      modifier = Modifier
        .size(squareSize)
        .background(color = color)
    )
  }
}

@Composable
fun UpdateTransitionDemo() {
  // create updateTransition(targetState) then use animate* extension function
  // on each animated parameter
  var boxState by remember { mutableStateOf(BoxState.Small) }
  val boxTransition = updateTransition(targetState = boxState, label = null)

  val color by boxTransition.animateColor(label = "boxColorTrans") { state ->
    when (state) {
      BoxState.Small -> Blue
      BoxState.Large -> Red
    }
  }

  val size by boxTransition.animateDp(
    transitionSpec = {
      if (targetState == BoxState.Large) {
        spring(
          dampingRatio = Spring.DampingRatioLowBouncy,
          stiffness = Spring.StiffnessVeryLow
        )
      } else {
        spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium
        )
      }
    },
    label = "boxSizeTrans"
  ) { state ->
    when (state) {
      BoxState.Small -> squareSize / 2
      BoxState.Large -> squareSize
    }
  }


  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      onClick = {
        boxState = when (boxState) {
          BoxState.Small -> BoxState.Large
          BoxState.Large -> BoxState.Small
        }
      },
      shape = CircleShape
    ) {
      Text(text = "Change Color and Size")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Box(
      modifier = Modifier
        .size(size = size)
        .background(color = color)
    )
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityDemo() {
  //replace if(visible) by AnimatedVisibility(visible)
  var visible by remember { mutableStateOf(true) }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      onClick = { visible = !visible },
      shape = CircleShape
    ) {
      Text(text = if (visible) "Hide" else "Show")
    }
    Spacer(modifier = Modifier.height(16.dp))

    AnimatedVisibility(visible) {
      Box(
        modifier = Modifier
          .size(squareSize)
          .background(color = Blue)
      )
    }
  }
}

@Composable
fun AnimateContentSizeDemo() {
  //animateContentSize() with Box modifier
  var expanded by remember { mutableStateOf(false) }

  Button(
    onClick = { expanded = !expanded },
    shape = CircleShape
  ) {
    Text(text = if (expanded) "Collapse" else "Expand")
  }
  Spacer(modifier = Modifier.height(16.dp))

  LazyColumn(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    item {
      Box(
        modifier = Modifier
          .background(color = Color.LightGray)
          .animateContentSize()
      ) {
        Text(
          text = stringResource(id = R.string.lorem),
          fontSize = 16.sp,
          textAlign = TextAlign.Justify,
          modifier = Modifier.padding(16.dp),
          maxLines = if (expanded) Int.MAX_VALUE else 3
        )
      }
    }
  }
}

@Composable
fun CrossFadeDemo() {
  //wrap scene state switch in Crossfade composable
  var scene by remember { mutableStateOf(DemoScene.Text) }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      onClick = {
        scene = when (scene) {
          DemoScene.Text -> DemoScene.Icon
          DemoScene.Icon -> DemoScene.Text
        }
      },
      shape = CircleShape
    ) {
      Text(text = "Toggle")
    }
    Spacer(modifier = Modifier.height(16.dp))

    Crossfade(targetState = scene) { scene ->
      when (scene) {
        DemoScene.Text -> Text(
          text = "Phone",
          fontSize = 32.sp
        )
        DemoScene.Icon -> Icon(
          imageVector = Icons.Default.Phone,
          contentDescription = null,
          modifier = Modifier.size(64.dp)
        )
      }
    }
  }
}




























