# ICTHack2App
Это платформа для удобного поиска проектов от клуба InfoLab

## Ключевой функционал 
✅ Создание аккаунта 

✅ Аккаунт содержит информацию о пользователе, телеграмм-аккаунте, гитхаб-аккаунте

✅ Выбор специализации пользователя

✅ Поиск проектов по тегам

✅ Прогрессивный UI/UX

✅ Возможность принимать/отправлять заявки пользователей/проектам

✅ Создание проекта для доски объявлений

✅ Указание необходимых специалистов для проекта

## Структура приложения 

```├── AndroidManifest.xml
├── ic_launcher-playstore.png
├── java
│   └── com
│       └── kepler88d
│           └── icthack2app
│               ├── activities
│               │   ├── MainActivity.kt
│               │   ├── SplashScreenActivity.kt
│               │   └── StartActivity.kt
│               ├── adapters
│               │   ├── RecyclerViewCheckboxAdapter.kt
│               │   ├── RecyclerViewMainAdapter.kt
│               │   └── RecyclerViewRepliesAdapter.kt
│               ├── fragments
│               │   ├── CreateProjectFragment.kt
│               │   ├── MainFragment.kt
│               │   ├── NotificationsFragment.kt
│               │   ├── SearchFragment.kt
│               │   └── UserPageFragment.kt
│               └── model
│                   ├── data
│                   │   ├── Project.kt
│                   │   ├── Reply.kt
│                   │   └── User.kt
│                   ├── enumerations
│                   │   ├── ProjectTags.kt
│                   │   └── UserSpecialization.kt
│                   ├── GlobalDataStorage.kt
│                   └── RequestWorker.kt
└── res
    ├── drawable
    │   ├── ic_baseline_add_24.xml
    │   ├── ic_baseline_arrow_back_24.xml
    │   ├── ic_baseline_close_24.xml
    │   ├── ic_baseline_cloud_24.xml
    │   ├── ic_baseline_comment_24.xml
    │   ├── ic_baseline_edit_24.xml
    │   ├── ic_baseline_lock_24.xml
    │   ├── ic_baseline_notifications_none_24.xml
    │   ├── ic_baseline_person_24.xml
    │   ├── ic_baseline_person_outline_24.xml
    │   ├── ic_baseline_reply_24.xml
    │   ├── ic_baseline_search_24.xml
    │   ├── ic_baseline_touch_app_24.xml
    │   ├── ic_launcher_background.xml
    │   ├── ic_logo_github.xml
    │   ├── rounded_bottom_sheet.xml
    │   ├── rounded_textview.xml
    │   └── rounded_view_header.xml
    ├── drawable-v24
    │   └── ic_launcher_foreground.xml
    ├── font
    │   ├── roboto_light.xml
    │   ├── roboto_medium.xml
    │   └── roboto_mono.xml
    ├── layout
    │   ├── activity_main.xml
    │   ├── activity_splash_screen.xml
    │   ├── activity_start.xml
    │   ├── fragment_main.xml
    │   ├── fragment_notifications.xml
    │   ├── item_checkbox_group.xml
    │   ├── item_checkbox_satellite.xml
    │   ├── item_checkbox.xml
    │   ├── item_project.xml
    │   ├── item_reply.xml
    │   ├── items
    │   │   └── project_item.xml
    │   ├── item_vacancy_locked.xml
    │   ├── item_vacancy.xml
    │   ├── layout_bottom_sheet_person.xml
    │   ├── screen_add_project.xml
    │   ├── screen_main.xml
    │   ├── screen_profile.xml
    │   ├── screen_project.xml
    │   ├── screen_responses.xml
    │   └── search_box.xml
    ├── mipmap-anydpi-v26
    │   ├── ic_launcher_round.xml
    │   └── ic_launcher.xml
    ├── mipmap-hdpi
    │   ├── ic_launcher_foreground.png
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-mdpi
    │   ├── ic_launcher_foreground.png
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-xhdpi
    │   ├── ic_launcher_foreground.png
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-xxhdpi
    │   ├── ic_launcher_foreground.png
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── mipmap-xxxhdpi
    │   ├── ic_launcher_foreground.png
    │   ├── ic_launcher.png
    │   └── ic_launcher_round.png
    ├── values
    │   ├── colors.xml
    │   ├── font_certs.xml
    │   ├── preloaded_fonts.xml
    │   ├── strings.xml
    │   └── themes.xml
    └── values-night
        └── themes.xml```
