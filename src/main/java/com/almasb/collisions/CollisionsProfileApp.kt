package com.almasb.collisions

import java.awt.Rectangle
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

val entities = ArrayList<Entity>()

fun populate() {
    val random = Random(80701620)

    for (i in 0..1999) {
//        entities.add(Entity(random.nextInt(1280), random.nextInt(1280),
//                random.nextInt(50) + 10, random.nextInt(50) + 10))

        entities.add(Entity(random.nextInt(1280), random.nextInt(1280),
                40, 40))
    }
}

fun runRaw() {
    var collisions = 0

    for (i in entities.indices) {
        for (j in i+1..entities.size-1) {
            val e1 = entities[i]
            val e2 = entities[j]

            if (e1.isColliding(e2)) {
                collisions++
            }
        }
    }

    println("Collisions: $collisions")
}

fun runOptimized() {
    var collisions = 0

    val start = System.nanoTime()

    val grid = Grid(1280 / 40, 1280 / 40, 40)
    entities.forEach { grid.add(it) }

    println("Grid construction: ${(System.nanoTime() - start) / 1000000000.0} sec")

    val checked = ArrayList<Pair<Entity, Entity>>(entities.size * entities.size)

    entities.forEach { e ->
        val maybeCollisions = grid.neighbors(e).filter { !checked.contains(e.to(it)) && !checked.contains(it.to(e)) }

        maybeCollisions.forEach {
            checked.add(e.to(it))

            if (e.isColliding(it)) {
                collisions++
            }
        }
    }

    println("Collisions: $collisions")
}

fun runQuadtree() {
    var collisions = 0

    val quad = Quadtree(0, Rectangle(0, 0, 1280, 1280));

    quad.clear();
    entities.forEach { quad.insert(it) }

    val returnObjects = ArrayList<Entity>();
    for (i in entities.indices) {

        val e1 = entities[i]

        returnObjects.clear();
        quad.retrieve(returnObjects, e1);

        returnObjects.forEach {
            if (e1.isColliding(it)) {
                collisions++
            }
        }
    }

    println("Collisions: $collisions")
}

fun main(args: Array<String>) {
    populate()

    val start = System.nanoTime()

    runRaw()
    //runOptimized()
    //runQuadtree()

    val end = System.nanoTime()

    println("Took: ${(end - start) / 1000000000.0} sec")
}