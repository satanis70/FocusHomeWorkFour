package ermilov.focushomeworkfour.Api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface imgurApi {
    @Multipart
    @Headers("Authorization: Client-ID 2ce1fcf8eb2ab1d")
    @POST("image")
    fun postImage(
            @Query("title") title: String?,
            @Query("description") description: String?,
            @Query("album") albumId: String?,
            @Query("account_url") username: String?,
            @Part file: MultipartBody.Part?): Call<ImageResponse?>?
}