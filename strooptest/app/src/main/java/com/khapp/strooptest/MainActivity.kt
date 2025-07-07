package com.khapp.strooptest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke

import androidx.compose.material3.*
import androidx.compose.runtime.*


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 상단 300x300 이미지
            Image(
                painter = painterResource(id = R.drawable.main_page_image),
                contentDescription = "메인 이미지",
                modifier = Modifier.size(300.dp)
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
                                    3 -> "RockScissorsPapers"
                                    4 -> "MoleGame"
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
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = when (gameId) {
                                                1 -> R.drawable.button_image_1
                                                2 -> R.drawable.button_image_2
                                                3 -> R.drawable.button_image_3
                                                4 -> R.drawable.button_image_4
                                                else -> R.drawable.button_image_1
                                            }
                                        ),
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
        // 오른쪽 아래 고정 이미지 추가
        Image(
            painter = painterResource(id = R.drawable.your_image), // 실제 리소스 ID로 교체
            contentDescription = "오른쪽 아래 이미지",
            modifier = Modifier
                .size(width = 250.dp, height = 100.dp)
                .align(Alignment.BottomEnd)
                .padding(16.dp) // 화면 끝에서 16dp 띄움
        )
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
    var currentStimulusResId by remember { mutableIntStateOf(R.drawable.st_01) }  // 자극 이미지 리소스 ID
    var rule1Type by remember { mutableIntStateOf(0) }
    var rule2Type by remember { mutableIntStateOf(0) }

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

        currentStimulusResId = when (gameId) {
            1 -> {
                val randomIndex = (1..16).random()
                val resName = "st_%02d".format(randomIndex)
                val resId = navController.context.resources.getIdentifier(
                    resName, "drawable", navController.context.packageName
                )
                if (resId != 0) resId else R.drawable.st_01
            }
            2 -> {
                val randomIndex = (1..64).random()
                val resName = "fr_%02d".format(randomIndex)
                val resId = navController.context.resources.getIdentifier(
                    resName, "drawable", navController.context.packageName
                )
                if (resId != 0) resId else R.drawable.fr_01
            }
            3 -> {
                val randomIndex = (1..3).random()
                val resName = "rsp_%02d".format(randomIndex)
                val resId = navController.context.resources.getIdentifier(
                    resName, "drawable", navController.context.packageName
                )
                if (resId != 0) resId else R.drawable.rsp_01
            }
            else -> R.drawable.sample_game_image
        }
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


    // RuleGrid에서 answerKey와 rule1Type, rule2Type 받아오기
    fun onAnswerKeyChanged(answerKey: Int, rule1: Int, rule2: Int) {
        currentAnswerKey = answerKey
        answerKeyReceived = answerKey
        rule1Type = rule1
        rule2Type = rule2
        showButtons = false
    }

    LaunchedEffect(answerKeyReceived, isRunning, roundId) {
        if (isRunning && answerKeyReceived != -1) {
            delay(1000L)
            showButtons = true
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // 상단 2x4 그리드
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, top = 50.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(170.dp)
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
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val buttonImages = listOf(
                            R.drawable.button_start,
                            R.drawable.button_reset,
                        )
                        Button(
                            onClick = { onStart() },
                            enabled = !isRunning,
                            shape = CircleShape,
                            modifier = Modifier.size(70.dp),
                            contentPadding = PaddingValues(0.dp), // 이미지 stretch
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White // 배경색 흰색
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.button_start),
                                contentDescription = "",
                                modifier = Modifier.size(70.dp),
                            )
                        }
                        Button(
                            onClick = { onReset() },
                            shape = CircleShape,
                            modifier = Modifier.size(70.dp),
                            contentPadding = PaddingValues(0.dp), // 이미지 stretch
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White // 배경색 흰색
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.button_reset),
                                contentDescription = "",
                                modifier = Modifier.size(70.dp),
                            )
                        }
                    }
                    Text(
                        "점수: $score",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        "반응시간: $reactionTime ms",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        "Response: $response",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

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
                onAnswerKeyChanged = { key, r1, r2 -> onAnswerKeyChanged(key, r1, r2) },
                gameId = gameId,
            )

            // 중앙 이미지 또는 "+" 표시
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp)
            ) {
                if (showImage) {
                    if (showButtons) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        when (gameId) {
                            1, 2, 3 -> Image(
                                painter = painterResource(id = currentStimulusResId),
                                contentDescription = "Game Stimulus",
                                modifier = Modifier.size(350.dp)
                            )

                            else -> Image(
                                painter = painterResource(id = R.drawable.sample_game_image),
                                contentDescription = "Game Stimulus",
                                modifier = Modifier.size(350.dp)
                            )
                        }
                    }
                }
            }

            fun getButtonCount(gameId: Int): Int = when (gameId) {
                1, 2 -> 4
                3 -> 4
                4 -> 9
                else -> 4
            }

            if (showButtons) {
                when (gameId) {
                    1 -> {
                        // 색상 원형 버튼 4개
                        val colorList = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            colorList.forEachIndexed { idx, color ->
                                Button(
                                    onClick = {
                                        val endTime = System.currentTimeMillis()
                                        val elapsed = endTime - startTime
                                        if (idx == currentAnswerKey) {
                                            score += 10
                                            reactionTime = elapsed.toString()
                                            response = "Correct!"
                                        } else {
                                            response = "Wrong!"
                                        }
                                        if (timeLeft > 0 && isRunning) {
                                            startNewRound()
                                        } else {
                                            showButtons = false
                                            showImage = false
                                        }
                                    },
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = color)
                                ) {}
                            }
                        }
                    }

                    2 -> {
                        // 버튼 4개: 이미지/색상 구분
                        val buttonImages = listOf(
                            R.drawable.button_r,
                            R.drawable.button_g,
                            R.drawable.button_b,
                            R.drawable.button_y
                        )
                        val buttonColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            (0..3).forEach { idx ->
                                Button(
                                    onClick = {
                                        val endTime = System.currentTimeMillis()
                                        val elapsed = endTime - startTime
                                        if (idx == currentAnswerKey) {
                                            score += 10
                                            reactionTime = elapsed.toString()
                                            response = "Correct!"
                                        } else {
                                            response = "Wrong!"
                                        }
                                        if (timeLeft > 0 && isRunning) {
                                            startNewRound()
                                        } else {
                                            showButtons = false
                                            showImage = false
                                        }
                                    },
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp), // 버튼 내부 패딩 제거
                                    colors = if (rule2Type == 1) {
                                        ButtonDefaults.buttonColors(containerColor = buttonColors[idx])
                                    } else {
                                        ButtonDefaults.buttonColors(containerColor = Color.White)
                                    }
                                ) {
                                    if (rule2Type == 0) {
                                        // 이미지 채움
                                        Image(
                                            painter = painterResource(id = buttonImages[idx]),
                                            contentDescription = null,
                                            modifier = Modifier.size(64.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // 가위, 바위, 보 버튼 3개
                        val buttonImages = listOf(
                            R.drawable.button_rock,
                            R.drawable.button_scissors,
                            R.drawable.button_paper,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            (0..2).forEach { idx ->
                                Button(
                                    onClick = {
                                        val endTime = System.currentTimeMillis()
                                        val elapsed = endTime - startTime
                                        if (idx == currentAnswerKey) {
                                            score += 10
                                            reactionTime = elapsed.toString()
                                            response = "Correct!"
                                        } else {
                                            response = "Wrong!"
                                        }
                                        if (timeLeft > 0 && isRunning) {
                                            startNewRound()
                                        } else {
                                            showButtons = false
                                            showImage = false
                                        }
                                    },
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp) // 버튼 내부 패딩 제거 (이미지 꽉 차게)
                                ) {
                                    Image(
                                        painter = painterResource(id = buttonImages[idx]),
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp) // 버튼보다 약간 작게
                                    )
                                }
                            }
                        }
                    }

                    4 -> {
                        // 예시: 두더지 게임 등 추가 게임 분기
                        // ... gameId 4에 맞는 버튼 레이아웃 ...
                    }
                }
            }
        }

        // 게임 메인으로 돌아가는 버튼
        Button(
            onClick = {
                // 현재 게임의 메인화면으로 이동
                navController.navigate("game${gameId}_main") {
                    // 현재 스크린을 백스택에서 제거 (뒤로가기 했을 때 다시 이 화면이 나오지 않게)
                    popUpTo("game${gameId}_main") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // 오른쪽 하단에 고정
                .padding(24.dp)             // 화면 끝에서 24dp 띄움
                .width(180.dp)              // 원하는 너비 지정
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
    onAnswerKeyChanged: (Int, Int, Int) -> Unit,
    gameId: Int,
) {
    // 규칙1: 0=글자, 1=사진
    var rule1Type by remember { mutableIntStateOf(0) }
    // 규칙2: 0=의미, 1=색상
    var rule2Type by remember { mutableIntStateOf(0) }

    // gameId=4면 Grid 숨김
    if (gameId == 4) return

    // 게임 모드일 때만 랜덤으로 결정
    // 게임 모드일 때만 랜덤/고정 결정
    LaunchedEffect(isGameMode, roundId, gameId) {
        if (isGameMode) {
            when (gameId) {
                1 -> {
                    rule1Type = 0 // 고정
                    rule2Type = (0..1).random()
                }
                2 -> {
                    rule1Type = (0..1).random()
                    rule2Type = (0..1).random()
                }
                3 -> {
                    rule1Type = 1 // 고정
                    rule2Type = (0..2).random()
                }
                else -> {
                    rule1Type = 0
                    rule2Type = 0
                }
            }
        } else {
            // 게임모드가 아니면 기본값(설명 등)
            rule1Type = 0
            rule2Type = 0
        }
        // 정답키 생성 방식도 gameId별로 다르게 하고 싶으면 아래 부분 수정
        onAnswerKeyChanged(rule1Type + rule2Type, rule1Type, rule2Type)
    }

    // RuleGrid FontStyle
    val myHeaderTextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.DarkGray,
    )

    val myRuleTextStyle = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )

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
                style = myHeaderTextStyle,
                textAlign = TextAlign.Center
            )
            Text(
                text = rule2,
                modifier = Modifier.weight(1f),
                style = myHeaderTextStyle,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 2행: 규칙1(글자/사진), 규칙2(의미/색상/추가)
        Row(Modifier.fillMaxWidth()) {
            // 규칙1: 글자 또는 사진
            Text(
                text = if (rule1Type == 0) text else picture,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = myRuleTextStyle,
                textAlign = TextAlign.Center
            )
            // 규칙2: 의미, 색상, 추가 규칙
            Text(
                text = when (gameId) {
                    3 -> when (rule2Type) {
                        0 -> "이기기"
                        1 -> "비기기"
                        2 -> "지기"
                        else -> ""
                    }
                    else -> when (rule2Type) {
                        0 -> meaning
                        1 -> color
                        else -> meaning
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = myRuleTextStyle,
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
