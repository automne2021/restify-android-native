package com.restify.android.ui.screens.model3d

import android.Manifest
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown

// --- Imports Project Resources ---
import com.restify.android.R
import com.restify.android.ui.theme.*

// --- Imports Google AR Core ---
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState

// --- Imports SceneView ---
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.node.ModelNode
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Position

import com.google.android.filament.LightManager
import io.github.sceneview.node.LightNode


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Model3DScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // Giả sử modelCategories đã được định nghĩa ở file khác
    var selectedModel by remember { mutableStateOf(modelCategories[0].items[0]) }
    var isArMode by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Trạng thái Loading chung cho cả màn hình
    var isLoading by remember { mutableStateOf(false) }

    // Check camera permission
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) isArMode = true
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = White
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    modelCategories.forEach { category ->
                        item {
                            ExpandableCategory(
                                category = category,
                                selectedModel = selectedModel,
                                onModelSelected = { model ->
                                    selectedModel = model
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                }
            }
        },
        gesturesEnabled = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            // 1. Hiển thị Viewer
            if (isArMode && hasCameraPermission) {
                ArModelViewer(
                    model = selectedModel,
                    modifier = Modifier.fillMaxSize(),
                    onLoadingChanged = { loading -> isLoading = loading }
                )
            } else {
                Model3DViewer(
                    model = selectedModel,
                    modifier = Modifier.fillMaxSize(),
                    onLoadingChanged = { loading -> isLoading = loading }
                )
            }

            // 2. UI Loading
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Orange)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading model...", color = Cream, fontSize = 14.sp)
                    }
                }
            }

            // 3. Các nút Menu/AR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        tint = Cream,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (!isArMode) {
                            if (hasCameraPermission) isArMode = true
                            else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            isArMode = false
                        }
                    },
                    modifier = Modifier.background(if (isArMode) Orange else Cream, CircleShape)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_cube),
                        contentDescription = "AR Mode",
                        tint = Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // 4. Hướng dẫn đáy màn hình
            if (!isLoading && !isArMode) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = Black.copy(alpha = 0.7f),
                        modifier = Modifier.border(1.dp, Cream, RoundedCornerShape(5.dp))
                    ) {
                        Text(
                            text = "Drag to rotate  •  Pinch to zoom",
                            style = TextStyle(color = Cream, fontSize = 14.sp),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
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
                imageVector = Icons.Default.KeyboardArrowDown,
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
    modifier: Modifier = Modifier,
    onLoadingChanged: (Boolean) -> Unit
) {
    key(model.path) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val childNodes = rememberNodes()

        LaunchedEffect(Unit) {
            // --- 1. ĐÈN CHÍNH (Key Light) ---
            val lightNodeFront = LightNode(
                engine = engine,
                type = LightManager.Type.DIRECTIONAL
            ) {
                intensity(150000.0f) // Tăng từ 110k lên 150k
                color(1.0f, 1.0f, 0.9f) // Màu hơi vàng nhẹ cho tự nhiên
            }
            lightNodeFront.rotation = Rotation(x = -45.0f, y = 45.0f, z = 0.0f)
            childNodes.add(lightNodeFront)

            // --- 2. ĐÈN PHỤ (Fill Light) ---
            val lightNodeBack = LightNode(
                engine = engine,
                type = LightManager.Type.DIRECTIONAL
            ) {
                intensity(150000.0f)
                color(0.9f, 0.9f, 1.0f)
            }
            // Chiếu từ hướng đối diện
            lightNodeBack.rotation = Rotation(x = -45.0f, y = -135.0f, z = 0.0f)
            childNodes.add(lightNodeBack)
        }

        var modelNode by remember { mutableStateOf<ModelNode?>(null) }
        var isModelPlaced by remember { mutableStateOf(false) }
        var isTracking by remember { mutableStateOf(false) }
        var currentFrame by remember { mutableStateOf<Frame?>(null) }

        LaunchedEffect(model.path) {
            onLoadingChanged(true)
            try {
                val instance = modelLoader.loadModelInstance(model.path)

                if (instance != null) {
                    modelNode = ModelNode(
                        modelInstance = instance,
                        scaleToUnits = 0.5f,
                        autoAnimate = false
                    ).apply {
                        isEditable = true

                        val bounds = instance.asset.boundingBox
                        val halfExtent = bounds.halfExtent
                        val center = bounds.center

                        collisionShape = io.github.sceneview.collision.Box(
                            // Tham số 1: Size (Vector3)
                            io.github.sceneview.collision.Vector3(
                                halfExtent[0] * 2,
                                halfExtent[1] * 2,
                                halfExtent[2] * 2
                            ),
                            // Tham số 2: Center (Vector3)
                            io.github.sceneview.collision.Vector3(
                                center[0],
                                center[1],
                                center[2]
                            )
                        )

                        editableScaleRange = 0.2f..1.5f
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                onLoadingChanged(false)
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
                    currentFrame = frame
                    if (frame.camera.trackingState == TrackingState.TRACKING) {
                        isTracking = true
                    }
                },
                onGestureListener = rememberOnGestureListener(
                    onSingleTapConfirmed = { motionEvent, node ->
                        if (!isModelPlaced && node == null) {
                            val frame = currentFrame ?: return@rememberOnGestureListener
                            val hitTestResult = frame.hitTest(motionEvent)

                            hitTestResult.firstOrNull {
                                it.trackable is Plane && ((it.trackable as Plane).isPoseInPolygon(it.hitPose))
                            }?.let { hitResult ->
                                hitResult.createAnchorOrNull()?.let { anchor ->
                                    modelNode?.let { newNode ->
                                        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
                                        anchorNode.addChildNode(newNode)
                                        childNodes += anchorNode
                                        isModelPlaced = true

                                    }
                                }
                            }
                        }
                    }
                )
            )

            // UI Hướng dẫn AR
            if (!isModelPlaced) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = if (isTracking) "Tap on the floor to set the object" else "Move to scan the floor...",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = "Use 2 fingers to rotate & zoom",
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(blurRadius = 3f)
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
    modifier: Modifier = Modifier,
    onLoadingChanged: (Boolean) -> Unit
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    var modelNode by remember { mutableStateOf<ModelNode?>(null) }
    val childNodes = rememberNodes()

    LaunchedEffect(model.path) {
        onLoadingChanged(true)
        try {
            modelNode?.let { node ->
                childNodes.remove(node)
                // node.destroy()
            }
            modelNode = null

            val instance = modelLoader.loadModelInstance(model.path)
            if (instance != null) {
                val newNode = ModelNode(
                    modelInstance = instance,
                    scaleToUnits = 1.0f
                ).apply {
                    isEditable = true
                    position = Position(0f, -0.5f, 0f)
                    rotation = Rotation(0f, 0f, 0f)
                }
                childNodes += newNode
                modelNode = newNode
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            onLoadingChanged(false)
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