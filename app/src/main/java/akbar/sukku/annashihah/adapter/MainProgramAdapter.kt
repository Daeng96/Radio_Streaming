package akbar.sukku.annashihah.adapter

import akbar.sukku.annashihah.R
import akbar.sukku.annashihah.schedule.MainProgram
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainProgramAdapter internal constructor(context: Context) : RecyclerView.Adapter<MainProgramAdapter.MainViewHolder>() {

    private var mainProgram = emptyList<MainProgram>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_program)
        val valueTimes: TextView = view.findViewById(R.id.time_card_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = inflater.inflate(R.layout.item_main_program, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder) {
            image.setImageDrawable(mainProgram[position].image)
            valueTimes.text = mainProgram[position].timePro
        }
    }

    override fun getItemCount(): Int {
        return mainProgram.size
    }

    internal fun setData(data: ArrayList<MainProgram>) {
        this.mainProgram = data
        notifyDataSetChanged()
    }
}