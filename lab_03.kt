import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.pow

const val CONST_A = 0.0
const val CONST_B = 1.0
var N = 1
val WIDTH
    get() = (CONST_B - CONST_A) / N
const val CONST_RATE = 1.0E-5
val I = 7 / (3 * ln(2.0))
val f: (Double) -> Double = { x -> 2.0.pow(3 * x) }
val ddf: (Double) -> Double = { x -> 9 * ln(2.0).pow(2) * f(x) }
val ddddf: (Double) -> Double = { x -> 27 * ln(2.0).pow(3) * f(x) }


fun leftRectangle(): Double {
    return List(N) {
        f(it / N.toDouble()) * WIDTH
    }.sum()
}

fun rightRectangle(): Double {
    return List(N) {
        f(it / N.toDouble() + WIDTH) * WIDTH
    }.sum()
}

fun centralRectangle(): Double {
    return List(N) {
        f(it / N.toDouble() + WIDTH / 2) * WIDTH
    }.sum()
}

fun trapeze(): Double {
    return List(N - 1) {
        val h = WIDTH
        val a = f(it / N.toDouble())
        val b = f((it + 1) / N.toDouble())
        (a + b) * h / 2
    }.sum()
}

fun simpson(): Double {
    return List(N) {
        when {
            it.toDouble() / N == CONST_A || it.toDouble() / N == CONST_B -> f(it.toDouble() / N)
            it % 2 == 0 -> 2 * f(it.toDouble() / N)
            else -> 4 * f(it.toDouble() / N)
        }
    }.sum() * WIDTH / 3
}

fun output(methodName: String, accuracyOutput: () -> Unit, method: () -> Double) {
    N = 1
    var lastValue: Double
    var currentValue = method()
    do {
        lastValue = currentValue
        N *= 2
        currentValue = method()
    } while (
        abs(currentValue - lastValue) >= CONST_RATE
    )
    println("${methodName}: ${String.format("%.7f", currentValue)}")
    accuracyOutput()
    println("Итераций потребовалось: $N")
    println("Относительная погрешность: ${String.format("%.7f", abs(I - currentValue) / I * 100)}\n")

}

fun main() {
    val rectangleAccuracyOutput: () -> Unit = {
        println(
            "Погрешность : ${
                String.format("%.12f", abs(ddf(CONST_B)) * (CONST_B - CONST_A) * WIDTH.pow(2) / 24)
            }"
        )
    }
    val trapezeAccuracyOutput: () -> Unit = {
        println(
            "Погрешность : ${
                String.format("%.12f", abs(ddf(CONST_B)) * (CONST_B - CONST_A) * WIDTH.pow(2) / 12)
            }"
        )
    }
    val simpsonAccuracyOutput: () -> Unit = {
        println(
            "Погрешность : ${
                String.format("%.25f", abs(ddddf(CONST_B)) * WIDTH.pow(4) * (CONST_B - CONST_A) / 2880)
            }"
        )
    }
    listOf(
        Triple("Метод левых прямоугольников", rectangleAccuracyOutput) { leftRectangle() },
        Triple("Метод центральных прямоугольников", rectangleAccuracyOutput) { centralRectangle() },
        Triple("Метод правых прямоугольников", rectangleAccuracyOutput) { rightRectangle() },
        Triple("Метод трапеций", trapezeAccuracyOutput) { trapeze() },
        Triple("Метод Симпсона", simpsonAccuracyOutput) { simpson() }
    ).forEach {
        output(it.first, it.second, it.third)
    }
}
