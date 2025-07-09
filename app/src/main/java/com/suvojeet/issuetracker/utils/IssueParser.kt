package com.suvojeet.issuetracker.utils

fun parseHistoryEntry(entry: String): Map<String, String> {
    val parsedMap = mutableMapOf<String, String>()
    val parts = entry.split(Regex(" \\| "))

    for (part in parts) {
        val keyValue = part.split(": ", 2) // Split only on the first occurrence of ": "
        if (keyValue.size == 2) {
            parsedMap[keyValue[0]] = keyValue[1]
        }
    }
    return parsedMap
}
