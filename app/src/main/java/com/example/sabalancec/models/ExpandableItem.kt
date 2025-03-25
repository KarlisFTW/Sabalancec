package com.example.sabalancec.models

data class ExpandableItem(
    val title: String,
    val contentLayout: Int,  // Resource ID of the layout to inflate for content
    var isExpanded: Boolean = false,
    val contentViewData: Any? = null  // Optional data to bind to the content view
)