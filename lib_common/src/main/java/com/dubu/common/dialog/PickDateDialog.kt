package com.dubu.common.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.utils.DateTool
import com.dubu.common.utils.StatusBarTool
import com.v.wheel.WheelView

/**
 * Author:v
 * Time:2023/4/4
 */
class PickDateDialog : DialogFragment() {
    private val TAG = "PickDateDialog"


    override fun onStart() {
        super.onStart()
        setWindow()
    }

    private fun setWindow() {
        dialog?.apply {
            window?.run {
                attributes = attributes.apply {
                    gravity = Gravity.BOTTOM
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }

                StatusBarTool.setNavigationColor(this, Color.WHITE)
            }
        }
    }


    private var onDateChooseListener: ((action: String) -> Unit)? = null

    override fun getTheme(): Int {
        return R.style.Theme_HiReal_BottomDialog
    }

    fun withDateChooseListener(listener: ((date: String) -> Unit)): PickDateDialog {
        onDateChooseListener = listener
        return this
    }

    fun withInitDate(date: String?): PickDateDialog {
        val list = date?.split("-")
        if (list?.size == 3) {
            selectYear = list[0]
            selectMonth = list[1]
            if (selectMonth.startsWith("0")) {
                selectMonth = selectMonth.substring(1)
            }
            selectDay = list[2]
            if (selectDay.startsWith("0")) {
                selectDay = selectDay.substring(1)
            }
        }
        return this
    }

    fun withYearRang(start: Int, end: Int): PickDateDialog {
        if (start < 0 || end < 0) throw IllegalArgumentException("year cannot be negative!")
        if (start >= end) throw IllegalArgumentException("start year cannot over end year!!")
        this.startYear = start
        this.endYear = end
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_pick_date, container, false)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        root.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            dismissAllowingStateLoss()
        }

        root.findViewById<View>(R.id.tv_confirm).setOnClickListener {


            val fixMonth = if (selectMonth.length == 1){
                "0$selectMonth"
            }else{
                selectMonth
            }

            val fixDay = if (selectDay.length == 1){
                "0$selectDay"
            }else{
                selectDay
            }

            val time = "${selectYear}-${fixMonth}-${fixDay}"

            onDateChooseListener?.invoke(time)
            dismissAllowingStateLoss()
        }
        initWheelView(root)

    }


    private var startYear = 1950
    private var endYear = (DateTool.formatDate(System.currentTimeMillis(),DateTool.DATE_FORMAT_101)).toInt()
    private var selectYear: String = ""
    private var selectMonth: String = ""
    private var selectDay: String = ""
    private val yearList: List<String> by lazy {
        initYearList()
    }

    private val monthList by lazy {
        initMonthList()
    }

    private lateinit var dayList: List<String>

    private lateinit var wvYear: WheelView
    private lateinit var wvMonth: WheelView
    private lateinit var wvDay: WheelView
    private fun initWheelView(root: View) {
        wvYear = root.findViewById<WheelView>(R.id.wv_year)
        wvMonth = root.findViewById<WheelView>(R.id.wv_month)
        wvDay = root.findViewById<WheelView>(R.id.wv_day)

        setupYear(wvYear)
        setupMonth(wvMonth)
        setupDay(wvDay)
    }


    private fun updateDaySelect() {
        dayList = getDayList(selectYear.toInt(), selectMonth.toInt())
        var d2Select = wvDay.currentItem().index()
        wvDay.setupStringItems(dayList)
        if (d2Select + 1 > dayList.size) d2Select = dayList.size - 1
        wvDay.setCurrentSelect(d2Select)
    }


    private fun setupYear(wvYear: WheelView) {
        wvYear.apply {

            var i = yearList.indexOf(selectYear)
            if (i < 0) {
                i = yearList.size / 2
            }
            selectYear = yearList[i]

            setupStringItems(yearList)
            setCurrentSelect(i)
            setOnItemSelectedListener { item ->
                selectYear = item.text()
                updateDaySelect()
            }
        }
    }

    private fun setupMonth(wvMonth: WheelView) {
        wvMonth.apply {
            var i = monthList.indexOf(selectMonth)

            if (i < 0) {
                i = monthList.size / 2
            }
            selectMonth = monthList[i]
            setupStringItems(monthList)
            setCurrentSelect(i)
            setOnItemSelectedListener { item ->
                selectMonth = item.text()
                updateDaySelect()
            }
        }
    }

    private fun setupDay(wvDay: WheelView) {
        wvDay.apply {
            dayList = getDayList(selectYear.toInt(), selectMonth.toInt())

            var i = dayList.indexOf(selectDay)
            if (i < 0) i = dayList.size / 2
            selectDay = dayList[i]

            setupStringItems(dayList)
            setCurrentSelect(i)
            setOnItemSelectedListener { item ->
                selectDay = item.text()
            }
        }
    }

    private fun getDayList(year: Int, month: Int): List<String> {
        val days = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
        val list = ArrayList<String>(days)
        for (d in 1..days) {
            list.add(d.toString())
        }
        return list
    }

    private fun isLeapYear(year: Int): Boolean {
        if (year % 4 == 0 && year % 100 != 0) return true
        return year % 400 == 0
    }

    private fun initYearList(): List<String> {
        val list = ArrayList<String>(endYear - startYear)
        for (y in startYear..endYear) {
            list.add(y.toString())
        }
        return list
    }

    private fun initMonthList(): List<String> {
        return listOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "10", "11", "12"
        )
    }


    fun show(manager: FragmentManager) {
        activity?.let {
            if (it.isFinishing || it.isDestroyed) return
        }

        val f = manager.findFragmentByTag(TAG)
        if (f?.isAdded == true) {
            manager.beginTransaction().show(f)
        } else {
            super.show(manager, TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDateChooseListener = null
    }

}