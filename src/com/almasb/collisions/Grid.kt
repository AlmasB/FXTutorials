package com.almasb.collisions

import javafx.geometry.Point2D
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Grid(val width: Int, val height: Int, val blockSize: Int) {

    private val grid = Array< Array<Cell> >(height, { i -> Array<Cell>(width, {j -> Cell() }) })

    private val map = HashMap<Entity, ArrayList<Pair<Int, Int>> >()
    //private val checked =

    fun add(entity: Entity) {
        val x1 = entity.x / blockSize
        val y1 = entity.y / blockSize

        val x2 = (entity.x + entity.w) / blockSize
        val y2 = (entity.y + entity.h) / blockSize

        val points = ArrayList<Pair<Int, Int>>()

        // -1, +1 is needed for the outer checks
        for (x in x1 - 1..x2 + 1) {
            for (y in y1 - 1..y2 + 1) {

                if (isValid(x, y)) {

                    // add only internal bounds
                    if (x != x1 - 1 && x != x2 + 1 && y != y1 - 1 && y != y2 + 1) {
                        grid[y][x].entities.add(entity)
                    }

                    points.add(x.to(y))
                }
            }
        }

        map[entity] = points
    }

    fun neighbors(entity: Entity): List<Entity> {
        val points = map[entity]!!

        return points.filter { isValid(it.first, it.second) }
                .map { grid[it.second][it.first].entities }
                .flatten()
                .distinct()
                .filter { it != entity }
    }

    fun isValid(x: Int, y: Int): Boolean {
        return x >= 0 && x < width && y >= 0 && y < height
    }
}

class Cell {

    val entities = ArrayList<Entity>()
}