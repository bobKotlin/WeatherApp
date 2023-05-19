package com.example.testweather.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.testweather.R
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.utils.roundTo5

class ChartTemperatureByHour @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var weatherForDay: WeatherForDay? = null
    private var listCoordsTime: List<Coord?> = listOf(null)

    private var startXView = 0f
    private var endXView = 0f
    private var startYView = 0f
    private var endYView = 0f
    private var heightView = 0f
    private var widthView = 0f

    private var padding = 40f

    private var paintWhiteLine = Paint()
    private var paintGreyLine = Paint()
    private var paintWhiteText = Paint()


    fun init(weatherForDay: WeatherForDay) {
        this.weatherForDay = weatherForDay
        invalidate()

        paintWhiteLine.apply {
            color = Color.WHITE
            strokeWidth = 5f
        }

        paintGreyLine.apply {
            color = context.getColor(R.color.white_transparently)
            strokeWidth = 2f
        }

        paintWhiteText.apply {
            color = Color.WHITE
            textSize = 22f
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        endXView = widthSize.toFloat()
        endYView = heightSize.toFloat()

        widthView = endXView - startXView
        heightView = endYView - startYView
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawVerticalText(canvas)
        drawHorizontalText(canvas)
        drawChart(canvas)
    }

    private fun drawVerticalText(canvas: Canvas) {

        val stepTemperature = 5

        val minMaxTempRoundTo5 = getMinMaxTemperatureRoundTo5(stepTemperature)
        val minTemperature = minMaxTempRoundTo5.first
        val maxTemperature = minMaxTempRoundTo5.second
        var previousYText = endYView - 2 * padding

        val spaceBetweenText =
            heightView / ((maxTemperature - minTemperature) / stepTemperature + 1)

        for ((index, temp) in (minTemperature.toInt()..maxTemperature.toInt() step stepTemperature).withIndex()) {

            val localX = startXView + padding

            val localY =
                if (index == 0) previousYText
                else previousYText - spaceBetweenText


            previousYText = localY

            canvas.drawText("$temp " + context.getText(R.string.celsius), localX, localY, paintWhiteText)

            canvas.drawLine(
                localX,
                localY + 10f,
                endXView - padding,
                localY + 10f,
                paintGreyLine,
            )
        }
    }

    private fun drawHorizontalText(canvas: Canvas) {
        val listStringTime = weatherForDay?.listStringTime ?: listOf()

        var previousXText = startXView + padding

        val spaceBetweenText = widthView / (listStringTime.size + 1)

        val localListCoordsTime = mutableListOf<Coord>()

        listStringTime.forEachIndexed { index, time ->
            val localY = endYView - padding

            val localX = previousXText + spaceBetweenText


            previousXText = localX

            canvas.drawText(time, localX, localY, paintWhiteText)
            localListCoordsTime.add(Coord(localX, localY))
        }
        listCoordsTime = localListCoordsTime

    }

    private fun drawChart(canvas: Canvas) {
        val stepTemperature = 5

        val minMaxTempRoundTo5 = getMinMaxTemperatureRoundTo5(stepTemperature)
        val minTemperature = minMaxTempRoundTo5.first
        val maxTemperature = minMaxTempRoundTo5.second

        val tempBetweenMinMax = maxTemperature - minTemperature

        val listWeatherTemperature = weatherForDay?.listAverageTempInDayCelsius ?: listOf()

        var previousTempY: Float? = null
        listWeatherTemperature.forEachIndexed { index, temp ->
            var percentTemp = temp / tempBetweenMinMax

            val localTempY = (startYView + padding) * percentTemp

            15 - 100
            13 - x
            if (previousTempY != null) {
             canvas.drawLine(
                    listCoordsTime[index]?.x ?: 0f,
                    localTempY,
                    listCoordsTime[index - 1]?.x ?: 0f,
                    previousTempY!!,
                    paintWhiteLine
                )
            }
            previousTempY = localTempY
        }


    }

    private fun getMinMaxTemperatureRoundTo5(stepTemperature: Int): Pair<Float, Float> {

        val listWeatherTemperature = weatherForDay?.listAverageTempInDayCelsius ?: listOf()
        val minWeatherTemperature = listWeatherTemperature.maxOrNull() ?: 0f
        val maxWeatherTemperature = listWeatherTemperature.minOrNull() ?: 0f


        val maxTemperature = maxWeatherTemperature.roundTo5().toFloat() + stepTemperature

        val minTemperature =
            if (minWeatherTemperature >= 0f) 0f
            else minWeatherTemperature.roundTo5().toFloat() - stepTemperature

        return Pair(minTemperature, maxTemperature)
    }

    private data class Coord(
        val x: Float,
        val y: Float,
    )
}