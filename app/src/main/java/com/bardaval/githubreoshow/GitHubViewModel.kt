package com.bardaval.githubreoshow
import androidx.compose.runtime.mutableStateOf
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

    // Make repositories a MutableStateList for reactivity
    var repositories = mutableStateOf<List<Repository>>(listOf())
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

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
                        // Update the state with the new list of repositories
                        repositories.value = response.body()?.items ?: listOf()
                    } else {
                        errorMessage.value = "error: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<RepositoryResponse>, t: Throwable) {

                    errorMessage.value = t.message
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

    fun getRepositoryDetails(owner: String, repo: String, callback: (Repository?) -> Unit) {
        api.getRepository(owner, repo).enqueue(object : Callback<Repository> {
            override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Repository>, t: Throwable) {
                callback(null)
            }
        })
    }
}

































