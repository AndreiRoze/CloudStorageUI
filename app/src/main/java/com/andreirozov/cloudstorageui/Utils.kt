package com.andreirozov.cloudstorageui

import androidx.compose.ui.geometry.Offset

object Utils {
    fun isPointInPolygon(point: Offset, polygon: List<Offset>): Boolean {
        var minX = 0f
        var maxX = 0f
        var minY = 0f
        var maxY = 0f

        for (coordinate in polygon) {
            minX = coordinate.x.coerceAtMost(minX)
            maxX = coordinate.x.coerceAtLeast(maxX)
            minY = coordinate.y.coerceAtMost(minY)
            maxY = coordinate.y.coerceAtLeast(maxY)
        }

        if (point.x < minX || point.x > maxX || point.y < minY || point.y > maxY) {
            return false
        }

        var isInside = false

        var j = polygon.size - 1

        polygon.forEachIndexed { index, coordinate ->
            if ((coordinate.y > point.y) != (polygon[j].y > point.y) &&
                point.x < (polygon[j].x - coordinate.x) * (point.y - coordinate.y) / (polygon[j].y - coordinate.y) + coordinate.x
            ) {
                isInside = !isInside
            }

            j = index
        }

        return isInside
    }
}