```kotlin
// ============================================================
// Самостоятельная работа №5
// Generics, коллекции и безопасная работа с типами в Kotlin
// ============================================================

// ─────────────────── МОДЕЛИ ───────────────────

data class User(val name: String, val age: Int)
data class Product(val name: String, val price: Double)
data class Course(val title: String, val isActive: Boolean)
data class Student(val name: String, val grades: List<Int>)
data class Teacher(val name: String, val subject: String)

// ─────────────────── ЗАДАНИЕ 1 — Box<T> ───────────────────

class Box<T>(private var value: T) {
    fun getValue(): T = value
    fun setValue(newValue: T) { value = newValue }
    override fun toString() = "Box($value)"
}

// ─────────────────── ЗАДАНИЕ 2 — Catalog<T> ───────────────────

class Catalog<T> {
    private val items = mutableListOf<T>()
    fun add(item: T) { items.add(item) }
    fun remove(item: T) { items.remove(item) }
    fun getAll(): List<T> = items.toList()
    fun find(condition: (T) -> Boolean): T? = items.firstOrNull(condition)
    fun count(): Int = items.size
}

// ─────────────────── ЗАДАНИЕ 3 — Generic Repository ───────────────────

interface Repository<T> {
    fun add(item: T)
    fun remove(item: T)
    fun update(old: T, new: T)
    fun getAll(): List<T>
    fun find(condition: (T) -> Boolean): T?
}

class MemoryRepository<T> : Repository<T> {
    private val storage = mutableListOf<T>()
    override fun add(item: T) { storage.add(item) }
    override fun remove(item: T) { storage.remove(item) }
    override fun update(old: T, new: T) {
        val index = storage.indexOf(old)
        if (index != -1) storage[index] = new
    }
    override fun getAll(): List<T> = storage.toList()
    override fun find(condition: (T) -> Boolean): T? = storage.firstOrNull(condition)
}

// ─────────────────── ЗАДАНИЕ 4 — findFirst<T> ───────────────────

fun <T> findFirst(items: List<T>, condition: (T) -> Boolean): T? =
    items.firstOrNull(condition)

// ─────────────────── ЗАДАНИЕ 5 — printCollection<T> ───────────────────

fun <T> printCollection(items: List<T>) {
    println("Коллекция [${items.size} элем.]:")
    items.forEachIndexed { i, item -> println("  $i: $item") }
}

// ─────────────────── ЗАДАНИЕ 6 — Ограничения типов <T : Number> ───────────────────

fun <T : Number> averageValue(items: List<T>): Double =
    items.fold(0.0) { acc, v -> acc + v.toDouble() } / items.size

fun <T : Number> maxValue(items: List<T>): Double =
    items.fold(Double.MIN_VALUE) { acc, v -> maxOf(acc, v.toDouble()) }

fun <T : Number> minValue(items: List<T>): Double =
    items.fold(Double.MAX_VALUE) { acc, v -> minOf(acc, v.toDouble()) }

// ─────────────────── ЗАДАНИЕ 7 — Generic Extension Functions ───────────────────

fun <T> List<T>.printAll() =
    println("printAll [${size}]: ${joinToString()}")

fun <T> List<T>.secondOrNull(): T? = if (size >= 2) this[1] else null

fun <T> List<T>.lastOrDefault(defaultValue: T): T = lastOrNull() ?: defaultValue

// ─────────────────── ЗАДАНИЕ 8 — Sealed Result<T> ───────────────────

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val message: String) : Result<T>()
    class Loading<T> : Result<T>()
}

fun getUser(id: Int): Result<User> = when {
    id == 1 -> Result.Success(User("Иван Петров", 28))
    id <= 0 -> Result.Error("Некорректный id: $id")
    else    -> Result.Error("Пользователь $id не найден")
}

fun getProducts(): Result<List<Product>> =
    Result.Success(listOf(Product("Планшет", 30000.0), Product("Ручка", 50.0)))

// ─────────────────── ЗАДАНИЕ 9 — Cache<T> ───────────────────

class Cache<T> {
    private val storage = mutableMapOf<String, T>()
    fun save(key: String, value: T) { storage[key] = value }
    fun get(key: String): T? = storage[key]
    fun clear() { storage.clear() }
    fun isEmpty() = storage.isEmpty()
    override fun toString() = "Cache($storage)"
}

// ─────────────────── ЗАДАНИЕ 10 — Система оценок ───────────────────

fun <T : Number> calculateAverage(values: List<T>): Double =
    values.fold(0.0) { acc, v -> acc + v.toDouble() } / values.size

// ─────────────────── ЗАДАНИЕ 11 — countMatching<T> ───────────────────

fun <T> countMatching(items: List<T>, condition: (T) -> Boolean): Int =
    items.count(condition)

// ─────────────────── ЗАДАНИЕ 12 — PairBox<K, V> ───────────────────

data class PairBox<K, V>(val key: K, val value: V) {
    override fun toString() = "$key → $value"
}

// ─────────────────── ЗАДАНИЕ 13 — List<*> Star Projection ───────────────────

fun printSize(items: List<*>) {
    println("Размер: ${items.size}")
    if (items.isNotEmpty()) {
        val first = items.first()
        val type = when (first) {
            is String  -> "String"
            is Int     -> "Int"
            is Double  -> "Double"
            is User    -> "User"
            else       -> first?.let { it::class.simpleName } ?: "null"
        }
        println("Тип первого элемента: $type (значение: $first)")
    }
}

// ─────────────────── ЗАДАНИЕ 14 — Ограничения через интерфейс ───────────────────

interface Printable {
    fun printInfo()
}

data class PrintableUser(val name: String, val age: Int) : Printable {
    override fun printInfo() = println("Пользователь: $name, возраст: $age")
}

data class PrintableProduct(val name: String, val price: Double) : Printable {
    override fun printInfo() = println("Товар: $name, цена: ${price}₽")
}

data class PrintableCourse(val title: String, val isActive: Boolean) : Printable {
    override fun printInfo() = println("Курс: $title, статус: ${if (isActive) "активен" else "неактивен"}")
}

fun <T : Printable> printObject(item: T) = item.printInfo()

// ─────────────────── ЗАДАНИЕ 15 — Учебный портал ───────────────────

fun <T> List<T>.filterAndPrint(label: String, condition: (T) -> Boolean): List<T> {
    val filtered = filter(condition)
    println("$label: $filtered")
    return filtered
}

fun <T : Printable> List<T>.printAllInfo() = forEach { it.printInfo() }

fun <T> List<T>.summarize(label: String) = println("$label: всего $size записей")

fun <T : Number> List<T>.numSum(): Double =
    fold(0.0) { acc, v -> acc + v.toDouble() }

fun <T : Number> List<T>.numRange(): Double {
    val max = fold(Double.MIN_VALUE) { acc, v -> maxOf(acc, v.toDouble()) }
    val min = fold(Double.MAX_VALUE) { acc, v -> minOf(acc, v.toDouble()) }
    return max - min
}

// ─────────────────── ЗАДАНИЕ ПОВЫШЕННОЙ СЛОЖНОСТИ — DataManager<T> ───────────────────

class DataManager<T> {
    private val data = mutableListOf<T>()
    fun add(item: T) { data.add(item) }
    fun remove(item: T) { data.remove(item) }
    fun find(condition: (T) -> Boolean): List<T> = data.filter(condition)
    fun sort(comparator: Comparator<T>): List<T> = data.sortedWith(comparator)
    fun filter(condition: (T) -> Boolean): List<T> = data.filter(condition)
    fun count(condition: (T) -> Boolean = { true }): Int = data.count(condition)
    fun getAll(): List<T> = data.toList()
}

// ═════════════════════════════════════════════
//                     MAIN
// ═════════════════════════════════════════════

fun main() {

    // ── 1 ──
    println("=== Задание 1: Box<T> ===")
    val intBox = Box(42)
    val stringBox = Box("Привет")
    val doubleBox = Box(3.14)
    val userBox = Box(User("Алексей", 22))
    println(intBox)
    println(stringBox)
    println(doubleBox)
    println(userBox)
    intBox.setValue(100)
    println("После setValue: $intBox")

    // ── 2 ──
    println("\n=== Задание 2: Catalog<T> ===")
    val userCatalog = Catalog<User>().apply {
        add(User("Анна", 20)); add(User("Иван", 25)); add(User("Мария", 17))
    }
    println("Пользователи (${userCatalog.count()}): ${userCatalog.getAll()}")
    println("Найден: ${userCatalog.find { it.name == "Иван" }}")
    val productCatalog = Catalog<Product>().apply {
        add(Product("Ноутбук", 75000.0)); add(Product("Мышь", 1500.0))
    }
    println("Товаров: ${productCatalog.count()}")
    val courseCatalog = Catalog<Course>().apply {
        add(Course("Kotlin", true)); add(Course("Java", false))
    }
    println("Курсов: ${courseCatalog.count()}")

    // ── 3 ──
    println("\n=== Задание 3: Generic Repository ===")
    val userRepo = MemoryRepository<User>().apply {
        add(User("Сергей", 30)); add(User("Ольга", 28))
    }
    println(userRepo.getAll())
    userRepo.update(User("Сергей", 30), User("Сергей", 31))
    println("После update: ${userRepo.getAll()}")
    val productRepo = MemoryRepository<Product>().apply {
        add(Product("Клавиатура", 3500.0)); add(Product("Наушники", 8000.0))
    }
    println("Товары: ${productRepo.getAll()}")
    val courseRepo = MemoryRepository<Course>().apply {
        add(Course("Android", true)); add(Course("iOS", true))
    }
    println("Курсы: ${courseRepo.getAll()}")

    // ── 4 ──
    println("\n=== Задание 4: findFirst<T> ===")
    val users = listOf(User("Анна", 20), User("Борис", 35), User("Вера", 22))
    println(findFirst(users) { it.name == "Борис" })
    val products = listOf(Product("Телефон", 50000.0), Product("Чехол", 500.0))
    println(findFirst(products) { it.price < 1000.0 })
    val courses = listOf(Course("Kotlin", true), Course("C++", false))
    println(findFirst(courses) { it.title == "Kotlin" })

    // ── 5 ──
    println("\n=== Задание 5: printCollection<T> ===")
    printCollection(listOf(1, 2, 3, 4, 5))
    printCollection(listOf("Kotlin", "Java", "Swift"))
    printCollection(listOf(User("Дима", 24), User("Лена", 19)))
    printCollection(listOf(Product("Диск", 300.0), Product("Кабель", 150.0)))

    // ── 6 ──
    println("\n=== Задание 6: Ограничения типов <T : Number> ===")
    val ints = listOf(10, 20, 30, 40, 50)
    println("Int — среднее: ${averageValue(ints)}, макс: ${maxValue(ints)}, мин: ${minValue(ints)}")
    val doubles = listOf(1.5, 2.7, 3.3, 4.0)
    println("Double — среднее: ${averageValue(doubles)}, макс: ${maxValue(doubles)}, мин: ${minValue(doubles)}")
    val floats = listOf(5.1f, 2.2f, 8.9f)
    println("Float — среднее: ${averageValue(floats)}, макс: ${maxValue(floats)}, мин: ${minValue(floats)}")

    // ── 7 ──
    println("\n=== Задание 7: Generic Extension Functions ===")
    val numbers = listOf(10, 20, 30)
    numbers.printAll()
    println("Второй: ${numbers.secondOrNull()}")
    println("Последний: ${numbers.lastOrDefault(0)}")
    val emptyList = listOf<String>()
    println("Второй из пустого: ${emptyList.secondOrNull()}")
    println("Последний из пустого: ${emptyList.lastOrDefault("нет данных")}")
    val userList = listOf(User("А", 1), User("Б", 2), User("В", 3))
    userList.printAll()
    println("Второй: ${userList.secondOrNull()}")

    // ── 8 ──
    println("\n=== Задание 8: Sealed Result<T> ===")
    for (id in listOf(1, 2, -1)) {
        when (val r = getUser(id)) {
            is Result.Success -> println("Пользователь: ${r.data}")
            is Result.Error   -> println("Ошибка: ${r.message}")
            is Result.Loading -> println("Загрузка...")
        }
    }
    when (val r = getProducts()) {
        is Result.Success -> println("Товары: ${r.data}")
        is Result.Error   -> println("Ошибка: ${r.message}")
        is Result.Loading -> println("Загрузка...")
    }

    // ── 9 ──
    println("\n=== Задание 9: Cache<T> ===")
    val userCache = Cache<User>().apply {
        save("user1", User("Катя", 23)); save("user2", User("Пётр", 31))
    }
    println(userCache)
    println("user1: ${userCache.get("user1")}")
    val productCache = Cache<List<Product>>().apply {
        save("catalog", listOf(Product("Зарядка", 700.0), Product("Провод", 200.0)))
    }
    println(productCache.get("catalog"))
    val courseCache = Cache<List<Course>>().apply {
        save("active", listOf(Course("Kotlin", true)))
    }
    println("isEmpty: ${courseCache.isEmpty()}")
    courseCache.clear()
    println("isEmpty после clear: ${courseCache.isEmpty()}")

    
    println("\n=== Задание 10: Система оценок студентов ===")
    val students = listOf(
        Student("Алина", listOf(5, 4, 5, 3, 4)),
        Student("Денис", listOf(3, 3, 4, 5, 3)),
        Student("Юля",   listOf(5, 5, 5, 4, 5))
    )
    for (s in students) {
        println("${s.name}:")
        println("  Средний балл: ${"%.2f".format(calculateAverage(s.grades))}")
        println("  Максимальная оценка: ${s.grades.max()}")
        println("  Минимальная оценка: ${s.grades.min()}")
    }

    
    println("\n=== Задание 11: countMatching<T> ===")
    val users11 = listOf(User("А", 16), User("Б", 18), User("В", 25), User("Г", 14))
    println("Совершеннолетних: ${countMatching(users11) { it.age >= 18 }}")
    val products11 = listOf(Product("X", 500.0), Product("Y", 15000.0), Product("Z", 3000.0))
    println("Дорогих (>5000₽): ${countMatching(products11) { it.price > 5000.0 }}")
    val courses11 = listOf(Course("K1", true), Course("K2", false), Course("K3", true))
    println("Активных курсов: ${countMatching(courses11) { it.isActive }}")

    
    println("\n=== Задание 12: PairBox<K, V> ===")
    println(PairBox(1, User("Николай", 29)))
    println(PairBox("ноутбук", 89000.0))
    println(PairBox("KT-001", Course("Kotlin Advanced", true)))

    // ── 13 ──
    println("\n=== Задание 13: List<*> Star Projection ===")
    printSize(listOf("Kotlin", "Java", "Swift"))
    printSize(listOf(1, 2, 3))
    printSize(listOf(User("Оля", 22), User("Рома", 27)))

    
    println("\n=== Задание 14: Ограничения через интерфейс Printable ===")
    printObject(PrintableUser("Светлана", 26))
    printObject(PrintableProduct("Планшет", 45000.0))
    printObject(PrintableCourse("Kotlin Basics", true))

  
    println("\n=== Задание 15: Учебный портал ===")
    val studentRepo = MemoryRepository<Student>().apply {
        add(Student("Алекс", listOf(5, 4, 4)))
        add(Student("Белла", listOf(3, 5, 5)))
        add(Student("Стас",  listOf(4, 4, 3)))
    }
    val courseRepo15 = MemoryRepository<Course>().apply {
        add(Course("Kotlin", true)); add(Course("Android", true)); add(Course("Архивный", false))
    }
    val cache15 = Cache<List<Student>>()
    cache15.save("all", studentRepo.getAll())
    println("Из кеша: ${cache15.get("all")?.map { it.name }}")
    when (val r = Result.Success(courseRepo15.getAll())) {
        is Result.Success -> r.data.filterAndPrint("Активные курсы") { it.isActive }
        is Result.Error   -> println("Ошибка: ${r.message}")
        is Result.Loading -> println("Загрузка...")
    }
    studentRepo.getAll().summarize("Студенты")
    studentRepo.getAll().filterAndPrint("Средний балл ≥ 4") { calculateAverage(it.grades) >= 4.0 }
    val allGrades = studentRepo.getAll().flatMap { it.grades }
    println("Сумма оценок: ${allGrades.numSum()}")
    println("Размах оценок: ${allGrades.numRange()}")

    // ── ПОВЫШЕННАЯ СЛОЖНОСТЬ ──
    println("\n=== Задание повышенной сложности: DataManager<T> ===")
    val userManager = DataManager<User>().apply {
        add(User("Женя", 19)); add(User("Зоя", 34))
        add(User("Артём", 22)); add(User("Надя", 17))
    }
    println("Все: ${userManager.getAll()}")
    println("Совершеннолетних: ${userManager.count { it.age >= 18 }}")
    println("Возраст ≥ 20: ${userManager.filter { it.age >= 20 }}")
    println("По имени: ${userManager.sort(compareBy { it.name })}")
    val productManager = DataManager<Product>().apply {
        add(Product("Принтер", 12000.0)); add(Product("Бумага", 350.0)); add(Product("Картридж", 2500.0))
    }
    println("Дороже 1000₽: ${productManager.filter { it.price > 1000.0 }}")
    println("По цене: ${productManager.sort(compareBy { it.price })}")
    val courseManager = DataManager<Course>().apply {
        add(Course("Flutter", true)); add(Course("React Native", false)); add(Course("KMM", true))
    }
    println("Активных: ${courseManager.count { it.isActive }}")
    println("Найти активные: ${courseManager.find { it.isActive }}")
}
