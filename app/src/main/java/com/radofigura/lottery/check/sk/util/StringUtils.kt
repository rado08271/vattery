package com.radofigura.lottery.check.sk.util

import java.text.Normalizer

object StringUtils {
    fun deAccent(inputString: String): String {
        return Normalizer.normalize(inputString.trim().lowercase(), Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    fun addHyphens(inputString: String): String {
        return StringBuilder(inputString).insert(4, '-').insert(9, '-').toString()
    }
}