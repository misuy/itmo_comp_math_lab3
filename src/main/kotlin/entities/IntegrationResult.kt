package entities

class IntegrationResult(val value: Double, val splitsCount: Int) {
    override fun toString(): String {
        return "Значение интеграла: $value; Количество отрезков разбиения: $splitsCount";
    }
}