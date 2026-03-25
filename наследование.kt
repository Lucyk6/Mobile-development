package com.example.kotlin1

open class Person {
   private var name = "ilya"
    private var weight=100


    fun getName (): String{
        return name

    }
    fun getWeight (): Int{
        return weight

    }
}
package com.example.kotlin1

class People:Person() {
    private var lastName=""
    fun setLastName(name: String){
        lastName=name
    }
    fun getLastName():String{
        return lastName
    }
}
