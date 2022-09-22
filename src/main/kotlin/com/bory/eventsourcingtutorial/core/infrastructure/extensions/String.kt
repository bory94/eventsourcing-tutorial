package com.bory.eventsourcingtutorial.core.infrastructure.extensions

fun String.toSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z]+)"), "\$1_\$2").lowercase()
}

fun String.toCamelCase(): String {
    var str = this

    while (str.contains('_')) {
        str = str.replaceFirst(Regex("_[a-z]"), str[str.indexOf('_') + 1].toString().uppercase())
    }
    return str
}
