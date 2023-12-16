package com.example.test


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.content.Context
import android.view.View


class MyAdapter(context: Context, arr: ArrayList<ItemList>?) : BaseAdapter() {
    var data = ArrayList<ItemList>()
    var context: Context

    init {
        if (arr != null) {
            data = arr
        }
        this.context = context
    }

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return data.size
    }

    override fun getItem(num: Int): Any {
        // TODO Auto-generated method stub
        return data[num]
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(i: Int, someView: View?, arg2: ViewGroup?): View? {
        //Получение объекта inflater из контекста
        var someView: View? = someView

        val inflater = LayoutInflater.from(context)
        //Если someView (View из ListView) вдруг оказался равен
        //null тогда мы загружаем его с помошью inflater
        someView = inflater.inflate(R.layout.list_item, arg2, false)

        if (data[i].header != null) {
            val header = someView?.findViewById(R.id.item_headerText) as TextView
            val subHeader = someView.findViewById(R.id.item_subHeaderText) as TextView
            header.setText(data[i].header)
            subHeader.setText(data[i].subHeader)

            return someView
        }
        else {
            val header = someView?.findViewById(R.id.item_headerText) as TextView
            val subHeader = someView.findViewById(R.id.item_subHeaderText) as TextView
            header.visibility = View.GONE

            header.setText(data[i].header)
            subHeader.setText(data[i].subHeader)

            return someView
        }
    }
}