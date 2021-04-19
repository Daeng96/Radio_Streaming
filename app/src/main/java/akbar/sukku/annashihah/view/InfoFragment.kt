package akbar.sukku.annashihah.view

import akbar.sukku.annashihah.R
import akbar.sukku.annashihah.databinding.FragmentInfoBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<WebView>(R.id.web_view_info)?.loadUrl("file:///android_asset/info_app.html")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).apply {
            this.supportActionBar?.hide()
        }
        toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }

        return binding.root
    }
}