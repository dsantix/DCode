package com.dsanti.dcode.data

data class Update(
    val version: String,
    val date: String,
    val whatsNew: List<String>,
    val fixed: List<String>,
    val improved: List<String>,
    val version_label: String
)