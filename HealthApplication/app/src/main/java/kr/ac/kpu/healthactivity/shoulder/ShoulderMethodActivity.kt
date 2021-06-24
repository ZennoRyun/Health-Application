package kr.ac.kpu.healthactivity.shoulder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.healthactivity.R
import kr.ac.kpu.healthactivity.YoutubePlayerActivity
import kr.ac.kpu.healthactivity.databinding.ActivityShoulderMethodBinding

class ShoulderMethodActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShoulderMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoulder_method)

        binding = ActivityShoulderMethodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, YoutubePlayerActivity::class.java)
        binding.sdrbutton1.setOnClickListener{
            intent.putExtra("url","https://www.youtube.com/watch?v=ywLWbwF42kE")
            startActivity(intent)
        }
        binding.sdrbutton2.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=dE5be4sTjxM")
            startActivity(intent)
        }
        binding.sdrbutton3.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=4PbLgURgTm4")
            startActivity(intent)
        }
        binding.sdrbutton4.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=CDYF9kk1wyE")
            startActivity(intent)
        }

    }
}