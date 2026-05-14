package com.varsha.grameenlight.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.varsha.grameenlight.R
import com.varsha.grameenlight.ui.report.ReportBottomSheet
import com.varsha.grameenlight.ui.viewmodel.PoleViewModel

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val viewModel: PoleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_map, container, false
    )

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(20.5937, 78.9629), 16f
            )
        )

        viewModel.poles.observe(viewLifecycleOwner) { poles ->
            map.clear()
            poles.forEach { pole ->
                val hue = when (pole.status) {
                    "WORKING" -> BitmapDescriptorFactory.HUE_GREEN
                    "FUSED"   -> BitmapDescriptorFactory.HUE_RED
                    "DAY_ON"  -> BitmapDescriptorFactory.HUE_YELLOW
                    else      -> BitmapDescriptorFactory.HUE_AZURE
                }
                val statusText = when (pole.status) {
                    "WORKING" -> "Working Fine"
                    "FUSED"   -> "Bulb Fused"
                    "DAY_ON"  -> "Burning in Daytime"
                    else      -> pole.status
                }
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(pole.latitude, pole.longitude)
                        )
                        .title(pole.poleId)
                        .snippet(
                            "Status: $statusText" +
                                    "\nTap info window to report"
                        )
                        .icon(
                            BitmapDescriptorFactory
                                .defaultMarker(hue)
                        )
                )
                marker?.tag = pole.poleId
            }
        }

        map.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            true
        }

        map.setOnInfoWindowClickListener { marker ->
            val poleId = marker.tag as? String
                ?: return@setOnInfoWindowClickListener
            ReportBottomSheet
                .newInstance(poleId)
                .show(childFragmentManager, "report")
        }
    }
}