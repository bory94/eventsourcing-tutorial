package com.bory.eventsourcingtutorial.core.infrastructure.extensions

import com.fasterxml.jackson.databind.ObjectMapper

fun <T : Any> MutableMap<String, Any>.toObj(objectMapper: ObjectMapper, clazz: Class<T>): T =
    try {
        objectMapper.convertValue(this, clazz)
    } catch (e: Exception) {
        val snakeCaseKeyMap = this.mapKeys { entry ->
            entry.key.toCamelCase()
        }

        objectMapper.convertValue(snakeCaseKeyMap, clazz)
    }

