package methods

import entities.Function
import entities.IntegrationResult
import entities.Segment
import entities.Variables
import kotlin.math.abs
import kotlin.math.pow

fun rungeRule(value: Double, prevValue: Double, accuracyOrder: Int): Double {
    return (prevValue - value) / ((2.0).pow(accuracyOrder) - 1);
}



fun interface IntegrationMethod {
    fun integrate(function: Function, segment: Segment, accuracy: Double): IntegrationResult;
}


fun leftRectanglesFormula(function: Function, segment: Segment, splitsCount: Int): Double {
    val variables: Variables = Variables();
    if (function.variableNames.size != 1) throw IllegalArgumentException();
    val variableName = function.variableNames[0];
    val step: Double = (segment.rightBorder - segment.leftBorder) / splitsCount;
    var result: Double = 0.0;
    for (i: Int in 0 until splitsCount) {
        variables.set(variableName, segment.leftBorder + step * i);
        result += function.getValue(variables) * step;
    }
    return result;
}

val leftRectanglesMethod: IntegrationMethod = IntegrationMethod { function: Function, segment: Segment, accuracy: Double ->
    val accuracyOrder = 1;
    var splitsCount: Int = 4;
    var prevValue: Double = 0.0;
    var value: Double = leftRectanglesFormula(function, segment, splitsCount);
    var firstIteration = true;
    while (firstIteration || (abs(rungeRule(value, prevValue, accuracyOrder)) >= accuracy)) {
        firstIteration = false;
        prevValue = value;
        splitsCount *= 2;
        value = leftRectanglesFormula(function, segment, splitsCount);
    }

    IntegrationResult(value, splitsCount);
}


fun rightRectanglesFormula(function: Function, segment: Segment, splitsCount: Int): Double {
    val variables: Variables = Variables();
    if (function.variableNames.size != 1) throw IllegalArgumentException();
    val variableName = function.variableNames[0];
    val step: Double = (segment.rightBorder - segment.leftBorder) / splitsCount;
    var result: Double = 0.0;
    for (i: Int in 0 until splitsCount) {
        variables.set(variableName, segment.leftBorder + step * (i + 1));
        result += function.getValue(variables) * step;
    }
    return result;
}

val rightRectanglesMethod: IntegrationMethod = IntegrationMethod { function: Function, segment: Segment, accuracy: Double ->
    val accuracyOrder = 1;
    var splitsCount: Int = 4;
    var prevValue: Double = 0.0;
    var value: Double = rightRectanglesFormula(function, segment, splitsCount);
    var firstIteration = true;
    while (firstIteration || (abs(rungeRule(value, prevValue, accuracyOrder)) >= accuracy)) {
        firstIteration = false;
        prevValue = value;
        splitsCount *= 2;
        value = rightRectanglesFormula(function, segment, splitsCount);
    }

    IntegrationResult(value, splitsCount);
}


fun middleRectanglesFormula(function: Function, segment: Segment, splitsCount: Int): Double {
    val variables: Variables = Variables();
    if (function.variableNames.size != 1) throw IllegalArgumentException();
    val variableName = function.variableNames[0];
    val step: Double = (segment.rightBorder - segment.leftBorder) / splitsCount;
    var result: Double = 0.0;
    for (i: Int in 0 until splitsCount) {
        variables.set(variableName, segment.leftBorder + step * i + step / 2);
        result += function.getValue(variables) * step;
    }
    return result;
}

val middleRectanglesMethod: IntegrationMethod = IntegrationMethod { function: Function, segment: Segment, accuracy: Double ->
    val accuracyOrder = 2;
    var splitsCount: Int = 4;
    var prevValue: Double = 0.0;
    var value: Double = middleRectanglesFormula(function, segment, splitsCount);
    var firstIteration = true;
    while (firstIteration || (abs(rungeRule(value, prevValue, accuracyOrder)) >= accuracy)) {
        firstIteration = false;
        prevValue = value;
        splitsCount *= 2;
        value = middleRectanglesFormula(function, segment, splitsCount);
    }

    IntegrationResult(value, splitsCount);
}


fun trapezeFormula(function: Function, segment: Segment, splitsCount: Int): Double {
    val variables: Variables = Variables();
    if (function.variableNames.size != 1) throw IllegalArgumentException();
    val variableName = function.variableNames[0];
    val step: Double = (segment.rightBorder - segment.leftBorder) / splitsCount;
    var result: Double = 0.0;
    var tmp: Double;
    for (i: Int in 0 until splitsCount) {
        tmp = 0.0;
        variables.set(variableName, segment.leftBorder + step * i);
        tmp += function.getValue(variables);
        variables.set(variableName, segment.leftBorder + step * (i + 1));
        tmp += function.getValue(variables);
        result += tmp * step / 2;
    }
    return result;
}

val trapezeMethod: IntegrationMethod = IntegrationMethod { function, segment, accuracy ->
    val accuracyOrder: Int = 2;
    var splitsCount: Int = 4;
    var prevValue: Double = 0.0;
    var value: Double = trapezeFormula(function, segment, splitsCount);
    var firstIteration = true;
    while (firstIteration || (abs(rungeRule(value, prevValue, accuracyOrder)) >= accuracy)) {
        firstIteration = false;
        prevValue = value;
        splitsCount *= 2;
        value = trapezeFormula(function, segment, splitsCount);
    }

    IntegrationResult(value, splitsCount);
}


fun simpsonFormula(function: Function, segment: Segment, splitsCount: Int): Double {
    val variables: Variables = Variables();
    if (function.variableNames.size != 1) throw IllegalArgumentException();
    val variableName = function.variableNames[0];
    val step: Double = (segment.rightBorder - segment.leftBorder) / splitsCount;
    variables.set(variableName, segment.leftBorder);
    var result: Double = function.getValue(variables);
    for (i: Int in 1 until splitsCount) {
        variables.set(variableName, segment.leftBorder + step * i);
        result += if (i % 2 == 0) 2 * function.getValue(variables) else 4 * function.getValue(variables);
    }
    variables.set(variableName, segment.leftBorder + step * splitsCount);
    result += function.getValue(variables);
    result *= step / 3;

    return result;
}

val simpsonMethod: IntegrationMethod = IntegrationMethod { function, segment, accuracy ->
    val accuracyOrder: Int = 4;
    var splitsCount: Int = 4;
    var prevValue: Double = 0.0;
    var value: Double = simpsonFormula(function, segment, splitsCount);
    var firstIteration = true;
    while (firstIteration || (abs(rungeRule(value, prevValue, accuracyOrder)) >= accuracy)) {
        firstIteration = false;
        prevValue = value;
        splitsCount *= 2;
        value = simpsonFormula(function, segment, splitsCount);
    }

    IntegrationResult(value, splitsCount);
}
