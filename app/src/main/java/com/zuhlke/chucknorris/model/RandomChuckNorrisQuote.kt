package com.zuhlke.chucknorris.model

import com.fasterxml.jackson.annotation.JsonProperty


data class RandomChuckNorrisQuote(
    @JsonProperty("icon_url") val iconUrl: String,
    val id: String,
    val category: List<String>? = emptyList(),
    val value: String,
    val url: String
)

typealias QuoteCategories = List<String>