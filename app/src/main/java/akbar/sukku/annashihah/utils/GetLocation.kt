package akbar.sukku.annashihah.utils

class GetLocation {
//    private inner class AddressResultReceiver(
//        handler: Handler
//    ) : ResultReceiver(handler) {
//
//        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
//
//            addressOutput = resultData.getString(Constant.RESULT_DATA_KEY).toString()
//            //displayAddressOutput()
//            val coordinateData = resultData.getDoubleArray(Constant.RESULT_DATA_KEY_COOR)
//            coordinatePreference.setCoordinate(
//                CoordinatePreference.CoordinateData(
//                    coordinateData!![0], coordinateData[1]
//                )
//            )
//            if (resultCode == Constant.SUCCESS_RESULT) {
//                Toast.makeText(this@MainActivity, addressOutput, Toast.LENGTH_SHORT).show()
//            } else if (resultCode == Constant.SUCCESS_RESULT_COORDINATE) {
//
//            }
//            addressRequested = false
//            initPrayerTimes(coordinatePreference.getCoordinate())
//        }
//    }
//
//    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
//        savedInstanceState ?: return
//
//        ADDRESS_REQUESTED_KEY.let {
//            // Check savedInstanceState to see if the address was previously requested.
//            if (savedInstanceState.keySet().contains(it)) {
//                addressRequested = savedInstanceState.getBoolean(it)
//            }
//        }
//
//        LOCATION_ADDRESS_KEY.let {
//            if (savedInstanceState.keySet().contains(it)) {
//                addressOutput = savedInstanceState.getString(it).toString()
//                //displayAddressOutput()
//                Log.i("TAG", addressOutput)
//            }
//        }
//    }
//
//    private fun startIntentService() {
//        val intent = Intent(this, LocationIntentService::class.java).apply {
//            putExtra(Constant.RECEIVER, resultReceiver)
//            putExtra(Constant.LOCATION_DATA_EXTRA, lastLocation)
//        }
//        LocationIntentService.enqueueWork(this@MainActivity, intent)
//    }
//
//    private fun checkPermissions(): Boolean {
//        val permissionState = ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        return permissionState == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestPermissions() {
//        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.")
//
//            showSnackBar(
//                R.string.permission_rationale, android.R.string.ok
//            ) {
//                // Request permission
//                ActivityCompat.requestPermissions(
//                    this@MainActivity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_PERMISSIONS_REQUEST_CODE
//                )
//            }
//
//        } else {
//            Log.i(TAG, "Requesting permission")
//            ActivityCompat.requestPermissions(
//                this@MainActivity,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private fun getAddress() {
//        fusedLocationProviderClient.lastLocation.addOnSuccessListener(
//            this,
//            OnSuccessListener { location ->
//                if (location == null) {
//                    Log.w(TAG, "onSuccess:null")
//                    return@OnSuccessListener
//                }
//
//                lastLocation = location
//
//                // Determine whether a Geocoder is available.
//                if (!Geocoder.isPresent()) {
//                    Snackbar.make(
//                        findViewById(R.id.content),
//                        R.string.no_geocoder_available, Snackbar.LENGTH_LONG
//                    ).show()
//                    return@OnSuccessListener
//                }
//                if (addressRequested) startIntentService()
//            }).addOnFailureListener(this) { e -> Log.w(TAG, "getLastLocation:onFailure", e) }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        Log.i(TAG, "onRequestPermissionResult")
//
//        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return
//
//        when {
//            grantResults.isEmpty() ->
//                Log.i(TAG, "User interaction was cancelled.")
//            grantResults[0] == PackageManager.PERMISSION_GRANTED -> getAddress()
//            else ->
//                showSnackBar(
//                    R.string.permission_denied_explanation, R.string.settings
//                ) {
//                    // Build intent that displays the App settings screen.
//                    val intent = Intent().apply {
//                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        data = Uri.fromParts(
//                            "package",
//                            "com.google.android.gms.location.sample.locationaddress",
//                            null
//                        )
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    }
//                    startActivity(intent)
//                }
//        }
//    }
//
//    private fun showSnackBar(
//        mainTextStringId: Int,
//        actionStringId: Int,
//        listener: View.OnClickListener
//    ) {
//        Snackbar.make(
//            findViewById(android.R.id.content), getString(mainTextStringId),
//            Snackbar.LENGTH_INDEFINITE
//        )
//            .setAction(getString(actionStringId), listener)
//            .show()
//    }
//
//    companion object {
//        private val TAG = MainActivity::class.java.simpleName
//        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
//        private const val ADDRESS_REQUESTED_KEY = "address-request-pending"
//        private const val LOCATION_ADDRESS_KEY = "location-address"
//    }
}