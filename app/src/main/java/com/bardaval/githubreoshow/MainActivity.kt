package com.bardaval.githubreoshow
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bardaval.githubreoshow.ui.theme.GithubReoShowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubReoShowTheme{
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

        // Observe the repositories state
        RepositoryList(viewModel.repositories.value, navController)
    }
}

@Composable
fun RepositoryList(repositories: List<Repository>, navController: NavHostController) {
    Column {
        repositories.forEach { repo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
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
    val context = LocalContext.current

    // Fetch repository details
    LaunchedEffect(Unit) {
        viewModel.getRepositoryDetails(owner, repo) { repoDetails ->
            repository = repoDetails
        }
    }

    // Fetch contributors
    LaunchedEffect(Unit) {
        viewModel.getContributors(owner, repo) { contributorsList ->
            contributors = contributorsList
        }
    }

    // Display repository details
    repository?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.titleMedium)

            // Load owner's avatar image using Coil
            AsyncImage(
                model = it.owner.avatar_url,
                contentDescription = "Owner ",
                modifier = Modifier.size(64.dp)
            )

            Text(text = "Description: ${it.description ?: "No description"}")
            Text(
                text = "Project Link: ${it.html_url}",
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.html_url))
                    context.startActivity(intent)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary // Change color to indicate clickable text
            )

            Text(text = "Contributors:", style = MaterialTheme.typography.titleSmall)
            contributors.forEach { contributor ->
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    AsyncImage(
                        model = contributor.avatar_url,
                        contentDescription = "Contributor Avatar",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = contributor.login)
                }
            }
        }
    } ?: run {
        // Loading or not found state
        Text("Repository not found.")
    }
}
































