package com.example.ui.view


import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.NestedScrollView
import androidx.navigation.NavController
import com.example.ui.activity.DetailActivity
import com.example.ui.data.Product

//상품 목록
@Composable
fun ProductGrid(item: List<Product>, navController: NavController) {
    LazyVerticalGrid(
        modifier = Modifier
            .nestedScroll(rememberNestedScrollInteropConnection()),
        columns = GridCells.Fixed(2),
        content = {
            items(item) { product ->
                ProductCard(item = product, navController = navController)
            }
        }
    )
}

@Composable
fun ProductCard(item: Product, navController: NavController) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("product", item)
                    startActivity(context, intent, null)
                }
        ) {
            Image(
                painter = painterResource(id = item.imgResId),
                contentDescription = "ItemImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
                    .clickable {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("product", item)
                        startActivity(context, intent, null)
                    }
            )
            Text(
                text = item.itemName,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = item.introduction,
                fontSize = 12.sp,
                color = Color.Black,
                maxLines = 1
            )
        }
    }
}
