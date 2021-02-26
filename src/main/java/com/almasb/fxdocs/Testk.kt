package com.almasb.fxdocs

import java.util.*

typealias D = String

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
fun main(args: Array<String>) {

    val D0 = "ab abc cd abd ef e ge bc"

    decompose(D0).forEach(::println)
}

private fun decompose(D0: D): List<D> {
    println("Decomposing...")

    val tokens = D0.split(" +".toRegex()).toMutableList()

    println(tokens)

    val elements = ArrayDeque<Char>()

    val result = arrayListOf<D>()

    while (tokens.isNotEmpty()) {

        var D1 = ""

        elements.add(tokens[0][0])
        var usedElements = "" + elements.first

        while (elements.isNotEmpty()) {
            val e = elements.pop()

            val iter = tokens.iterator()
            while (iter.hasNext()) {
                val token = iter.next()

                if (token.contains(e)) {
                    D1 += token + " "

                    token.filter { !usedElements.contains(it) }.forEach {
                        elements.add(it)
                        usedElements += it
                    }
                    iter.remove()
                }
            }
        }

        result.add(D1.trim())
    }

    return result
}