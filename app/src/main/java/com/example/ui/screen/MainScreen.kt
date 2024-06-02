package com.example.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.rememberMotionLayoutState
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ui.R
import com.example.ui.data.ProductList
import com.example.ui.navigation.SetProductListNavGraph
import com.example.ui.navigation.TopNavigationBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var searchText by rememberSaveable { mutableStateOf("") }
    val navController = rememberNavController()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var headerHeight by remember { mutableStateOf(250.dp) }
    val animatedHeaderHeight by animateDpAsState(targetValue = headerHeight)

    //LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                coroutineScope.launch {
                    headerHeight = (headerHeight + available.y.dp).coerceIn(100.dp, 250.dp)
                }
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.navy))
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    coroutineScope.launch {
                        headerHeight = (headerHeight + dragAmount.y.dp).coerceIn(100.dp, 250.dp)
                        change.consume()
                    }
                }
            }
    ) {
        Column {
            // HeaderBox
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeaderHeight),
                contentAlignment = Alignment.Center
            ) {

                // 검색 텍스트 필드
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(25.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            searchProductByName(searchText, navController, context)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Icon"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions {
                        searchProductByName(searchText, navController, context)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .widthIn(300.dp, 300.dp)
                )
            }


            // Content Column
            Column (
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .weight(1f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                    )
            ) {
                // 카테고리
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                        )
                ) {
                    TopNavigationBar(navController = navController)
                }

                // 목록
                Box(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    SetProductListNavGraph(navController = navController)
                }
            }
        }
    }
}


//상품 이름으로 검색
fun searchProductByName(keyword: String, navController: NavController, context: Context) {
    if (keyword != "") {
        val searchList = ProductList.filter { product ->
            product.itemName.contains(keyword, ignoreCase = true)
        }

        if (searchList.size != 0) {
            navController.currentBackStackEntry?.savedStateHandle?.set(key = "searchlist", value = searchList)
            navController.navigate("Search")
        } else {
            Toast.makeText(context, "No search results found", Toast.LENGTH_SHORT).show()
        }

    } else {
        Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
    }
}