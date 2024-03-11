# TestTimeTonic

In this repo you will find my solution to the Timetonic Android Dev Test, I'll try to explain  
my logic to solve each point, this could be updated a long the project is being develop

## Technologies and approach

I will face this problem with Kotlin for all logic and Jetpack Compose to build the UI, also I'll use the next dependencies to build the project

- androidx.navigation:navigation-ui-ktx:2.7.7 : This will allow me navigation from login to the books list
- com.squareup.retrofit2:retrofit:2.9.0 | com.squareup.retrofit2:converter-gson:2.9.0 | com.google.code.gson:gson:2.9.0 | org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0 : All this dependencies will help me to do the API calls and handle the responses
- lifecycle-runtime-ktx:2.7.0 | runtime-livedata:1.6.3: This 2 will help me to implement the MVVM design pattern

Now for this app will be required to add only the internet permission on the manifest in order to do the API calls, keeping in mind this technologies, lets take a look how the repo will be handle with Git and the folders Structure

## Project Structure

The **git branches** get the next hierarchy:
**Main** - Where you will find the final code of the app and the APK, in other words, the releases
|_ **Dev** - Here will be joined joined all the features and UI, lets say a test environment to integrate new features
&nbsp;&nbsp;&nbsp; |__ **ft_login** - you will see the combination of the login UI and the logic
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **ft_ui_login** - Only the UI of the Login Screen
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **ft_logic_login** - The required logic for Login Screen works
&nbsp;&nbsp;&nbsp; |__ **ft_landing** - you will see the combination of the landing UI and the logic
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **ft_ui_login** - Only the UI of the Landing Screen
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **ft_logic_login** - The required logic for Landing Screen works

---
The **folders** in the project get the next hierarchy:
|_ **testtimetonic**
&nbsp;&nbsp;&nbsp; |__ **ui** - All the @Composable components and the 2 app views
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **login**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ LoginView.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **landing**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ LandingView.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ BookFragment.kt
&nbsp;&nbsp;&nbsp; |__ **ModelViews** - The connector between the model and views, this will also take care of consume the encription logic to protect the data
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ LoginVM.kt - Only the UI of the Landing Screen
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ LandingVM.kt - The required logic for Landing Screen works
&nbsp;&nbsp;&nbsp; |__ **Model** - You will find the logic for the app just works, API calls, DTOs for handle API responses and Encryption sensitive data
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **ApiInteraction** - The retrofit client and web services for Api conection and requests, the required clases to handle the API responses, also concume some of the encription logic to protect the data
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ RetrofitClient.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ LoginWebService.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ LandingWebService.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **Responses**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ AppkeyResponse.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ OauthkeyResponse.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ SesskeyReponse.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **DTOs**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ AllBooks.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ Book.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ AllContacts.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ Contact.kt
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ **Cryptographer** - The required logic for encrypt the sensitive data
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |__ CryptographerManager.kt
