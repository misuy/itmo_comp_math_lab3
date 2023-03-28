package ui

//TODO: object that reads his own value

fun ask(message: String, handler: (Any) -> Unit) {
    println(message);
    handler
}