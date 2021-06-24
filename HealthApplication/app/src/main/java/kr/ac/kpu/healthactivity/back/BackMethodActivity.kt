package kr.ac.kpu.healthactivity.back

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.healthactivity.R
import kr.ac.kpu.healthactivity.YoutubePlayerActivity
import kr.ac.kpu.healthactivity.databinding.ActivityBackMethodBinding

class BackMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBackMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_method)

        binding = ActivityBackMethodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, YoutubePlayerActivity::class.java)
        binding.backbutton1.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=G11ySWVXA48")
            startActivity(intent)
        }
        binding.backbutton2.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=b_seZMf3MfM")
            startActivity(intent)
        }
        binding.backbutton3.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=m5LO72gdYGQ")
            startActivity(intent)
        }
        binding.backbutton4.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=4bx17wuyJ2o&t")
            startActivity(intent)
        }
        binding.backbutton5.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=XwybtFgfSRU")
            startActivity(intent)
        }
    }
}