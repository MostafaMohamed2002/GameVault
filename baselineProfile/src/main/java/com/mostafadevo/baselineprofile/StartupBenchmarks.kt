package com.mostafadevo.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class benchmarks the speed of app startup.
 * Run this benchmark to verify how effective a Baseline Profile is.
 * It does this by comparing [CompilationMode.None], which represents the app with no Baseline
 * Profiles optimizations, and [CompilationMode.Partial], which uses Baseline Profiles.
 *
 * Run this benchmark to see startup measurements and captured system traces for verifying
 * the effectiveness of your Baseline Profiles. You can run it directly from Android
 * Studio as an instrumentation test, or run all benchmarks for a variant, for example benchmarkRelease,
 * with this Gradle task:
 * ```
 * ./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest
 * ```
 *
 * You should run the benchmarks on a physical device, not an Android emulator, because the
 * emulator doesn't represent real world performance and shares system resources with its host.
 *
 * For more information, see the [Macrobenchmark documentation](https://d.android.com/macrobenchmark#create-macrobenchmark)
 * and the [instrumentation arguments documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args).
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        // The application id for the running build variant is read from the instrumentation arguments.
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()
                device.waitForIdle()
                device.findObject(By.res("free_games_list")).apply {
                    wait(Until.hasObject(By.res("free_games_list")), 20_000)
                    setGestureMargin(device.displayWidth / 5)
                    fling(Direction.DOWN)
                    fling(Direction.DOWN)
                }
                device.waitForIdle()

                device.findObjects(By.res("FreeGameListItem")).apply {
                    val index = (iteration?:0)% this.size
                    this[index].click()
                }
                device.wait(Until.gone(By.res("FreeGameListItem")), 20_000)
                device.wait(Until.hasObject(By.res("FreeGameDetailesScreen")), 20_000)
                //scroll throught the details screen
                device.findObject(By.res("FreeGameDetailesScreen")).apply {
                    setGestureMargin(device.displayWidth / 5 )
                    fling(Direction.DOWN)
                }
                device.pressBack()

                //deals screen
                device.findObject(By.text("Deals")).apply {
                    click()
                }
                device.wait(Until.hasObject(By.res("deals_list")), 20_000)
                device.findObject(By.res("deals_list")).apply {
                    setGestureMargin(device.displayWidth / 5)
                    fling(Direction.DOWN)
                    fling(Direction.DOWN)
                }
                device.waitForIdle()
                device.findObjects(By.res("DealsListItem")).apply {
                    val index = (iteration?:0)% this.size
                    this[index].click()
                }
                device.wait(Until.gone(By.res("DealsListItem")), 20_000)
                device.pressBack()

                //giveaways screen
                device.findObject(By.text("Giveaways")).apply {
                    click()
                }
                device.wait(Until.hasObject(By.res("giveaways_list")), 20_000)
                device.findObject(By.res("giveaways_list")).apply {
                    setGestureMargin(device.displayWidth / 5)
                    fling(Direction.DOWN)
                    fling(Direction.DOWN)
                }
            }
        )
    }
}
