SkyCast (Ваш ежедневный погодный компаньон)

SkyCast

SkyCast — это приложение на JavaFX, предназначенное для предоставления актуальной информации о погоде на основе местоположения пользователя. Используя OpenWeather API, SkyCast получает данные о погоде в режиме реального времени и представляет их в удобном интерфейсе. Пользователи могут получить доступ к основным показателям погоды, таким как состояние погоды, температура, влажность, скорость и направление ветра, а также к почасовому прогнозу, чтобы эффективно планировать свой день.

Возможности
Обновления погоды в реальном времени, включая состояние погоды, температуру, влажность, скорость и направление ветра.
Почасовой прогноз погоды с подробной информацией.
Отображение текущего города и страны пользователя на основе его IP-адреса.
Автоматическое обновление времени и даты в реальном времени.
Проверка подключения к сети перед запуском приложения.

Скриншот SkyCast
![image](https://github.com/Ksaers/SkyCast/assets/61120576/45c881cc-6b42-4190-8ed7-e2a130f02518)

Установка
Чтобы запустить SkyCast локально, выполните следующие шаги:

Клонируйте репозиторий:

bash
Копировать код
git clone https://github.com/yourusername/skycast.git
cd skycast
Откройте проект в предпочитаемой среде разработки (например, IntelliJ IDEA, Eclipse).

Добавьте библиотеку JavaFX в ваш проект:

Скачайте JavaFX SDK с Gluon.

Настройте JavaFX в вашей среде разработки:

Для IntelliJ IDEA:

Перейдите в File -> Project Structure -> Libraries и добавьте библиотеку JavaFX SDK.
Для Eclipse:

Щелкните правой кнопкой мыши на вашем проекте -> Build Path -> Configure Build Path и добавьте библиотеку JavaFX SDK.
Запустите приложение:

Выполните главный метод в классе SkyCastApplication.
Использование
Запустите приложение.
SkyCast проверит подключение к сети.
После успешного запуска будут отображены текущие данные о погоде и 3-часовой прогноз.
Приложение также покажет текущую дату и время, которые обновляются в реальном времени.
Конфигурация
Убедитесь, что у вас есть действительные API-ключи для OpenWeather и GeoLocation API. Замените ключи-заглушки в классе SkyCastController на ваши собственные ключи:

// OpenWeather API Key
String weatherApiKey = "your_openweather_api_key";
// GeoLocation API Key
String geoApiKey = "your_ipgeolocation_api_key";

Видео на заднем фоне
Видео на заднем плане bgSkyCastRain.mp4 должно быть размещено в каталоге ресурсов. Вы можете заменить это видео любым другим видео по вашему выбору.

Лицензия
Этот проект лицензирован под GNU General Public License v3.0. Подробности смотрите в файле LICENSE.

Этот проект разработан в ходе учебной практики.

Контакт
Автор: AE MIHALEV
Телеграмм: https://t.me/ksaers
Электронная почта: mihalev.andrey77@gmail.com






