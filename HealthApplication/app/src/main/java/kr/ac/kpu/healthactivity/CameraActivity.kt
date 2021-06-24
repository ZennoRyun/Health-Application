package kr.ac.kpu.healthactivity


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kr.ac.kpu.healthactivity.databinding.ActivityCameraBinding
import java.io.File

import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import java.io.FileNotFoundException
import java.io.IOException


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    var curNum = 0
    var imageFiles : Array<File>? = null
    var apiService: ApiService? = null

    lateinit var imageFname : String
    lateinit var uploadName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),Context.MODE_PRIVATE)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val curPhotoPath:String? = intent.getStringExtra("filePath")

        imageFiles = File("sdcard/$curPhotoPath").listFiles()
        Log.d("imageFiles",imageFiles.toString())
        imageFname = imageFiles!![0].toString()
        uploadName = "${imageFiles!![0]}.$curPhotoPath"
        Log.d("imageFname",imageFname)
        binding.myPictureView.imagePath = imageFname


        initRetrofitClient()


        binding.back.setOnClickListener{
            if(curNum <= 0){
                Toast.makeText(this, "This is First Image", Toast.LENGTH_SHORT).show()
            }else{
                curNum--
                imageFname = imageFiles!![curNum].toString()
                binding.myPictureView.imagePath = imageFname
                binding.myPictureView.invalidate()
            }
        }

        binding.next.setOnClickListener{
            if(curNum >= imageFiles!!.size -1){
                Toast.makeText(this, "This is last",Toast.LENGTH_SHORT).show()
            }else{
                curNum++
                imageFname = imageFiles!![curNum].toString()
                binding.myPictureView.imagePath = imageFname
                binding.myPictureView.invalidate()
            }
        }

        binding.upload.setOnClickListener{
            multipartImageUpload()
        }
    }

    private fun initRetrofitClient() {
        val client = OkHttpClient.Builder().build()
        apiService = Retrofit.Builder()
            .baseUrl("http://ec2-3-35-4-187.ap-northeast-2.compute.amazonaws.com:3000")
            .client(client).build().create(ApiService::class.java)
    }


    private fun multipartImageUpload() {
        try {
            Toast.makeText(this,"OK upload",Toast.LENGTH_SHORT).show()
            /*
            filenamesave = /storage/emulated/0/DCIM/Camera/파일명.확장자
            */

            val reqFile = RequestBody.create(MediaType.parse("image/*"), imageFname)
            val body = MultipartBody.Part.createFormData("upload", imageFname, reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")


            // 여기가 전송부분 apiService 인터페이스에 선언해놓은 post 방식으로 이미지를 보냄
            val req = apiService!!.postImage(body, name)

            //이후 처리 부분
            req!!.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    Toast.makeText(applicationContext, "Uploaded Successfully!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }// multipartImageUpload end

}