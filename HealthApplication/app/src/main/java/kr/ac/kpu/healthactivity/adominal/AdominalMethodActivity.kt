package kr.ac.kpu.healthactivity.adominal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.healthactivity.R
import kr.ac.kpu.healthactivity.YoutubePlayerActivity
import kr.ac.kpu.healthactivity.databinding.ActivityAdominalMethodBinding


class AdominalMethodActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAdominalMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adominal_method)

        binding = ActivityAdominalMethodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, YoutubePlayerActivity::class.java)
        binding.adomibutton1.setOnClickListener{
            intent.putExtra("url","https://www.youtube.com/watch?v=KqnFav4Edvw")
            startActivity(intent)
        }
        binding.adomibutton2.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=B--6YfhmBGc")
            startActivity(intent)
        }
        binding.adomibutton3.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=5IBhz4c2D4o")
            startActivity(intent)
        }
    }
}