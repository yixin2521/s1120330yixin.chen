package yixin.com.example.s1120330yixin

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yixin.com.example.s1120330yixin.ui.theme.S1120330yixinTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S1120330yixinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    Start(m = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun Start(m: Modifier) {
    val context = LocalContext.current

    // 背景顏色
    val colors = listOf(
        Color(0xff95fe95),
        Color(0xfffdca0f),
        Color(0xfffea4a4),
        Color(0xffa5dfed)
    )

    var currentIndex by remember { mutableStateOf(0) } // Box 的背景顏色索引
    var isSwiping by remember { mutableStateOf(false) }
    var gameTime by remember { mutableStateOf(0) } // 遊戲持續時間
    var mariaPosition by remember { mutableStateOf(0f) } // 瑪利亞水平位置
    var mariaImageIndex by remember { mutableStateOf(0) } // 隨機生成瑪利亞圖片索引
    var score by remember { mutableStateOf(0) } // 遊戲分數
    var isGameOver by remember { mutableStateOf(false) } // 判斷遊戲是否結束

    val coroutineScope = rememberCoroutineScope()

    // 啟動遊戲邏輯
    LaunchedEffect(isGameOver) {
        // 只有遊戲未結束時才更新時間和移動瑪利亞
        if (!isGameOver) {
            while (!isGameOver && mariaPosition < 1080f) { // 假設螢幕寬度為 1080 像素
                gameTime += 1 // 每秒增加遊戲持續時間
                delay(1000L) // 等待 1 秒
                mariaPosition += 50f // 瑪利亞每秒向右移動
            }
        }
    }

    // 當瑪利亞移出螢幕右側時，停止遊戲時間
    if (mariaPosition >= 1080f) {
        isGameOver = true
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors[currentIndex])
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (!isSwiping) {
                        isSwiping = true
                        change.consume()
                        if (dragAmount > 0) {
                            currentIndex = (currentIndex - 1 + colors.size) % colors.size
                        } else if (dragAmount < 0) {
                            currentIndex = (currentIndex + 1) % colors.size
                        }
                        coroutineScope.launch {
                            delay(500)
                            isSwiping = false
                        }
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "2024期末上機考(資管二A黃千津)",
                style = TextStyle(fontSize = 10.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.class_a),
                contentDescription = "Class Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "遊戲持續時間 $gameTime 秒",
                style = TextStyle(fontSize = 10.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "您的成績 $score 分",
                style = TextStyle(fontSize = 10.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val activity = context as? Activity
                    activity?.finish()
                },
                modifier = Modifier
                    .padding(top = 1.dp)
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
            ) {
                Text(text = "結束App")
            }
        }

        // 瑪利亞圖片
        Image(
            painter = painterResource(id = when (mariaImageIndex) {
                0 -> R.drawable.maria0
                1 -> R.drawable.maria1
                2 -> R.drawable.maria2
                else -> R.drawable.maria3
            }), // 根據索引顯示不同圖片
            contentDescription = "瑪利亞",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = mariaPosition.dp, y = 0.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            // 雙擊檢測
                            if (currentIndex == mariaImageIndex) { // 背景顏色與圖片顏色索引相同
                                score += 1
                            } else {
                                score -= 1
                            }
                            // 重置瑪利亞圖片
                            mariaPosition = 0f
                            mariaImageIndex = (0..3).random()
                        }
                    )
                }
        )
    }
}