package au.edu.utas.daih.a2huaizhidai530562

//import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


const val NEWUSER_KEY : String= "newUser"
const val OLDUSER_KEY = "oldUser"



class MainActivity : AppCompatActivity() {

    private lateinit var ui : ActivityMainBinding

//    var setting = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//    var oldUser = setting.getBoolean(OLDUSER_KEY, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var setting = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//        var oldUser = setting.getBoolean(OLDUSER_KEY, false)

//        if (oldUser == true) {
            val i2 = Intent(this, HomeActivity::class.java)
            startActivity(i2)
//        } else {



            ui = ActivityMainBinding.inflate(layoutInflater)
            setContentView(ui.root)




            ui.originalChooseNo.setOnClickListener {
//                with(setting.edit()){
//                    putBoolean(OLDUSER_KEY, true)
//                    apply()
//                }
                val i1 = Intent(this, HomeActivity::class.java)
                startActivity(i1)
            }

            ui.originalChooseYes.setOnClickListener {

                val i2 = Intent(this, HomeActivity::class.java)
                startActivity(i2)

            }

            val db = Firebase.firestore
//        }
    }
}