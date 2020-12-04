import java.io.File
import kotlin.math.exp
fun main() {

    var validPassports = 0
    var matches = 0
    val regex = Regex("(byr|iyr|eyr|hgt|hcl|ecl|pid)\\:([\\#\\w0-9A-Za-z]+)")
    val heightRegex = Regex("(\\d+)(in|cm)")
    val hairRegex = Regex("#[0-9a-f]{6}")
    File("src/input/day4.txt").readLines().map {
        val trim = it.trim()
        if (trim.isBlank()) {
            println(matches)
            if (matches >= 7) { validPassports++;}
            matches = 0
        }
        matches += regex.findAll(trim, 0).count{ match ->
            when (match.groupValues[1]) {
                "byr" -> {
                    val year = match.groupValues[2].toInt()
                    year in 1920..2002
                }
                "iyr" -> {
                    val year = match.groupValues[2].toInt()
                    year in 2010..2020
                }
                "eyr" -> {
                    val year = match.groupValues[2].toInt()
                    year in 2020..2030
                }
                "hgt" -> {
                    val heightMatch = heightRegex.matchEntire(match.groupValues[2])
                    heightMatch?.let { height ->
                        val heightNum = height.groupValues[1].toInt()
                        when (height.groupValues[2]) {
                            "cm" -> heightNum in 150..193
                            "in" -> heightNum in 59..76
                            else -> throw Exception("What is this... $it")
                        }
                    } ?: false
                }
                "hcl" -> {
                     hairRegex.matches(match.groupValues[2])
                }
                "ecl" -> {
                    listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(match.groupValues[2])
                }
                "pid" -> {
                    match.groupValues[2].toIntOrNull()?.let {
                        match.groupValues[2].count() == 9
                    } ?: false
                }
                else -> {
                   false
                }
            }
        }
    }

    println()
    println(validPassports)
}