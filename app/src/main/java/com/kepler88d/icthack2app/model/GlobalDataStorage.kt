package com.kepler88d.icthack2app.model

class GlobalDataStorage {
    companion object {
        val itTreeMap: Map<String, List<String>> = mapOf(
            "Frontend-разработка" to listOf("React", "Vue", "Angular", "Flutter for web"),
            "Backend-разработка" to listOf("Flask", "Ktor", "Spring", "Django"),
            "Мобильная разработка" to listOf("Android", "IOS", "Flutter", "React Native"),
            "Data Science" to listOf("Машинное обучение", "Data-аналитика", "Big Data"),
            "Игровая разработка" to listOf(
                "Game Developer",
                "Геймдизайнер",
                "Игровой художник",
                "Балансер",
                "QA"
            ),
            "Языки программирования" to listOf(
                "Python",
                "Kotlin",
                "Java",
                "C#",
                "C++",
                "Clojure",
                "Haskell",
                "Go"
            ),
            "Дизайн" to listOf("Web-дизайн", "UI/UX", "Промышленный дизайн")
        )

        val randomSplashScreenText: List<String> = listOf(
            "Приложение, заслуженно занявшее предпоследнее место на хакатоне ICTHack#2"
        )
    }
}