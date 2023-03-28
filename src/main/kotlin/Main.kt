//^^

import entities.Function
import entities.Segment
import entities.Variables
import methods.*
import ui.ChartPanel
import util.*
import java.awt.Dimension
import javax.swing.JFrame

fun main(args: Array<String>) {
    val expression: String = "cos(x)";
    val tokens: List<Token> = parseTokens(expression, constants, openingBrackets, closingBrackets, unaryPreOperations, unaryPostOperations, binaryOperations);
    val variableNames: List<String> = getVariableNamesFromTokens(tokens);
    val function: Function = Function(buildCompTree(tokens), variableNames);

    println(simpsonMethod.integrate(function, Segment(-2.0, 2.0), 0.01).value);

    val frame: JFrame = JFrame("chart");
    val chartPanel: ChartPanel = ChartPanel();
    chartPanel.addFunction(function);
    chartPanel.setChartSegments(Segment(-2.0, 2.0), Segment(-2.0, 2.0));
    frame.add(chartPanel);
    frame.size = Dimension(1000, 1000);
    frame.isVisible = true;
}
