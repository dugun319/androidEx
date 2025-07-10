package com.khapp.strooptest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

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

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import kotlinx.coroutines.delay

import com.khapp.strooptest.ui.theme.strooptestTheme
import kotlinx.coroutines.launch

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
                composable("GameScreenDesc/{gameId}") { backStackEntry ->
                    val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull() ?: 1
                    GameScreenDesc(navController, gameId)
                }
                composable("GamePlayDesc/{gameId}") { backStackEntry ->
                    val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull() ?: 1
                    GamePlayDesc(navController, gameId)
                }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                                // 게임 이름이 필요하면 넣지만 필요 없을 듯 함.
                                // Text(gameName)

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
            painter = painterResource(id = R.drawable.bottmlogo), // 실제 리소스 ID로 교체
            contentDescription = "오른쪽 아래 이미지",
            modifier = Modifier
                .size(width = 250.dp, height = 80.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 16.dp) // 화면 끝에서 16dp 띄움
        )
    }
}

// 각 게임별 메인화면
@Composable
fun GameMainScreen(navController: NavController, gameId: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 완전 흰색 지정
    ) {
        // 상단 좌측 게임 이미지
        Image(
            painter = painterResource(id = getGameImageRes(gameId)),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        // 중앙 3x3 그리드 버튼
        Column(
            Modifier
                .align(Alignment.Center)
                .padding(top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until 3) {
                Row(
                    Modifier,
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        when (index) {
                            in 0..6 -> {
                                LevelImageButton(
                                    level = index + 1,
                                    navController = navController,
                                    gameId = gameId
                                )
                            }

                            7 -> DescTextButton(
                                text = "screen",
                                onClick = { navController.navigate("GameScreenDesc/$gameId") }
                            )

                            8 -> DescTextButton(
                                text = "play",
                                onClick = { navController.navigate("GamePlayDesc/$gameId") }
                            )
                        }

                        if (col < 2) Spacer(Modifier.width(16.dp))
                    }

                }
                if (row < 2) Spacer(Modifier.height(16.dp))
            }
        }

        // 하단 우측 메인화면 돌아가기 버튼
        Button(
            onClick = { navController.navigate("main") },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(width = 120.dp, height = 80.dp),
            contentPadding = PaddingValues(0.dp), // 이미지 stretch
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_to_main),
                contentDescription = "메인화면으로 돌아가기",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}

