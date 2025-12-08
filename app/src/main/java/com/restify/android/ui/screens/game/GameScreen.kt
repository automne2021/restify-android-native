package com.restify.android.ui.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restify.android.R
import com.restify.android.ui.theme.*

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    // Main Scaffold
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isGameActive) {
            ActiveGameContent(state, viewModel)
        } else {
            MenuContent(viewModel)
        }
    }
}

// --- SCREEN 1: MENU ---
@Composable
fun MenuContent(viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "TIC TAC TOE",
            color = Orange,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 69.sp
        )

        Spacer(modifier = Modifier.height(43.dp))

        // Game Board Image (Decoration)
        Box(
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.game_board),
                contentDescription = "Game Board",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        GameButton(text = "Easy Mode") { viewModel.startGame(isHard = false) }
        Spacer(modifier = Modifier.height(30.dp))
        GameButton(text = "Hard Mode") { viewModel.startGame(isHard = true) }
    }
}

// --- SCREEN 2: ACTIVE GAME ---
@Composable
fun ActiveGameContent(state: GameState, viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 20.dp)
        ) {
            IconButton(
                onClick = { viewModel.quitGame() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "TIC TAC TOE",
            color = Orange,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 69.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Status Text
        val statusColor = if (state.winner.isNotEmpty()) {
            if (state.winner == "X") Gray else Orange
        } else {
            Gray
        }

        Text(
            text = state.statusMessage,
            color = statusColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(28.dp))

        // The Board
        Box(
            modifier = Modifier
                .size(320.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(10.dp),
                    spotColor = Orange.copy(alpha = 0.5f)
                )
                .background(Beige, shape = RoundedCornerShape(10.dp))
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            GridItem(
                                symbol = state.board[index],
                                onClick = { viewModel.playerMove(index) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(43.dp))

        // Control Buttons
        if (state.winner.isEmpty()) {
            GameButton(text = "Reset") { viewModel.startGame(state.isHardMode) }
        } else {
            GameButton(text = "Play Again") { viewModel.startGame(state.isHardMode) }
            Spacer(modifier = Modifier.height(15.dp))
            val switchModeText = if (state.isHardMode) "Easy Mode" else "Hard Mode"
            GameButton(text = switchModeText) { viewModel.startGame(!state.isHardMode) }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// --- COMPONENTS ---

@Composable
fun GridItem(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(86.dp) // (320 - 40 padding - 40 spacing) / 3 approx
            .shadow(4.dp, RoundedCornerShape(10.dp), spotColor = Orange)
            .background(Cream, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (symbol == "X") {
            Icon(
                painter = painterResource(id = R.drawable.x),
                contentDescription = "X",
                tint = Gray,
                modifier = Modifier.size(40.dp)
            )
        } else if (symbol == "O") {
            Icon(
                painter = painterResource(id = R.drawable.o),
                contentDescription = "O",
                tint = Orange,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun GameButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(60.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp), spotColor = Black.copy(alpha = 0.2f))
            .background(Orange, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
    }
}