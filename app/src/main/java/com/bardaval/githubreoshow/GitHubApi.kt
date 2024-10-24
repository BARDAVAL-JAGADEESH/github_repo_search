package com.bardaval.githubreoshow

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Call<RepositoryResponse>

    @GET("repos/{owner}/{repo}/contributors")
    fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<List<Contributor>>

    @GET("repos/{owner}/{repo}")
    fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Repository>
}



























