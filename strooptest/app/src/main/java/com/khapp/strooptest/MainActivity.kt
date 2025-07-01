package com.khapp.strooptest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.*
import androidx.compose.runtime.*


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.CircleShape

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import kotlinx.coroutines.delay

import com.khapp.strooptest.ui.theme.strooptestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 상단바 등 시스템 UI를 덮음
        enableEdgeToEdge()
        setContent {
            strooptestTheme {
                MiniGamesApp()
            }
        }
    }
}

// 앱의 진입지점
@Composable
fun MiniGamesApp() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") { MainScreen(navController) }
            (1..4).forEach { gameId ->
                composable("game${gameId}_main") { GameMainScreen(navController, gameId) }
                // 게임화면 설명
                composable("game${gameId}_screen_desc") { GameScreenDesc(navController, gameId) }
                // 게임방법 설명
                composable("game${gameId}_play_desc") { GamePlayDesc(navController, gameId) }
                // 레벨선택 화면
                (1..7).forEach { level ->
                    composable("game${gameId}_level$level") {
                        GameLevelScreen(
                            navController,
                            gameId,
                            level
                        )
                    }
                }
            }
        }
    }
}

// 메인화면
@Composable
fun MainScreen(navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Mini Games",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        (1..4).forEach { gameId ->
            Button(
                onClick = { navController.navigate("game${gameId}_main") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) { Text("미니게임 $gameId") }
        }
    }
}

// 각 게임별 메인화면
@Composable
fun GameMainScreen(navController: NavController, gameId: Int) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("미니게임 $gameId", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        (1..7).forEach { level ->
            Button(
                onClick = { navController.navigate("game${gameId}_level$level") },
                modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 4.dp)
            ) { Text("레벨 $level") }
        }
        Button(
            onClick = { navController.navigate("game${gameId}_screen_desc") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 4.dp)
        ) { Text("게임화면설명") }
        Button(
            onClick = { navController.navigate("game${gameId}_play_desc") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 4.dp)
        ) { Text("게임방법설명") }
        Button(
            onClick = { navController.navigate("main") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 4.dp)
        ) { Text("메인화면 돌아가기") }
    }
}

// 게임화면 설명
// gameId로 통제
@Composable
fun GameScreenDesc(navController: NavController, gameId: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.game_ui_1),
                contentDescription = "게임화면설명",
                modifier = Modifier.size(200.dp)
            )
            Text("설명", modifier = Modifier.padding(top = 16.dp))
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("메인으로")
        }
    }
}

// 게임방법 설명
// gameId로 통제
@Composable
fun GamePlayDesc(navController: NavController, gameId: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.gameplay_description_1),
                contentDescription = "게임방법설명",
                modifier = Modifier.size(200.dp)
            )
            Text("설명", modifier = Modifier.padding(top = 16.dp))
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("메인으로")
        }
    }
}

//실제 게임 실행화면
@Composable
fun GameLevelScreen(
    navController: NavController,
    gameId: Int,
    level: Int
) {
    // 상태 변수들
    var timeLeft by remember { mutableIntStateOf(120) }
    var isRunning by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var reactionTime by remember { mutableStateOf("--") }
    var response by remember { mutableStateOf("Ready") }
    var showImage by remember { mutableStateOf(false) }
    var startTime by remember { mutableLongStateOf(0L) }

    // 타이머 로직
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false
            // TODO: 게임 종료 처리
        }
    }

    // 타이머 포맷
    fun formatTime(sec: Int): String = String.format("%02d:%02d", sec / 60, sec % 60)

    // Start 버튼 클릭
    fun onStart() {
        isRunning = true
        timeLeft = 120
        score = 0
        reactionTime = "--"
        response = "Go"
        showImage = true
        startTime = System.currentTimeMillis()
    }

    // Reset 버튼 클릭
    fun onReset() {
        isRunning = false
        timeLeft = 120
        score = 0
        reactionTime = "--"
        response = "Ready"
        showImage = false
        startTime = 0L
    }

    // 상단 2x4 그리드
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Column1 (RowSpan 4): 원형 타이머
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                // Circular Timer
                CircularProgressIndicator(
                    progress = { timeLeft / 120f },
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 10.dp,
                    color = Color.Blue,
                    trackColor = Color.LightGray,
                )
                Text(
                    text = formatTime(timeLeft),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Column2: 4 Row
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Row1: 버튼 2개
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onStart() },
                        enabled = !isRunning
                    ) { Text("Start") }
                    Button(
                        onClick = { onReset() }
                    ) { Text("Reset") }
                }
                // Row2: 점수
                Text(
                    text = "점수: $score",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
                // Row3: 반응시간
                Text(
                    text = "반응시간: $reactionTime ms",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
                // Row4: Response
                Text(
                    text = "Response: $response",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RuleGrid: 중앙 이미지와 하단 버튼 사이에 배치
        RuleGrid(
            rule1 = "규칙 1",
            rule2 = "규칙 2",
            photoResId = R.drawable.sample_game_image,
            meaning = "의미",
            text = "글자",
            color = Color.Red,
            isGameMode = isRunning,
            showImage = showImage
        )

        Spacer(modifier = Modifier.height(24.dp))
        // 중앙: 게임 이미지 표시
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (showImage) {
                // 예시 이미지, 실제 리소스에 맞게 교체
                Image(
                    painter = painterResource(id = R.drawable.sample_game_image),
                    contentDescription = "Game Stimulus",
                    modifier = Modifier.size(180.dp)
                )
            }
        }
    }
}

// 규칙표시
@Composable
fun RuleGrid(
    rule1: String,
    rule2: String,
    photoResId: Int,
    meaning: String,
    text: String,
    color: Color,
    isGameMode: Boolean,
    showImage: Boolean
) {
    // 게임 모드 랜덤 표시 상태
    var showPhotoOrMeaning by remember { mutableStateOf(true) } // true: 사진, false: 의미
    var showTextOrColor by remember { mutableStateOf(true) }    // true: 글자, false: 색상

    // 이미지가 새로 출력될 때마다 랜덤 갱신
    LaunchedEffect(showImage) {
        if (isGameMode && showImage) {
            showPhotoOrMeaning = (0..1).random() == 0
            showTextOrColor = (0..1).random() == 0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 1행: 규칙1, 규칙2
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = rule1,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = rule2,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 2행: 사진 or 의미
        Row(Modifier.fillMaxWidth()) {
            if (!isGameMode || (isGameMode && showPhotoOrMeaning)) {
                // 사진
                Image(
                    painter = painterResource(id = photoResId),
                    contentDescription = "사진",
                    modifier = Modifier
                        .weight(1f)
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                )
            } else {
                // 의미
                Text(
                    text = meaning,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (!isGameMode || (isGameMode && !showPhotoOrMeaning)) {
                // 의미
                Text(
                    text = meaning,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            } else {
                // 사진
                Image(
                    painter = painterResource(id = photoResId),
                    contentDescription = "사진",
                    modifier = Modifier
                        .weight(1f)
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 3행: 글자 or 색상
        Row(Modifier.fillMaxWidth()) {
            if (!isGameMode || (isGameMode && showTextOrColor)) {
                // 글자
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                // 색상
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .background(color, shape = CircleShape)
                        .align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (!isGameMode || (isGameMode && !showTextOrColor)) {
                // 색상
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .background(color, shape = CircleShape)
                        .align(Alignment.CenterVertically)
                )
            } else {
                // 글자
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    strooptestTheme {
        MainScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    strooptestTheme {
        GameMainScreen(navController = rememberNavController(), gameId = 1)
    }
}
