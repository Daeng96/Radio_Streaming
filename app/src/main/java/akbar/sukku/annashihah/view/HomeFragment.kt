package akbar.sukku.annashihah.view

import akbar.sukku.annashihah.R
import akbar.sukku.annashihah.databinding.FragmentHomeBinding
import akbar.sukku.annashihah.media.RadioManager
import akbar.sukku.annashihah.utils.AdjustTimes
import akbar.sukku.annashihah.utils.Constant
import akbar.sukku.annashihah.utils.CoordinateData
import akbar.sukku.annashihah.utils.CoordinatePreference
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.IslamicCalendar
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.daeng96.radioan_nashihah.player.PlaybackStatus
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
class HomeFragment : Fragment() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var radioManager: RadioManager
    private lateinit var floatingActionButton: ExtendedFloatingActionButton
    private lateinit var floatingActionButtonChanel2: ExtendedFloatingActionButton
    private var islamicCalendar: IslamicCalendar? = null
    private var typeIslamicCalendar: IslamicCalendar.CalculationType? = null
    private lateinit var hijryDate: TextView
    private lateinit var myType: String
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var rotateAnimation: Animation
    private lateinit var parameters: CalculationParameters
    private lateinit var nameMethod: String
    private lateinit var adjustTime: AdjustTimes
    private lateinit var coordinatePreference: CoordinatePreference
    private var clickChanel: Int = 0
    private var location : Location ? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coordinatePreference = CoordinatePreference(requireContext())
        if (coordinatePreference.getCoordinate().latitude == 0.0) {
            coordinatePreference.setCoordinate(CoordinateData(-5.147665, 119.432732))
        }
        (activity as AppCompatActivity).apply {
            this.supportActionBar?.hide()
        }
        radioManager = RadioManager(requireContext())
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainPlay()
        toolBarAction()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        toolbar = binding.mainToolbar
        initPreference()
        init()
        calendarHijri()
        rotateAnimation =
            android.view.animation.AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.rotate_anim
            )
        return binding.root
    }

    private fun setAnim() {
        binding.logoApp.animation = rotateAnimation
    }

    private fun initPreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        PreferenceManager.setDefaultValues(requireContext(), R.xml.header_preferences, true)
        myType =
            sharedPreferences.getString(getString(R.string.type_key), "islamic-civil") as String
        nameMethod = sharedPreferences.getString(
            getString(R.string.method_key),
            getString(R.string.default_method_key)
        ) as String
        parameters = setMethod(nameMethod).parameters
        adjustTime = AdjustTimes(requireContext(), sharedPreferences, parameters)

    }


    @SuppressLint("SetTextI18n")
    private fun calendarHijri() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            islamicCalendar = IslamicCalendar(Date())
            typeIslamicCalendar = setTypeIslamicCalendar(myType)
            islamicCalendar!!.calculationType = typeIslamicCalendar
            val dDate = islamicCalendar!![5]
            val mDate = setNameMonth(islamicCalendar!![2] + 1)
            val yDate = islamicCalendar!![1]
            hijryDate.text = getString(R.string.calendar_hijri, dDate, mDate, yDate)
        } else {
            hijryDate.text = "Tdk dpt menampilkan Tgl Hijriyah"
        }
    }

    private fun toolBarAction() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    findNavController().navigate(R.id.settingsActivity)
                    true
                }
                R.id.location_menu_id -> {
                    if (!checkPermissions()) {
                        requestPermissions()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setMessage("Apakah Anda ingin memperbarui lokasi? (Koneksi jaringan mungkin diperlukan)")
                            .setTitle("Perbarui Lokasi")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_location)
                            .setNegativeButton("Batalkan", null)
                            .setPositiveButton("Perbarui") { _, _ -> updateLoc() }.show()

                    }
                    true
                }
                R.id.info_menu_id -> {
                    findNavController().navigate(R.id.action_homeFragment_to_infoFragment)
                    true
                }
                R.id.schedule__menu_id -> {
                    findNavController().navigate(R.id.action_homeFragment_to_scheduleFragment)
                    true
                }
                else -> false
            }
        }
    }


    private fun init() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )
        floatingActionButton = binding.floating
        floatingActionButtonChanel2 = binding.floatingChanel2
        hijryDate = binding.dateHijry

    }


    private fun mainPlay() {
        floatingActionButton.setOnClickListener {
            radioManager.playOrPause("http://radio.an-nashihah.com/live")
            clickChanel = 1
        }
        floatingActionButtonChanel2.setOnClickListener {
            radioManager.playOrPause("http://radio.an-nashihah.com/live2")
            clickChanel = 2
        }
    }

    @SuppressLint("MissingPermission")
    private fun getAddress() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location = it
            if (location != null) {
                coordinatePreference.setCoordinate(CoordinateData(location!!.latitude, location!!.longitude))
            }
            val geocode = Geocoder(requireContext(), Locale.getDefault())
            var address: List<Address> = emptyList()
            try {
                if (location != null) address = geocode.getFromLocation(location!!.latitude, location!!.longitude, 1)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
            }

            if (address.isNotEmpty()) {
                val loc = address[0]
                coordinatePreference.setAddressName(loc.locality + " " + loc.subAdminArea)
                binding.locationId.text = coordinatePreference.getAddressName()
                Log.i("Check Lok", loc.locality)
            }
        }


    }

    @SuppressLint("MissingPermission")
    private fun updateLoc(){
        val geocodeL = Geocoder(requireContext(), Locale.getDefault())
        var addressL: List<Address> = emptyList()
        val locRec = LocationRequest.create().apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this.isWaitForAccurateLocation = true
            this.maxWaitTime = 5000
        }
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                locRec ?: return
                for (location in locationResult.locations){
                    if (location != null) addressL = geocodeL.getFromLocation(location.latitude, location.longitude, 1)
                    coordinatePreference.setCoordinate(CoordinateData(location!!.latitude, location.longitude))
                    val loc = addressL[0]
                    coordinatePreference.setAddressName(loc.locality + " " + loc.subAdminArea)
                    binding.locationId.text = coordinatePreference.getAddressName()
                }
                super.onLocationResult(locationResult)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locRec, locationCallback, Looper.getMainLooper())
    }



    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i("MainActivity", "Displaying permission rationale to provide additional context.")
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(android.R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        34
                    )
                }
                .show()

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                34
            )
        }

    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode != 34) return
        when {
            grantResults.isEmpty() ->
                Log.i("Main", "User interaction was cancelled.")
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> getAddress()

            else -> Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(android.R.string.ok) {
                    val intent = Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts(
                            "package",
                            "akbar.sukku.annashihah",
                            null
                        )
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                }
                .show()
        }

    }


    private fun initPrayerTimes(mCoordinate: CoordinateData) {

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeZoneId = TimeZone.getDefault()
        formatter.timeZone = TimeZone.getTimeZone(timeZoneId.id)
        val dateComponent = DateComponents.from(Date())
        val coordinate = Coordinates(mCoordinate.latitude, mCoordinate.longitude)
        adjustTime.getAdjustTimes()
        val prayerTimes = PrayerTimes(coordinate, dateComponent, parameters)
        binding.fajrId.text = getString(R.string.label_fajr, formatter.format(prayerTimes.fajr))
        binding.sunriseId.text =
            getString(R.string.label_sunrise, formatter.format(prayerTimes.sunrise))
        binding.dzuhrId.text = getString(R.string.label_dzuhr, formatter.format(prayerTimes.dhuhr))
        binding.asarId.text = getString(R.string.label_asr, formatter.format(prayerTimes.asr))
        binding.maghribId.text =
            getString(R.string.label_maghrib, formatter.format(prayerTimes.maghrib))
        binding.isyaId.text = getString(R.string.label_isya, formatter.format(prayerTimes.isha))
        binding.locationId.text = coordinatePreference.getAddressName()
    }

    private fun setMethod(key: String?): CalculationMethod {
        return when (key) {
            Constant.EGYPT -> CalculationMethod.EGYPTIAN
            Constant.UMM_QURO -> CalculationMethod.UMM_AL_QURA
            Constant.MUSLIM_LEAGUE -> CalculationMethod.MUSLIM_WORLD_LEAGUE
            Constant.QATAR -> CalculationMethod.QATAR
            Constant.KARACHI -> CalculationMethod.KARACHI
            Constant.NORTH_AMERICA -> CalculationMethod.NORTH_AMERICA
            Constant.SINGAPURA -> CalculationMethod.SINGAPORE
            Constant.KUWAIT -> CalculationMethod.KUWAIT
            Constant.DUBAI -> CalculationMethod.DUBAI
            Constant.MSC -> CalculationMethod.MOON_SIGHTING_COMMITTEE
            Constant.INDONESIA -> CalculationMethod.SINGAPORE
            else -> CalculationMethod.OTHER
        }
    }


    private fun setNameMonth(index: Int): String {
        val monthName = HashMap<Int, String>()
        monthName[1] = "Muharram"
        monthName[2] = "Safar"
        monthName[3] = "Rabī‘ al-awwal"
        monthName[4] = "Rabī‘ ath-thānī"
        monthName[5] = "Jumādá al-ūlá"
        monthName[6] = "Jumādá al-ākhirah"
        monthName[7] = "Rajab"
        monthName[8] = "Sha‘bān"
        monthName[9] = "Ramaḍān"
        monthName[10] = "Shawwāl"
        monthName[11] = "Dhū al-Qa‘dah"
        monthName[12] = "Dhū al-Ḥijjah"
        val getMonth = monthName[index]
        return getMonth.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setTypeIslamicCalendar(type: String): IslamicCalendar.CalculationType {
        return when (type) {
            "islamic-civil" -> IslamicCalendar.CalculationType.ISLAMIC_CIVIL
            "islamic-umalqura" -> IslamicCalendar.CalculationType.ISLAMIC_UMALQURA
            "islamic" -> IslamicCalendar.CalculationType.ISLAMIC
            else -> IslamicCalendar.CalculationType.ISLAMIC_TBLA
        }
    }

    @Subscribe
    fun onEvent(status: String) {
        when (status) {
            PlaybackStatus.LOADING -> {

            }
            PlaybackStatus.ERROR -> Toast.makeText(requireContext(), "Offline", Toast.LENGTH_LONG)
                .show()
        }

        if (status == PlaybackStatus.PLAYING) {
            setAnim()
            rotateAnimation.start()
        } else {
            rotateAnimation.cancel()
            binding.logoApp.clearAnimation()
        }

        floatingActionButton.icon =
            (if (status == PlaybackStatus.PLAYING && clickChanel == 1) ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pause
            ) else ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))

        floatingActionButtonChanel2.icon =
            (if (status == PlaybackStatus.PLAYING && clickChanel == 2) ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pause
            ) else ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
    }


    override fun onDetach() {
        super.onDetach()
        radioManager.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        radioManager.bind()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        initPreference()
        calendarHijri()
        initPrayerTimes(coordinatePreference.getCoordinate())
        radioManager.bind()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}