package com.ari.curve.ui.naviagtions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ari.curve.BuildConfig
import com.ari.curve.ui.screens.MainScreen
import com.ari.curve.ui.screens.SignInScreen
import com.ari.curve.ui.viewmodels.MainViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavGraph (
    context : Context,
    navHostController: NavHostController,
    vm : MainViewModel,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)
    NavHost(navController = navHostController, startDestination = Screen.signin.route) {
        composable(route = Screen.mainChatScreen.route) {
            MainScreen()
        }
        composable(route= Screen.signin.route) {
            SignInScreen({
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(
                            request = request,
                            context = context
                        )
                        vm.onGetCredentialResponse( context,result.credential)
                    } catch (e: GetCredentialCancellationException) {
                        // User cancelled the One Tap prompt — just log it or ignore
                        Log.d("CredExp", "User cancelled One Tap")
                    } catch (e: NoCredentialException) {
                        // No credentials saved on the device — show a toast or fallback
                        Toast.makeText(context, "No credentials available. Please add a Google Account on the device or check you internet connection.", Toast.LENGTH_SHORT).show()
                    } catch (e: GetCredentialException) {
                        // Other credential errors
                        Log.e("CredExp", "Other credential error", e)
                    } catch (e: Exception) {
                        // Any other unexpected exception
                        Log.e("CredExp", "Unexpected error", e)
                    }
                }
            })
        }
    }
}

sealed class Screen(val route:String){
    object mainChatScreen: Screen(route = "main_chat_screen")
    object signin : Screen(route = "signin_screen")
}