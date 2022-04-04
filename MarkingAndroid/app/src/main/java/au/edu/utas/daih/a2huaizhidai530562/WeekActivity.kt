package au.edu.utas.daih.a2huaizhidai530562


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityWeekBinding
import au.edu.utas.daih.a2huaizhidai530562.databinding.WeeklyStudentListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

val items = mutableListOf<studentClass>()


const val FIREBASE_TAG = "FirebaseLogging"
const val TOSTUDENT_KEY = "tostudentpage"
const val WEEKNO_KEY = "week number"
const val REQUEST_SCHEME = 105
const val ID_KEY = "123"
const val WK_KEY = "wkkey"
const val WEEKK_KEY = "week 2 key"

const val OUT_OF_SCORE_KEY = "total score"


class WeekActivity : AppCompatActivity() {

    private lateinit var ui : ActivityWeekBinding
    val singleCheckbox = arrayOf("attended")
    val multi2 = arrayOf("1", "2")
    val multi3 = arrayOf("1", "2", "3")
    val multi4 = arrayOf("1", "2", "3", "4")
    val multi5 = arrayOf("1", "2", "3", "4", "5")
    var number1  = 0   //scheme number when is multi or outofscore
    var summary = 0
    var summary1 = 0
    var count = 0

   // val multiCheckboxArray = arrayOf ("1")
    var attendance = false//checkbox var
    var gradeHDA = 0//gradeHD var
    var score = 0

    // data base
    val db = Firebase.firestore
    val studentCollection = db.collection("students")
    val schemeCollection = db.collection("scheme")


    //var wkNo = intent.getStringExtra(WEEK_KEY)

