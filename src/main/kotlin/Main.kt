//^^

import entities.Function
import entities.Segment
import entities.Variables
import ui.ChartPanel
import util.*
import java.awt.Dimension
import javax.swing.JFrame

fun main(args: Array<String>) {
    val expression: String = "sin(x^2+y^2)-e^(x*y)";
    val tokens: List<Token> = parseTokens(expression, constants, openingBrackets, closingBrackets, unaryPreOperations, unaryPostOperations, binaryOperations);
    val variableNames: List<String> = getVariableNamesFromTokens(tokens);
    val function: Function = Function(buildCompTree(tokens), variableNames);

    val frame: JFrame = JFrame("chart");
    val chartPanel: ChartPanel = ChartPanel();
    chartPanel.addFunction(function);
    chartPanel.setChartSegments(Segment(-2.0, 2.0), Segment(-2.0, 2.0));
    frame.add(chartPanel);
    frame.size = Dimension(1000, 1000);
    frame.isVisible = true;
}
