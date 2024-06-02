package com.example.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.R
import com.example.ui.data.Product
import com.example.ui.data.ProductList
import com.example.ui.data.models
import com.example.ui.ui.theme.Purple40
import com.google.android.filament.Engine
import kotlinx.coroutines.delay
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode


// ComponentActivity를 상속받아 사용자 인터페이스를 구성
// onCreate 메서드에서는 ARView 컴포저블 함수를 호출하여 화면을 구성
class ARViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productId = intent.getIntExtra("productId", -1)
        setContent {
            ARView(productId = productId, onBackPressed = { onBackPressed() })
        }
    }
}

const val scaleToUnits = 1f

// 선택된 제품의 3D 모델을 AR 화면에 표시합니다. 제품 선택, 모델 선택, AR 화면 등의 UI 요소를 구성
@Composable
fun ARView(productId: Int?, onBackPressed: () -> Unit, onClick: (Product) -> Unit = {}) {

    val product = ProductList.find {
        it.pid == productId
    }
    var showInstructions by remember {
        mutableStateOf(true)
    }
    val currentModel = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        delay(3000)
        showInstructions = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        product?.let { product ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), contentAlignment = Alignment.Center
            ) {

                ARScreen(currentModel.value)

                Box(modifier = Modifier.fillMaxSize(0.8f)) {
                    Image(
                        painter = painterResource(id = R.drawable.vector_1),
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.TopStart
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.vector_2),
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.vector_3),
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.BottomEnd
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.vector_4),
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.BottomStart
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterEnd),
                ) {
                    ModelSelector(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.White)
                            .padding(5.dp),
                        onClick = {
                            currentModel.value = it
                        })
                }

                // Instructions to scan the room
                this@Column.AnimatedVisibility(visible = showInstructions) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(Color(0xFF393939))
                            .padding(16.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_view_in_ar_new_googblue_48dp),
                            contentDescription = "image description",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Please scan the room by\nmoving your phone around",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(600),
                                color = Color.White,
                                letterSpacing = 0.06.sp,
                            )
                        )
                    }
                }
            }

        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterHorizontally
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF828282),
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .weight(0.20f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    .clickable {
                        onBackPressed()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "image description", modifier = Modifier.alpha(0f)
                )
                Text(
                    text = "Back",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.07.sp,
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF828282),
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .weight(0.60f)
                    .background(
                        color = Purple40,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .padding(start = 10.dp, top = 10.dp, end = 18.dp, bottom = 10.dp)
                    .clickable {
                        product?.let { onClick(it) }
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    tint = Color.White,
                    contentDescription = "image description"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.06.sp,
                    )
                )
            }

        }

    }

}

// 실제 AR 화면을 구성합니다. ARScene 라이브러리를 사용하여 AR 화면을 생성하고, 선택된 3D 모델을 로드하여 화면에 표시합니다.
@Composable
fun ARScreen(model: String) {

    val nodes = remember {
        mutableListOf<ArNode>()
    }
    var engine by remember {
        mutableStateOf<Engine?>(null)
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }

    /*
    modelNode.value?.rotation = Rotation(0f, 30f)

    fun rotateModel() {
        modelNode.value?.apply {
            transform(rotation = Rotation(0f, 30f, 0f))
            isEditable = true
        }
    } */

    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->
                // 조명 추정
                arSceneView.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                // 평면 그림자 표시
                arSceneView.planeRenderer.isShadowReceiver = false
                // 심도 모드 (카메라의 깊이 정보를 사용하여 물체의 심도를 계산, 지원가능 기기만)
                arSceneView.depthEnabled = true

                engine = arSceneView.engine

                engine?.let {
                    val arModelNode = ArModelNode(it, PlacementMode.BEST_AVAILABLE).apply {
                        loadModelGlbAsync(
                            glbFileLocation = "models/${model}.glb",
                            scaleToUnits = 1f
                        ) {

                        }
                        // sceneview/ar/node/ArNode
                        // applyPoseRotation = true
                        // isRotationEditable = true
                        // isScaleEditable = false
                    }
                    modelNode.value = arModelNode
                    nodes.add(arModelNode)
                }
            },
            onSessionCreate = {
                // 평면 시각화
                planeRenderer.isVisible = true
            }
        )

        // 모델 삭제 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable {
                    modelNode.value?.let { node ->
                        nodes.remove(node)
                        node.destroy()
                        modelNode.value = null
                    }
                }
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color.Red,
                modifier = Modifier.size(30.dp)
            )
        }
    }


    // 모델 로드
    LaunchedEffect(key1 = model) {

        if (modelNode.value?.isAnchored == false)
            modelNode.value?.loadModelGlbAsync(
                glbFileLocation = "models/${model}.glb",
                scaleToUnits = scaleToUnits
            )

        if (modelNode.value?.isAnchored == true || modelNode.value == null)
            engine?.let {
                val arModelNode = ArModelNode(it, PlacementMode.BEST_AVAILABLE).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = scaleToUnits
                    ) {

                    }
                    // sceneview/ar/node/ArNode
                    // applyPoseRotation = true
                    // isRotationEditable = true
                    // isScaleEditable = false

                }
                modelNode.value = arModelNode
                nodes.add(arModelNode)
            }
        modelNode.value?.anchor()
    }

}


// 선택된 3D 모델을 표시하기 위한 모델 선택 화면을 구성합니다. 모델 선택 화면은 LazyColumn을 사용하여 구현하였습니다.
@Composable
fun ModelSelector(modifier: Modifier = Modifier, onClick: (String) -> Unit) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(models) {
            ModelView(modifier = Modifier.clickable { onClick(it.name) }, imageId = it.imageId)
        }

    }
}

@Composable
fun ModelView(
    modifier: Modifier = Modifier,
    imageId: Int
) {
    Box(
        modifier = modifier
            .size(35.dp)
            .clip(RoundedCornerShape(5.dp))

    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
