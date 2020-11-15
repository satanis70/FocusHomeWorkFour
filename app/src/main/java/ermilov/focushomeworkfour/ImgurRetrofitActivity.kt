package ermilov.focushomeworkfour

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ermilov.focushomeworkfour.Api.ImageResponse
import ermilov.focushomeworkfour.Api.imgurApi
import ermilov.focushomeworkfour.ImgurRetrofitActivity.Companion.imageBitmap
import kotlinx.android.synthetic.main.five_work.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class ImgurRetrofitActivity : AppCompatActivity() {

    companion object {
        const val MY_REQUEST_PICK_IMAGE = 1000
        lateinit var imageBitmap: Bitmap
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.five_work)
        buttonChoose.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            intent.type = "image/*"
            intent.putExtra("crop", "true")
            intent.putExtra("scale", true)
            intent.putExtra("aspectX", 16)
            intent.putExtra("aspectY", 9)
            startActivityForResult(intent, ImgurRetrofitActivity.MY_REQUEST_PICK_IMAGE)
        }

        buttonUpload.setOnClickListener {
            val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val outputStream = ByteArrayOutputStream()
            ImgurRetrofitActivity.imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val outStreamtoByteArray = outputStream.toByteArray()
            val imgurApi: imgurApi = retrofit.create(imgurApi::class.java)
            val call = imgurApi.postImage(
                    "NikitaTest",
                    "NikitaTest", "", "",
                    MultipartBody.Part.createFormData(
                            "image",
                            ImgurRetrofitActivity.imageBitmap.toString(),
                            RequestBody.create(MediaType.parse("image/*"), outStreamtoByteArray)
                    ))
            call?.enqueue(object : Callback<ImageResponse?> {
                override fun onResponse(call: Call<ImageResponse?>, response: Response<ImageResponse?>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Succeful", Toast.LENGTH_SHORT).show()
                        Log.d("URLP", "http://imgur.com/" + response.body()?.data?.id)
                    }
                }

                override fun onFailure(call: Call<ImageResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext, "An unknown error has occured.", Toast.LENGTH_SHORT)
                            .show()
                }

            }
            )
        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode== ImgurRetrofitActivity.MY_REQUEST_PICK_IMAGE){
                val uri = data?.data
                ImgurRetrofitActivity.imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                Glide.with(this).load(ImgurRetrofitActivity.imageBitmap).centerCrop()
                        .override(800, 800)
                        .into(imageView)
            }
        }
}
