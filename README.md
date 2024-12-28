# Music Management System

Проєкт створений для управління музичними треками, гуртами та жми. Використовує Java Spring Boot, PostgreSQL, і Docker для розгортання.

## Опис

Ця система дозволяє:
- Додавати, редагувати та видаляти інформацію про музичні треки.
- Зберігати інформацію про гурти та їх жанри.
- Управляти користувачами (авторизація, ролі).

Проєкт адаптовано для запуску у будь-якому середовищі за допомогою Docker.

---

## Функціональність

1. **Музичні треки:**
   - Додавання/видалення/редагування треків.

2. **Гурти:**
   - Додавання/видалення/редагування гуртів.

3. **Музичні жанри:**
   - ДДодавання/видалення/редагування жанрів.

4. **Користувачі:**
   - Авторизація (з ролями: Адміністратор, Користувач).

---

## Як додати дані при першому запуску

Проєкт автоматично наповнить базу даних початковими даними з файлу main.sql.

---

## Запуск проєкту

### Варіант 1: Використання Docker

1. **Встановіть Docker**  
   Переконайтеся, що у вас встановлений [Docker](https://www.docker.com/).

2. **Клонуйте репозиторій**  
   ```bash
   git clone https://github.com/your-username/your-repo.git
   cd your-repo

3. **Налаштуйте змінні середовища**

   Замініть ${LOGIN} і ${PASSWORD} на ваші облікові дані у файлах:

   - application.properties
   - compose.yaml або встановіть ці змінні в середовищі.

4. **Запустіть додаток**

   - Виконайте команду з кореневої папки проєкту: docker-compose up

5. **Відкрийте додаток у браузері**

   Перейдіть за адресою: http://localhost:2222

6. **Зупиніть додаток**

Щоб зупинити контейнер, використовуйте команду: docker-compose down

### Варіант 2: Використання PostgreSQL та Maven

1. **Встановіть необхідні інструменти**

   - PostgreSQL
   - Maven
   - JDK 21

2. **Налаштуйте змінні середовища**

   Замініть ${LOGIN} і ${PASSWORD} на потрібні значення у файлах: 

   - application.properties
   - compose.yaml

3. **Запакуйте проєкт**

   Використовуйте Maven для створення пакета: mvn package

4. **Запустіть додаток**

   Використовуйте вашу IDE або запустіть програму безпосередньо з папки target.

5. **Відкрийте додаток у браузері**

   Перейдіть за адресою: http://localhost:2222

---

## Ролі та можливості користувачів

- Неавторизований користувач може бачити лише сторінки авторизації.
- Авторизований користувач, який має роль USER, має доступ на перегляд даних.
- Авторизований користувач, який має роль ADMIN, може додавати, оновлювати та видаляти дані.

---

## Технології

- Java 21 (Spring Boot)
- PostgreSQL
- Docker & Docker Compose
- Maven






