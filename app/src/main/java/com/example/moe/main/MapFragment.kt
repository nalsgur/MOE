package com.example.moe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moe.MainAPI.RetrofitClient
import com.example.moe.MainAPI.SharedViewModel
import com.example.moe.databinding.FragmentMapBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment() {
    lateinit var binding: FragmentMapBinding
    private lateinit var subSeoulBtns: List<Button>
    private lateinit var subBusanBtns: List<Button>
    private val seoulBtns = mutableListOf<Button>()
    private val busanBtns = mutableListOf<Button>()
    private val buttonSave = mutableMapOf<String, MutableList<String>>()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.homeSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.mapLeftBtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }

        binding.mapGeoBtn1.setOnClickListener {
            binding.mapGeoBtn1.isSelected = true
            binding.mapGeoBtn2.isSelected = false
            updateVisibility()
        }

        binding.mapGeoBtn2.setOnClickListener {
            binding.mapGeoBtn2.isSelected = true
            binding.mapGeoBtn1.isSelected = false
            updateVisibility()
        }

        binding.mapCheckBtn.setOnClickListener {
            showLoading()
            saveFilters()
        }

        setUpSubSeoulBtn()
        setUpSubBusanBtn()

        return binding.root
    }

    private fun updateVisibility() {
        if(binding.mapGeoBtn1.isSelected == true) {
            binding.mapBtnSeoulContainer.visibility = View.VISIBLE
            binding.scrollSeoul.visibility = View.VISIBLE
            binding.scrollBusan.visibility = View.INVISIBLE
            binding.mapHeadTx.visibility = View.GONE
            binding.mapBtnBusanContainer.visibility = View.GONE
        }
        else {
            binding.scrollBusan.visibility = View.VISIBLE
            binding.scrollSeoul.visibility = View.INVISIBLE
            binding.mapHeadTx.visibility = View.GONE
            binding.mapBtnBusanContainer.visibility = View.VISIBLE
            binding.mapBtnSeoulContainer.visibility = View.GONE
        }
    }

    private fun setUpSubSeoulBtn() {
        subSeoulBtns = listOf(
            binding.mapSubGeoBtn1, binding.mapSubGeoBtn2, binding.mapSubGeoBtn3, binding.mapSubGeoBtn4,
            binding.mapSubGeoBtn5, binding.mapSubGeoBtn6, binding.mapSubGeoBtn7, binding.mapSubGeoBtn8,
            binding.mapSubGeoBtn9, binding.mapSubGeoBtn10, binding.mapSubGeoBtn11, binding.mapSubGeoBtn12,
            binding.mapSubGeoBtn13, binding.mapSubGeoBtn14, binding.mapSubGeoBtn15, binding.mapSubGeoBtn16,
            binding.mapSubGeoBtn17, binding.mapSubGeoBtn18, binding.mapSubGeoBtn19, binding.mapSubGeoBtn20,
            binding.mapSubGeoBtn21, binding.mapSubGeoBtn22, binding.mapSubGeoBtn23, binding.mapSubGeoBtn24,
            binding.mapSubGeoBtn25
        )

        subSeoulBtns.forEach { button ->
            button.setOnClickListener {
                val text = button.text.toString()
                button.isSelected = !button.isSelected
                if (button.isSelected) {
                    addButtonToSave("서울", text)
                    createNewButton(button.text.toString(), true)
                } else {
                    removeButtonFromSave("서울", text)
                    removeBtn(button.text.toString(), true)
                }
                updateMapCheckBtn()
            }
        }
    }

    private fun setUpSubBusanBtn() {
        subBusanBtns = listOf(
            binding.mapSub2GeoBtn1, binding.mapSub2GeoBtn2, binding.mapSub2GeoBtn3, binding.mapSub2GeoBtn4,
            binding.mapSub2GeoBtn5, binding.mapSub2GeoBtn6, binding.mapSub2GeoBtn7, binding.mapSub2GeoBtn8,
            binding.mapSub2GeoBtn9, binding.mapSub2GeoBtn10, binding.mapSub2GeoBtn11, binding.mapSub2GeoBtn12,
            binding.mapSub2GeoBtn13, binding.mapSub2GeoBtn14, binding.mapSub2GeoBtn15
        )

        subBusanBtns.forEach { button ->
            button.setOnClickListener {
                val text = button.text.toString()
                button.isSelected = !button.isSelected
                if (button.isSelected) {
                    addButtonToSave("부산", text)
                    createNewButton(button.text.toString(), false)
                } else {
                    removeButtonFromSave("부산", text)
                    removeBtn(button.text.toString(), false)
                }
                updateMapCheckBtn()
            }
        }
    }

    private fun addButtonToSave(region: String, text: String) {
        val district = "${region}_$text"
        buttonSave.computeIfAbsent(region) { mutableListOf() }.add(district)
        logButtonSaveState()
        sharedViewModel.updateFilters(region, buttonSave[region] ?: emptyList())
    }

    private fun removeButtonFromSave(region: String, text: String) {
        val district = "${region}_$text"
        buttonSave[region]?.remove(district)
        if (buttonSave[region]?.isEmpty() == true) {
            buttonSave.remove(region)
        }
        logButtonSaveState()
        sharedViewModel.updateFilters(region, buttonSave[region] ?: emptyList())
    }

    private fun logButtonSaveState() {
        Log.d("MapFragment", "Current buttonSave state: $buttonSave")
    }

    private fun updateMapCheckBtn() {
        if (!seoulBtns.isEmpty() || !busanBtns.isEmpty()) {
            binding.mapCheckBtn.isEnabled = true
            binding.mapCheckBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.main_map_check_btn_click, null)
            binding.mapCheckBtn.setTextColor(Color.WHITE)
        } else {
            binding.mapCheckBtn.isEnabled = false
            binding.mapCheckBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.main_map_check_btn_default, null)
            binding.mapCheckBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_300))
        }
    }

    private fun createNewButton(text: String, isSelected: Boolean) {
        val newBtn = Button(requireContext(), null, 0, R.style.CustomButtonStyle).apply {
            this.text = text
            textSize = 14f
            setTextColor(Color.WHITE)
            typeface = ResourcesCompat.getFont(context, R.font.freesentation_6semibold)
            background = ResourcesCompat.getDrawable(resources, R.drawable.main_map_sub_geo_btn_shape, null)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20,0,0,0)
                setPadding(30,12,30,12)
            }

            this.layoutParams = layoutParams
        }


        if (isSelected) {
            binding.mapBtnSeoulContainer.addView(newBtn)
            seoulBtns.add(newBtn)
        }
        else {
            binding.mapBtnBusanContainer.addView(newBtn)
            busanBtns.add(newBtn)
        }
    }

    private fun removeBtn(text: String, isSelected: Boolean) {
        val container = if (isSelected) binding.mapBtnSeoulContainer else binding.mapBtnBusanContainer
        val btnList = if (isSelected) seoulBtns else busanBtns

        val buttonToRemove = btnList.find { it.text.toString() == text }
        buttonToRemove?.let {
            container.removeView(it)
            btnList.remove(it)
        }
    }

    private fun saveFilters() {
        val seoulDistricts = buttonSave["서울"] ?: emptyList()
        val busanDistricts = buttonSave["부산"] ?: emptyList()
        sharedViewModel.setFilters("서울", seoulDistricts)
        sharedViewModel.setFilters("부산", busanDistricts)
    }

    private fun showLoading() {

        seoulBtns.forEach { button ->
            button.isSelected = false
        }
        busanBtns.forEach { button ->
            button.isSelected = false
        }

        binding.mapGeoBtn1.isSelected = false
        binding.mapGeoBtn2.isSelected = false
        subSeoulBtns.forEach { it.isSelected = false }
        subBusanBtns.forEach { it.isSelected = false }

        binding.mapCheckBtn.isEnabled = false
        binding.mapCheckBtn.background = ResourcesCompat.getDrawable(resources, R.drawable.main_map_check_btn_default, null)
        binding.mapCheckBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_300))

        val loadingContainer: FrameLayout = binding.loadingContainer
        loadingContainer.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            loadingContainer.visibility = View.GONE
            Toast.makeText(requireContext(), "적용되었습니다", Toast.LENGTH_SHORT).show()
            binding.mapBtnSeoulContainer.removeViews(1, binding.mapBtnSeoulContainer.childCount - 1)
            binding.mapBtnBusanContainer.removeViews(1, binding.mapBtnBusanContainer.childCount - 1)
            binding.mapHeadTx.visibility = View.VISIBLE
            binding.mapBtnSeoulContainer.visibility = View.INVISIBLE
            binding.mapBtnBusanContainer.visibility = View.INVISIBLE
            binding.scrollSeoul.visibility = View.INVISIBLE
            binding.scrollBusan.visibility = View.INVISIBLE
        }, 4000)
    }

}