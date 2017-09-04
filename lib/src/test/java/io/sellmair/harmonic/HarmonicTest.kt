package io.sellmair.harmonic

import junit.framework.Assert
import org.junit.Test

/**
 * Created by sebastiansellmair on 04.09.17.
 */
class HarmonicTest {

    val REST_POSITION_RUNS = 4.0
    val OVERSHOOT = 0.2
    val STEPS = 500
    val TEST_PRECISION = 0.01

    @Test
    fun test(){
        val interpolator = HarmonicInterpolator.create(REST_POSITION_RUNS, OVERSHOOT)

        var lastInterpolation = -1.0
        var restPositionCounter = 0
        var detectedOvershoot = 0.0

        for(i in 0..STEPS){
            val time = i.toDouble() / STEPS.toDouble()
            val normalizedInterpolation = interpolator.getInterpolation(time.toFloat()).toDouble() - 1.0
            if(lastInterpolation *  normalizedInterpolation < 0){
                restPositionCounter ++
            }

            if(restPositionCounter > 0 && Math.abs(detectedOvershoot) < Math.abs(normalizedInterpolation)){
                detectedOvershoot = normalizedInterpolation
            }

            lastInterpolation = normalizedInterpolation
        }

        Assert.assertEquals("Rest position run", REST_POSITION_RUNS +1.toDouble() , restPositionCounter.toDouble(), 0.001)
        Assert.assertTrue("Overshoot. Target: $OVERSHOOT  - actual $detectedOvershoot", Math.abs(detectedOvershoot-OVERSHOOT) - TEST_PRECISION < 0)

    }
}