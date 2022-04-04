package au.edu.utas.daih.a2huaizhidai530562

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityStudentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


//var singleStudent = mutableListOf<studentClass>()



const val IDD_KEY = "student ID"


class StudentActivity : AppCompatActivity() {
    private lateinit var ui : ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(ui.root)

        // get week number
        val wkNo2 = intent.getStringExtra(WEEKNO_KEY)

        //share
        var shareText = ""

        //document id
        var did = ""

        //go back
        ui.backToWeek.setOnClickListener {
            val i2 = Intent(this,WeekActivity::class.java)
            i2.putExtra(WEEK_KEY, wkNo2)
            startActivity(i2)
        }

        //receive data from week

        val idStdPage:String = intent.getStringExtra(TOSTUDENT_KEY).toString()


        ui.id.text = idStdPage

        //database
        val db = Firebase.firestore
        val studentCollection = db.collection("students")

        //show information
        var name = "none"
        var summary = 0
        var summary1 = 0
        var a2 = 0
        var a3 = 0
        var a4 = 0
        var a5 = 0
        var a6 = 0
        var a7 = 0
        var a8 = 0
        var a9 = 0
        var a10 = 0
        var a11 = 0
        var a12 = 0

        studentCollection.document(idStdPage).get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG, result.toObject<studentClass>().toString())
            val a = result.toObject<studentClass>()
            if (a != null) {
                a.did = result.id


               // ui.otherName.text = a.otherName
                ui.id.text = a.id.toString()

                ui.SSm2.text = a.w2.toString()
                ui.SSm3.text = a.w3.toString()
                ui.SSm4.text = a.w4.toString()
                ui.SSm5.text = a.w5.toString()
                ui.SSm6.text = a.w6.toString()
                ui.SSm7.text = a.w7.toString()
                ui.SSm8.text = a.w8.toString()
                ui.SSm9.text = a.w9.toString()
                ui.SSm10.text = a.w10.toString()
                ui.SSm11.text = a.w11.toString()
                ui.SSm12.text = a.w12.toString()

                name = a.firstName + " " + a.familyName
                ui.studentName.text = name

                //summary
                a2 = if (a.w2 == null) 0 else a.w2.toString().toInt()
                a3 = if (a.w3 == null) 0 else a.w3.toString().toInt()
                a4 = if (a.w4 == null) 0 else a.w4.toString().toInt()
                a5 = if (a.w5 == null) 0 else a.w5.toString().toInt()
                a6 = if (a.w6 == null) 0 else a.w6.toString().toInt()
                a7 = if (a.w7 == null) 0 else a.w7.toString().toInt()
                a8 = if (a.w8 == null) 0 else a.w8.toString().toInt()
                a9 = if (a.w9 == null) 0 else a.w9.toString().toInt()
                a10 = if (a.w10 == null) 0 else a.w10.toString().toInt()
                a11 = if (a.w11 == null) 0 else a.w11.toString().toInt()
                a12 = if (a.w12 == null) 0 else a.w12.toString().toInt()

                summary1 = a2+a3+a4+a5+a6+a7+a8+a9+a10+a11+a12
                summary = summary1/11
                ui.singleSummaryMark.text = summary.toString()

                // inflate share variable
                shareText = "Student Name: $name. Student ID: ${a.id}; Marks: Week 2: $a2, Week 3: $a3" +
                        "Week 4: $a4, Week 5: $a5, Week 6: $a6, Week 7: $a7, Week 8:$a8, Week 9: $a9, " +
                        "Week 10: $a10, Week 11: $a11, Week 12: $a12; Summary: $summary"
            }
        }

        ui.removeBtnSS.setOnClickListener {

            //test


            MaterialAlertDialogBuilder(it.context)
                .setTitle("Warning!")
                .setMessage("Are you sure to remove $name ?")
                .setNegativeButton(R.string.cancel){dialog, which ->}
                .setCancelable(true)
                .setPositiveButton(R.string.ok){dialog, which ->

                    //second level alert
                    MaterialAlertDialogBuilder(it.context)
                        .setTitle("Warning Again!")
                        .setMessage("All the data of this student will be removed from the database, and this operation cannot be undone!")
                        .setCancelable(true)
                        .setNegativeButton(R.string.cancel){dialog, which ->}
                        .setPositiveButton(R.string.ok){dialog, which ->
                            studentCollection.document(idStdPage)
                                .delete()
                                .addOnSuccessListener { Log.d(FIREBASE_TAG, "DELETE success") }
                                .addOnFailureListener{ e -> Log.w (FIREBASE_TAG, "delete fail", e)}
                            val i = Intent(this@StudentActivity, WeekActivity::class.java)
                            i.putExtra(WEEK_KEY,wkNo2)
                            startActivity(i)
                        }
                        .show()
                }
                .show()
        }

        //edit    student
        ui.modifyBtnSS.setOnClickListener {
            val i = Intent(this, newStudentActivity::class.java)
            i.putExtra(IDD_KEY, idStdPage)
            startActivity(i)

        }

        //share
        ui.shareBtnSS1.setOnClickListener {
            var sendIntent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, "share via ..."))
        }




    } ///////////////////////////////////// on create eeeeeeeeeeeeeeeeeeend
}

