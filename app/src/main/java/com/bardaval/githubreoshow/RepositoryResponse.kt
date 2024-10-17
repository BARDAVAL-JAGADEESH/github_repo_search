package com.bardaval.githubreoshow

data class RepositoryResponse(
    val items: List<Repository>
)

data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val html_url: String,
    val description: String?,
    val owner: Owner
)

data class Owner(
    val login: String,
    val avatar_url: String
)
