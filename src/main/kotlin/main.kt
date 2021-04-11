typealias Rule = Pair<Char, Char>
typealias RulesGraph = Map<Char, List<Char>>

fun buildRulesGraph(rules: List<Rule>): RulesGraph =
    ('a'..'z').toList().map { it to emptyList<Char>() }.toMap() +
            rules.groupBy { it.first }.mapValues { x -> x.value.map { it.second } }

fun countLCP(s: String, t: String): Int {
    var pos = 0
    while (pos < s.length && pos < t.length && s[pos] == t[pos]) {
        ++pos
    }
    return pos
}

fun buildRules(names: Array<String>): List<Rule>? {
    val rules = mutableListOf<Rule>()
    for (i in 1 until names.size) {
        val s = names[i - 1]
        val t = names[i]
        val pos = countLCP(s, t)
        if (s.length != t.length && pos == t.length)
            return null
        if (pos < s.length && pos < t.length) {
            rules += Pair(s[pos], t[pos])
        }
    }
    return rules
}

fun buildOrder(rulesGraph: RulesGraph): List<Char>? {
    val parents = mutableSetOf<Char>()
    val used = mutableSetOf<Char>()
    val order = mutableListOf<Char>()

    fun dfs(v: Char): Boolean {
        parents += v
        rulesGraph[v]?.forEach {
            if (parents.contains(it)) {
                return false
            }
            if (!used.contains(it)) {
                dfs(it)
            }
        }
        parents -= v
        used += v
        order += v
        return true
    }

    for (c in 'a'..'z') {
        if (used.contains(c)) {
            continue
        }
        if (!dfs(c)) {
            return null
        }
    }
    return order.reversed()
}

fun findAlphabetOrder(names: Array<String>): List<Char>? {
    val rules: List<Rule> = buildRules(names) ?: return null
    val rulesGraph: RulesGraph = buildRulesGraph(rules)
    return buildOrder(rulesGraph)
}

fun main() {
    val n = readLine()!!.toInt()
    val names: Array<String> = Array(n) { readLine()!! }
    val order = findAlphabetOrder(names)
    println(order?.joinToString(separator = " ") ?: "Impossible")
}