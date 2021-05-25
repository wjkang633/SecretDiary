package woojin.project.android.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val openButton by lazy {
        findViewById<Button>(R.id.openButton)
    }

    private val changePasswordButton by lazy {
        findViewById<Button>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if ((passwordPreferences.getString("password", "000")).equals(passwordFromUser)) {
                //패스워드 성공
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                //패스워드 실패
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            if (changePasswordMode) {
                //번호 저장 기능
//                passwordPreferences.edit {
//                    putString("password", "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}")
//                    commit()
//                }
                passwordPreferences.edit(true) {
                    putString(
                        "password",
                        "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
                    )
                }

                changePasswordMode = false

                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {
                //비밀번호 맞는지 체크
                val passwordFromUser =
                    "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

                if ((passwordPreferences.getString("password", "000")).equals(passwordFromUser)) {
                    //패스워드 성공
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    //패스워드 실패
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인", { dialog, which -> })
            .create().show()
    }
}