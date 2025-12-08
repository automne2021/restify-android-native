package com.restify.android.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

// Data class to hold the UI state
data class GameState(
    val board: List<String> = List(9) { "" },
    val xMoves: List<Int> = emptyList(),
    val oMoves: List<Int> = emptyList(),
    val isXTurn: Boolean = true,
    val winner: String = "",
    val isHardMode: Boolean = false,
    val statusMessage: String = "Your turn!",
    val isGameActive: Boolean = false
)

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun startGame(isHard: Boolean) {
        _state.update {
            GameState(
                isHardMode = isHard,
                isGameActive = true,
                statusMessage = "Your turn!"
            )
        }
    }

    fun quitGame() {
        _state.update { it.copy(isGameActive = false) }
    }

    fun playerMove(index: Int) {
        val currentState = _state.value
        if (currentState.board[index].isNotEmpty() || currentState.winner.isNotEmpty()) return

        // Logic: Limit to 3 moves (remove oldest if needed)
        val newXMoves = currentState.xMoves.toMutableList()
        val newBoard = currentState.board.toMutableList()

        if (newXMoves.size >= 3) {
            val removedIndex = newXMoves.removeAt(0)
            newBoard[removedIndex] = ""
        }

        newXMoves.add(index)
        newBoard[index] = "X"

        val (winner, message) = checkWinner(newBoard)
        val finalMessage = if (winner.isNotEmpty()) message else "AI turns!"

        _state.update {
            it.copy(
                board = newBoard,
                xMoves = newXMoves,
                isXTurn = false,
                statusMessage = finalMessage,
                winner = winner
            )
        }

        if (winner.isEmpty()) {
            viewModelScope.launch {
                delay(500) // Delay for UX
                makeAIMove()
            }
        }
    }

    private fun makeAIMove() {
        val currentState = _state.value
        if (currentState.winner.isNotEmpty()) return

        val moveIndex = if (currentState.isHardMode) {
            getBestMove(currentState)
        } else {
            getRandomMove(currentState.board)
        }

        if (moveIndex != -1) {
            val newOMoves = currentState.oMoves.toMutableList()
            val newBoard = currentState.board.toMutableList()

            if (newOMoves.size >= 3) {
                val removedIndex = newOMoves.removeAt(0)
                newBoard[removedIndex] = ""
            }

            newOMoves.add(moveIndex)
            newBoard[moveIndex] = "O"

            val (winner, message) = checkWinner(newBoard)
            val finalMessage = if (winner.isNotEmpty()) message else "Your turn!"

            _state.update {
                it.copy(
                    board = newBoard,
                    oMoves = newOMoves,
                    isXTurn = true,
                    statusMessage = finalMessage,
                    winner = winner
                )
            }
        }
    }

    // --- LOGIC HELPERS ---

    private fun checkWinner(board: List<String>, checkOnly: Boolean = false): Pair<String, String> {
        val winPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Cols
            listOf(0, 4, 8), listOf(2, 4, 6)                   // Diagonals
        )

        for (pattern in winPatterns) {
            val a = board[pattern[0]]
            val b = board[pattern[1]]
            val c = board[pattern[2]]

            if (a == b && b == c && a.isNotEmpty()) {
                val msg = if (a == "X") "You win!" else "AI wins!"
                return a to msg
            }
        }
        return "" to ""
    }

    private fun getRandomMove(board: List<String>): Int {
        val available = board.indices.filter { board[it].isEmpty() }
        return if (available.isNotEmpty()) available[Random.nextInt(available.size)] else -1
    }

    // --- MINIMAX ALGORITHM ---

    private fun getBestMove(currentState: GameState): Int {
        // Simple check if board is empty to save processing
        if (currentState.board.all { it.isEmpty() }) return 4 // Center is best

        var bestScore = -1000
        var move = -1
        val board = currentState.board.toMutableList()
        val oMoves = currentState.oMoves.toMutableList()
        val xMoves = currentState.xMoves

        for (i in 0 until 9) {
            if (board[i].isEmpty()) {
                // Simulate Move
                var removedIndex: Int? = null
                if (oMoves.size >= 3) {
                    removedIndex = oMoves[0]
                    board[removedIndex] = ""
                }
                board[i] = "O"
                val nextOMoves = ArrayList(oMoves).apply {
                    if (removedIndex != null) removeAt(0)
                    add(i)
                }

                // Minimax Call
                val score = minimax(board, 0, false, -1000, 1000, xMoves, nextOMoves)

                // Backtrack
                board[i] = ""
                if (removedIndex != null) board[removedIndex] = "O"

                if (score > bestScore) {
                    bestScore = score
                    move = i
                }
            }
        }
        return if (move != -1) move else getRandomMove(currentState.board)
    }

    private fun minimax(
        board: MutableList<String>,
        depth: Int,
        isMaximizing: Boolean,
        alpha: Int,
        beta: Int,
        currentXMoves: List<Int>,
        currentOMoves: List<Int>
    ): Int {
        val (result, _) = checkWinner(board, checkOnly = true)
        if (result == "O") return 10 - depth
        if (result == "X") return depth - 10
        if (depth >= 6) return 0 // Depth limit

        var currentAlpha = alpha
        var currentBeta = beta

        if (isMaximizing) { // AI Turn (O)
            var bestScore = -1000
            for (i in 0 until 9) {
                if (board[i].isEmpty()) {
                    // Logic: O moves
                    var removedIndex: Int? = null
                    if (currentOMoves.size >= 3) {
                        removedIndex = currentOMoves[0]
                        board[removedIndex] = ""
                    }
                    board[i] = "O"
                    val nextOMoves = ArrayList(currentOMoves).apply {
                        if (removedIndex != null) removeAt(0)
                        add(i)
                    }

                    val score = minimax(board, depth + 1, false, currentAlpha, currentBeta, currentXMoves, nextOMoves)

                    // Backtrack
                    board[i] = ""
                    if (removedIndex != null) board[removedIndex] = "O"

                    bestScore = max(score, bestScore)
                    currentAlpha = max(currentAlpha, bestScore)
                    if (currentBeta <= currentAlpha) break
                }
            }
            return bestScore
        } else { // Player Turn (X)
            var bestScore = 1000
            for (i in 0 until 9) {
                if (board[i].isEmpty()) {
                    // Logic: X moves
                    var removedIndex: Int? = null
                    if (currentXMoves.size >= 3) {
                        removedIndex = currentXMoves[0]
                        board[removedIndex] = ""
                    }
                    board[i] = "X"
                    val nextXMoves = ArrayList(currentXMoves).apply {
                        if (removedIndex != null) removeAt(0)
                        add(i)
                    }

                    val score = minimax(board, depth + 1, true, currentAlpha, currentBeta, nextXMoves, currentOMoves)

                    // Backtrack
                    board[i] = ""
                    if (removedIndex != null) board[removedIndex] = "X"

                    bestScore = min(score, bestScore)
                    currentBeta = min(currentBeta, bestScore)
                    if (currentBeta <= currentAlpha) break
                }
            }
            return bestScore
        }
    }
}