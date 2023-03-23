//^^

import entities.Variables
import util.*

fun main(args: Array<String>) {
    val variables: Variables = Variables();
    variables.set("x", 3.0);

    println(buildCompTree(parseTokens("x+cos(pi)", constants, openingBrackets, closingBrackets, unaryPreOperations, unaryPostOperations, binaryOperations)).compute(variables));
}
