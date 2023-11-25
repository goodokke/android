package com.example.test

class ItemList
/**
 * Конструктор создает новый элемент в соответствии с передаваемыми
 * параметрами:
 * @param h - заголовок элемента
 * @param s - подзаголовок
 */(h: String?, s: String?) {
    /**
     * Заголовок
     */
    var header: String? = h

    /**
     * Подзаголовок
     */
    var subHeader: String? = s

    //Всякие гетеры и сеттеры
//    fun getHeader(): String? {
//        return header
//    }
//
//    fun setHeader(header: String?) {
//        this.header = header
//    }
//
//    fun getSubHeader(): String? {
//        return subHeader
//    }
//
//    fun setSubHeader(subHeader: String?) {
//        this.subHeader = subHeader
//    }
}