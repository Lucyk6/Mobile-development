```kotlin
import kotlin.properties.Delegates

// ==================== 1: Ленивое подключение к БД ====================

class DatabaseConnection {
    init { println("Подключение создано") }
    override fun toString() = "DatabaseConnection@active"
}

// ==================== 2: Ленивое формирование отчёта ====================

class Report {
    init { println("Отчёт сформирован") }
    val content = "Отчёт о продажах за июнь 2025"
}

// ==================== 3: Контроль изменения цены товара ====================

class Product(val name: String, initialPrice: Double) {
    var price: Double by Delegates.observable(initialPrice) { _, old, new ->
        println("[$name] Старая цена: $old → Новая цена: $new")
    }

    companion object {
        fun demoLaptop() = Product("Ноутбук", 75000.0)
        fun demoPhone()  = Product("Телефон", 45000.0)
        fun demoTablet() = Product("Планшет", 35000.0)
    }

    override fun toString() = "Product(name=$name, price=$price)"
}

// ==================== 4: Контроль возраста сотрудника ====================

class Employee(val name: String, initialAge: Int) {
    var age: Int by Delegates.vetoable(initialAge) { _, _, new ->
        if (new < 18 || new > 80) {
            println("[$name] Отказ: возраст $new вне диапазона (18–80)")
            false
        } else {
            println("[$name] Возраст изменён на $new")
            true
        }
    }
}

// ==================== 5: Контроль скидки ====================

class Discount(initialPercent: Int) {
    var percent: Int by Delegates.vetoable(initialPercent) { _, _, new ->
        if (new < 0 || new > 90) {
            println("Отказ: скидка $new% вне диапазона (0–90%)")
            false
        } else {
            println("Скидка установлена: $new%")
            true
        }
    }
}

// ==================== 6: Singleton настроек приложения ====================

object AppSettings {
    val appName = "Учебный портал"
    val version  = "1.0.0"
    var theme    = "Light"
}

// ==================== 7: Счётчик пользователей ====================

object UserStatistics {
    private var count = 0
    fun registerUser(name: String) { count++; println("Зарегистрирован: $name (всего: $count)") }
    fun totalUsers() = count
}

// ==================== 8: Companion Object для создания пользователей ====================

data class User(val name: String, val role: String) {
    companion object {
        fun createGuest() = User("Гость", "GUEST")
        fun createAdmin() = User("Администратор", "ADMIN")
    }
}

// ==================== 9: Тестовые данные через companion object (Product уже есть выше) ====================

// ==================== 10: Анонимный объект — создаётся прямо в main ====================

// ==================== 11–13: Интерфейсы и делегирование ====================

interface Logger   { fun log(message: String) }
interface Notifier { fun notify(message: String) }

class ConsoleLogger  : Logger   { override fun log(message: String)    = println("[LOG]   $message") }
class FileLogger     : Logger   { override fun log(message: String)    = println("[FILE]  $message") }
class DatabaseLogger : Logger   { override fun log(message: String)    = println("[DB]    $message") }
class EmailNotifier  : Notifier { override fun notify(message: String) = println("[EMAIL] $message") }
class SmsNotifier    : Notifier { override fun notify(message: String) = println("[SMS]   $message") }

class UserService(logger: Logger)       : Logger by logger   { fun createUser(name: String) = log("Создан пользователь: $name") }
class OrderService(notifier: Notifier)  : Notifier by notifier { fun placeOrder(id: Int) = notify("Заказ #$id оформлен") }
class ProductService(logger: Logger)    : Logger by logger   { fun addProduct(name: String) = log("Добавлен товар: $name") }

// ==================== 14: Lazy + Singleton ====================

object ApiClient {
    val connection by lazy { println("HTTP клиент создан"); "https://api.example.com" }
    fun get(endpoint: String) = "GET $connection/$endpoint"
}

// ==================== 15: Система управления учебным порталом ====================

object PortalSettings {
    val portalName  = "Kotlin Academy"
    val maxCourses  = 50
    var maintenance = false
}

data class Course(val title: String, initialPrice: Double, initialSlots: Int) {
    var price: Double by Delegates.observable(initialPrice) { _, old, new ->
        println("[КУРС '$title'] Цена: $old → $new руб.")
    }
    var slots: Int by Delegates.vetoable(initialSlots) { _, _, new ->
        if (new < 1 || new > 100) { println("[КУРС '$title'] Отказ: мест $new (1–100)"); false } else { println("[КУРС '$title'] Мест: $new"); true }
    }
    companion object { fun demo() = Course("Демо-курс Kotlin", 0.0, 30) }
}

data class Student(val name: String, val role: String = "STUDENT") {
    companion object { fun guest() = Student("Гость", "GUEST") }
}

data class Teacher(val name: String, val role: String = "TEACHER") {
    companion object { fun admin() = Teacher("Главный преподаватель", "ADMIN") }
}

class PortalService(logger: Logger, notifier: Notifier) : Logger by logger, Notifier by notifier {
    val dbConnection by lazy { println("[ПОРТАЛ] Подключение к БД установлено"); "jdbc:postgresql://localhost/portal" }
    fun enroll(student: Student, course: Course) {
        log("${student.name} записан на '${course.title}'")
        notify("Вы записаны на курс '${course.title}'")
    }
}

// ==================== Бонус: Мониторинг серверов ====================

object ServerStatistics {
    val servers = mutableListOf<Server>()
    fun register(s: Server) = servers.add(s)
    fun printSummary() {
        println("\n=== Статистика серверов ===")
        servers.forEach { println("  ${it.name}: статус=${it.status}, CPU=${it.cpuLoad}%, RAM=${it.memoryLoad}%") }
        println("Всего: ${servers.size}")
    }
}

data class Server(val name: String) {
    var status: String by Delegates.observable("offline") { _, old, new -> println("[$name] Статус: $old → $new") }
    var cpuLoad: Int by Delegates.vetoable(0) { _, _, new ->
        if (new < 0 || new > 100) { println("[$name] Отказ CPU: $new%"); false } else true
    }
    var memoryLoad: Int by Delegates.vetoable(0) { _, _, new ->
        if (new < 0 || new > 100) { println("[$name] Отказ RAM: $new%"); false } else true
    }
}

class MonitoringService(logger: Logger) : Logger by logger {
    val metricsDb by lazy { println("[МОНИТОРИНГ] База метрик подключена"); "metrics-db" }
    fun check(server: Server) = log("Проверка ${server.name}: CPU=${server.cpuLoad}%")
}

// ==================== MAIN ====================

fun main() {

    // 1
    println("=== 1: Ленивое подключение к БД ===")
    val database by lazy { DatabaseConnection() }
    println("Объект ещё не создан")
    println(database)
    println()

    // 2
    println("=== 2: Ленивое формирование отчёта ===")
    val report by lazy { Report() }
    println("Без обращения — отчёт не создан")
    println(report.content)
    println()

    // 3
    println("=== 3: Контроль изменения цены товара ===")
    val product = Product("Телевизор", 50000.0)
    product.price = 48000.0
    product.price = 52000.0
    product.price = 47500.0
    println()

    // 4
    println("=== 4: Контроль возраста сотрудника ===")
    val employee = Employee("Иванов", 30)
    employee.age = 25
    employee.age = 15
    employee.age = 85
    employee.age = 45
    println("Текущий возраст: ${employee.age}")
    println()

    // 5
    println("=== 5: Контроль скидки ===")
    val discount = Discount(10)
    discount.percent = 50
    discount.percent = -5
    discount.percent = 95
    discount.percent = 90
    println("Текущая скидка: ${discount.percent}%")
    println()

    // 6
    println("=== 6: Singleton настроек ===")
    println("${AppSettings.appName} v${AppSettings.version}, тема: ${AppSettings.theme}")
    AppSettings.theme = "Dark"
    println("Тема изменена: ${AppSettings.theme}")
    println()

    // 7
    println("=== 7: Счётчик пользователей ===")
    UserStatistics.registerUser("Алиса")
    UserStatistics.registerUser("Борис")
    UserStatistics.registerUser("Виктор")
    println("Итого: ${UserStatistics.totalUsers()}")
    println()

    // 8
    println("=== 8: Companion Object — создание пользователей ===")
    println(User.createGuest())
    println(User.createAdmin())
    println(User("Мария", "USER"))
    println()

    // 9
    println("=== 9: Тестовые данные через companion object ===")
    val products = listOf(Product.demoLaptop(), Product.demoPhone(), Product.demoTablet())
    products.forEach { println("${it.name}: ${it.price} руб.") }
    println()

    // 10
    println("=== 10: Анонимный объект ===")
    val settings = object {
        val theme    = "Dark"
        val fontSize = 16
        val language = "ru"
    }
    println("Тема: ${settings.theme}, шрифт: ${settings.fontSize}, язык: ${settings.language}")
    println()

    // 11
    println("=== 11: Делегирование логирования ===")
    val userService = UserService(ConsoleLogger())
    userService.createUser("Анна")
    userService.log("Сервис работает")
    println()

    // 12
    println("=== 12: Делегирование уведомлений ===")
    OrderService(EmailNotifier()).placeOrder(101)
    OrderService(SmsNotifier()).placeOrder(102)
    println()

    // 13
    println("=== 13: Несколько реализаций логирования ===")
    listOf(ConsoleLogger(), FileLogger(), DatabaseLogger()).forEach {
        ProductService(it).addProduct("Ноутбук Pro")
    }
    println()

    // 14
    println("=== 14: Lazy + Singleton ===")
    println("ApiClient создан, соединение ещё не установлено")
    println(ApiClient.get("users"))
    println(ApiClient.get("courses"))
    println()

    // 15
    println("=== 15: Учебный портал ===")
    println("Портал: ${PortalSettings.portalName}, макс. курсов: ${PortalSettings.maxCourses}")

    val tempConfig = object { val sessionTimeout = 30; val debugMode = true }
    println("Сессия: ${tempConfig.sessionTimeout} мин, debug: ${tempConfig.debugMode}")

    val course  = Course.demo()
    val guest   = Student.guest()
    val teacher = Teacher.admin()
    println(teacher)

    course.price = 2500.0
    course.price = 3000.0
    course.slots = 50
    course.slots = 0
    course.slots = 150

    val portal = PortalService(ConsoleLogger(), EmailNotifier())
    println("БД: ${portal.dbConnection}")
    portal.enroll(guest, course)
    portal.enroll(Student("Ольга"), course)

    val allCourses = listOf(course, Course("Kotlin Advanced", 5000.0, 20))
    println("\nКурсы:")
    allCourses.forEach { println("  - ${it.title}: ${it.price} руб., мест: ${it.slots}") }
    println()

    // Бонус
    println("=== Бонус: Мониторинг серверов ===")
    val s1 = Server("web-01"); val s2 = Server("db-01")
    ServerStatistics.register(s1); ServerStatistics.register(s2)
    s1.status = "online"; s1.cpuLoad = 45; s1.memoryLoad = 60; s1.cpuLoad = 110
    s2.status = "online"; s2.cpuLoad = 80; s2.memoryLoad = 105; s2.memoryLoad = 95
    val monitor = MonitoringService(ConsoleLogger())
    println("База: ${monitor.metricsDb}")
    monitor.check(s1); monitor.check(s2)
    ServerStatistics.printSummary()
}
