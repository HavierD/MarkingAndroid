package au.edu.utas.daih.a2huaizhidai530562

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityHomeBinding
import au.edu.utas.daih.a2huaizhidai530562.databinding.ActivityMainBinding
import au.edu.utas.daih.a2huaizhidai530562.databinding.WeekListBinding
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

val weeks = listOf<String>( "Week 2", "Week 3","Week 4", "Week 5", "Week 6", "Week 7", "Week 8", "Week 9", "Week 10", "Week 11", "Week 12" )

const val  WEEK_KEY: String = "week0"


class HomeActivity : AppCompatActivity() {

    private lateinit var ui : ActivityHomeBinding

//     val newUser = intent.getBooleanExtra(NEWUSER_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


/*
        if (newUser == true ){
            //introductions part
        }
        else {*/

             ui = ActivityHomeBinding.inflate(layoutInflater)
             setContentView(ui.root)



       //}




    ui.weekList.adapter = weekListAdapter(week = weeks)
        ui.weekList.layoutManager = LinearLayoutManager(this)

        ui.HomeHelp.setOnClickListener {
            var a = getString(R.string.homeHelp1) + "\n\n" + getString(R.string.homeHelp2)
            MaterialAlertDialogBuilder(it.context)
                    .setTitle(R.string.HELP)
                    .setMessage(a)
                    .setCancelable(true)
                    .show()
        }





    } /////////////////////////////on create eeeeeeeeeeeend

    // turn to weekly page

     fun turnToWeekly(w: String){
       val i = Intent(this, WeekActivity::class.java)
        i.putExtra(WEEK_KEY, w)
      startActivity(i)
    }


    inner class weekListHolder(var ui: WeekListBinding) : RecyclerView.ViewHolder(ui.root){}
    inner class weekListAdapter(private val week: List<String>) : RecyclerView.Adapter<weekListHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): weekListHolder {
            val ui = WeekListBinding.inflate(layoutInflater, parent,false)
            return weekListHolder(ui)
        }

        override fun onBindViewHolder(holder: weekListHolder, position: Int) {
            val wk = week[position]
            holder.ui.weekListRow.text = wk
            holder.itemView.setOnClickListener {
                turnToWeekly(wk)
            }
        }

        override fun getItemCount(): Int {
           return week.size
        }
    }


}