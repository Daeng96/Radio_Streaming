package akbar.sukku.annashihah.view

import akbar.sukku.annashihah.R
import akbar.sukku.annashihah.adapter.MainProgramAdapter
import akbar.sukku.annashihah.adapter.ScheduleAdapter
import akbar.sukku.annashihah.databinding.FragmentScheduleBinding
import akbar.sukku.annashihah.schedule.MainProgram
import akbar.sukku.annashihah.schedule.Schedule
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {
    private var listSchedule = listOf<Any>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter
    private lateinit var adapterPro: MainProgramAdapter
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var rvMainPro: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listSchedule = schedule
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.scheduleToolbar.setNavigationOnClickListener { view -> view.findNavController().navigateUp() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated( view, savedInstanceState)
        init()
        prepare()
        prepareDataMainProgram()
    }

    private fun init() {
        adapter = ScheduleAdapter(requireContext() ,listSchedule)
        adapterPro = MainProgramAdapter()
        recyclerView = binding.rvSchedule
        rvMainPro = binding.mainProgram
    }

    private fun prepare() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun prepareDataMainProgram() {
        rvMainPro.setHasFixedSize(true)
        rvMainPro.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvMainPro.adapter = adapterPro
        adapterPro.setData(listPro())
    }

    private fun listPro(): ArrayList<MainProgram> {
        val listPro = ArrayList<MainProgram>()
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_0
        )!!, "20:45"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_1
        )!!, "07:00"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_2
        )!!, "16:30"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_3
        )!!, "10:30"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_4
        )!!, "20:45"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_5
        )!!, "07:30"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_6
        )!!, "05:30"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_7
        )!!, "19:30"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_8
        )!!, "16:00"))
        listPro.add(MainProgram(ContextCompat.getDrawable(requireContext(),
                R.drawable.poster_program_9
        )!!, "16:00"))
        return listPro
    }

    companion object {
        val schedule: List<Any>
            get() = mutableListOf(
                    "Senin",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Mengenal Rasulullah", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Murottal dan Jeda Pilihan", "07:00 - 07:30"),
                    Schedule("Adab - adab Seorang Muslim", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Tafsir Al Qur'an", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Fiqih Asmaul Husna", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Murottal dan Jeda Pilihan", "12:00 - 13:00"),
                    Schedule("Siaran Ulang konsultasi agama", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Fiqih Madzhab Syafi'i", "16:00 - 16:30"),
                    Schedule("Murottal dan Jeda Pilihan", "16:30 - 17:00"),
                    Schedule("Murottal dan Jeda Pilihan", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Kajian Umum", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("Kajian Pilihan", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),
                    "Selasa",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Mengenal Rasulullah", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Murottal dan Jeda Pilihan", "07:00 - 07:30"),
                    Schedule("Adab - adab Seorang Muslim", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Tafsir Al Qur'an", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Fiqih Asmaul Husna", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Murottal dan Jeda Pilihan", "12:00 - 13:00"),
                    Schedule("Bulughul Maram", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Fiqih Madzhab Syafi'i", "16:00 - 16:30"),
                    Schedule("Murottal dan Jeda Pilihan", "16:30 - 17:00"),
                    Schedule("Murottal dan Jeda Pilihan", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Mengenal Jalan Menuju Allah", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("(LIVE) Bincang Inspirasi Dan Bisnis", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),
                    "Rabu",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Taman Orang-Orang Shalih", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Murottal dan Jeda Pilihan", "07:00 - 07:30"),
                    Schedule("Kajian Pilihan", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Tuntunan Fiqih Praktis", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Sipakatau Sipakainga'", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Murottal dan Jeda Pilihan", "12:00 - 13:00"),
                    Schedule("Siaran Ulan Bincang Inspirasi Bisnis", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Motivasi dan adab Menuntut Ilmu", "16:00 - 16:30"),
                    Schedule("Murottal dan Jeda Pilihan", "16:30 - 17:00"),
                    Schedule("Murottal dan Jeda Pilihan", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Kajian Umum", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("Kajian Pilihan", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),

                    "Kamis",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Taman Orang-Orang Shalih", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Murottal dan Jeda Pilihan", "07:00 - 07:30"),
                    Schedule("Pokok-pokok Keimanan", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Tuntunan Fiqih Praktis", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Sipakatau Sipakainga'", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Murottal dan Jeda Pilihan", "12:00 - 13:00"),
                    Schedule("Tafsir Surah Al-Baqarah", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Syair-syair Manhaj Ahlusunnah", "16:00 - 16:30"),
                    Schedule("Syair-syair Manhaj Ahlusunnah", "16:30 - 17:00"),
                    Schedule("Murottal dan Jeda Pilihan", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Budi Pekerti Teladan Ummat", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("(LIVE) Konsultasi Agama", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),

                    "Jum'at",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Taman Orang-Orang Shalih", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Murottal dan Jeda Pilihan", "07:00 - 07:30"),
                    Schedule("Khutbah Jum'at Pilihan", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Tuntunan Fiqih Praktis", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Sipakatau Sipakainga'", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Khutbah Jum'at", "12:00 - 13:00"),
                    Schedule("Siaran Ulang Konsultasi Agama", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Syair-syair Manhaj Ahlusunnah", "16:00 - 16:30"),
                    Schedule("Belajar Ngaji (Tajwid)", "16:30 - 17:00"),
                    Schedule("Belajar Ngaji (Tajwid)", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Untaian Mutiara dari Perjalanan Hidup Rasul Terakhir", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("(LIVE) Ushul Fi Tafsir", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),

                    "Sabtu",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Taman Orang-Orang Shalih", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Murottal dan Jeda Pilihan", "07:00 - 07:30"),
                    Schedule("Pohon Iman", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Tuntunan Fiqih Praktis", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Sipakatau Sipakainga'", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Khutbah Jum'at", "12:00 - 13:00"),
                    Schedule("Siaran Ulang Konsultasi Agama", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Syair-syair Manhaj Ahlusunnah", "16:00 - 16:30"),
                    Schedule("Belajar Ngaji (Tajwid)", "16:30 - 17:00"),
                    Schedule("Belajar Ngaji (Tajwid)", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Mengenal Jalan Menuju Allah", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("(LIVE) Ensiklopedia Fiqih dari Al-Qur'an", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),

                    "Ahad",
                    Schedule("Murottal dan Jeda Pilihan", "05:00 - 05:30"),
                    Schedule("Taman Orang-Orang Shalih", "05:30 - 06:30"),
                    Schedule("Murottal dan Jeda Pilihan", "06:30 - 07:00"),
                    Schedule("Sunnah Itu Indah", "07:00 - 07:30"),
                    Schedule("Sunnah Itu Indah", "07:30 - 08:00"),
                    Schedule("Murottal dan Jeda Pilihan", "08:00 - 09:00"),
                    Schedule("Dosa-Dosa Besar", "09:00 - 09:30"),
                    Schedule("Murottal dan Jeda Pilihan", "09:30 - 10:30"),
                    Schedule("Sipakatau Sipakainga'", "10:30 - 11:00"),
                    Schedule("Murottal dan Jeda Pilihan", "11:00 - 12:00"),
                    Schedule("Khutbah Jum'at", "12:00 - 13:00"),
                    Schedule("Siaran Ulang Konsultasi Agama", "13:00 - 14:00"),
                    Schedule("Murottal dan Jeda Pilihan", "14:00 - 16:00"),
                    Schedule("Bercermin pada Diri", "16:00 - 16:30"),
                    Schedule("(Reply) Belajar Ngaji (Tahsin)", "16:30 - 17:00"),
                    Schedule("(Reply) Belajar Ngaji (Tahsin)", "17:00 - 17:30"),
                    Schedule("Murottal dan Jeda Pilihan", "17:30 - 18:00"),
                    Schedule("Murottal dan Jeda Pilihan", "18:00 - 19:00"),
                    Schedule("Murnikan Penghambaanmu", "19:00 - 20:00"),
                    Schedule("Murottal dan Jeda Pilihan", "20:00 - 20:45"),
                    Schedule("(LIVE) Konsultasi Agama", "20:45 - 22:15"),
                    Schedule("Memurnikan Ibadah Kepada Allah", "22:15 - 23:15"),
                    Schedule("Murottal dan Jeda Pilihan", "23:15 - 00:00"),
            )
    }

}