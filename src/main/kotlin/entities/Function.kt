package entities

import util.Node

class Function(private val compTree: Node, val variableNames: List<String>) {
    fun getValue(variables: Variables): Double {
        return this.compTree.compute(variables);
    }
}