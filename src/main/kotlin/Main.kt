//^^

import entities.*
import entities.Function
import methods.*
import ui.*
import util.*
import java.awt.Color
import java.awt.Dimension
import java.io.InputStream
import java.io.PrintStream
import javax.swing.JFrame

const val DEFAULT_STEP: Double = 0.01;
val FRAME_SIZE: Dimension = Dimension(1000, 1000);

//TODO: ui
fun main(args: Array<String>) {
    /*
    val expression: String = "cos(x)";
    val tokens: List<Token> = parseTokens(expression, constants, openingBrackets, closingBrackets, unaryPreOperations, unaryPostOperations, binaryOperations);
    val variableNames: List<String> = getVariableNamesFromTokens(tokens);
    val function: Function = Function(buildCompTree(tokens), variableNames);

    println(simpsonMethod.integrate(function, Segment(-2.0, 2.0), 0.01).value);

    val frame: JFrame = JFrame("chart");
    val chartPanel: ChartPanel = ChartPanel();
    chartPanel.addFunction(FunctionGraph(function, Color.BLUE));
    chartPanel.addFilledArea(FilledAreaUnderFunctionGraph(function, Segment(-2.0, 2.0), Color.LIGHT_GRAY));
    chartPanel.setChartSegments(Segment(-5.0, 5.0), Segment(-5.0, 5.0));
    frame.add(chartPanel);
    frame.size = Dimension(1000, 1000);
    frame.isVisible = true;
    */

    val inputStream: InputStream = System.`in`;
    val outputStream: PrintStream = System.out;
    val greeting: NothingReader = NothingReader(inputStream, outputStream, "Добро пожаловать в лаботаторую работу №3 \"Численное интегрирование\"\n");
    val functionReader: FunctionReader = FunctionReader(inputStream, outputStream, "Введите функцию, интеграл которой хотите найти:\n");
    val segmentReader: SegmentReader = SegmentReader(inputStream, outputStream, "Введите границы интегрирования (левая_граница правая_граница): ");
    val accuracyReader: DoubleReader = DoubleReader(inputStream, outputStream, "Введите точность: ");
    val methodReader: MethodReader = MethodReader(inputStream, outputStream, "Выберите метод интегрирования (нужно ввести одно целое число) {\n1 -- метод левых прямоугольников\n2 -- метод правых прямоугольников\n3 -- метод средних прямоугольников\n4 -- метод трапеций\n5 -- метод Симпсона\n}\n: ");

    greeting.read();
    functionReader.read();
    segmentReader.read();
    accuracyReader.read();
    methodReader.read();

    val function: Function = functionReader.function;
    val segment: Segment = segmentReader.segment;

    val frame: JFrame = JFrame("chart");
    val chartPanel: ChartPanel = ChartPanel();
    chartPanel.addFunction(FunctionGraph(function, Color.BLUE));
    val minAndMaxValuesOnSegment: Pair<Double, Double> = function.getMinAndMaxValuesOnSegment(segment, DEFAULT_STEP);
    chartPanel.setChartSegments(Segment(segment.leftBorder - (segment.rightBorder - segment.leftBorder) / 4,
                        segment.rightBorder + (segment.rightBorder - segment.leftBorder) / 4),
                                Segment(minAndMaxValuesOnSegment.first - (minAndMaxValuesOnSegment.second - minAndMaxValuesOnSegment.first) / 4,
                                    minAndMaxValuesOnSegment.second + (minAndMaxValuesOnSegment.second - minAndMaxValuesOnSegment.first) / 4));
    frame.add(chartPanel);
    frame.size = FRAME_SIZE;
    frame.isVisible = true;

    val accuracy: Double = accuracyReader.double;
    val method: IntegrationMethod = methodReader.method;
    val integrationResult: IntegrationResult = method.integrate(function, segment, accuracy);
    chartPanel.addFilledArea(FilledAreaUnderFunctionGraph(function, segment, Color.LIGHT_GRAY));
    println(integrationResult.value);
    println(integrationResult.splitsCount);
}
