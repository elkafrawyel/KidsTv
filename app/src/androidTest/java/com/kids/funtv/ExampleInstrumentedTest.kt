package com.kids.funtv

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented jumping_girl, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under jumping_girl.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.kidscartoons.videos", appContext.packageName)
    }
}
