//^^

import entities.*
import entities.Function
import methods.*
import ui.*
import util.*
import java.awt.Color
import java.awt.Dimension
import java.io.FileInputStream
import java.io.InputStream
import java.io.PrintStream
import java.lang.Double.max
import java.lang.Double.min
import java.util.Scanner
import javax.swing.JFrame

const val DEFAULT_STEP: Double = 0.01;
val FRAME_SIZE: Dimension = Dimension(1000, 1000);

//TODO: ui
fun main(args: Array<String>) {
    val scanner: Scanner = if (args.size == 1) Scanner(FileInputStream(args[0])) else Scanner(System.`in`);
    val outputStream: PrintStream = System.out;

    val greeting: NothingReader = NothingReader(scanner, outputStream, "Добро пожаловать в лаботаторую работу №3 \"Численное интегрирование\"\n");
    val functionReader: FunctionReader = FunctionReader(scanner, outputStream, "Введите функцию, интеграл которой хотите найти:\n");
    val segmentReader: SegmentReader = SegmentReader(scanner, outputStream, "Введите границы интегрирования (левая_граница правая_граница): ");
    val accuracyReader: DoubleReader = DoubleReader(scanner, outputStream, "Введите точность: ");
    val methodReader: MethodReader = MethodReader(scanner, outputStream, "Выберите метод интегрирования (нужно ввести одно целое число) {\n1 -- метод левых прямоугольников\n2 -- метод правых прямоугольников\n3 -- метод средних прямоугольников\n4 -- метод трапеций\n5 -- метод Симпсона\n}\n: ");

    greeting.read();
    functionReader.read();
    segmentReader.read();

    val function: Function = functionReader.function;
    val segment: Segment = segmentReader.segment;

    val frame: JFrame = JFrame("chart");
    val chartPanel: ChartPanel = ChartPanel();
    chartPanel.addFunction(FunctionGraph(function, Color.BLUE));
    val minAndMaxValuesOnSegment: Pair<Double, Double> = function.getMinAndMaxValuesOnSegment(segment, DEFAULT_STEP);
    chartPanel.setChartSegments(Segment(segment.leftBorder - (segment.rightBorder - segment.leftBorder) / 4,
                        segment.rightBorder + (segment.rightBorder - segment.leftBorder) / 4),
                                Segment(min(0.0, minAndMaxValuesOnSegment.first) - (minAndMaxValuesOnSegment.second - minAndMaxValuesOnSegment.first) / 4,
                                    max(0.0, minAndMaxValuesOnSegment.second) + (minAndMaxValuesOnSegment.second - minAndMaxValuesOnSegment.first) / 4));
    frame.add(chartPanel);
    frame.size = FRAME_SIZE;
    frame.isVisible = true;

    accuracyReader.read();
    methodReader.read();

    val accuracy: Double = accuracyReader.double;
    val method: IntegrationMethod = methodReader.method;
    val integrationResult: IntegrationResult = method.integrate(function, segment, accuracy);

    println(integrationResult);

    chartPanel.addFilledArea(FilledAreaUnderFunctionGraph(function, segment, Color.LIGHT_GRAY));
    frame.repaint();
}
