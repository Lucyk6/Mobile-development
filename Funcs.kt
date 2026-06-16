```kotlin
// ============================================================
// Самостоятельная работа №4
// Extension Functions, Scope Functions и Idiomatic Kotlin
// ============================================================

// ─────────────────── МОДЕЛИ ───────────────────

data class User(
    var firstName: String,
    var lastName: String,
    var age: Int,
    var email: String?,
    var phone: String?
)

data class Movie(
    val title: String,
    val rating: Double,
    val genre: String,
    val duration: Int
)

data class Student(
    val name: String,
    val email: String?,
    val group: String?,
    val phone: String?
)

data class Product(
    val name: String,
    val price: Double?,
    val category: String
)

// ─────────────────── ЗАДАНИЕ 1 — Extension Functions для User ───────────────────

fun User.fullName(): String = "$firstName $lastName"

fun String?.orNotSpecified(): String = this ?: "Не указано"

fun Int.isAdult(): Boolean = this >= 18

fun User.getShortInfo(): String = "${fullName()} ($age лет)"

fun String.normalizeName(): String = trim().split("\\s+".toRegex()).joinToString(" ") { word ->
    word.lowercase().replaceFirstChar { it.uppercase() }
}

// ─────────────────── ЗАДАНИЕ 2 — Extension Functions для Movie ───────────────────

fun Movie.toCardText(): String =
    "🎬 $title | ${rating.toRatingText()} | $genre | $duration мин."

fun Double.toRatingText(): String = "$this / 10"

fun List<Movie>.topRated(): List<Movie> = filter { it.rating > 8.0 }

fun List<Movie>.shortMovies(): List<Movie> = filter { it.duration < 100 }

// ─────────────────── ЗАДАНИЕ 7 — Мини-библиотека расширений ───────────────────

fun String.shorten(maxLength: Int = 20): String =
    if (length <= maxLength) this else "${take(maxLength)}..."

fun String.isEmailValid(): Boolean =
    matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))

fun Double?.toPriceText(): String =
    if (this == null) "Цена не указана" else "${"%.2f".format(this)} ₽"

fun List<Int>.averageValue(): Double =
    if (isEmpty()) 0.0 else fold(0.0) { acc, v -> acc + v } / size

fun Int.square(): Int = this * this

// ─────────────────── ЗАДАНИЕ 9 — Extension Functions для Product ───────────────────

fun Product.toCardText(): String =
    "🛒 $name | ${price.toPriceText()} | $category"

fun List<Product>.expensiveProducts(): List<Product> =
    filter { (it.price ?: 0.0) > 10000.0 }

// ─────────────────── ЗАДАНИЕ ПОВЫШЕННОЙ СЛОЖНОСТИ — Extensions для коллекций ───────────────────

fun List<Product>.averagePrice(): Double {
    val withPrice = filter { it.price != null }
    return if (withPrice.isEmpty()) 0.0
    else withPrice.fold(0.0) { acc, p -> acc + p.price!! } / withPrice.size
}

fun List<Movie>.averageRating(): Double =
    if (isEmpty()) 0.0 else fold(0.0) { acc, m -> acc + m.rating } / size

fun List<User>.adultsOnly(): List<User> = filter { it.age.isAdult() }

// ═════════════════════════════════════════════
//                     MAIN
// ═════════════════════════════════════════════

fun main() {

    //1
    println("=== Задание 1: Система профилей пользователей ===")

    val user1 = User("Иван", "Петров", 20, "ivan@mail.ru", "+79001112233")
    val user2 = User("  анна  ", "сидорова  ", 17, null, "+79002223344")
    val user3 = User("Сергей", "Козлов", 35, "sergey@gmail.com", null)
    val user4 = User("Мария", "Новикова", 28, null, null)
    val user5 = User("Дмитрий", "Волков", 15, "dima@yandex.ru", "+79003334455")

    user2.firstName = user2.firstName.normalizeName()
    user2.lastName  = user2.lastName.normalizeName()

    val allUsers = listOf(user1, user2, user3, user4, user5)

    println("\nВсе пользователи:")
    allUsers.forEach { u ->
        println("${u.fullName()} | возраст: ${u.age} | email: ${u.email.orNotSpecified()} | тел: ${u.phone.orNotSpecified()}")
    }

    println("\nТолько совершеннолетние:")
    allUsers.filter { it.age.isAdult() }.forEach { println(it.getShortInfo()) }

    println("\nБез email:")
    allUsers.filter { it.email == null }.forEach { println(it.getShortInfo()) }

    println("\nБез телефона:")
    allUsers.filter { it.phone == null }.forEach { println(it.getShortInfo()) }

    //2
    println("\n=== Задание 2: Каталог фильмов ===")

    val movies = listOf(
        Movie("Интерстеллар",    8.6, "Фантастика",  169),
        Movie("Тёмный рыцарь",   9.0, "Боевик",      152),
        Movie("Начало",          8.8, "Триллер",      148),
        Movie("Побег из Шоушенка", 9.3, "Драма",     142),
        Movie("Криминальное чтиво", 8.9, "Криминал", 154),
        Movie("Форрест Гамп",    8.8, "Драма",        142),
        Movie("Матрица",         8.7, "Фантастика",   136),
        Movie("Бойцовский клуб", 8.8, "Драма",        139),
        Movie("Леон",            8.5, "Боевик",       110),
        Movie("Малыш на драйве", 7.6, "Криминал",     113),
        Movie("Малышка на миллион", 8.1, "Драма",     133),
        Movie("Птичий короб",    6.6, "Ужасы",        124)
    )

    println("\nЛучшие фильмы (рейтинг > 8):")
    movies.topRated().forEach { println(it.toCardText()) }

    println("\nКороткие фильмы (< 100 мин):")
    val short = movies.shortMovies()
    if (short.isEmpty()) println("  (нет фильмов короче 100 минут)")
    else short.forEach { println(it.toCardText()) }

    println("\nКарточки всех фильмов:")
    movies.forEach { println(it.toCardText()) }

    //3
    println("\n=== Задание 3: apply + also ===")

    val u3_1 = User("", "", 0, null, null).apply {
        firstName = "Алексей"
        lastName  = "Смирнов"
        age       = 22
        email     = "alex@mail.ru"
        phone     = "+79001234567"
    }.also { println("Пользователь ${it.firstName} успешно создан") }

    val u3_2 = User("", "", 0, null, null).apply {
        firstName = "Елена"
        lastName  = "Кузнецова"
        age       = 19
        email     = null
        phone     = "+79007654321"
    }.also { println("Пользователь ${it.firstName} успешно создан") }

    val u3_3 = User("", "", 0, null, null).apply {
        firstName = "Павел"
        lastName  = "Морозов"
        age       = 31
        email     = "pavel@gmail.com"
        phone     = null
    }.also { println("Пользователь ${it.firstName} успешно создан") }

    val u3_4 = User("", "", 0, null, null).apply {
        firstName = "Ольга"
        lastName  = "Фёдорова"
        age       = 25
        email     = "olga@yandex.ru"
        phone     = "+79009876543"
    }.also { println("Пользователь ${it.firstName} успешно создан") }

    val u3_5 = User("", "", 0, null, null).apply {
        firstName = "Николай"
        lastName  = "Попов"
        age       = 16
        email     = null
        phone     = null
    }.also { println("Пользователь ${it.firstName} успешно создан") }

    val users3 = listOf(u3_1, u3_2, u3_3, u3_4, u3_5)

    //4
    println("\n=== Задание 4: let — поиск пользователя по email ===")

    users3.find { it.email == "alex@mail.ru" }
        ?.let { println("Найден: ${it.fullName()}, возраст: ${it.age}") }
        ?: println("Пользователь не найден")

    users3.find { it.email == "unknown@test.com" }
        ?.let { println("Найден: ${it.fullName()}") }
        ?: println("Пользователь не найден")

    //5
    println("\n=== Задание 5: run — отчёт по каталогу фильмов ===")

    movies.run {
        val avgRating = fold(0.0) { acc, m -> acc + m.rating } / size
        val maxRating = maxByOrNull { it.rating }
        val minRating = minByOrNull { it.rating }
        println("Количество фильмов: $size")
        println("Средний рейтинг: ${"%.2f".format(avgRating)}")
        println("Максимальный рейтинг: ${maxRating?.rating} (${maxRating?.title})")
        println("Минимальный рейтинг: ${minRating?.rating} (${minRating?.title})")
    }

    //6
    println("\n=== Задание 6: with — вывод данных пользователя ===")

    with(u3_1) {
        println("Имя: ${fullName()}")
        println("Возраст: $age")
        println("Email: ${email.orNotSpecified()}")
        println("Телефон: ${phone.orNotSpecified()}")
    }

    //7
    println("\n=== Задание 7: Мини-библиотека расширений ===")

    println("shorten: ${"Очень длинное название фильма на экране".shorten()}")
    println("isEmailValid: ${"test@mail.ru".isEmailValid()}")
    println("isEmailValid (некорректный): ${"not-an-email".isEmailValid()}")
    println("toPriceText: ${15990.0.toPriceText()}")
    println("averageValue: ${listOf(5, 3, 4, 5, 2).averageValue()}")
    println("square: ${7.square()}")

    //8
    println("\n=== Задание 8: Nullable типы — Student ===")

    val students = listOf(
        Student("Алина Соколова",  "alina@mail.ru",  "ИТ-21",  "+79001111111"),
        Student("Денис Тихонов",   null,              "ИТ-22",  "+79002222222"),
        Student("Юлия Баранова",   "yulia@gmail.com", null,     null),
        Student("Кирилл Захаров",  null,              null,     "+79003333333"),
        Student("Вероника Ильина", "vera@yandex.ru",  "ИТ-21",  null)
    )

    students.forEach { s ->
        println("Студент: ${s.name}")
        println("  Email: ${s.email.orNotSpecified()}")
        println("  Группа: ${s.group.orNotSpecified()}")
        println("  Телефон: ${s.phone.orNotSpecified()}")
    }

    //9
    println("\n=== Задание 9: Каталог товаров ===")

    val products = listOf(
        Product("Ноутбук Apple MacBook",   95000.0,  "Электроника"),
        Product("Смартфон Samsung",        45000.0,  "Электроника"),
        Product("Наушники Sony",           12000.0,  "Электроника"),
        Product("Мышь Logitech",           2500.0,   "Периферия"),
        Product("Клавиатура механическая", 8000.0,   "Периферия"),
        Product("Монитор LG 27\"",         25000.0,  "Электроника"),
        Product("Стол компьютерный",       15000.0,  "Мебель"),
        Product("Кресло офисное",          18000.0,  "Мебель"),
        Product("Подставка для ноутбука",  null,     "Аксессуары"),
        Product("Коврик для мыши",         700.0,    "Аксессуары"),
        Product("USB-хаб",                 null,     "Периферия"),
        Product("Веб-камера Logitech",     6500.0,   "Периферия")
    )

    println("\nКарточки всех товаров:")
    products.forEach { println(it.toCardText()) }

    println("\nДорогие товары (> 10 000 ₽):")
    products.expensiveProducts().forEach { println(it.toCardText()) }

    println("\nТовары без цены:")
    products.filter { it.price == null }.forEach { println(it.toCardText()) }

    //10
    println("\n=== Задание 10: Комплексная система ===")

    // apply — создаём пользователя
    val mainUser = User("", "", 0, null, null).apply {
        firstName = "Артём"
        lastName  = "Беляев"
        age       = 27
        email     = "artem@mail.ru"
        phone     = "+79005556677"
    }

    // with — выводим профиль
    println("\n--- Профиль пользователя ---")
    with(mainUser) {
        println("Имя: ${fullName()}")
        println("Возраст: $age (совершеннолетний: ${age.isAdult()})")
        println("Email: ${email.orNotSpecified()}")
        println("Телефон: ${phone.orNotSpecified()}")
    }

    // let — поиск и обработка
    println("\n--- Поиск пользователя ---")
    users3.find { it.email == "olga@yandex.ru" }
        ?.let {
            println("Найден пользователь: ${it.getShortInfo()}")
            println("Email: ${it.email.orNotSpecified()}")
        } ?: println("Пользователь не найден")

    // run — отчёт по товарам
    println("\n--- Отчёт по товарам ---")
    products.run {
        println("Всего товаров: $size")
        println("Средняя цена: ${averagePrice().toPriceText()}")
        println("Дорогих товаров: ${expensiveProducts().size}")
        println("Без цены: ${count { it.price == null }}")
    }

    // also — логирование
    println("\n--- Топ фильмов с логом ---")
    movies.topRated()
        .also { println("Найдено топ-фильмов: ${it.size}") }
        .forEach { println(it.toCardText()) }

    // Повышенная сложность
    println("\n--- Статистика коллекций (повышенная сложность) ---")
    println("Средний рейтинг фильмов: ${"%.2f".format(movies.averageRating())}")
    println("Средняя цена товаров: ${products.averagePrice().toPriceText()}")
    println("Совершеннолетних пользователей: ${users3.adultsOnly().size}")
}
``` 
