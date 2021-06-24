package kr.ac.kpu.healthactivity.chest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.healthactivity.R
import kr.ac.kpu.healthactivity.YoutubePlayerActivity
import kr.ac.kpu.healthactivity.back.*
import kr.ac.kpu.healthactivity.databinding.ActivityChestMethodBinding

class ChestMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChestMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chest_method)

        binding = ActivityChestMethodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, YoutubePlayerActivity::class.java)
        binding.chestbutton1.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=Et2yw2b5D4k")
            startActivity(intent)
        }
        binding.chestbutton2.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=sIZTgao-YT4")
            startActivity(intent)
        }
        binding.chestbutton3.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=Xat3np4Zr-w")
            startActivity(intent)
        }
        binding.chestbutton4.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=1pBcQ-77ho4")
            startActivity(intent)
        }
        binding.chestbutton5.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=HcDzxNNrSBo")
            startActivity(intent)
        }
    }
}