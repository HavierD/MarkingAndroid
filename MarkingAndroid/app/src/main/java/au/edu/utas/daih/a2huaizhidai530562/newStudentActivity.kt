package au.edu.utas.daih.a2huaizhidai530562

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityNewStudentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


const val BACKTOSTUDENT_KEY = "back to student"
const val REQUEST_IMAGE_CAPTURE = 1;

class newStudentActivity : AppCompatActivity() {
    private lateinit var ui:ActivityNewStudentBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNewStudentBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //get week number
        val wk = intent.getStringExtra(WEEKK_KEY)

        //database
        val db = Firebase.firestore
        val studentCollection = db.collection("students")

        val edit = intent.getStringExtra(IDD_KEY)
        var name = " "
        if (edit != null) {

            studentCollection.document(edit).get().addOnSuccessListener { result ->
                Log.d(FIREBASE_TAG, result.toObject<studentClass>().toString())
                val a = result.toObject<studentClass>()
                ui.newStudentTitle.text = getString(R.string.editTitle)
                if (a != null) {
                    ui.firstNameNew.setText(a.firstName)
                    ui.familyNameNew.setText(a.familyName)
                    ui.studentIDNew.setText(edit)
                    name = a.firstName + " " + a.familyName
                }
            }
        }
        //add and goback function
        fun addAndGoBack(id : String, firstName : String, familyName : String){
            studentCollection.document(id)
                    .set(hashMapOf(
                            "firstName" to firstName,
                            "familyName" to familyName,
                            "id" to  id,
                    ))
                    .addOnSuccessListener { Log.d(FIREBASE_TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener{e -> Log.e(FIREBASE_TAG, "Error writing document", e)}

            var i = Intent(this,WeekActivity::class.java)
            i.putExtra(WEEK_KEY, wk)
            startActivity(i)
            finish()
        }
        // update and goback function
        fun updateAndGoBack( firstName : String, familyName : String, id : String){
            if (edit != null) {
                studentCollection.document(edit)
                        .update( "firstName", firstName)
                studentCollection.document(edit)
                        .update("familyName", familyName)
                studentCollection.document(edit)
                        .update("id", id )
            }
            var i = Intent(this, StudentActivity::class.java)
            i.putExtra(TOSTUDENT_KEY, edit)
            startActivity(i)
            finish()
        }


        ui.confirmAdd.setOnClickListener {


            if (ui.firstNameNew.text.toString() == ""){
                var builder = AlertDialog.Builder(it.context)
                builder.setTitle(R.string.firstNameEmptyAlert)
                builder.setCancelable(true)
                builder.setPositiveButton(R.string.ok, {dialog, which ->})
                builder.create().show()

            }else if(ui.familyNameNew.text.toString() == ""){
                var builder = AlertDialog.Builder(it.context)
                builder.setTitle(R.string.familyNameEmptyAlert)
                builder.setCancelable(true)
                builder.setPositiveButton(R.string.ok, {dialog, which ->})
                builder.create().show()
            }else if(ui.studentIDNew.text.toString() == ""){
                var builder = AlertDialog.Builder(it.context)
                builder.setTitle(R.string.studentIDEmptyAlert)
                builder.setCancelable(true)
                builder.setPositiveButton(R.string.ok,{dialog, which ->})
                builder.create().show()
            }else{

                if(edit != null){

                    var builder = AlertDialog.Builder(it.context)
                    builder.setTitle("Information of $name has been updated!")
                    builder.setCancelable(true)
                    builder.setPositiveButton(R.string.ok){dialog, which ->
                        updateAndGoBack(ui.firstNameNew.text.toString(), ui.familyNameNew.text.toString(), ui.studentIDNew.text.toString())
                    }
                    builder.create().show()
                }else {
                    var builder = AlertDialog.Builder(it.context)
                    builder.setTitle(R.string.addSuccessfullyAlert)
                    builder.setMessage("New student name: " + ui.firstNameNew.text.toString()
                            + " " + ui.familyNameNew.text.toString()
                            + "\n" + "New Student ID: " + ui.studentIDNew.text.toString())
                    builder.setCancelable(true)
                    builder.setPositiveButton(R.string.ok) { dialog, which ->
                        addAndGoBack(ui.studentIDNew.text.toString(),
                                ui.firstNameNew.text.toString(),
                                ui.familyNameNew.text.toString())
                    }
                    builder.create().show()
                }
            }

        }
        ui.cancelAdd.setOnClickListener {
            if (edit != null){
                val i = Intent(this, StudentActivity::class.java)
                i.putExtra(TOSTUDENT_KEY, edit)
                startActivity(i)
                finish()
            }else {
                val i = Intent(this, WeekActivity::class.java)
                startActivity(i)
                i.putExtra(WEEK_KEY, wk)
                finish()
            }
        }


        //camera function
        ui.takeAPhotoByCamera.setOnClickListener {
            requestToTakeAPicture()

        }
    } /////////////////////onCreate    eeeeeeeeeeeeeeeeeeeeeeeeend

    // picture part
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun requestToTakeAPicture(){
//        requestPermissions(
//                arrayOf(Manifest.permission.CAMERA),
//                REQUEST_IMAGE_CAPTURE
//        )
//
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestToTakeAPicture()
    {
        requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
        )
    }

    //step 5
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            REQUEST_IMAGE_CAPTURE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted.
                    takeAPicture()
                } else {
                    Toast.makeText(this, "Cannot access camera, permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //step 6
    private fun takeAPicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //try {
        val photoFile: File = createImageFile()!!
        val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "au.edu.utas.daih.a2huaizhidai530562",
                photoFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        //} catch (e: Exception) {}

    }

    //step 6 part 2
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //step 7
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            setPic(ui.imageView)
        }
    }

    //step 7 pt2
    private fun setPic(imageView: ImageView) {
        // Get the dimensions of the View
        val targetW: Int = imageView.measuredWidth
        val targetH: Int = imageView.measuredHeight

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//
//
//        when(requestCode){
//            REQUEST_IMAGE_CAPTURE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
//                    takeAPicture()
//                }else{
//                    Toast.makeText(this, "Cannot access camera, permission denied", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }


//    private fun takeAPicture(){
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//       // if(takePictureIntent.resolveActivity(packageManager) != null)
//        //{
//            //try {
//                val photoFile: File = createImageFile()!!
//                val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "au.edu.utas.daih.a2huaizhidai530562",
//                        photoFile
//                )
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//           // }catch (e: Exception){}
//        //}
//    }



//    lateinit var currentPhotoPath: String
//    @Throws(IOException::class)
//    private fun createImageFile(): File{
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        return File.createTempFile(
//                "JPEG_${timeStamp}_",
//                ".jpg",
//                storageDir
//        ).apply {
//            currentPhotoPath = absolutePath
//        }
//    }




//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
//        {
//            setPic(ui.imageView)
//        }
//    }
//
//    private fun setPic(imageView: ImageView){
//        val targetW: Int = imageView.width
//        val targetH: Int = imageView.height
//        val bmOptions = BitmapFactory.Options().apply {
//            inJustDecodeBounds = true
//            BitmapFactory.decodeFile(currentPhotoPath, this)
//            val photoW: Int = outWidth
//            val photoH: Int = outHeight
//            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))
//            inJustDecodeBounds = false
//            inSampleSize = scaleFactor
//        }
//        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also {bitmap ->
//            imageView.setImageBitmap(bitmap)
//        }
//    }




}