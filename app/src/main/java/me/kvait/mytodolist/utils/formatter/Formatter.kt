package me.kvait.mytodolist.utils.formatter

interface Formatter {
    fun getFormatDate(date: Long): String?
}