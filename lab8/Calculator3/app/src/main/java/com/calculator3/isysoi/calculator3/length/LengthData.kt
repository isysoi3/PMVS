package com.calculator3.isysoi.calculator3.length

class LengthData {
    companion object {
        const val OUTPUT_SAVE_ID = "LENGTH_DATA_ID"

        val lengths: HashMap<String, Double> = hashMapOf(
                "METER" to 1.0,
                "CENTIMETER" to 100.0,
                "FEET" to 3.28,
                "INCH" to 39.37,
                "KILOMETER" to 0.001,
                "MILE" to 0.000621,
                "MILLIMETER" to 1000.0,
                "YARD" to 1.093613)
    }
}