package ui

import entities.Function
import entities.Segment
import entities.Variables
import java.awt.BasicStroke
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel
import kotlin.math.abs

class ChartPanel : JPanel() {
    private lateinit var horizontalSegment: Segment;
    private lateinit var verticalSegment: Segment;

    private val functions: MutableList<Function> = mutableListOf();

    fun setChartSegments(horizontalSegment: Segment, verticalSegment: Segment) {
        this.horizontalSegment = horizontalSegment;
        this.verticalSegment = verticalSegment;
    }

    fun addFunction(function: Function) {
        this.functions.add(function);
    }

    override fun paintComponent(g: Graphics?) {
        val graphics: Graphics2D = g as? Graphics2D ?: throw IllegalArgumentException();
        super.paintComponent(graphics);
        graphics.stroke = BasicStroke(4F);

        val widthPerPixel: Double = (this.horizontalSegment.rightBorder - this.horizontalSegment.leftBorder) / this.width;
        val heightPerPixel: Double = (this.verticalSegment.rightBorder - this.verticalSegment.leftBorder) / this.height;

        for (function: Function in this.functions) {
            when (function.variableNames.size) {
                1 -> plotFunctionByOneVariable(graphics, function, widthPerPixel, heightPerPixel);
                2 -> plotFunctionByTwoVariables(graphics, function, widthPerPixel, heightPerPixel);
                else -> throw IllegalArgumentException();
            }
        }
    }

    private fun plotFunctionByOneVariable(graphics: Graphics2D, function: Function, widthPerPixel: Double, heightPerPixel: Double) {
        val variables: Variables = Variables();
        val variableName: String = function.variableNames[0];
        for (horizontalPixel: Int in 0 until this.width) {
            variables.set(variableName, horizontalPixelToCoordinate(horizontalPixel, widthPerPixel));
            val value: Double = function.getValue(variables);
            for (verticalPixel: Int in 0 until this.height) {
                if (abs(value - verticalPixelToCoordinate(verticalPixel, heightPerPixel)) < heightPerPixel) graphics.drawLine(horizontalPixel, verticalPixel, horizontalPixel, verticalPixel);
            }
        }
    }

    private fun plotFunctionByTwoVariables(graphics: Graphics2D, function: Function, widthPerPixel: Double, heightPerPixel: Double) {
        val variables: Variables = Variables();
        var prevValue: Double;
        var value: Double
        val horizontalVariableName: String = function.variableNames[0];
        val verticalVariableName: String = function.variableNames[1];
        for (horizontalPixel: Int in 0 until this.width) {
            variables.set(horizontalVariableName, horizontalPixelToCoordinate(horizontalPixel, widthPerPixel));
            variables.set(verticalVariableName, verticalPixelToCoordinate(0, heightPerPixel));
            value = function.getValue(variables);
            for (verticalPixel: Int in 0 until this.height) {
                variables.set(verticalVariableName, verticalPixelToCoordinate(verticalPixel, heightPerPixel));
                prevValue = value;
                value = function.getValue(variables);
                if (value * prevValue <= 0) graphics.drawLine(horizontalPixel, verticalPixel, horizontalPixel, verticalPixel);
            }
        }
        for (verticalPixel: Int in 0 until this.height) {
            variables.set(horizontalVariableName, horizontalPixelToCoordinate(0, widthPerPixel));
            variables.set(verticalVariableName, verticalPixelToCoordinate(verticalPixel, heightPerPixel));
            value = function.getValue(variables);
            for (horizontalPixel: Int in 0 until this.width) {
                variables.set(horizontalVariableName, horizontalPixelToCoordinate(horizontalPixel, widthPerPixel));
                prevValue = value;
                value = function.getValue(variables);
                if (value * prevValue <= 0) graphics.drawLine(horizontalPixel, verticalPixel, horizontalPixel, verticalPixel);
            }
        }
    }

    private fun horizontalPixelToCoordinate(horizontalPixel: Int, widthPerPixel: Double): Double = horizontalSegment.leftBorder + horizontalPixel * widthPerPixel;

    private fun verticalPixelToCoordinate(verticalPixel: Int, heightPerPixel: Double): Double = verticalSegment.leftBorder + (this.height - verticalPixel) * heightPerPixel;
}