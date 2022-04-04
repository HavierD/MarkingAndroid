package au.edu.utas.daih.a2huaizhidai530562

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivitySchemeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


const val RESULT_SCHEME_NO_CHANGED = 0
const val RESULT_SCHEME_CHANGED = 1
const val SCHEME_KEY = "one of five scheme"
const val SCHEME_NUMBER_KEY = "a number"

class schemeActivity : AppCompatActivity(){

    private lateinit var ui : ActivitySchemeBinding


    //get data from database
    val db = Firebase.firestore
    val schemeCollection = db.collection("scheme")


    private fun updateScheme(wkNo:String, type:String, number:Int=0){
        schemeCollection.document(wkNo).update("type",type)
                .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully updated") }
                .addOnFailureListener{e -> Log.w(FIREBASE_TAG, "Error updating document",e)}
        schemeCollection.document(wkNo).update("number", number)
                .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully updated") }
                .addOnFailureListener{e -> Log.w(FIREBASE_TAG, "Error updating document",e)}

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySchemeBinding.inflate(layoutInflater)
        setContentView(ui.root)



        //get string from intent
        val wkNo = intent.getStringExtra(WEEKNO_KEY).toString()






       //get data from data base 2.0
        schemeCollection.document(wkNo).get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG, result.toString())
            val scheme = result.toObject<schemeClass>()
            if (scheme != null) {
                ui.currentWeekScheme.text = scheme.type
            }
        }
        ui.currentWeek.text = wkNo

        // series of onclick listener
        ui.cancelBtnMS.setOnClickListener {
            setResult(RESULT_SCHEME_NO_CHANGED)
            finish()
        }

        ui.checkBoxBtn.setOnClickListener {
            val data = Intent()
            updateScheme(wkNo,"Checkbox")
 //           data.putExtra(SCHEME_KEY, "Checkbox")
            setResult(RESULT_SCHEME_CHANGED, data)
            finish()
        }

        ui.gradeHDBtn.setOnClickListener {
            val scheme = Intent()
            updateScheme(wkNo,"Grade (HD/DN/CR/PP/NN)")
//            scheme.putExtra(SCHEME_KEY, "Grade (HD/DN/CR/PP/NN)")
            setResult(RESULT_SCHEME_CHANGED, scheme)
            finish()
        }

        ui.gradeABtn.setOnClickListener {
            val scheme = Intent()
            updateScheme(wkNo,"Grade (A/B/C/D/F)")
//            scheme.putExtra(SCHEME_KEY, "Grade (A/B/C/D/F)")
            setResult(RESULT_SCHEME_CHANGED, scheme)
            finish()
        }

        ui.MultiCheckBoxBtn.setOnClickListener {
            val a = ui.MultiCheckBoxInput.text.toString().toInt()
             if(a in 2..5)
            {
                val scheme = Intent()
                updateScheme(wkNo,"Multi-Checkbox")
                updateScheme(wkNo,"Multi-Checkbox",ui.MultiCheckBoxInput.text.toString().toInt())
                setResult(RESULT_SCHEME_CHANGED, scheme)
                finish()

            }else{
                var builder1 = AlertDialog.Builder(it.context)
                builder1.setTitle("The Number of Checkboxes is incorrect!")
                builder1.setMessage("The number cannot be empty and must be between 2 and 5 (inclusive).")
                builder1.apply {
                    setNegativeButton(R.string.cancel, { dialog, id ->  })
                }
                builder1.setCancelable(true)
                builder1.create().show()
            }
        }

        ui.outOfNoBtn.setOnClickListener {
            val a = ui.outOfNoInput.text.toString().toInt()
           if (a in 1..100){
                val scheme = Intent()
                updateScheme(wkNo, "Score",ui.outOfNoInput.text.toString().toInt())
                setResult(RESULT_SCHEME_CHANGED, scheme)
                finish()
            }else{
                var builder1 = AlertDialog.Builder(it.context)
                builder1.setTitle("The Total Score is incorrect!")
                builder1.setMessage("The number of total score cannot be empty and must be between 1 and 100 (inclusive).")
                builder1.apply {
                    setNegativeButton(R.string.cancel, { dialog, id ->})
                }
                builder1.setCancelable(true)
                builder1.create().show()
            }
        }


    }

}