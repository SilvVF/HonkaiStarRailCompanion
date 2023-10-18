package io.silv.hsrdmgcalc.data

import app.cash.sqldelight.ColumnAdapter

object Adapters {

    val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String) =
            if (databaseValue.isEmpty()) {
                listOf()
            } else {
                databaseValue.split(",")
            }
        override fun encode(value: List<String>) = value.joinToString(separator = ",")
    }

    val listOfPairStringFloatAdapter = object : ColumnAdapter<List<Pair<String, Float>>, String> {
        override fun decode(databaseValue: String): List<Pair<String, Float>> =
            if (databaseValue.isEmpty()) {
                listOf()
            } else {
                databaseValue.split("<>").map { v ->
                    val (key, value) = v.removePrefix("(").removeSuffix(")").split(",")
                    Pair(key, value.toFloat())
                }
            }
        override fun encode(value: List<Pair<String, Float>>) = value.joinToString(separator = "<>")
    }
}
