package com.example.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.data.Product

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val product = intent.getSerializableExtra("product") as Product
        setContent {
            DetailScreen(product = product)
        }
    }
}

//상품 상세 화면
@Composable
fun DetailScreen(product: Product) {
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxHeight(0.3f)) {
                // 상품 이미지
                Image(

                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = product.imgResId),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop
                )

                // 뒤로가기
                IconButton(
                    onClick = {
                        (context as Activity).finish()
                    },
                    modifier = Modifier
                        .offset(10.dp, 10.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .size(50.dp)
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxHeight(0.8f)
            ) {

                // 상품 이름
                Text(
                    text = product.itemName,
                    modifier = Modifier
                        .padding(5.dp),

                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
                
                // 상품 소개
                Text(
                    text = product.introduction,
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                )

            }

            // 선택 버튼
            ElevatedButton(
                onClick = {
                    // ARViewActivity로 이동하는 인텐트 생성 및 시작
                    val intent = Intent(context, ARViewActivity::class.java)
                    intent.putExtra("productId", product.pid)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Select")
            }
        }
    }
}
