package com.github.wakingrufus.funk.util

/**
 * Spring Boot relaxed binding strictly requires kebab case when binding a configuration properties,
 * so when those bindings are driven by user-supplied input, this function is useful to normalize that to kebab case prior to binding
 */
fun normalizeConfigKey(rawKey: String): String {
    return translateLowerCaseWithSeparator(rawKey.replace("_", "-"), '-')
}

private fun translateLowerCaseWithSeparator(input: String, separator: Char): String {
    val length = input.length
    if (length == 0) {
        return input
    }

    val result = StringBuilder(length + (length shr 1))
    var upperCount = 0
    for (i in 0 until length) {
        val ch = input[i]
        val lc = ch.lowercaseChar()

        if (lc == ch) { // lower-case letter means we can get new word
            // but need to check for multi-letter upper-case (acronym), where assumption
            // is that the last upper-case char is start of a new word
            if (upperCount > 1) {
                // so insert hyphen before the last character now
                result.insert(result.length - 1, separator)
            }
            upperCount = 0
        } else {
            // Otherwise starts new word, unless beginning of string
            if ((upperCount == 0) && (i > 0)) {
                result.append(separator)
            }
            ++upperCount
        }
        result.append(lc)
    }
    return result.toString()
}
