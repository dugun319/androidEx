package com.khapp.strooptest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke

import androidx.compose.material3.*
import androidx.compose.runtime.*


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign

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
        Modifier
            .fillMaxSize()
            .padding(top = 32.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 상단 300x300 이미지
        Image(
            painter = painterResource(id = R.drawable.main_page_image),
            contentDescription = "메인 이미지",
            modifier = Modifier.size(350.dp)
        )

        // 2x2 버튼 그리드
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            (0..1).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (1..2).forEach { col ->
                        val gameId = row * 2 + col
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val gameName = when (gameId) {
                                1 -> "Stroop"
                                2 -> "StroopFruit"
                                3 -> "MoleGame"
                                4 -> "RockScissorsPapers"
                                else -> "미니게임 $gameId"
                            }

                            Text(gameName)
                            Button(
                                onClick = { navController.navigate("game${gameId}_main") },
                                modifier = Modifier.size(150.dp),
                                shape = RoundedCornerShape(24.dp), // 둥근 사각형
                                contentPadding = PaddingValues(0.dp), // 이미지 stretch
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White // 배경색 흰색
                                ),
                                border = BorderStroke(3.dp, Color(0xFF001970)), // 파란색 계열 테두리
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = when (gameId) {
                                        1 -> R.drawable.button_image_1
                                        2 -> R.drawable.button_image_2
                                        3 -> R.drawable.button_image_3
                                        4 -> R.drawable.button_image_4
                                        else -> R.drawable.button_image_1
                                    }),
                                    contentDescription = "미니게임 $gameId 이미지",
                                    modifier = Modifier.size(150.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 각 게임별 메인화면
@Composable
fun GameMainScreen(navController: NavController, gameId: Int) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "미니게임 $gameId",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        (1..7).forEach { level ->
            Button(
                onClick = { navController.navigate("game${gameId}_level$level") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 4.dp)
            ) { Text("레벨 $level") }
        }
        Button(
            onClick = { navController.navigate("game${gameId}_screen_desc") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 4.dp)
        ) { Text("게임화면설명") }
        Button(
            onClick = { navController.navigate("game${gameId}_play_desc") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 4.dp)
        ) { Text("게임방법설명") }
        Button(
            onClick = { navController.navigate("main") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 4.dp)
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
    var showButtons by remember { mutableStateOf(false) }
    var startTime by remember { mutableLongStateOf(0L) }
    var currentAnswerKey by remember { mutableIntStateOf(-1) } // 정답키 저장
    var answerKeyReceived by remember { mutableIntStateOf(-1) } // 정답키 저장 여부
    var roundId by remember { mutableIntStateOf(0) } // 라운드 구분

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
    @SuppressLint("DefaultLocale")
    fun formatTime(sec: Int): String = String.format("%02d:%02d", sec / 60, sec % 60)

    // 라운드 시작 함수
    fun startNewRound() {
        showImage = true
        showButtons = false
        startTime = System.currentTimeMillis()
        roundId++
        currentAnswerKey = -1
    }

    // Start 버튼 클릭
    fun onStart() {
        isRunning = true
        timeLeft = 120
        score = 0
        reactionTime = "--"
        response = "Go"
        startTime = System.currentTimeMillis()
        startNewRound()
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
        showButtons = false
        currentAnswerKey = -1
        answerKeyReceived = -1
        roundId = 0
    }


    // RuleGrid에서 answerKey를 받아 처리
    fun onAnswerKeyChanged(answerKey: Int) {
        currentAnswerKey = answerKey
        answerKeyReceived = answerKey // 상태만 변경
        showButtons = false           // 버튼은 일단 숨김
    }

    LaunchedEffect(answerKeyReceived, isRunning, roundId) {
        if (isRunning && answerKeyReceived != -1) {
            delay(1000L)
            showButtons = true
        }
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                CircularProgressIndicator(
                    progress = { timeLeft / 120f },
                    modifier = Modifier.size(120.dp),
                    color = Color.Blue,
                    strokeWidth = 10.dp,
                    trackColor = Color.LightGray,
                )
                Text(
                    text = formatTime(timeLeft),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onStart() }, enabled = !isRunning) { Text("Start") }
                    Button(onClick = { onReset() }) { Text("Reset") }
                }
                Text("점수: $score", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
                Text("반응시간: $reactionTime ms", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
                Text("Response: $response", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RuleGrid에 answerKey 콜백 전달
        RuleGrid(
            rule1 = "규칙1",
            rule2 = "규칙2",
            roundId = roundId,
            picture = "사진",
            meaning = "의미",
            text = "글자",
            color = "색상",
            isGameMode = isRunning,
            onAnswerKeyChanged = { key -> onAnswerKeyChanged(key) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 중앙 이미지 또는 "+" 표시
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (showImage) {
                if (showButtons) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.sample_game_image),
                        contentDescription = "Game Stimulus",
                        modifier = Modifier.size(180.dp)
                    )
                }
            }
        }

        // 버튼 3개 표시 (정답 맞추기용)
        if (showButtons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 예시 버튼들: 정답 후보 3개 (숫자, 텍스트 등 자유롭게 변경 가능)
                listOf(0, 1, 2).forEach { choice ->
                    Button(onClick = {
                        val endTime = System.currentTimeMillis()
                        val elapsed = endTime - startTime
                        if (choice == currentAnswerKey) {
                            score += 10
                            reactionTime = elapsed.toString()
                            response = "Correct!"
                        } else {
                            response = "Wrong!"
                        }
                        // 시간 남아있으면 다음 라운드로 자동 진행!
                        if (timeLeft > 0 && isRunning) {
                            startNewRound()
                        } else {
                            showButtons = false
                            showImage = false
                        }
                    }) {
                        Text("선택 $choice")
                    }
                }
            }
        }

        // 게임 메인으로 돌아가는 버튼
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // 현재 게임의 메인화면으로 이동
                navController.navigate("game${gameId}_main") {
                    // 현재 스크린을 백스택에서 제거 (뒤로가기 했을 때 다시 이 화면이 나오지 않게)
                    popUpTo("game${gameId}_main") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("게임 메인으로")
        }
    }
}

// 규칙표시
@Composable
fun RuleGrid(
    rule1: String,
    rule2: String,
    roundId: Int,
    picture: String,
    meaning: String,
    text: String,
    color: String, // 색상 이름을 String으로 받음
    isGameMode: Boolean,
    onAnswerKeyChanged: (Int) -> Unit
) {
    // 규칙1: 0=글자, 1=사진
    var rule1Type by remember { mutableIntStateOf(0) }
    // 규칙2: 0=의미, 1=색상
    var rule2Type by remember { mutableIntStateOf(0) }

    // 게임 모드일 때만 랜덤으로 결정
    LaunchedEffect(isGameMode, roundId) {
        if (isGameMode) {
            rule1Type = (0..1).random()
            rule2Type = (0..1).random()
        } else {
            rule1Type = 0
            rule2Type = 0
        }
        onAnswerKeyChanged(rule1Type + rule2Type)
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
        // 2행: 규칙1(글자/사진), 규칙2(의미/색상)
        Row(Modifier.fillMaxWidth()) {
            // 규칙1: 글자 또는 사진
            Text(
                text = if (rule1Type == 0) text else picture,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            // 규칙2: 의미 또는 색상
            Text(
                text = if (rule2Type == 0) meaning else color,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
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
