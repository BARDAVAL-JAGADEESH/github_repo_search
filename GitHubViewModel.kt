class GitHubViewModel : ViewModel() {
    private val api: GitHubApi

    var repositories = mutableStateOf<List<Repository>>(listOf())
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var isLoading = mutableStateOf(false) // New loading state
        private set

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GitHubApi::class.java)
    }

    fun searchRepositories(query: String) {
        isLoading.value = true // Set loading state to true
        viewModelScope.launch(Dispatchers.IO) {
            api.searchRepositories(query).enqueue(object : Callback<RepositoryResponse> {
                override fun onResponse(
                    call: Call<RepositoryResponse>,
                    response: Response<RepositoryResponse>
                ) {
                    isLoading.value = false // Reset loading state
                    if (response.isSuccessful) {
                        repositories.value = response.body()?.items ?: listOf()
                    } else {
                        errorMessage.value = "Error: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<RepositoryResponse>, t: Throwable) {
                    isLoading.value = false // Reset loading state
                    errorMessage.value = t.message
                }
            })
        }
    }
}
