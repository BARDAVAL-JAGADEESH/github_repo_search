package com.bardaval.githubreoshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubViewModel : ViewModel() {
    private val api: GitHubApi

    var repositories = mutableListOf<Repository>()
    var errorMessage: String? = null

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GitHubApi::class.java)
    }

    fun searchRepositories(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            api.searchRepositories(query).enqueue(object : Callback<RepositoryResponse> {
                override fun onResponse(
                    call: Call<RepositoryResponse>,
                    response: Response<RepositoryResponse>
                ) {
                    if (response.isSuccessful) {
                        repositories.clear()
                        response.body()?.items?.let {
                            repositories.addAll(it)
                        }
                    } else {
                        errorMessage = "Error: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<RepositoryResponse>, t: Throwable) {
                    errorMessage = t.message
                }
            })
        }
    }

    fun getContributors(owner: String, repo: String, callback: (List<Contributor>) -> Unit) {
        api.getContributors(owner, repo).enqueue(object : Callback<List<Contributor>> {
            override fun onResponse(
                call: Call<List<Contributor>>,
                response: Response<List<Contributor>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body() ?: emptyList())
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Contributor>>, t: Throwable) {
                callback(emptyList())
            }
        })
    }
}
