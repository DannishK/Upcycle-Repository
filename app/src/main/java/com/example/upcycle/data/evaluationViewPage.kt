package com.example.upcycle.data
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.upcycle.models.ProductsModel
import com.example.upcycle.navigation.ROUTE_ADMIN_HOME
import com.example.upcycle.navigation.ROUTE_USER_HOME
import com.example.upcycle.network.ImgurService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import com.google.firebase.Timestamp
import com.google.firebase.database.DatabaseReference


class EvaluationViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().getReference("productsModel")
    private val adminDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("userProducts")

    private fun getImgurService(): ImgurService {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ImgurService::class.java)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
            file.outputStream().use { output -> inputStream?.copyTo(output) }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
// Users Upload,View and Delete
    fun uploadProductWithImage(
        uri: Uri,
        context: Context,
        name: String,
        price: String,
        category: String,
        location: String,
        description: String,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = getFileFromUri(context, uri)
            if (file == null) {
                showToast(context, "Failed to process image")
                return@launch
            }

            val imagePart = MultipartBody.Part.createFormData(
                "image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull())
            )

            try {
                val response = getImgurService().uploadImage(
                    imagePart,
                    "Client-ID 9fc797293a51bed"
                )

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link ?: ""
                    val productId = database.push().key ?: return@launch
                    val product = ProductsModel(
                        id = productId,
                        name = name,
                        price = price,
                        description = description,
                        category = category,
                        imageUrl = imageUrl,//listOf(imageUrl),
                        timestamp = System.currentTimeMillis(),
                        location = location
                    )

                    database.child(productId).setValue(product)
                        .addOnSuccessListener {
                            showToast(context, "Product saved successfully")
                            navController.navigate(ROUTE_USER_HOME)
                        }
                        .addOnFailureListener {
                            showToast(context, "Failed to save product")
                        }

                } else {
                    showToast(context, "Upload failed: ${response.code()}")
                }

            } catch (e: Exception) {
                showToast(context, "Upload error: ${e.localizedMessage}")
            }
        }
    }

    fun viewProducts(
        product: MutableState<ProductsModel>,
        productList: SnapshotStateList<ProductsModel>,
        context: Context
    ): SnapshotStateList<ProductsModel> {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                snapshot.children.mapNotNullTo(productList) {
                    it.getValue(ProductsModel::class.java)
                }
                if (productList.isNotEmpty()) {
                    product.value = productList.first()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(context, "Fetch failed: ${error.message}")
            }
        })
        return productList
    }

    fun updateProduct(
        context: Context,
        navController: NavController,
        name: String,
        price: String,
        category: String,
        location: String,
        description: String,
        imageUrl: String,
        productId: String
    ) {
        val updatedProduct = ProductsModel(
            id = productId,
            name = name,
            price = price,
            description = description,
            category = category,
            imageUrl = imageUrl, //listOf(imageUrl),
            timestamp = System.currentTimeMillis(),
            location = location
        )
        database.child(productId).setValue(updatedProduct)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(context, "Product updated successfully")
                    navController.navigate(ROUTE_USER_HOME)
                } else {
                    showToast(context, "Update failed")
                }
            }
    }

    fun deleteProduct(context: Context, productId: String, navController: NavController) {
        AlertDialog.Builder(context)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                database.child(productId).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(context, "Product deleted")
                        navController.navigate(ROUTE_USER_HOME)
                    } else {
                        showToast(context, "Deletion failed")
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    // Users Upload,View and Delete

    //Admin Upload, View and delete
    fun AdminUploadProductWithImage(
        uri: Uri,
        context: Context,
        name: String,
        price: String,
        category: String,
        location: String,
        description: String,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = getFileFromUri(context, uri)
            if (file == null) {
                showToast(context, "Failed to process image")
                return@launch
            }

            val imagePart = MultipartBody.Part.createFormData(
                "image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull())
            )

            try {
                val response = getImgurService().uploadImage(
                    imagePart,
                    "Client-ID 9fc797293a51bed"
                )

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link ?: ""
                    val productId = adminDatabase.push().key ?: return@launch
                    val product = ProductsModel(
                        id = productId,
                        name = name,
                        price = price,
                        description = description,
                        category = category,
                        imageUrl = imageUrl,
                        timestamp = System.currentTimeMillis(),
                        location = location
                    )

                    adminDatabase.child(productId).setValue(product)
                        .addOnSuccessListener {
                            showToast(context, "Product saved successfully")
                            navController.navigate(ROUTE_ADMIN_HOME)
                        }
                        .addOnFailureListener {
                            showToast(context, "Failed to save product")
                        }

                } else {
                    showToast(context, "Upload failed: ${response.code()}")
                }

            } catch (e: Exception) {
                showToast(context, "Upload error: ${e.localizedMessage}")
            }
        }
    }

    fun AdminViewProducts(
        product: MutableState<ProductsModel>,
        productList: SnapshotStateList<ProductsModel>,
        context: Context
    ): SnapshotStateList<ProductsModel> {
        adminDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                snapshot.children.mapNotNullTo(productList) {
                    it.getValue(ProductsModel::class.java)
                }
                if (productList.isNotEmpty()) {
                    product.value = productList.first()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(context, "Fetch failed: ${error.message}")
            }
        })
        return productList
    }

    fun AdminUpdateProduct(
        context: Context,
        navController: NavController,
        name: String,
        price: String,
        category: String,
        location: String,
        description: String,
        imageUrl: String,
        productId: String
    ) {
        val updatedProduct = ProductsModel(
            id = productId,
            name = name,
            price = price,
            description = description,
            category = category,
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis(),
            location = location
        )
        adminDatabase.child(productId).setValue(updatedProduct)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(context, "Product updated successfully")
                    navController.navigate(ROUTE_ADMIN_HOME)
                } else {
                    showToast(context, "Update failed")
                }
            }
    }

    fun AdminDeleteProduct(context: Context, productId: String, navController: NavController) {
        AlertDialog.Builder(context)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                adminDatabase.child(productId).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(context, "Product deleted")
                        navController.navigate(ROUTE_ADMIN_HOME)
                    } else {
                        showToast(context, "Deletion failed")
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }
//Admin Upload, View and delete


    private fun showToast(context: Context, message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}
