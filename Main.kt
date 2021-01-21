package sorting

import java.io.File
import java.io.FileNotFoundException
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.math.roundToInt


fun println(line: String, output: File?) {
    if (output == null)
        println(line)
    else
        output.appendText("$line\n")
}

fun sortStrings(line: Boolean = false, sortType: String, input: Scanner, output: File?) {
    val strings = mutableListOf<String>()
    while (input.hasNext()) {
        strings.add(if (line) input.nextLine() else input.next())
    }
    println("Total ${if (line) "lines" else "words"}: ${strings.size}.", output)
    if (sortType == "natural") {
        strings.sort()
        if (line) {
            println("Sorted data:", output)
            strings.forEach { println(it, output) }
        } else {
            println("Sorted data: ${strings.joinToString(" ")}", output)
        }
    }
    else if (sortType == "byCount") {
        for ((count, elements) in strings.groupBy { elm -> strings.count { it == elm } }.toSortedMap()) {
            for (element in elements.distinct().sorted()) {
                println("$element: $count time(s), " +
                        "${(count.toDouble() / strings.size * 100).roundToInt()}%", output)
            }
        }
    }
}

fun sortNumbers(sortType: String, input: Scanner,  output: File?) {
    val integers = mutableListOf<Long>()
    while (input.hasNext()) {
        if (input.hasNextLong()) {
            integers.add(input.nextLong())
        } else {
            println("\"${input.next()}\" is not long. It will be skipped")
        }
    }
    println("Total numbers: ${integers.size}.", output)
    if (sortType == "natural") {
        println("Sorted data: ${integers.sorted().joinToString(" ")}", output)
    } else if (sortType == "byCount") {
        for ((count, elements) in integers.groupBy { elm -> integers.count { it == elm } }.toSortedMap()) {
            for (element in elements.distinct().sorted()) {
                println("$element: $count time(s), " +
                        "${(count.toDouble() / integers.size * 100).roundToInt()}%", output)
            }
        }
    }
}

fun main(args: Array<String>) {
    var dataType = "word"
    var sortingType = "natural"

    var input = Scanner(System.`in`)
    var output: File? = null

    for (i in args.indices) {
        try {
            when (args[i]) {
                "-sortingType" -> {
                    if (args[i + 1] in arrayOf("natural", "byCount"))
                        sortingType = args[i + 1]
                    else
                        println("Invalid sorting type: Using natural by default")
                }
                "-dataType" -> {
                    if (args[i + 1] in arrayOf("word", "long", "line")) {
                        dataType = args[i + 1]
                    } else {
                        println("Invalid data type: Using word by default")
                    }
                }
                "-inputFile" -> input = Scanner(File(args[i + 1]))
                "-outputFile" -> {
                    output = File(args[i + 1])
                    output.writeText("")
                }
                else -> {
                    if (args[i].startsWith("-"))
                        println("\"${args[i]}\" is not a valid parameter. It will be skipped.")
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            if (args[i] == "-sortingType") println("No sorting type defined!")
            else println("No data type defined!")
            return
        } catch (e: FileNotFoundException) {
            println(e.message)
            println("Input file could not be opened.")
        }
    }

    when (dataType) {
        "word" -> sortStrings(false, sortingType, input, output)
        "line" -> sortStrings(true, sortingType, input, output)
        "long" -> sortNumbers(sortingType, input, output)
    }

}
