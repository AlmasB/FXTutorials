/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.tutorialx1

import javafx.animation.Interpolator

/**
 * Adapted from https://github.com/grapefrukt/juicy-breakout/tree/master/lib/com/gskinner/motion/easing
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class Interpolators : EasingInterpolator {

    LINEAR {
        override fun easeIn(ratio: Double): Double {
            return ratio
        }

        override fun easeOut(ratio: Double): Double {
            return ratio
        }

        override fun easeInOut(ratio: Double): Double {
            return ratio
        }
    },

    QUADRATIC {
        override fun easeIn(ratio: Double): Double {
            return ratio*ratio
        }

        override fun easeOut(ratio: Double): Double {
            return -ratio*(ratio-2)
        }

        override fun easeInOut(ratio: Double): Double {
            return if (ratio < 0.5)
                2*ratio*ratio
            else
                -2*ratio*(ratio-2)-1
        }
    },

    CUBIC {
        override fun easeIn(ratio: Double): Double {
            return ratio*ratio*ratio
        }

        override fun easeOut(ratio: Double): Double {
            val r = ratio - 1
            return r*r*r+1
        }

        override fun easeInOut(ratio: Double): Double {
            val r = ratio - 1

            return if (ratio < 0.5)
                4*ratio*ratio*ratio
            else
                4*r*r*r+1
        }
    },

    QUARTIC {
        override fun easeIn(ratio: Double): Double {
            return ratio*ratio*ratio*ratio
        }

        override fun easeOut(ratio: Double): Double {
            val r = ratio - 1

            return 1-r*r*r*r
        }

        override fun easeInOut(ratio: Double): Double {
            val r = ratio - 1

            return if (ratio < 0.5)
                8*ratio*ratio*ratio*ratio
            else
                -8*r*r*r*r+1
        }
    },

    QUINTIC {
        override fun easeIn(ratio: Double): Double {
            return ratio*ratio*ratio*ratio*ratio
        }

        override fun easeOut(ratio: Double): Double {
            val r = ratio - 1

            return 1 + r*r*r*r*r
        }

        override fun easeInOut(ratio: Double): Double {
            val r = ratio - 1

            return if (ratio < 0.5)
                16*ratio*ratio*ratio*ratio*ratio
            else
                16 * r*r*r*r*r + 1
        }
    },

    EXPONENTIAL {
        override fun easeIn(ratio: Double): Double {
            return if (ratio == 0.0) 0.0 else Math.pow(2.0, 10 * (ratio - 1))
        }

        override fun easeOut(ratio: Double): Double {
            return if (ratio == 1.0) 1.0 else 1 - Math.pow(2.0, -10 * ratio)
        }

        override fun easeInOut(ratio: Double): Double {
            if (ratio == 0.0 || ratio == 1.0)
                return ratio

            val r = ratio * 2 - 1
            if (r < 0)
                return 0.5*Math.pow(2.0, 10*r)

            return 1 - 0.5*Math.pow(2.0, -10*r)
        }
    },

    SINE {
        override fun easeIn(ratio: Double): Double {
            if (ratio == 1.0)
                return 1.0

            return 1 - Math.cos(ratio * (Math.PI / 2))
        }

        override fun easeOut(ratio: Double): Double {
            return Math.sin(ratio * (Math.PI / 2))
        }

        override fun easeInOut(ratio: Double): Double {
            return -0.5 * (Math.cos(ratio * Math.PI) - 1)
        }
    },

    CIRCULAR {
        override fun easeIn(ratio: Double): Double {
            return -(Math.sqrt(1 - ratio*ratio) - 1)
        }

        override fun easeOut(ratio: Double): Double {
            return Math.sqrt(1 - (ratio - 1) * (ratio - 1))
        }

        override fun easeInOut(ratio: Double): Double {
            val r = ratio * 2
            val r2 = r - 2

            return if (r < 1)
                -0.5 * (Math.sqrt(1 - r * r) - 1)
            else
                0.5 * (Math.sqrt(1 - r2 * r2) + 1)
        }
    },

    SMOOTH {
        override fun easeIn(ratio: Double): Double {
            return Interpolator.EASE_IN.interpolate(0.0, 1.0, ratio)
        }

        override fun easeOut(ratio: Double): Double {
            return Interpolator.EASE_OUT.interpolate(0.0, 1.0, ratio)
        }

        override fun easeInOut(ratio: Double): Double {
            return Interpolator.EASE_BOTH.interpolate(0.0, 1.0, ratio)
        }
    },

    BOUNCE {
        override fun easeIn(ratio: Double): Double {
            return 1 - easeOut(1 - ratio)
        }

        override fun easeOut(ratio: Double): Double {
            if (ratio < 1/2.75) {
                return 7.5625*ratio*ratio
            } else if (ratio < 2/2.75) {
                val r = ratio - 1.5/2.75
                return 7.5625*r*r+0.75
            } else if (ratio < 2.5/2.75) {
                val r = ratio-2.25/2.75
                return 7.5625*r*r+0.9375
            } else {
                val r = ratio - 2.625/2.75
                return 7.5625*r*r+0.984375
            }
        }

        override fun easeInOut(ratio: Double): Double {
            val r = ratio * 2

            return if (r < 1)
                0.5 * easeIn(r)
            else
                0.5 * easeOut(r - 1) + 0.5
        }
    },

    ELASTIC {
        private val a = 1
        private val p = 0.3
        private val s = p / 4

        override fun easeIn(ratio: Double): Double {
            if (ratio == 0.0 || ratio == 1.0)
                return ratio

            val r = ratio - 1

            return -(a * Math.pow(2.0, 10 * r) * Math.sin((r - s) * (2 * Math.PI) / p))
        }

        override fun easeOut(ratio: Double): Double {
            if (ratio == 0.0 || ratio == 1.0)
                return ratio

            return a * Math.pow(2.0, -10 * ratio) *  Math.sin((ratio - s) * (2 * Math.PI) / p) + 1
        }

        override fun easeInOut(ratio: Double): Double {
            if (ratio == 0.0 || ratio == 1.0)
                return ratio

            val r = ratio*2 - 1

            if (r < 0) {
                return -0.5 * (a * Math.pow(2.0, 10 * r) * Math.sin((r - s*1.5) * (2 * Math.PI) /(p*1.5)))
            }

            return 0.5 * a * Math.pow(2.0, -10 * r) * Math.sin((r - s*1.5) * (2 * Math.PI) / (p*1.5)) + 1
        }
    },

    BACK {
        private val s = 1.70158

        override fun easeIn(ratio: Double): Double {
            if (ratio == 1.0)
                return 1.0

            return ratio * ratio * ((s+1) * ratio - s)
        }

        override fun easeOut(ratio: Double): Double {
            if (ratio == 0.0)
                return 0.0

            val r = ratio - 1

            return r * r * ((s+1) * r + s) + 1
        }

        override fun easeInOut(ratio: Double): Double {
            val r = ratio * 2
            val r2 = r - 2

            return if (r < 1)
                0.5*(r*r*((s*1.525+1)*r-s*1.525))
            else
                0.5*(r2 * r2 * ((s*1.525 + 1) * r2 + s*1.525)+2)
        }
    }
}

interface EasingInterpolator {

    fun EASE_IN(): Interpolator = object : Interpolator() {
        override fun curve(t: Double): Double {
            return easeIn(t)
        }
    }

    fun EASE_OUT(): Interpolator = object : Interpolator() {
        override fun curve(t: Double): Double {
            return easeOut(t)
        }
    }

    fun EASE_IN_OUT(): Interpolator = object : Interpolator() {
        override fun curve(t: Double): Double {
            return easeInOut(t)
        }
    }

    fun easeIn(ratio: Double): Double
    fun easeOut(ratio: Double): Double
    fun easeInOut(ratio: Double): Double
}