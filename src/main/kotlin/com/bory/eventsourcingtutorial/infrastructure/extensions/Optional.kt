package com.bory.eventsourcingtutorial.infrastructure.extensions

import java.util.*

fun Optional<String>.toIntOrDefault(default: Int): Int =
    try {
        this.get().toInt()
    } catch (e: Exception) {
        default
    }

fun Optional<String>.toLongOrDefault(default: Long): Long =
    try {
        this.get().toLong()
    } catch (e: Exception) {
        default
    }