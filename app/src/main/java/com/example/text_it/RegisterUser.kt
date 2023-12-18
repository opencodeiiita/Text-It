package com.example.text_it

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterUser : AppCompatActivity() {
    companion object {
        private const val SELECT_IMAGE_REQUEST = 1
    }
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        imageView = findViewById(R.id.imageView)

        imageView.setOnClickListener {
            openImagePicker()
        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_REQUEST)

    }
    private fun adjustImageViewSize(imageView: ImageView, bitmap: Bitmap) {
        val targetWidth = imageView.width
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val targetHeight = (targetWidth / aspectRatio).toInt()
        val layoutParams = imageView.layoutParams
        layoutParams.height = targetHeight
        imageView.layoutParams = layoutParams
        imageView.scaleType =
            ImageView.ScaleType.FIT_XY
    }
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val contentResolver: ContentResolver = contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE_REQUEST) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                val imageBitmap = getBitmapFromUri(imageUri)
                if (imageBitmap != null) {
                    imageView.setImageBitmap(imageBitmap)
                    adjustImageViewSize(imageView, imageBitmap)
                    Toast.makeText(this, "Image SET", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Image NOT SET", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
