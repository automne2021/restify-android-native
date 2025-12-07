package com.restify.android.ui.screens.model3d

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.restify.android.R
import com.restify.android.ui.theme.*
import io.github.sceneview.SceneView
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch
import com.restify.android.ui.theme.Black
import com.restify.android.ui.theme.Orange
import com.restify.android.ui.theme.Gray
import com.restify.android.ui.theme.Cream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Model3DScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedModel by remember { mutableStateOf(modelCategories[0].items[0]) }
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = White
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 0.dp)
                ) {
                    modelCategories.forEach { category ->
                        item {
                            ExpandableCategory(
                                category = category,
                                selectedModel = selectedModel,
                                onModelSelected = { model ->
                                    selectedModel = model
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        gesturesEnabled = false // Disable swipe gesture to prevent interference with model rotation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            Model3DViewer(
                model = selectedModel,
                context = context,
                modifier = Modifier.fillMaxSize()
            )

            // Menu Button with proper padding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        tint = Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(5.dp),
                    color = Black,
                    modifier = Modifier.border(1.dp, Cream, RoundedCornerShape(5.dp))
                ) {
                    Text(
                        text = "Drag to rotate  •  Pinch to zoom",
                        style = TextStyle(
                            color = Cream,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableCategory(
    category: ModelCategory,
    selectedModel: Model3DItem,
    onModelSelected: (Model3DItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation",
        animationSpec = tween(300)
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = category.iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = category.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = 16.sp
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = "Expand",
                tint = Gray,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotationState)
            )
        }

        if (expanded) {
            category.items.forEach { item ->
                val isSelected = item.path == selectedModel.path
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onModelSelected(item) }
                        .padding(horizontal = 40.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = item.name,
                        style = TextStyle(
                            color = if (isSelected) Orange else Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Model3DViewer(
    model: Model3DItem,
    context: Context,
    modifier: Modifier = Modifier
) {
    var sceneView by remember { mutableStateOf<SceneView?>(null) }
    var currentModelNode by remember { mutableStateOf<ModelNode?>(null) }
    val modelKey = remember(model.path) { model.path }

    LaunchedEffect(modelKey) {
        sceneView?.let { scene ->
            // Properly remove old model node
            currentModelNode?.let { oldNode ->
                try {
                    // Clear the model node completely
                    scene.childNodes.forEach { node ->
                        scene.removeChildNode(node)
                        node.destroy()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                currentModelNode = null
            }

            try {
                val newModelInstance = scene.modelLoader.createModelInstance(
                    assetFileLocation = model.path
                )

                val newNode = ModelNode(
                    modelInstance = newModelInstance,
                    scaleToUnits = 1f
                ).apply {
                    isEditable = true
                }
                scene.addChildNode(newNode)
                currentModelNode = newNode
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                currentModelNode?.let { node ->
                    sceneView?.childNodes?.forEach { n ->
                        sceneView?.removeChildNode(n)
                        n.destroy()
                    }
                }
                currentModelNode = null
                sceneView?.destroy()
                sceneView = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    AndroidView(
        factory = { ctx ->
            SceneView(ctx).apply {
                sceneView = this
            }
        },
        modifier = modifier,
        onRelease = { view ->
            try {
                currentModelNode?.let { node ->
                    view.childNodes.forEach { n ->
                        view.removeChildNode(n)
                        n.destroy()
                    }
                }
                currentModelNode = null
                view.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    )
}
