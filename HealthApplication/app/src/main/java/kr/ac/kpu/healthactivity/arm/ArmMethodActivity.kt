package kr.ac.kpu.healthactivity.arm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.healthactivity.R
import kr.ac.kpu.healthactivity.YoutubePlayerActivity
import kr.ac.kpu.healthactivity.databinding.ActivityArmMethodBinding

class ArmMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArmMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arm_method)

        binding = ActivityArmMethodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = Intent(this, YoutubePlayerActivity::class.java)
        binding.armbutton1.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=A3AWaC2uKnU")
            startActivity(intent)
        }
        binding.armbutton3.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=on6qHp03OC4")
            startActivity(intent)
        }
        binding.armbutton5.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=NG13nTvI06E")
            startActivity(intent)
        }
        binding.armbutton6.setOnClickListener {
            intent.putExtra("url","https://www.youtube.com/watch?v=cF9NczPANp4")
            startActivity(intent)
        }
    }
}