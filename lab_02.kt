import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Написать, отладить и выполнить программы решения систем линейных алгебраических уравнений,
 * записанных в векторно-матричной форме и приведенных в таблице.
 * В колонке х* приведено точное решение.
 * Решить систему методом Гаусса с выбором главного элемента и методом Зейделя.
 *
 * Оценить погрешности методов.
 *
 * Для метода Гаусса привести матрицу, приведенную к треугольному виду.
 * Для метода Зейделя - преобразованную матрицу и количество итераций.
 * Показать, что условия сходимости выполнены.
 */

val A = listOf(
    mutableListOf(1.32, 2.06, -3.40, 7.11),
    mutableListOf(-9.13, 5.84, 1.21, 0.76),
    mutableListOf(3.12, -8.14, 2.51, -1.13),
    mutableListOf(0.77, 0.17, 2.32, 1.10)
)
val B = listOf(
    mutableListOf(30.17),
    mutableListOf(3.62),
    mutableListOf(-19.06),
    mutableListOf(2.09)
)
val X = listOf(
    listOf(1),
    listOf(2),
    listOf(-1),
    listOf(3)
)

fun gauss() {
    val gA = A.map { it.toMutableList() }
    val gB = B.map { it[0] }.toMutableList()
    val firstAgElem = gA[0][0]
    gB[0] /= firstAgElem
    for (index in gA[0].indices) {
        gA[0][index] /= firstAgElem
    }

    for (i1 in 0..2) {
        for (i in i1 + 1..3) {
            val k: Double = -gA[i][i1] / gA[i1][i1]
            val tempRow = gA[i1].map { k * it }
            for (j in gA[i].indices) {
                gA[i][j] += tempRow[j]
            }
            gB[i] += gB[i1] * k
        }
    }
    val x3 = gB[3] / gA[3][3]
    val x2 = (gB[2] - x3 * gA[2][3]) / gA[2][2]
    val x1 = (gB[1] - x3 * gA[1][3] - x2 * gA[1][2]) / gA[1][1]
    val x0 = (gB[0] - x3 * gA[0][3] - x2 * gA[0][2] - x1 * gA[0][1]) / gA[0][0]
    val x3S = String.format("%.15f", x3)
    val x2S = String.format("%.15f", x2)
    val x1S = String.format("%.15f", x1)
    val x0S = String.format("%.15f", x0)

    println("Метод Гаусса:")
    for (row in gA) {
        println(row.joinToString(" ") { String.format("%.9f", it) })
    }
    println(
        "Погрешность: ${
            String.format(
                "%.16f",
                (listOf(x0, x1, x2, x3).mapIndexed { index, it ->
                    abs(it - X[index][0])
                }).max()
            )
        }"
    )
    println("x_0: ${x0S}\nx_1: ${x1S}\nx_2: ${x2S}\nx_3: ${x3S}\n")
}


fun seidel() {
    val changedA = listOf(
        A[1].toMutableList(),
        A[2].toMutableList(),
        A[3].toMutableList(),
        A[0].toMutableList()
    )
    val changedB = listOf(
        B[1].toMutableList(),
        B[2].toMutableList(),
        B[3].toMutableList(),
        B[0].toMutableList()
    )
    val sA = changedA.mapIndexed { indexList, it ->
        it.toMutableList().mapIndexed { index, number ->
            if (index != indexList) -number / it[indexList] else 0.0
        }
    }.toMutableList()
    val sB = changedB.mapIndexed { index, it -> it[0] / changedA[index][index] }.toMutableList()

    val x: MutableList<MutableList<Double>> = mutableListOf(
        mutableListOf(
            0.0,
            0.0,
            0.0,
            0.0
        )
    )

    // Условие сходимости:
    // Сумма модулей элементов (кроме диагонального) в каждой строке меньше, чем модуль диагонального элемента
    val sufficientCondition = changedA.mapIndexed { listIndex, list ->
        list.mapIndexed { index, d ->
            if (index != listIndex) abs(d) else 0.0
        }.sum() < abs(list[listIndex])
    }.all { it }
    println("Условие сходимости: $sufficientCondition")

    do {
        val xK1 = mutableListOf<Double>(

        )
        for (listIndex in sA.indices) {
            var tempValue = 0.0
            for (index in sA[listIndex].indices) {
                tempValue +=
                    if (index >= listIndex) sA[listIndex][index] * x.last()[index] else xK1[index] * sA[listIndex][index]
            }
            tempValue += sB[listIndex]
            xK1.addLast(tempValue)
        }
        x.add(
            xK1
        )
    } while (sqrt(x[x.size - 2].mapIndexed { index, d -> (d - x.last()[index]).pow(2) }.sum()) > 10.0.pow(-7))
    println("Метод Зейделя:")
    println(changedA.joinToString("\n") { it.joinToString(" ") })
    println("Количество итераций: ${x.size}")
    println(x.last().joinToString("\n") { "x_${x.last().indexOf(it)}: " + String.format("%.9f", it) })
    println("Погрешность: ${String.format("%.9f", (x.last().mapIndexed { index, it -> abs(it - X[index][0]) }).max())}")
}


fun main() {
    gauss()
    seidel()
}