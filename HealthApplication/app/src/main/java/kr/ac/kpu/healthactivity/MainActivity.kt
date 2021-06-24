package kr.ac.kpu.healthactivity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.TabActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceControlViewHost
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TabHost
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.ac.kpu.healthactivity.adominal.AdominalMethodActivity
import kr.ac.kpu.healthactivity.arm.ArmMethodActivity
import kr.ac.kpu.healthactivity.back.BackMethodActivity
import kr.ac.kpu.healthactivity.chest.ChestMethodActivity
import kr.ac.kpu.healthactivity.databinding.ActivityMainBinding
import kr.ac.kpu.healthactivity.lowerbody.LowerBodyActivity
import kr.ac.kpu.healthactivity.shoulder.ShoulderMethodActivity
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class MainActivity : TabActivity() {
    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var index :Int = 1
    var conversionTime = ""
    private lateinit var binding: ActivityMainBinding
    private var mediaplayer : MediaPlayer? = null

    lateinit var fileName2 : String  //파일 이름을 저장할 스트링
    lateinit var fileName3 : String  //파일 이름을 저장할 스트링
    lateinit var fileName4 : String  //파일 이름을 저장할 스트링
    var sdCardPath = "/storage/emulated/0/myRecord" //sd카드에 저장할 사진 경로
    var sdCardPath2 = "/storage/emulated/0/myRoutine" //sd카드에 저장할 루틴 경로
    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청 코드
    var curPath=""
    lateinit var curPhotoPath:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var tabHost = this.tabHost

        var tabSpecMain = tabHost.newTabSpec("Main").setIndicator("메인")
        tabSpecMain.setContent(R.id.tabMain)
        tabHost.addTab(tabSpecMain)
        var tabSpecMethod = tabHost.newTabSpec("Meothod").setIndicator("운동법")
        tabSpecMethod.setContent(R.id.tabMethod)
        tabHost.addTab(tabSpecMethod)
        var tabSpecRoutine = tabHost.newTabSpec("Routine").setIndicator("루틴 생성")
        tabSpecRoutine.setContent(R.id.tabRoutine)
        tabHost.addTab(tabSpecRoutine)
        var tabSpecRecord = tabHost.newTabSpec("Record").setIndicator("바디 체크")
        tabSpecRecord.setContent(R.id.tabRecord)
        tabHost.addTab(tabSpecRecord)

        var midList = ArrayList<String>()
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, midList)
        binding.ListView1.adapter = adapter
        var myDir = File("$sdCardPath2")//생성할 디렉토리의 경로를 설정한다.
        myDir.mkdir()
        var files = File(sdCardPath2).listFiles()   //files 디렉토리의 파일들을 저장한다.
        var strFname: String
        try{
            for (i in files.indices) {
                strFname = files[i].name.toString()
                adapter.add(strFname)
            }
        }catch (e: Exception) {}
        binding.btnAdd.setOnClickListener {
            var files = File(sdCardPath2).listFiles()   //files 디렉토리의 파일들을 저장한다.
            var fileName = binding.Routinename.text.toString()
            val routineFile = File("$sdCardPath2/$fileName")
            try {
                try {
                    for (i in files.indices) {
                        strFname = files[i].name.toString()
                        if (strFname == fileName) {
                            Toast.makeText(applicationContext, "이미 존재합니다", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                }catch (e : Exception){}
                val fos = FileOutputStream(routineFile)
                val msg = binding.spinner.getSelectedItem().toString()+" "+binding.cnt.text.toString()+"회\n"+binding.spinner2.getSelectedItem().toString()+" "+binding.cnt2.text.toString()+"회\n"+binding.spinner3.getSelectedItem().toString()+" "+binding.cnt3.text.toString()+"회\n"
                fos.write(msg.toByteArray())    //파일에 내용을 저장한다.
                fos.close()
                Toast.makeText(applicationContext, "$fileName 추가됨", Toast.LENGTH_SHORT).show()
                adapter.add(binding.Routinename.text.toString())
            } catch (fnfe: FileNotFoundException) {
                Toast.makeText(applicationContext, "루틴 이름을 입력하세요", Toast.LENGTH_SHORT).show()
            } catch (ioe: IOException) {
                Toast.makeText(applicationContext, "파일에 데이터를 쓸 수 없습니다", Toast.LENGTH_SHORT).show()
            }
            binding.Routinename.setText(null)
            binding.cnt.setText(null)
            binding.cnt2.setText(null)
            binding.cnt3.setText(null)
            binding.spinner.setSelection(0)
            binding.spinner2.setSelection(0)
            binding.spinner3.setSelection(0)
        }
        binding.ListView1.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(applicationContext, "롱클릭시 삭제", Toast.LENGTH_SHORT).show()
            var title = midList[position]
            var content : String
            var ifs = FileInputStream("$sdCardPath2/$title") //sd카드에 저장된 title.txt 파일을 읽기 모드로 연다.
            var txt = ByteArray(ifs.available())
            ifs.read(txt)
            content = txt.toString(Charsets.UTF_8) //title.txt의 내용을 읽고 스트링으로 변환하여 content에 저장
            ifs.close()
            var dlg = AlertDialog.Builder(this@MainActivity)
            dlg.setTitle("$title")
            dlg.setMessage("$content")
            dlg.show()
        }
        binding.ListView1.setOnItemLongClickListener { parent, view, position, id ->
            var title = midList[position]
            var file = File("$sdCardPath2/$title") //삭제할 파일 경로
            file.delete()   //해당 파일 삭제
            Toast.makeText(applicationContext, "$title 삭제됨", Toast.LENGTH_SHORT).show()
            midList.removeAt(position)
            adapter.notifyDataSetChanged()
            false
        }

        binding.fabStart.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) start() else pause()
        }

        binding.fabReset.setOnClickListener {
            reset()
        }

        binding.btnStart.setOnClickListener {
            try {
                conversionTime = binding.time.text.toString()
                if (conversionTime.length>6){
                    Toast.makeText(applicationContext, "6자리로 입력하세요", Toast.LENGTH_SHORT).show()
                    binding.time.text=null
                    return@setOnClickListener
                }
                countDown(conversionTime)
                binding.time.text=null
                mediaplayer = MediaPlayer.create(this, R.raw.finish_sound) //실행할 음악 파일
            }catch (e : Exception){
                Toast.makeText(applicationContext, "6자리로 입력하세요", Toast.LENGTH_SHORT).show()
                binding.time.text=null
            }
        }
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, BackMethodActivity::class.java)
            startActivity(intent)
        }
        binding.btnArm3.setOnClickListener {
            val intent = Intent(this, ArmMethodActivity::class.java)
            startActivity(intent)
        }
        binding.btnStomach5.setOnClickListener {
            val intent = Intent(this, AdominalMethodActivity::class.java)
            startActivity(intent)
        }
        binding.btnShoulder2.setOnClickListener {
            val intent = Intent(this, ShoulderMethodActivity::class.java)
            startActivity(intent)
        }
        binding.btnLeg4.setOnClickListener {
            val intent = Intent(this, LowerBodyActivity::class.java)
            startActivity(intent)
        }
        binding.btnChest5.setOnClickListener {
            val intent = Intent(this, ChestMethodActivity::class.java)
            startActivity(intent)
        }
        //현재 연, 월, 일을 구한다.
        var cal = Calendar.getInstance()
        var cYear = cal.get(Calendar.YEAR)
        var cMonth = cal.get(Calendar.MONTH)
        var cDay = cal.get(Calendar.DAY_OF_MONTH)
        curPath = "${cYear}/${cMonth + 1}/${cDay}/"
        setPermission()

        binding.camera.setOnClickListener{
            takeCapture(curPath)
        }
        binding.bodycam.setOnClickListener{
            if(File("sdcard/$curPath").listFiles() == null){
                Log.d("where","sdcard/$curPath")
                Toast.makeText(this,"No file in here",Toast.LENGTH_SHORT).show()
            }
            else
                getImage(curPath)
        }

        fileName2 = (Integer.toString(cYear) + "_"
                + Integer.toString(cMonth + 1) + "_"
                + Integer.toString(cDay) + "-weight.txt")
        fileName3 = (Integer.toString(cYear) + "_"
                + Integer.toString(cMonth + 1) + "_"
                + Integer.toString(cDay) + "-BFR.txt")
        fileName4 = (Integer.toString(cYear) + "_"
                + Integer.toString(cMonth + 1) + "_"
                + Integer.toString(cDay) + "-BMI.txt")

        var str2 = readRecord(fileName2)
        var str3 = readRecord(fileName3)
        var str4 = readRecord(fileName4)

        binding.edtWeight.setText(str2)
        binding.edtBFR.setText(str3)
        binding.edtBMI.setText(str4)
        binding.dp.init(cYear, cMonth, cDay) { view, year, monthOfYear, dayOfMonth ->
            fileName2 = (Integer.toString(year) + "_"
                    + Integer.toString(monthOfYear + 1) + "_"
                    + Integer.toString(dayOfMonth) + "-weight.txt")    //현재 날짜에 해당하는 파일 이름을 만든다.
            fileName3 = (Integer.toString(year) + "_"
                    + Integer.toString(monthOfYear + 1) + "_"
                    + Integer.toString(dayOfMonth) + "-BFR.txt")    //현재 날짜에 해당하는 파일 이름을 만든다.
            fileName4 = (Integer.toString(year) + "_"
                    + Integer.toString(monthOfYear + 1) + "_"
                    + Integer.toString(dayOfMonth) + "-BMI.txt")    //현재 날짜에 해당하는 파일 이름을 만든다.
            curPath = "${year}/${monthOfYear+1}/${dayOfMonth}/"
            str2 = readRecord(fileName2)
            str3 = readRecord(fileName3)
            str4 = readRecord(fileName4)
            binding.edtWeight.setText(str2)
            binding.edtBFR.setText(str3)
            binding.edtBMI.setText(str4)
        }


        binding.btnRecord.setOnClickListener {
            var myDir = File("$sdCardPath")//생성할 디렉토리의 경로를 설정한다.

            val weightFile = File("$sdCardPath/$fileName2")
            val bfrFile = File("$sdCardPath/$fileName3")
            val bmiFile = File("$sdCardPath/$fileName4")
            try {
                myDir.mkdir()   //mydiary 디렉토리를 생성한다.
                val fos2 = FileOutputStream(weightFile)
                val fos3 = FileOutputStream(bfrFile)
                val fos4 = FileOutputStream(bmiFile)
                val msg2 = binding.edtWeight.text.toString()
                val msg3 = binding.edtBFR.text.toString()
                val msg4 = binding.edtBMI.text.toString()
                fos2.write(msg2.toByteArray())    //파일에 내용을 저장한다.
                fos3.write(msg3.toByteArray())    //파일에 내용을 저장한다.
                fos4.write(msg4.toByteArray())    //파일에 내용을 저장한다.

                fos2.close()
                fos3.close()
                fos4.close()
                Toast.makeText(applicationContext, "기록이 저장됨", Toast.LENGTH_SHORT).show()
            } catch (fnfe: FileNotFoundException) {
                Toast.makeText(applicationContext, "지정된 파일을 생성할 수 없습니다", Toast.LENGTH_SHORT).show()
            } catch (ioe: IOException) {
                Toast.makeText(applicationContext, "파일에 데이터를 쓸 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
        val items = resources.getStringArray(R.array.my_array)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = myAdapter
        binding.spinner2.adapter = myAdapter
        binding.spinner3.adapter = myAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when(position) {
                    0   ->  {

                    }
                    1   ->  {

                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when(position) {
                    0   ->  {

                    }
                    1   ->  {

                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        binding.spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when(position) {
                    0   ->  {

                    }
                    1   ->  {

                    }
                    //...
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }


private fun getImage(curPath: String) {
        var intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("filePath",curPath)
        startActivity(intent)
    }

    fun start() {
        binding.fabStart.setImageResource(R.drawable.ic_pause)

        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100

            runOnUiThread {
                binding.secText.text = "$sec"
                binding.milliText.text = "$milli"
            }
        }
    }
    fun pause() {
        binding.fabStart.setImageResource(R.drawable.ic_play)
        timerTask?.cancel();
    }
    fun reset() {
        timerTask?.cancel()
        time = 0
        isRunning = false
        binding.fabStart.setImageResource(R.drawable.ic_play)
        binding.secText.text = "0"
        binding.milliText.text = "00"
        index = 1
    }
    fun countDown(time : String){
        var conversionTime : Long = 0
        var getHour = time.substring(0, 2)
        var getMin = time.substring(2, 4)
        var getSecond = time.substring(4, 6)

        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2)
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2)
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2)
        }
        conversionTime = getHour.toLong() * 1000 * 3600 + getMin.toLong() * 60 * 1000 + getSecond.toLong() * 1000

        object : CountDownTimer(conversionTime, 1000) {
            // 특정 시간마다 뷰 변경
            override fun onTick(millisUntilFinished: Long) {

                // 시간단위
                var hour = (millisUntilFinished / (60 * 60 * 1000)).toString()

                // 분단위
                val getMin = millisUntilFinished - millisUntilFinished / (60 * 60 * 1000)
                var min = (getMin / (60 * 1000)).toString() // 몫

                // 초단위
                var second = (getMin % (60 * 1000) / 1000).toString() // 나머지

                // 밀리세컨드 단위
                val millis = (getMin % (60 * 1000) % 1000).toString() // 몫

                // 시간이 한자리면 0을 붙인다
                if (hour.length == 1) {
                    hour = "0$hour"
                }

                // 분이 한자리면 0을 붙인다
                if (min.length == 1) {
                    min = "0$min"
                }

                // 초가 한자리면 0을 붙인다
                if (second.length == 1) {
                    second = "0$second"
                }
                binding.countView.setText("$hour:$min:$second")
            }

            // 제한시간 종료시
            override fun onFinish() {
                mediaplayer?.start()    //음악 재생
                // 변경 후
                binding.countView.setText("종료!")
            }
        }.start()
    }
    private fun takeCapture(curPath: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also{
                val photoFile: File? = try{
                    createImageFile(curPath)
                }catch (ex: IOException){
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "kr.ac.kpu.healthactivity.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(curPath: String): File? {

        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/$curPath")
        return File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir)
                .apply { curPhotoPath = absolutePath }
    }

    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(applicationContext,"ACCEPT",Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext,"DENIED",Toast.LENGTH_SHORT).show()
            }

        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라를 사용하려면 권한을 허용하세요")
            .setDeniedMessage("권한을 거부하였습니다. 권한을 허용하세요")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val bitmap : Bitmap
            val file = File(curPhotoPath)
            if(Build.VERSION.SDK_INT<28){
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
            }else{
                val decode = ImageDecoder.createSource(
                        this.contentResolver,
                        Uri.fromFile(file)
                )
                bitmap = ImageDecoder.decodeBitmap(decode)
            }
            savePhoto(bitmap, curPath)
        }
    }

    private fun savePhoto(bitmap: Bitmap, curPath:String) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/$curPath/"
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if(!folder.isDirectory){
            folder.mkdirs()
        }

        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }

    //기록을 읽는 함수
    fun readRecord(fName: String) : String? {
        var recordStr : String? = null
        val file = File("$sdCardPath/$fName")//읽어올 파일의 경로
        try {
            var fos = FileInputStream(file)
            var txt = ByteArray(fos.available())
            fos.read(txt)   //파일 내용을 txt에 읽어들인다.
            fos.close()
            recordStr = txt.toString(Charsets.UTF_8).trim()//txt를 문자열로 변경 한후 앞뒤의 공백을 제거하고 반환할 diartStr에 대입한다.
            binding.btnRecord.text = "수정 하기" //일기가 있는 상태이므로 버튼의 글자를 "수정하기"로 변경한다.
        } catch (e : IOException) {
            binding.btnRecord.text = "새로 저장"
        }
        return recordStr
    }
}