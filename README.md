# GitHub Repository Search App

## Overview

The **GitHub Repository Search App** is a mobile application built for Android that allows users to search for
GitHub repositories, view details about selected repositories, and explore contributors. The application consists of two main screens: 
the Home screen for searching and displaying repositories, and the Repo Details screen for viewing detailed information about a selected repository.

## Features

- **Home Screen**:
  - A search bar to search for repositories using the GitHub API.
  - A RecyclerView using CardView to display search results.
  - Displays up to 10 repositories per page.
  - Clicking on an item navigates to the Repo Details screen.

- **Repo Details Screen**:
  - Displays detailed information about the selected repository.
  - Shows the repository's image, name, project link, description, and contributors.
  - Clicking on the project link opens a WebView to show the repository's content.

## Screenshots

    #Homescreen
<img src="https://github.com/user-attachments/assets/44c13867-286f-4838-9baf-416bd8bc0bfb" alt="homescreen" width="200"/> 




Listof Repositories displays 
                             
<img src="https://github.com/user-attachments/assets/e5deda22-5c10-4955-8d43-0a92b3a686ef" alt="listofrepo" width="200"/>  
      Details of repo  --> <img src="https://github.com/user-attachments/assets/8f2494d7-51d2-4e8f-a9bf-6f1d40da1155" alt="detailscreen" width="200"/>
      


  opening in web ---> <img src="https://github.com/user-attachments/assets/dfb5dee5-3206-4919-8085-b3e113a1bb36" alt="openinweb" width="200"/>







## Technologies Used

- Kotlin
- Jetpack Compose for UI
- Retrofit for networking
- Coil for image loading
- AndroidX for components
- Coroutines for asynchronous programming

## API Reference

This application uses the GitHub REST API. The key endpoints used in this app are:

1. **Search Repositories**:
   - `GET https://api.github.com/search/repositories?q={query}`
   - Returns a list of repositories matching the search query.

2. **Get Repository Details**:
   - `GET https://api.github.com/repos/{owner}/{repo}`
   - Returns detailed information about the specified repository.

3. **Get Contributors**:
   - `GET https://api.github.com/repos/{owner}/{repo}/contributors`
   - Returns a list of contributors to the specified repository.

## Getting Started

To get a copy of the project up and running on your local machine for development and testing purposes, follow these steps:

### Prerequisites

- Android Studio (version 4.1 or higher)
- Kotlin (version 1.5 or higher)
- Gradle (version 7.0 or higher)

### Installation

1. Clone the repository:

   ```bash
  https://github.com/BARDAVAL-JAGADEESH/github_repo_search.git
   cd gthub_repo_searc
