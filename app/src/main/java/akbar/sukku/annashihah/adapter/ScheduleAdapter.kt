package akbar.sukku.annashihah.adapter

import akbar.sukku.annashihah.R
import akbar.sukku.annashihah.databinding.ItemHeaderBinding
import akbar.sukku.annashihah.databinding.ItemScheduleBinding
import akbar.sukku.annashihah.schedule.Schedule
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter internal constructor(
        private val context: Context,
        private val list: List<Any>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HeaderViewHolder(private val mBinding: ItemHeaderBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bindHeader(day: String) {
            mBinding.headerDay = day
        }
    }

    class ScheduleItemHolder(private val  binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvContainer: View = binding.itemContainer
        fun bindSchedule(schedule: Schedule) {
            binding.schedule = schedule
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val headerView = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemView = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            ITEM_HEADER -> HeaderViewHolder(headerView)
            ITEM_SCHEDULE -> ScheduleItemHolder(itemView)
            else -> throw java.lang.IllegalArgumentException("Undefined view Type")
        }
    }


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itm = position % 2
        when (holder.itemViewType) {
            ITEM_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.bindHeader(list[position] as String)
            }
            ITEM_SCHEDULE -> {
                val itemHolder = holder as ScheduleItemHolder
                itemHolder.bindSchedule(list[position] as Schedule)
                if (itm == 1) {
                    itemHolder.tvContainer.background = ColorDrawable(context.getColor(R.color.bg_list_schedule))
                } else {
                    itemHolder.tvContainer.background = ColorDrawable(context.getColor(R.color.transparent))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is String -> ITEM_HEADER
            is Schedule -> ITEM_SCHEDULE
            else -> throw IllegalArgumentException("Undefined view Type")
        }
    }

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_SCHEDULE = 1
    }
}