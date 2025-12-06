package com.restify.android.ui.screens.model3d

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.restify.android.R
import com.restify.android.ui.theme.*
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.rememberOnGestureListener
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.HitResult
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
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
    var isArMode by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Check camera permission
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            isArMode = true
        }
    }

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
            // Show AR or 3D viewer based on mode
            if (isArMode && hasCameraPermission) {
                ArModelViewer(
                    model = selectedModel,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Model3DViewer(
                    model = selectedModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Menu Button and AR Button with proper padding
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
                        tint = Cream,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // AR Toggle Button
                IconButton(
                    onClick = {
                        if (!isArMode) {
                            // Request camera permission if not granted
                            if (hasCameraPermission) {
                                isArMode = true
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        } else {
                            isArMode = false
                        }
                    },
                    modifier = Modifier
                        .background(
                            color = if (isArMode) Orange else Cream,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ar),
                        contentDescription = "AR Mode",
                        tint = Black,
                        modifier = Modifier.size(24.dp)
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
                        text = if (isArMode) "Tap to place model  •  Pinch to scale" else "Drag to rotate  •  Pinch to zoom",
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
                tint = Black,
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
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = "Expand",
                tint = Gray,
                modifier = Modifier
                    .size(24.dp)
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
fun ArModelViewer(
    model: Model3DItem,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val childNodes = rememberNodes()
    var modelNode by remember { mutableStateOf<ModelNode?>(null) }

    // We need to capture the current frame to perform hit tests on tap
    var currentFrame by remember { mutableStateOf<Frame?>(null) }

    LaunchedEffect(model.path) {
        modelNode?.destroy()
        modelLoader.loadModelInstance(model.path)?.let { modelInstance ->
            modelNode = ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 0.5f
            ).apply {
                isEditable = true
            }
        }
    }

    Box(modifier = modifier) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            modelLoader = modelLoader,
            planeRenderer = true,
            onSessionUpdated = { session, frame ->
                // Keep the frame updated so we can use it in the gesture listener
                currentFrame = frame
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    // If user tapped on an empty space (node == null)
                    if (node == null) {
                        // Use the captured frame to perform a hit test
                        currentFrame?.hitTest(motionEvent)?.firstOrNull { hit ->
                            // Check if we hit a Plane
                            hit.trackable is Plane
                        }?.let { hitResult ->
                            // Create the anchor
                            hitResult.createAnchorOrNull()?.let { anchor ->
                                modelNode?.let { node ->
                                    // If we haven't placed the model yet, add it
                                    if (childNodes.isEmpty()) {
                                        childNodes += AnchorNode(
                                            engine = engine,
                                            anchor = anchor
                                        ).apply {
                                            addChildNode(node)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            modelNode?.destroy()
            childNodes.clear()
        }
    }
}

@Composable
fun Model3DViewer(
    model: Model3DItem,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)

    var modelNode by remember { mutableStateOf<ModelNode?>(null) }
    val childNodes = rememberNodes()

    LaunchedEffect(model.path) {
        // Clear old model
        modelNode?.let { oldNode ->
            childNodes.remove(oldNode)
            oldNode.destroy()
        }

        // Load new model
        modelLoader.loadModelInstance(model.path)?.let { modelInstance ->
            val newNode = ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 1f
            ).apply {
                isEditable = true
                // Move the model down vertically
                position = io.github.sceneview.math.Position(0f, -0.5f, 0f)
            }
            childNodes += newNode
            modelNode = newNode
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            modelNode?.destroy()
            childNodes.clear()
        }
    }

    io.github.sceneview.Scene(
        modifier = modifier,
        engine = engine,
        modelLoader = modelLoader,
        childNodes = childNodes,
        onViewUpdated = { }
    )
}
