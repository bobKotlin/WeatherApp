package com.example.testweather.utils

import android.content.Context
import android.view.Menu
import android.widget.PopupMenu
import android.widget.TextView
import com.example.testweather.R

class SelectLocationMenu(private val context: Context) {

    fun showMenu(textView: TextView, select: (selectItemName: String) -> Unit) {
        val popupMenu = PopupMenu(context, textView)

        val listLocation = context.resources.getStringArray(R.array.list_country_name)
        listLocation.forEachIndexed { index, nameCity ->
            popupMenu.menu.add(Menu.NONE, index, Menu.NONE, nameCity)
        }


        popupMenu.setOnMenuItemClickListener { menuItem ->
            select.invoke(listLocation[menuItem.itemId])
            true
        }

        popupMenu.show()
    }
}