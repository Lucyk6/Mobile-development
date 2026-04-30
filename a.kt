
//interface GeneralVehicle
interface GeneralVehicle
{
    fun setSpeed(): Int
    fun getSpeed() : Int
}


//car
class Car: Vehicle(type = "mil") {
    override fun setSpeed(): Int {
        return 0;
    }
    override fun getSpeed(): Int {
        return 0;
    }

}
//vehicle
abstract class Vehicle(val type : String): GeneralVehicle {

    fun getTypeSpeed() : Double{
        if (type =="mile"){
          return getSpeed() / 1.6
        }
        return getSpeed().toDouble()
    }
}
//AirVehicle
class AirVehicle: Vehicle(type = "km") {
    override fun getSpeed() : Int  {
         return 0;
    }

    override fun setSpeed() : Int{
        return 0;

    }
}
