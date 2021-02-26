package com.almasb.algo.traversal;

import java.util.Random;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class AlgorithmFactory {

    public static TraversalAlgorithm breadthFirst() {
        return tiles -> tiles.remove(0);
    }

    public static TraversalAlgorithm depthFirst() {
        return tiles -> tiles.remove(tiles.size() - 1);
    }

    public static TraversalAlgorithm randomFirst() {
        return tiles -> tiles.remove(new Random().nextInt(tiles.size()));
    }

    public static TraversalAlgorithm test() {
        return tiles -> {
            Tile tile = tiles.stream().reduce((tile1, tile2) -> tile2.x < tile1.x ? tile2 : tile1).get();

            tiles.remove(tile);
            return tile;
        };
    }
}