@Composable
fun LevelImageButton(level: Int, navController: NavController, gameId: Int) {
    Button(
        onClick = { navController.navigate("game${gameId}_level$level") },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.size(90.dp),
        contentPadding = PaddingValues(0.dp), // 이미지 stretch
    ) {
        Image(
            painter = painterResource(id = getLevelImageRes(level)),
            contentDescription = "레벨 $level",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Composable
fun DescTextButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.size(90.dp),
        contentPadding = PaddingValues(0.dp), // 이미지 stretch
    ) {
        val buttonImageRes = when (text) {
            "screen" -> R.drawable.screen_desc
            "play" -> R.drawable.play_desc
            else -> R.drawable.screen_desc
        }
        Image(
            painter = painterResource(id = buttonImageRes),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
    }
}

// 리소스 매핑 함수 예시
fun getGameImageRes(gameId: Int): Int = when (gameId) {
    1 -> R.drawable.button_image_1
    2 -> R.drawable.button_image_2
    3 -> R.drawable.button_image_3
    4 -> R.drawable.button_image_4
    else -> R.drawable.button_image_1
}

fun getLevelImageRes(level: Int): Int = when (level) {
    1 -> R.drawable.level_1
    2 -> R.drawable.level_2
    3 -> R.drawable.level_3
    4 -> R.drawable.level_4
    5 -> R.drawable.level_5
    6 -> R.drawable.level_6
    7 -> R.drawable.level_7
    else -> R.drawable.level_1
}


// 게임화면 설명
// gameId로 통제
@Composable
fun GameScreenDesc(navController: NavController, gameId: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 첫 번째 이미지를 Row로 감싸서 왼쪽 정렬
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val resName = "title_$gameId"
                val context = LocalContext.current
                val resId =
                    context.resources.getIdentifier(resName, "drawable", context.packageName)

                Image(
                    painter = painterResource(id = if (resId != 0) resId else R.drawable.screen_desc_1),
                    contentDescription = "게임화면설명",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.width(30.dp)) // 그림과 텍스트 사이 간격
                if(gameId != 4) {
                    Text(
                        text = "게임화면설명",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                    )
                }else {
                    Text(
                        text = "두더지 게임을 즐기세요",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                }
            }

            if(gameId != 4) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬(필요시)
                ) {
                    Image(
                        painter = painterResource(
                            id = when (gameId) {
                                1 -> R.drawable.screen_desc_11
                                2 -> R.drawable.screen_desc_21
                                3 -> R.drawable.screen_desc_31
                                else -> R.drawable.screen_desc_1
                            }
                        ),
                        contentDescription = "게임화면설명1",
                        modifier = Modifier
                            .size(300.dp)
                            .padding(top = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(
                            id = when (gameId) {
                                1 -> R.drawable.screen_desc_12
                                2 -> R.drawable.screen_desc_22
                                3 -> R.drawable.screen_desc_32
                                else -> R.drawable.screen_desc_1
                            }
                        ),
                        contentDescription = "게임화면설명2",
                        modifier = Modifier
                            .size(300.dp)
                            .padding(top = 0.dp)
                    )
                }
            }
        }

        // 하단 우측 메인화면 돌아가기 버튼
        Button(
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 10.dp)
                .size(width = 120.dp, height = 80.dp),
            contentPadding = PaddingValues(0.dp), // 이미지 stretch
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_to_main),
                contentDescription = "메인화면으로 돌아가기",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
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
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 첫 번째 이미지를 Row로 감싸서 왼쪽 정렬
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val resName = "title_$gameId"
                val context = LocalContext.current
                val resId =
                    context.resources.getIdentifier(resName, "drawable", context.packageName)

                Image(
                    painter = painterResource(id = if (resId != 0) resId else R.drawable.screen_desc_1),
                    contentDescription = "게임방법설명",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.width(30.dp)) // 그림과 텍스트 사이 간격
                if(gameId != 4) {
                    Text(
                        text = "게임방법설명",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                    )
                }else {
                    Text(
                        text = "두더지 게임을 즐기세요",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                }
            }

            if(gameId != 4) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬(필요시)
                ) {
                    Image(
                        painter = painterResource(
                            id = when (gameId) {
                                1 -> R.drawable.play_desc11
                                2 -> R.drawable.play_desc21
                                3 -> R.drawable.play_desc31
                                else -> R.drawable.play_desc_1
                            }
                        ),
                        contentDescription = "게임방법설명1",
                        modifier = Modifier
                            .size(300.dp)
                            .padding(top = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(
                            id = when (gameId) {
                                1 -> R.drawable.play_desc12
                                2 -> R.drawable.play_desc22
                                3 -> R.drawable.play_desc32
                                else -> R.drawable.play_desc_1
                            }
                        ),
                        contentDescription = "게임방법설명2",
                        modifier = Modifier
                            .size(300.dp)
                            .padding(top = 0.dp)
                    )
                }
            }
        }


        // 하단 우측 메인화면 돌아가기 버튼
        Button(
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 10.dp)
                .size(width = 120.dp, height = 80.dp),
            contentPadding = PaddingValues(0.dp), // 이미지 stretch
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_to_main),
                contentDescription = "메인화면으로 돌아가기",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
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

    //정답 키 관리
    var currentAnswerKey by remember { mutableIntStateOf(-1) }
    var randomIndex by remember { mutableIntStateOf(1) }
    var correctAnswerKey by remember { mutableIntStateOf(0) }

    // roundId가 증가하면 규칙이 변경됨
    var roundId by remember { mutableIntStateOf(0) } // 라운드 구분
    var currentStimulusResId by remember { mutableIntStateOf(R.drawable.st_01) }  // 자극 이미지 리소스 ID
    var rule1Type by remember { mutableIntStateOf(0) }
    var rule2Type by remember { mutableIntStateOf(0) }

    // 두더지 게임 전용 상태
    var moleIndex by remember { mutableIntStateOf(-1) } // 활성화된 두더지 인덱스(0~8)
    var moleState by remember { mutableStateOf("idle") } // "idle", "active", "hit", "miss"
    var moleStartTime by remember { mutableLongStateOf(0L) }

    var isGameOver by remember { mutableStateOf(false) }

    // Level-dependent stimulus exposure duration
    val delayAccordingToLevel =
        if(gameId != 4) {
            when (level) {
                1 -> 5000L
                2 -> 4000L
                3 -> 3000L
                4 -> 2000L
                5 -> 1000L
                6 -> 750L
                7 -> 500L
                else -> {
                    10000L
                }
            }
        }else{
            when (level) {
                1 -> 3000L
                2 -> 2500L
                3 -> 2000L
                4 -> 1500L
                5 -> 1000L
                6 -> 800L
                7 -> 600L
                else -> {
                    1000L
                }
            }
        }

    // 타이머 로직
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false
            isGameOver = true
            showButtons = false
            // TODO: 게임 종료 처리
        }
    }

    // 타이머 포맷
    @SuppressLint("DefaultLocale")
    fun formatTime(sec: Int): String = String.format("%02d:%02d", sec / 60, sec % 60)

    // 라운드 시작 함수
    fun startNewRound() {
        // gameId가 4면 false, 나머지는 모두 true
        showImage = gameId != 4
        showButtons = false
        startTime = System.currentTimeMillis()
        roundId++
        currentAnswerKey = -1

        // 0 : text, 1 : picture
        rule1Type = when (gameId) {
            1 -> 0
            2 -> (0..1).random()
            3 -> 1
            else -> {
                0
            }
        }

        // gameId == 1 or 2, 0 : meaning, 1 : color
        // gameId == 3, 0 -> "이기기", 1 -> "비기기", 2 -> "지기"
        rule2Type = when (gameId) {
            1, 2 -> (0..1).random()
            3 -> (0..2).random()
            else -> {
                0
            }
        }

        // 가위바위보 정답용 Array
        // randomIndex: 1~3, rule2Type: 0~2
        val answerTable = arrayOf(
            // rule2Type == 0
            arrayOf(2, 0, 1),
            // rule2Type == 1
            arrayOf(0, 1, 2),
            // rule2Type == 2
            arrayOf(1, 2, 0)
        )

        when (gameId) {
            // 0 : meaning, 1 : color
            1 -> {
                randomIndex = (1..16).random()
                correctAnswerKey = if (rule2Type == 0) {
                    when (randomIndex % 4) {
                        1 -> 0
                        2 -> 1
                        3 -> 2
                        0 -> 3
                        else -> 0 // 예외 방지
                    }
                } else {
                    ((randomIndex - 1) / 4) % 4
                }
            }

            // 0 : text, 1 : picture
            // 0 : meaning, 1 : color
            2 -> {
                randomIndex = (1..64).random()
                correctAnswerKey = if (rule1Type == 0) {
                    if (rule2Type == 0) {
                        when (randomIndex % 4) {
                            1 -> 0
                            2 -> 1
                            3 -> 2
                            0 -> 3
                            else -> 0 // 예외 방지
                        }
                    } else {
                        ((randomIndex - 1) / 4) % 4
                    }
                } else {
                    ((randomIndex - 1) / 16) % 4
                }
            }


            // 0: 이기기, 1: 비기기, 2: 지기
            // 3번 케이스
            3 -> {
                randomIndex = (1..3).random()
                correctAnswerKey = if (rule2Type in 0..2 && randomIndex in 1..3) {
                    answerTable[rule2Type][randomIndex - 1]
                } else {
                    0
                }
            }

            4 -> {
                randomIndex = (1..9).random()
                showButtons = true
            }
        }

        currentStimulusResId = when (gameId) {
            1 -> {
                val resName = "st_%02d".format(randomIndex)
                val resId = navController.context.resources.getIdentifier(
                    resName, "drawable", navController.context.packageName
                )
                if (resId != 0) resId else R.drawable.st_01
            }

            2 -> {
                val resName = "fr_%02d".format(randomIndex)
                val resId = navController.context.resources.getIdentifier(
                    resName, "drawable", navController.context.packageName
                )
                if (resId != 0) resId else R.drawable.fr_01
            }

            3 -> {
                val resName = "rsp_%02d".format(randomIndex)
                val resId = navController.context.resources.getIdentifier(
                    resName, "drawable", navController.context.packageName
                )
                if (resId != 0) resId else R.drawable.rsp_01
            }

            else -> R.drawable.sample_game_image
        }
    }

    fun startMoleRound() {
        moleIndex = (0..8).random()
        moleState = "active"
        moleStartTime = System.currentTimeMillis()
    }

    val effectDealy = 750L
    val coroutineScope = rememberCoroutineScope()

    fun proceedToNextRoundAfterDelay(state: String) {
        coroutineScope.launch {
            delay(effectDealy)
            moleState = "idle"
            delay(effectDealy)
            if (timeLeft > 0 && isRunning) {
                roundId++
            }
        }
    }

    // 두더지 클릭 처리
    fun onMoleClick(idx: Int) {
        if (moleState == "active" && idx == moleIndex) {
            val elapsed = System.currentTimeMillis() - moleStartTime
            moleState = "hit"
            score += 10
            reactionTime = elapsed.toString()
            response = "Correct!"
            proceedToNextRoundAfterDelay("hit")
        }
    }

    // 두더지 게임은 onStart에서 시작하지 않고 여기서 시작
    // 라운드 시작 및 miss 처리
    LaunchedEffect(roundId, isRunning) {
        if (isRunning && gameId == 4) {
            startMoleRound()
            delay(delayAccordingToLevel)
            if (moleState == "active") {
                moleState = "miss"
                response = "Miss!"
                reactionTime = "--"
                proceedToNextRoundAfterDelay("miss")
            }
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
        if (gameId != 4) {
            startNewRound()
        }
        isGameOver = false
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
        roundId = 0
        isGameOver = false
    }

    LaunchedEffect(roundId) {
        if (isRunning) {
            if (gameId != 4) {
                delay(delayAccordingToLevel)
                showButtons = true
            }
        }
    }

    // 진한초록색, 원형스크롤 타이머, 버튼에 사용
    val darkGreen = Color(0xFF006400)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 2x4 그리드
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, top = 50.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                        color = darkGreen,
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
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        "반응시간: $reactionTime ms",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        "Response: $response",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            if (gameId != 4) {
                RuleGrid(
                    rule1 = "규칙1",
                    rule2 = "규칙2",
                    roundId = roundId,
                    picture = "사진",
                    meaning = "의미",
                    text = "글자",
                    color = "색상",
                    isGameMode = isRunning,
                    gameId = gameId,
                    rule1Type = rule1Type,
                    rule2Type = rule2Type,
                )

                // 중앙 이미지 또는 "+" 표시
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(370.dp)
                ) {
                    when {
                        isGameOver -> {
                            Image(
                                painter = painterResource(id = R.drawable.gameover),
                                contentDescription = "Game Over",
                                modifier = Modifier.size(350.dp)
                            )
                        }

                        showImage -> {
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
                }
            }

            if (showButtons && !isGameOver) {
                when (gameId) {
                    1 -> {
                        // 색상 원형 버튼 4개
                        val colorList = listOf(Color.Red, darkGreen, Color.Blue, Color.Yellow)
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
                                        val elapsed = endTime - startTime - delayAccordingToLevel

                                        if (idx == correctAnswerKey) {
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
                                    modifier = Modifier.size(90.dp),
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

                        val buttonColors = listOf(Color.Red, darkGreen, Color.Blue, Color.Yellow)
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
                                        val elapsed = endTime - startTime - delayAccordingToLevel
                                        if (idx == correctAnswerKey) {
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
                                    modifier = Modifier.size(90.dp),
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
                                            modifier = Modifier.size(90.dp)
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
                                        val elapsed = endTime - startTime - delayAccordingToLevel
                                        if (idx == correctAnswerKey) {
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
                                    modifier = Modifier.size(120.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp) // 버튼 내부 패딩 제거 (이미지 꽉 차게)
                                ) {
                                    Image(
                                        painter = painterResource(id = buttonImages[idx]),
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (gameId == 4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGameOver) {
                        Image(
                            painter = painterResource(id = R.drawable.gameover),
                            contentDescription = "Game Over",
                            modifier = Modifier.size(350.dp)
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .width(320.dp)
                                .height(320.dp)
                        ) {
                            for (row in 0..2) {
                                Row(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    for (col in 0..2) {
                                        val idx = row * 3 + col
                                        Button(
                                            onClick = { onMoleClick(idx) },
                                            enabled = (moleState == "active" && idx == moleIndex),
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(2.dp),
                                            shape = RectangleShape,
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            val imageRes = when {
                                                moleState == "hit" && idx == moleIndex -> R.drawable.mole_hit
                                                moleState == "miss" && idx == moleIndex -> R.drawable.mole_miss
                                                moleState == "active" && idx == moleIndex -> R.drawable.mole_active
                                                else -> R.drawable.mole_idle
                                            }
                                            Image(
                                                painter = painterResource(id = imageRes),
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        // 하단 우측 메인화면 돌아가기 버튼
        if (!isRunning) {
            Button(
                onClick = { navController.navigate("main") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(width = 120.dp, height = 80.dp),
                contentPadding = PaddingValues(0.dp), // 이미지 stretch
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_to_main),
                    contentDescription = "메인화면으로 돌아가기",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }
}


// 규칙표시
@Composable
fun RuleGrid(
    rule1Type: Int,
    rule2Type: Int,
    rule1: String,
    rule2: String,
    roundId: Int,
    picture: String,
    meaning: String,
    text: String,
    color: String, // 색상 이름을 String으로 받음
    isGameMode: Boolean,
    gameId: Int,
) {

    // gameId=4면 Grid 숨김
    if (gameId == 4) return

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
