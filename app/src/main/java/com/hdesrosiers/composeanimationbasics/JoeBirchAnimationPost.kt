package com.hdesrosiers.composeanimationbasics

import android.graphics.drawable.VectorDrawable
import android.provider.ContactsContract
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Indicator
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//////https://joebirch.co/android/building-an-exploding-fab-transition-in-jetpack-compose/
@Composable
fun JoeBirchDemo() {
  Scaffold(
    topBar = {
      TopAppBar(title = {
        Text(
          text = "M",
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }, elevation = 0.dp)
    },
    floatingActionButton = {
      var sizeState by remember { mutableStateOf(48.dp) }
      FloatingActionButton(
        onClick = {
          sizeState += 50.dp
        },
        modifier = Modifier
          .size(sizeState)
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
      }
    },
    content = {
      Column {
        IconTab()
        CombinedTabWithCustomIndicator()
      }
    }
  )
}

@Composable
fun ExplodingShape(modifier: Modifier = Modifier) {
  var sizeState by remember { mutableStateOf(48.dp) }
  Surface(
    shape = CircleShape,
    color = Color.Red,
    modifier = Modifier
      .size(sizeState)
      .clickable { sizeState += 48.dp }

  ) {

  }
}

enum class FabState { Idle, Exploded }

@Composable
fun IconTab() {
  var tabIndex by remember { mutableStateOf(0) }
  val tabData = listOf(
//    "Music" to painterResource(id = R.drawable.ic_music),
    "Music" to Icons.Filled.MusicNote,
    "Market" to Icons.Filled.ShoppingCart,
    "Films" to Icons.Filled.LocalMovies,
    "Books" to Icons.Filled.MenuBook,
  )

  TabRow(
    selectedTabIndex = tabIndex,
    indicator = { tabPositions ->
      Indicator(
        color = Color.Red,
        height = 4.dp,
        modifier = Modifier
          .tabIndicatorOffset(tabPositions[tabIndex])
      )
    }
  ) {
    tabData.forEachIndexed { index, pair ->
      Tab(
        selected = tabIndex == index,
        onClick = { tabIndex = index },
        text = { Text(text = pair.first) },
        icon = { Icon(imageVector = pair.second, contentDescription = null) },
      )
    }
  }
}

@Composable
fun CustomTab() {
  var tabIndex by remember { mutableStateOf(0) }
  val tabData = listOf(
    "MUSIC",
    "MARKET",
    "FILMS",
    "BOOKS",
  )
  TabRow(selectedTabIndex = tabIndex) {
    tabData.forEachIndexed { index, text ->
      val selected = tabIndex == index

      Tab(
        selected = selected,
        onClick = { tabIndex = index }
      ) {
        Column(
          modifier = Modifier
            .padding(10.dp)
            .height(50.dp)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Box(
            Modifier
              .size(24.dp)
              .align(Alignment.CenterHorizontally)
              .background(color = if (selected) Color.Red else Color.White)
          )
          Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
        }
      }
    }
  }
}

@Composable
fun CombinedTabWithCustomIndicator() {
  var tabIndex by remember { mutableStateOf(0) }
  val tabData = listOf(
    "MUSIC" to Icons.Filled.Home,
    "MARKET" to Icons.Filled.ShoppingCart,
    "FILMS" to Icons.Filled.AccountBox,
    "BOOKS" to Icons.Filled.Settings,
  )
  TabRow(
    selectedTabIndex = tabIndex,
    backgroundColor = Color.Yellow,
    contentColor = Color.Black,
    divider = {
      TabRowDefaults.Divider(
        thickness = 5.dp,
        color = Color.Blue
      )
    },
    indicator = { tabPositions ->
      TabRowDefaults.Indicator(
        modifier = Modifier.customTabIndicatorOffset(tabPositions[tabIndex]),
        height = 5.dp,
        color = Color.Green
      )
    }
  ) {
    tabData.forEachIndexed { index, pair ->
      Tab(selected = tabIndex == index, onClick = {
        tabIndex = index
      }, text = {
        Text(text = pair.first)
      }, icon = {
        Icon(imageVector = pair.second, contentDescription = null)
      })
    }
  }
}

fun Modifier.customTabIndicatorOffset(
  currentTabPosition: TabPosition
): Modifier = composed(
  inspectorInfo = debugInspectorInfo {
    name = "tabIndicatorOffset"
    value = currentTabPosition
  }
) {
  val indicatorWidth = 32.dp
  val currentTabWidth = currentTabPosition.width
  val indicatorOffset by animateDpAsState(
    targetValue = currentTabPosition.left + currentTabWidth / 2 - indicatorWidth / 2,
    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
  )
  fillMaxWidth()
    .wrapContentSize(Alignment.BottomStart)
    .offset(x = indicatorOffset)
    .width(indicatorWidth)
}


@Preview
@Composable
fun JoeBirchDemoPreview() {
  JoeBirchDemo()
}