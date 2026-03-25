package com.example.kotlin1

fun main(){
    val valueint: Int = 10
    val valuelong: Long=1444
    val valuelongv2=1000_00L
    sumValues(a=10, b=12)
    val sumValue= sumValues(a=42, b=33)
    var variableInt=10
  variableInt=11
}

fun sumValues(a: Int, b: Int): Int{
    println(a+b)
    return a+b
}
fun anyType (a: String): Any{
    return when (a) {
        "hi"-> 42
        "bee"->"hello"
        else ->'r'
    }
}
