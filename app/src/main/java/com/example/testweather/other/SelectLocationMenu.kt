package com.example.testweather.other

import android.content.Context
import android.view.Menu
import android.widget.PopupMenu
import android.widget.TextView
import com.example.testweather.R
import com.example.testweather.utils.Constants

class SelectLocationMenu(private val context: Context) {

    fun showMenu(textView: TextView, select: (selectItemName: String?) -> Unit) {
        val popupMenu = PopupMenu(context, textView)

        val listLocation = context.resources.getStringArray(R.array.list_country_name)
        listLocation.forEachIndexed { index, nameCity ->
            popupMenu.menu.add(Menu.NONE, index, Menu.NONE, nameCity)
        }


        popupMenu.setOnMenuItemClickListener { menuItem ->
            val cityName =
                if (listLocation[menuItem.itemId] == Constants.MY_LOCATION) null
                else listLocation[menuItem.itemId]
            select.invoke(cityName)

            true
        }

        popupMenu.show()
    }
}