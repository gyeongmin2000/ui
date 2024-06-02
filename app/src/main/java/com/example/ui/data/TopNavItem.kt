package com.example.ui.data

import com.example.ui.R

data class TopNav(val text: String, val imageResId: Int)

//카테고리 종류
val TopNavItemList = listOf(
    TopNav("Furniture", R.drawable.furniture),
    TopNav("Appliances", R.drawable.refrigerator),
    TopNav("Plants", R.drawable.plant),
    TopNav("Decoration", R.drawable.decoration),
    TopNav("Kitchenware", R.drawable.pan)
)
