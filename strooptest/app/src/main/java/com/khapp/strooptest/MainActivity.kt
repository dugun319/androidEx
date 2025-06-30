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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import kotlinx.coroutines.delay

import com.khapp.strooptest.ui.theme.strooptestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            strooptestTheme {
                MiniGamesApp()
            }
        }
    }
}

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
            composable("game1") { DivScreen(navController, gameId = 1) }
            composable("game2") { DivScreen(navController, gameId = 2) }
            composable("game3") { DivScreen(navController, gameId = 3) }
            composable("game4") { DivScreen(navController, gameId = 4) }
            // 필요 시 레벨 선택 화면 등 추가
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mini Games",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = { navController.navigate("game1") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) { Text("미니게임 1") }
        Button(
            onClick = { navController.navigate("game2") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) { Text("미니게임 2") }
        Button(
            onClick = { navController.navigate("game3") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) { Text("미니게임 3") }
        Button(
            onClick = { navController.navigate("game4") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) { Text("미니게임 4") }
    }
}

@Composable
fun DivScreen(navController: NavController, gameId: Int) {
    when (gameId) {
        1 -> GameID1(navController)
        else -> DefaultGameScreen(gameId)
    }
}

// gameId가 1일 때만 사용하는 함수
@Composable
fun GameID1(navController: NavController) {
    val navControllerLocal = rememberNavController()
    NavHost(
        navController = navControllerLocal,
        startDestination = "game1_main"
    ) {
        composable("game1_main") {
            GameID1Main(navControllerLocal)
        }
        composable("game1_screen_desc") {
            GameID1ScreenDesc(navControllerLocal)
        }
        composable("game1_play_desc") {
            GameID1PlayDesc(navControllerLocal)
        }
    }
}

// gameId1 메인 (레벨, 설명 선택)
@Composable
fun GameID1Main(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "미니게임 1",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        // 1~7 레벨 + 2개 설명 버튼 (총 9개)
        val buttonTexts = listOf(
            "레벨 1", "레벨 2", "레벨 3", "레벨 4", "레벨 5", "레벨 6", "레벨 7",
            "게임화면설명", "게임방법설명",
            "MainMenu"
        )
        buttonTexts.forEach { text ->
            Button(
                onClick = {
                    when (text) {
                        "게임화면설명" -> navController.navigate("game1_screen_desc")
                        "게임방법설명" -> navController.navigate("game1_play_desc")
                        "MainMenu" -> navController.navigate("mainMenu")
                        else -> {
                            // TODO: 레벨 선택 시 해당 레벨 게임 시작
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)
            ) {
                Text(text)
            }

        }
    }
}

// gameId1 게임화면설명 화면
@Composable
fun GameID1ScreenDesc(navController: NavController) {
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

// gameId1 게임방법설명 화면
@Composable
fun GameID1PlayDesc(navController: NavController) {
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

// gameId가 1이 아닐 때 기본 게임 화면
@Composable
fun DefaultGameScreen(gameId: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Game $gameId",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        // TODO: 다른 게임 ID별로 추가 구현
    }
}

@Composable
fun GameScreen(navController: NavController, gameId: Int) {
    var timeLeft by remember { mutableStateOf(120) } // 2분 = 120초
    var isRunning by remember { mutableStateOf(false) }

    // 타이머 로직
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Game $gameId",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            CircularProgressIndicator(
                progress = timeLeft / 120f,
                modifier = Modifier.size(140.dp),
                strokeWidth = 10.dp
            )
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isRunning = true },
                enabled = !isRunning && timeLeft > 0
            ) { Text("Start") }
            Button(
                onClick = {
                    isRunning = false
                    timeLeft = 120
                    // 점수/반응시간 등 초기화 추가
                }
            ) { Text("Reset") }
        }
        // 이하 규칙, 메인자극, 반응버튼 등 UI 추가 예정
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
        DivScreen(navController = rememberNavController(), gameId = 1)
    }
}
