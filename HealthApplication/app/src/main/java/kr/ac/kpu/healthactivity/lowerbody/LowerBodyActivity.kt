package kr.ac.kpu.healthactivity.lowerbody

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.healthactivity.R
import kr.ac.kpu.healthactivity.YoutubePlayerActivity
import kr.ac.kpu.healthactivity.databinding.ActivityLowerBodyBinding

class LowerBodyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLowerBodyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lower_body)

        binding = ActivityLowerBodyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, YoutubePlayerActivity::class.java)
        binding.lowerbutton1.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=EZh7J7PeSJE")
            startActivity(intent)
        }
        binding.lowerbutton2.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=NZcwOWUkBt4")
            startActivity(intent)
        }
        binding.lowerbutton3.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=6_S6s224reY")
            startActivity(intent)
        }
        binding.lowerbutton4.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=_QVBWUjmdKI")
            startActivity(intent)
        }
    }
}