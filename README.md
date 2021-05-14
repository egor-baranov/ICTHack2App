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

## Особенности

Большую часть ресурсов мы потратили на эффектный и "воздушный" дизайн, который может порадовать пользователя обилием неброских анимаций, чтобы максимально улучшить опыт использования.

## Скриншоты 

![image](https://user-images.githubusercontent.com/49813134/117571298-aacd4000-b0d6-11eb-8a55-b512c646ed07.png)


## Структура приложения 

```
├── activities                         
│   ├── MainActivity.kt                
│   ├── SplashScreenActivity.kt        
│   └── StartActivity.kt               
├── adapters                           
│   ├── RecyclerViewCheckboxAdapter.kt 
│   ├── RecyclerViewMainAdapter.kt     
│   └── RecyclerViewRepliesAdapter.kt  
├── fragments                          
│   ├── CreateProjectFragment.kt       
│   ├── MainFragment.kt                
│   ├── NotificationsFragment.kt       
│   ├── SearchFragment.kt              
│   └── UserPageFragment.kt            
└── model                              
    ├── data                           
    │   ├── Project.kt                 
    │   ├── Reply.kt                   
    │   └── User.kt                    
    ├── enumerations                   
    │   ├── ProjectTags.kt             
    │   └── UserSpecialization.kt      
    ├── GlobalDataStorage.kt           
    └── RequestWorker.kt               
```
## Link to the server repository
https://github.com/egor-baranov/ICTHack2Server
