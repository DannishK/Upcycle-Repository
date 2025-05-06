
package com.example.upcycle.data

import android.widget.Toast
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.upcycle.models.UserModel
import com.example.upcycle.navigation.ROUTE_HOME
import com.example.upcycle.navigation.ROUTE_LOGIN
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.auth.ktx.auth
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.firebase.ktx.Firebase

import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.core.Context
import kotlinx.coroutines.flow.MutableStateFlow

class authViewModel: ViewModel() {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState: StateFlow<LogoutState> = _logoutState
    private val auth: FirebaseAuth = Firebase.auth
    sealed class LogoutState {
        object Idle : LogoutState()
        object Loading : LogoutState()
        data class Success(val message: String) : LogoutState()
        data class Error(val message: String) : LogoutState()
    }
    fun signup(firstname: String,email: String,password: String,
               navController: NavController,
               context: Context){
        if (firstname.isBlank() || email.isBlank() || password.isBlank() ){
            Toast.makeText(context,"Please fill all the fields", Toast.LENGTH_LONG).show()

            return
        }
        _isLoading.value = true

        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful){
                    val userId = mAuth.currentUser?.uid ?: ""
                    val userData = UserModel(firstname = firstname,
                        email=email,password=password,userId=userId)
                    saveUserToDatabase(userId,userData,navController,context)



                } else{
                    _errorMessage.value = task.exception?.message
                    Toast.makeText(context,"Registration failed", Toast.LENGTH_LONG).show()
                }
            }

    }
    fun saveUserToDatabase(userId: String,
                           userData: UserModel,
                           navController: NavController,
                           context: Context) {
        val regRef = FirebaseDatabase.getInstance()
            .getReference("Users/$userId")
        regRef.setValue(userData).addOnCompleteListener {regRef ->
            if (regRef.isSuccessful){

                Toast.makeText(context,"User Successfully Registered", Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_LOGIN)
            }else{
                _errorMessage.value = regRef.exception?.message

                Toast.makeText(context,"Database error", Toast.LENGTH_LONG).show()

            }
        }

    }
    fun login (email: String,password: String,navController: NavController,context: Context){
        if (email.isBlank() || password.isBlank()){
            Toast.makeText(context, "Email and password required", Toast.LENGTH_LONG).show()
            return
        }
        _isLoading.value=true
        mAuth
            .signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                _isLoading.value=false
                if (task.isSuccessful){
                    Toast.makeText(context, "User successfully logged in ", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_HOME)

                }else{
                    _errorMessage.value=task.exception?.message
                    Toast.makeText(context,"Login failed",Toast.LENGTH_LONG).show()
                }
            }
    }
    fun logout(navController: NavController? = null) {
        _logoutState.value = LogoutState.Loading

        viewModelScope.launch {
            try {
                auth.signOut()
                _logoutState.value = LogoutState.Success(
                    message = "Successfully logged out"
                )
                navController?.navigate(ROUTE_LOGIN) {
                    popUpTo(0)
                }
            } catch (e: Exception) {
                _logoutState.value = LogoutState.Error(
                    message = e.message ?: "Unknown error occurred during logout"
                )
            }
        }
    }
}
