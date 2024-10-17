package com.bardaval.githubreoshow



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.bardaval.githubreoshow.ui.theme.GithubReoShowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubReoShowTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("repoDetails/{owner}/{repo}") { backStackEntry ->
                        RepoDetailsScreen(
                            owner = backStackEntry.arguments?.getString("owner") ?: "",
                            repo = backStackEntry.arguments?.getString("repo") ?: ""
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel = remember { GitHubViewModel() }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Repositories") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.searchRepositories(searchQuery) }) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))
        RepositoryList(viewModel.repositories, navController)
    }
}

@Composable
fun RepositoryList(repositories: List<Repository>, navController: NavHostController) {
    Column {
        repositories.forEach { repo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    navController.navigate("repoDetails/${repo.owner.login}/${repo.name}")
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = repo.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = repo.description ?: "No description", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun RepoDetailsScreen(owner: String, repo: String) {
    val viewModel = remember { GitHubViewModel() }
    var repository by remember { mutableStateOf<Repository?>(null) }
    var contributors by remember { mutableStateOf<List<Contributor>>(emptyList()) }

    viewModel.getContributors(owner, repo) { contributorsList ->
        contributors = contributorsList
    }

    // Fetch repository details
    LaunchedEffect(Unit) {
        viewModel.searchRepositories(repo)
        repository = viewModel.repositories.find { it.name == repo }
    }

    repository?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.titleMedium)
            it.owner.avatar_url?.let { url ->
                // Use Glide or any image loading library to load the image
                // Glide.with(context).load(url).into(imageView)
            }
            Text(text = "Description: ${it.description ?: "No description"}")
            Text(text = "Project Link: ${it.html_url}", modifier = Modifier.clickable {
                // Open WebView
            })
            Text(text = "Contributors:")
            contributors.forEach { contributor ->
                Text(text = contributor.login)
            }
        }
    }
}
