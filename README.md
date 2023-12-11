## Система управления задачами

Для запуска приложения необходимо, сделать git clone https://github.com/VladimirS0l/TaskManagmentSystem 
в свой локальный репозиторий.

Перейти в директорию /task-management-api , открыть терминал в текущей директории в ввести команду docker compose up --build.

Дождаться пока приложение запустится в Docker-compose.

Перейти по ссылке http://localhost:8000/swagger-ui/index.html

![Главная страница](/docs/swagger-main.png)

Зарегистрировать нового пользователя: /v1/auth/register

![Регистрация](/docs/swagger-register.png)

Авторизоваться: /v1/auth/login

![Авторизация](/docs/swagger-login.png)

Из полученного ответа скопировать Access token и ввести его в поле авторизации Swagger: 

![Swagger авторизация](/docs/swagger-auth.png)

Проверить вход: /v1/auth/info

![Проверка входа](/docs/swagger-enter.png)

Вы получили доступ к работе с приложением.


Sequence diagram: 

![Sequence diagram](/docs/sequenceDiagram.png)


Database diagram:

![Database diagram](/docs/taskmanagement.jpg)


