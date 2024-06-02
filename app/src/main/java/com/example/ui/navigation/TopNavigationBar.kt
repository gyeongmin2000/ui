package com.example.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.R
import com.example.ui.data.TopNav
import com.example.ui.data.TopNavItemList

//카테고리
@Composable
fun TopNavigationBar(navController: NavController) {
    LazyRow {
        items(TopNavItemList) { item ->
            TopNavigationCard(item = item, navController)
        }
    }
}

@Composable
fun TopNavigationCard(item: TopNav, navController: NavController) {
    ElevatedCard (
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(12.dp)
            .size(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.lightblue)
        )
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate(item.text) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource( id = item.imageResId ),
                contentDescription = "NavImage",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
            )
            Text(
                text = item.text,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = colorResource(id = R.color.navy)
            )
        }
    }
}