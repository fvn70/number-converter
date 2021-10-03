package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.math.pow

fun main() {
    while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        val cmd = readLine()!!
        if (cmd.contains("exit")) return
        val (sbase, tbase) = cmd.split(" ").map { it.toInt() }
        while (true) {
            print("Enter number in base $sbase to convert to base $tbase (To go back type /back) ")
            val str = readLine()!!
            if (str == "/back") break
            val dec = convertToDec(str, sbase)
            val res = convertFromDec(dec, tbase)
            println("Conversion result: $res")
            println()
        }
    }
}

fun convertFromDec(num: String, base: Int): String {
    if (num.contains('.')) {
        var n = num.substringBefore('.')
        n = baseFromDec(n, base)
        var f = "0." + num.substringAfter('.')
        f = fromDecFrac(f, base)
        return n + f.substring(1)
    }
    return baseFromDec(num, base)
}

fun convertToDec(num: String, base: Int): String {
    if (num.contains('.')) {
        var n = num.substringBefore('.')
        n = baseToDec(n, base)
        var f = "0." + num.substringAfter('.')
        f = toDecFrac(f, base)
        return n + f.substring(1)
    }
    return baseToDec(num, base)
}

fun baseToDec(num: String, base: Int): String {
    var sum = BigInteger.ZERO
    val l= num.lastIndex
    var pow = BigInteger.ONE
    for (i in l downTo 0) {
        val n = if (num[i] in 'a'..'z') BigInteger.TEN + (num[i] - 'a').toBigInteger() else num[i].digitToInt().toBigInteger()
        sum += n * pow
        pow *= base.toBigInteger()
    }
    return sum.toString()
}
fun toDecFrac(num: String, base: Int): String {
    var sum = 0.toBigDecimal().setScale(10)
    val l= num.lastIndex
    var pow = 1.toBigDecimal().setScale(10)
    for (i in 2..l) {
        val n = if (num[i] in 'a'..'z') 10 + (num[i] - 'a') else num[i].digitToInt()
        pow /= base.toBigDecimal().setScale(10)
        sum += n.toBigDecimal() * pow
    }
    return "%.5f".format(Locale.US, sum)
}
fun fromDecFrac(num: String, base: Int): String {
    var frac = num.toBigDecimal().setScale(10)
    var result = "0."
    while (frac > BigDecimal.ZERO && result.substringAfter('.').length < 5) {
        val str = (frac * base.toBigDecimal()).toString()
        result += str.substringBefore('.').toInt().toString(base)
        frac = ("0." + str.substringAfter('.')).toBigDecimal().setScale(10)
    }
    return result.padEnd(7,'0')
}

fun baseFromDec(num: String, base: Int): String {
    var quot = num.toBigInteger()
    val d = base.toBigInteger()
    var rem = BigInteger.ZERO
    var result = ""
    if (quot < d) return quot.toString(base)
    while (quot >= d) {
        rem = quot % d
        quot = quot / d
        result = rem.toString(base) + result
    }
    result = quot.toString(base) + result
    return result
}