    //summary function
    fun summary() {
        studentCollection.get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG, "all scores")
            for (document in result) {
                count += 1
                val st = document.toObject<studentClass>()
                st.did = document.id
                var a: Int? = when (ui.weekTitle1.text.toString()) {
                    "Week 2" -> st.w2
                    "Week 3" -> st.w3
                    "Week 4" -> st.w4
                    "Week 5" -> st.w5
                    "Week 6" -> st.w6
                    "Week 7" -> st.w7
                    "Week 8" -> st.w8
                    "Week 9" -> st.w9
                    "Week 10" -> st.w10
                    "Week 11" -> st.w11
                    "Week 12" -> st.w12
                    else -> -1
                }
                if (a == null) {
                    a = 0
                }
                summary += a
                summary1 = summary / count
            }
            ui.summaryMark.text = summary1.toString()
        }
        if (ui.titleMark.text.contains("score", ignoreCase = true)) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityWeekBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //get week number
        val wkNo = intent.getStringExtra(WEEK_KEY)

        if (wkNo != null){
            ui.weekTitle1.text = wkNo
        }
        else{
          
        }

        val wkNo3 = ui.weekTitle1.text.toString()

        //filter tool
        var yesOrNo = 0

        //get student data from data base
        studentCollection.get().addOnSuccessListener { result ->
            items.clear()
            Log.d(FIREBASE_TAG, "---All Students---")
            for (document in result){
                val studentFor = document.toObject<studentClass>()
                studentFor.did = document.id
                Log.d(FIREBASE_TAG,studentFor.toString())
                items.add(studentFor)
                (ui.studentList.adapter as studentAdapter).notifyDataSetChanged()
                var studentCount = items.size.toString()
                ui.studentCount.text = studentCount + " students"
            }
        }
        //get scheme data from data base
        schemeCollection.document(wkNo3).get().addOnSuccessListener { result ->
            Log.d(FIREBASE_TAG,"scheme")
            val scheme = result.toObject<schemeClass>()

            if (scheme != null) {
                ui.titleMark.text = scheme.type
                number1 = scheme.number
            }
            else{
            }
        }
        //go gack
        ui.backToHome.setOnClickListener {
            val i2 = Intent(this, HomeActivity::class.java)
            i2.putExtra(WEEKNO_KEY, wkNo)
            startActivity(i2)

        }
        //edit Scheme
        ui.editScheme.setOnClickListener {
            var i3 = Intent(this, schemeActivity::class.java)
            i3.putExtra(WEEKNO_KEY, wkNo3)
            startActivityForResult(i3, REQUEST_SCHEME)
        }

        //recycler View
        ui.studentList.adapter = studentAdapter(studentRow = items)
        ui.studentList.layoutManager = LinearLayoutManager(this)

        //add new student
        ui.newStudentBtn.setOnClickListener {
            var i = Intent(this,newStudentActivity::class.java)
            i.putExtra(WEEKK_KEY, wkNo3)
            startActivity(i)
        }

        //get all data of scores
        var weekToWk = when(ui.weekTitle1.text.toString()){
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


        summary()

        //filter
        ui.filterBtn.setOnClickListener {
            var a = ui.filterInput.text.toString()

            if(a == "" && yesOrNo == 0){
                MaterialAlertDialogBuilder(it.context)
                        .setTitle("ERROR!")
                        .setMessage("You have to input something otherwise I can do nothing!")
                        .setCancelable(true)
                        .show()
            }else if(a == ""){
                studentCollection.get().addOnSuccessListener { result ->
                    items.clear()
                    Log.d(FIREBASE_TAG, "---All Students---")
                    for (document in result){
                        val studentFor = document.toObject<studentClass>()
                        studentFor.did = document.id
                        Log.d(FIREBASE_TAG,studentFor.toString())
                        items.add(studentFor)
                        (ui.studentList.adapter as studentAdapter).notifyDataSetChanged()
                        var studentCount = items.size.toString()
                        ui.studentCount.text = studentCount + " students"
                    }
                }
                yesOrNo = 0
            }
            else{
                yesOrNo += 1
                studentCollection.whereEqualTo("id", a)
                        .get()
                        .addOnSuccessListener { result ->
                            Log.d(FIREBASE_TAG, "filter")
                            items.clear()
                            var filterCount = 0
                            for (document in result){
                                val studentFor = document.toObject<studentClass>()
                                studentFor.did = document.id
                                Log.d(FIREBASE_TAG,studentFor.toString())
                                items.add(studentFor)
                                (ui.studentList.adapter as studentAdapter).notifyDataSetChanged()
                                var studentCount = items.size.toString()
                                ui.studentCount.text = studentCount + " students"
                                filterCount+= 1
                            }
                            if (filterCount == 0){
                                MaterialAlertDialogBuilder(it.context)
                                        .setTitle("ERROR!")
                                        .setMessage("No Student's ID is $a !!")
                                        .setCancelable(true)
                                        .show()
                            }
                        }
            }
        }

        ui.weekHelp.setOnClickListener {
            var a = getString(R.string.WeekHelp1) + "\n\n" + getString(R.string.WeekHelp2) + "\n\n" +
             getString(R.string.weekHelp3) + "\n\n" + getString(R.string.WeekHelp4)
            MaterialAlertDialogBuilder(it.context)
                    .setTitle(R.string.HELP)
                    .setMessage(a)
                    .setCancelable(true)
                    .show()

        }



    }///////////////////////on  create  eeeeeeeeeeeeeeeeeeeeeeeeend

    // get scheme result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SCHEME)
        {
            if (resultCode == RESULT_SCHEME_NO_CHANGED)
            {

            }
            else if (resultCode == RESULT_SCHEME_CHANGED)
            {
                val wkNo = ui.weekTitle1.text.toString()
                val db = Firebase.firestore
                val schemeCollection = db.collection("scheme")
                schemeCollection.document(wkNo).get().addOnSuccessListener { result ->
                    Log.d(FIREBASE_TAG, "new scheme")
                    val scheme = result.toObject<schemeClass>()
                    if (scheme != null) {
                        ui.titleMark.text = scheme.type
                        number1 = scheme.number



                    }

                }
            }

        }
    }





    inner class studentHolder(var ui : WeeklyStudentListBinding ) : RecyclerView.ViewHolder(ui.root){}
    inner class studentAdapter(private val studentRow: MutableList<studentClass>) : RecyclerView.Adapter<studentHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): studentHolder {
            val ui = WeeklyStudentListBinding.inflate(layoutInflater,parent,false)
            return studentHolder(ui)
        }


        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: studentHolder, position: Int) {
            val st = studentRow[position]


            holder.ui.weeklyStudentFirstName.text = st.firstName
            holder.ui.weeklyStudentFamilyName.text = st.familyName
            holder.ui.weeklyStudentID.text = st.id

            //display marks
            //           ////////////////////////////////////////// //display score    1
            if(ui.titleMark.text.contains("Score", ignoreCase = true)){
                var b = 0
                var a: Int? = when(ui.weekTitle1.text.toString()) {
                    "Week 2" -> st.w2
                    "Week 3" -> st.w3
                    "Week 4" -> st.w4
                    "Week 5" -> st.w5
                    "Week 6" -> st.w6
                    "Week 7" -> st.w7
                    "Week 8" -> st.w8
                    "Week 9" -> st.w9
                    "Week 10" -> st.w10
                    "Week 11" -> st.w11
                    "Week 12" -> st.w12
                    else -> -1
                }
                if(a != null){
                    b = a*number1/100
                }
                holder.ui.weeklyStudentMark.text = "$b/$number1"

            }else if (ui.titleMark.text.contains("HD", ignoreCase = true)){   ///////////display HD   2
                var a = when(ui.weekTitle1.text.toString()){
                    "Week 2" -> st.w2
                    "Week 3" -> st.w3
                    "Week 4" -> st.w4
                    "Week 5" -> st.w5
                    "Week 6" -> st.w6
                    "Week 7" -> st.w7
                    "Week 8" -> st.w8
                    "Week 9" -> st.w9
                    "Week 10" -> st.w10
                    "Week 11" -> st.w11
                    "Week 12" -> st.w12
                    else -> -1
                }
                holder.ui.weeklyStudentMark.text = when(a){
                    100 -> "HD+"
                    80 -> "HD"
                    70 -> "DN"
                    60 -> "CR"
                    50 -> "PP"
                    0 -> "NN"
                    null -> "NN"
                    else -> a.toString()
                }
            }else if(ui.titleMark.text.contains("A/B", ignoreCase = true)){   //////////////display A/B   3
                var a = when(ui.weekTitle1.text.toString()){
                    "Week 2" -> st.w2
                    "Week 3" -> st.w3
                    "Week 4" -> st.w4
                    "Week 5" -> st.w5
                    "Week 6" -> st.w6
                    "Week 7" -> st.w7
                    "Week 8" -> st.w8
                    "Week 9" -> st.w9
                    "Week 10" -> st.w10
                    "Week 11" -> st.w11
                    "Week 12" -> st.w12
                    else -> -1
                }
                holder.ui.weeklyStudentMark.text = when(a){
                    100 ->"A"
                    80 -> "B"
                    70 -> "C"
                    60 -> "D"
                    0 -> "F"
                    null -> "F"
                    else -> a.toString()
                }
            }else if(ui.titleMark.text.contains("mult", ignoreCase = true)){    ////////////////display multibox 444
                var a = when(ui.weekTitle1.text.toString()){
                    "Week 2" -> st.w2
                    "Week 3" -> st.w3
                    "Week 4" -> st.w4
                    "Week 5" -> st.w5
                    "Week 6" -> st.w6
                    "Week 7" -> st.w7
                    "Week 8" -> st.w8
                    "Week 9" -> st.w9
                    "Week 10" -> st.w10
                    "Week 11" -> st.w11
                    "Week 12" -> st.w12
                    else -> -1
                }
                var b = 0
                var c = 0
                if(a != null){
                    c = a + 1
                    b = c*number1/100
                }
                var d = ""
                d = "$b/$number1"
                holder.ui.weeklyStudentMark.text = d
            }else{
                holder.ui.weeklyStudentMark.text = when (ui.weekTitle1.text.toString()) {   /////////////display score   5
                    "Week 2" -> st.w2.toString()
                    "Week 3" -> st.w3.toString()
                    "Week 4" -> st.w4.toString()
                    "Week 5" -> st.w5.toString()
                    "Week 6" -> st.w6.toString()
                    "Week 7" -> st.w7.toString()
                    "Week 8" -> st.w8.toString()
                    "Week 9" -> st.w9.toString()
                    "Week 10" -> st.w10.toString()
                    "Week 11" -> st.w11.toString()
                    "Week 12" -> st.w12.toString()
                    else -> "error"
                }
            }
            //turn to single student function
        fun turnToStudent(){
            val wkNo1 = ui.weekTitle1.text
            val i =  Intent(this@WeekActivity,StudentActivity::class.java)
            i.putExtra(TOSTUDENT_KEY, st.did.toString())
            i.putExtra(WEEKNO_KEY, wkNo1)
            startActivity(i)
        }

            fun updateMark(id : String, wk : String, mark: Int){
                var wk1 = when(wk){
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
                studentCollection.document(id)
                        .update(wk1,mark)
            }

            holder.ui.weeklyStudentFirstName.setOnClickListener {
                turnToStudent()
            }
            holder.ui.weeklyStudentFamilyName.setOnClickListener {
                turnToStudent()
            }
            holder.ui.weeklyStudentID.setOnClickListener {
                turnToStudent()
            }

            //modify marks
            holder.ui.weeklyStudentMark.setOnClickListener {
                if(ui.titleMark.text == "Checkbox")///////////////////////////////////////////////////////////checkbox 1
                {

                    val sin = ArrayList<Int>()
                    val checkedItems = booleanArrayOf(false)
                    var markSin : Int = 0
                    MaterialAlertDialogBuilder(it.context)
                            .setTitle(R.string.checkboxTitle)
                            .setNegativeButton(R.string.cancel){ dialog,which -> }
                            .setPositiveButton(R.string.ok){dialog, which ->
                                if(sin.size == 0){
                                    markSin = 0
                                }else if(sin.size == 1){
                                    markSin = 100
                                }
                                updateMark(st.did.toString(), ui.weekTitle1.text.toString(), markSin)
                                holder.ui.weeklyStudentMark.text = markSin.toString()
                                summary()
                            }
                            .setMultiChoiceItems(singleCheckbox,checkedItems,
                                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                                        sin.clear()
                                        if(isChecked){
                                            sin.add(which)
                                        }else if (sin.contains(which)) {
                                            sin.remove(Integer.valueOf(which))
                                        }
                                    })
                            .show()
                }else if(ui.titleMark.text.contains("HD",ignoreCase = false)){///////////////////////HD 2
                    val singleItems = arrayOf("HD+","HD","DN","CR","PP","NN")
                    val checkedItem = 5
                    MaterialAlertDialogBuilder(it.context)
                            .setTitle(R.string.gradeHDTitle)
                            .setNegativeButton(R.string.cancel){ dialog, which -> }
                            .setPositiveButton(R.string.ok){dialog, which ->
                                updateMark(st.did.toString(),ui.weekTitle1.text.toString(),gradeHDA)
                                var a = gradeHDA
                                var b = when(a){
                                    100 -> "HD+"
                                    80 -> "HD"
                                    70 -> "DN"
                                    60 -> "CR"
                                    50 -> "PP"
                                    0 -> "NN"
                                    else -> a.toString()
                                }
                                holder.ui.weeklyStudentMark.text = b
                                summary()
                            }
                            .setSingleChoiceItems(singleItems, checkedItem){dialog, which ->
                                gradeHDA = when(which){
                                    0 -> 100
                                    1 -> 80
                                    2 -> 70
                                    3 -> 60
                                    4 -> 50
                                    5 -> 0
                                else -> gradeHDA
                                }
                            }
                            .show()
                }else if (ui.titleMark.text.contains("A/B",ignoreCase = false)){ /////////////////ABC 3
                    val singleItems = arrayOf("A","B","C","D","F")
                    val checkedItem = 4
                    MaterialAlertDialogBuilder(it.context)
                            .setTitle(R.string.gradeHDTitle)
                            .setNegativeButton(R.string.cancel){ dialog, which -> }
                            .setPositiveButton(R.string.ok){dialog, which ->
                                updateMark(st.did.toString(),ui.weekTitle1.text.toString(),gradeHDA)
                                var a = gradeHDA
                                var b = when(a){
                                    100 -> "A"
                                    80 -> "B"
                                    70 -> "C"
                                    60 -> "D"
                                    0 -> "F"
                                    else -> a.toString()
                                }
                                holder.ui.weeklyStudentMark.text = b
                                summary()
                            }
                            .setSingleChoiceItems(singleItems, checkedItem){dialog, which ->
                                gradeHDA = when(which){
                                    0 -> 100
                                    1 -> 80
                                    2 -> 70
                                    3 -> 60
                                    4 -> 0
                                    else -> gradeHDA
                                }
                            }
                            .show()
                }else if(ui.titleMark.text.contains("multi", ignoreCase = true)){////////////////Multi 4
                    val multi = ArrayList<Int>()
                //    val multiCheckedItems = booleanArrayOf(false)
                    var markMulti = 0
                    var mul = when(number1){
                        2 -> multi2
                        3 -> multi3
                        4 -> multi4
                        5 -> multi5
                        else -> multi5
                    }
                    MaterialAlertDialogBuilder(it.context)
                            .setTitle(R.string.MultiCheckBox)
                            .setNegativeButton(R.string.cancel){ dialog, which ->}
                            .setPositiveButton(R.string.ok){dialog, which ->
                                score = multi.size*100/number1
                                updateMark(st.did.toString(),ui.weekTitle1.text.toString(),score)
                                var a = score
                                var c = a + 1
                                var d = c*number1/100

                                holder.ui.weeklyStudentMark.text = "$d/$number1"
                                summary()
                            }
                            .setMultiChoiceItems( mul, null,
                                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                                        if(isChecked){
                                            multi.add(which)
                                        }else if(multi.contains(which)){
                                            multi.remove(Integer.valueOf(which))
                                        }
                            })
                            .show()

                }else if(ui.titleMark.text.contains("score", ignoreCase = true)){ ///////////////////score 5
                    val i = Intent(this@WeekActivity, ScoreActivity::class.java)
                    i.putExtra(OUT_OF_SCORE_KEY, number1.toString())
                    i.putExtra(ID_KEY, st.did.toString())
                    i.putExtra(WK_KEY, ui.weekTitle1.text.toString())
                    startActivity(i)



                }
            }
        }
        override fun getItemCount(): Int {
            return studentRow.size
        }
    }
}













