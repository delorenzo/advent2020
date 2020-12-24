import java.io.File

fun main() {
    val ingredients = mutableSetOf<String>()
    val allergies = mutableSetOf<String>()
    val counts = mutableMapOf<String, Int>()
    val listings = File("src/input/day21.txt").readLines().map { line ->
        val listing = line.split(" (contains ")
        val ingredients = listing[0].split(" ").map { ingredient ->
            ingredients.add(ingredient)
            counts.merge(ingredient, 1) { a, b -> a+ b }
            ingredient
        }
        val allergyList = listing[1].substring(0, listing[1].length-1).trim().split(", ")
        val allergies = allergyList.map { allergy ->
            allergies.add(allergy.trim())
            allergy
        }
        ingredients to allergies
    }

    println("Ingredients:  $ingredients")
    println("Allergies:  $allergies")

    val possible = mutableMapOf<String, MutableSet<String>>()
    for (allergy in allergies) {
        possible[allergy] = ingredients.toMutableSet()
    }

    println("Possible:  $possible")

    println(listings)
    listings.map { pair ->
        val recipeIngredients = pair.first
        val allergies = pair.second

        allergies.map {allergy ->
            ingredients.filterNot { recipeIngredients.contains(it) }.map { possible[allergy]?.remove(it) }
        }
    }

    println("Possible:  $possible")
    val notPossible = ingredients.filterNot { ingredient -> possible.values.any { it.contains(ingredient )} }
    println("Not possible:  $notPossible")

    val count = notPossible.map { counts[it]!! }.sum()

    println("Part 1:  $count")

    // part 2

    var found = mutableSetOf<String>()
    var allergiesFound = mutableMapOf<String, Boolean>()
    val allergyMap = mutableMapOf<String, String>()
    while (found.size < allergies.size) {
        allergies.filterNot { allergiesFound[it] == true }.map { allergy ->
            if (possible[allergy]?.size == 1) {
                val ingredient = possible[allergy]!!.first()
                allergiesFound[allergy] = true
                found.add(ingredient)
                allergyMap[ingredient] = allergy
                // Remove from other lists
                allergies.filterNot { it == allergy }.map{ possible[it]?.remove(possible[allergy]!!.first())}
            }
        }
    }
    println(possible)
    println("Found:  $found")
    println("Answer:  ${found.sortedBy { allergyMap[it] }.joinToString(",", "", "")}")
}