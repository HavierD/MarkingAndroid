package au.edu.utas.daih.a2huaizhidai530562

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityScoreBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScoreActivity : AppCompatActivity() {
    private lateinit var ui : ActivityScoreBinding

    //database
    val db = Firebase.firestore
    val studentCollection = db.collection("students")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val OutOfScore = intent.getStringExtra(OUT_OF_SCORE_KEY)
        var scoreInt = 1
        if (OutOfScore != null){
             scoreInt = OutOfScore.toInt()
        }
        val ID = intent.getStringExtra(ID_KEY)
        val wk = intent.getStringExtra(WK_KEY)
        val wk1 = when(wk){
            "Week 2" -> "w2"
            "Week 3" -> "w3"
            "Week 4" -> "w4"
            "Week 5" -> "w5"
            "Week 6" -> "w6"
            "Week 7" -> "w7"
            "Week 8" -> "w8"
            "Week 9" -> "w9"
            "Week 10" -> "w10"
            "Week 11" -> "w11"
            "Week 12" -> "w12"
            else -> ""
        }
        ui.scoreText.text = "The total score is " + OutOfScore +"\n" + "StudentID: " +ID + "\n" + wk
        var score100: Int = 0






        ui.cancelScore.setOnClickListener {
            var i = Intent(this, WeekActivity::class.java)
            startActivity(i)
            finish()
        }
        ui.confirmScore.setOnClickListener {

            if(OutOfScore != null) {
                var a = ui.scoreInput.text.toString().toIntOrNull()
                var b = OutOfScore.toInt()
                if (a != null) {
                    score100 = a * 100 / b
                }
            }
            if (ui.scoreInput.text.toString() == "") {

                MaterialAlertDialogBuilder(it.context)
                        .setTitle("ERROR!")
                        .setMessage("The score cannot be empty!")
                        .setCancelable(true)
                        .show()
            }else if (ui.scoreInput.text.toString().toInt() in 0..scoreInt) {
                if (ID != null) {
                    studentCollection.document(ID)
                            .update(wk1, score100)
                            .addOnSuccessListener { Log.d(FIREBASE_TAG, "score success") }
                            .addOnFailureListener { e -> Log.w(FIREBASE_TAG, "score error", e) }
                    var i = Intent(this, WeekActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }else {
                MaterialAlertDialogBuilder(it.context)
                        .setTitle("ERROR!")
                        .setMessage("The score cannot be out of the scope! 0 - $OutOfScore")
                        .setCancelable(true)
                        .show()

                }

        }
    }
}