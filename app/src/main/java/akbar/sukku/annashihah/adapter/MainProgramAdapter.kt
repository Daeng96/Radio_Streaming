package akbar.sukku.annashihah.adapter

import akbar.sukku.annashihah.databinding.ItemMainProgramBinding
import akbar.sukku.annashihah.schedule.MainProgram
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainProgramAdapter : RecyclerView.Adapter<MainProgramAdapter.MainViewHolder>() {

    private var mainProgram = emptyList<MainProgram>()

    class MainViewHolder(binding: ItemMainProgramBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.ivProgram
        val valueTimes: TextView = binding.timeCardId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = ItemMainProgramBinding.inflate(LayoutInflater.from(parent.context))
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