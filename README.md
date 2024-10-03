# ShareHub

**ShareHub** — это демо приложение для обмена предметами, целью которого является показать возможности SQL и REST API.

## Описание

Проект **ShareHub** включает в себя два основных модуля:
1. **Gateway (Шлюз)** — отвечает за обработку запросов, маршрутизацию и валидацию данных.
2. **Main Service (Основной сервис)** — отвечает за бизнес-логику, управление предметами, запросами и бронированиями.

## Взаимодействие модулей

1. Клиент отправляет запрос в **Gateway**.
2. **Gateway** валидирует запрос и отправляет его в **Main Service**.
3. **Main Service** обрабатывает запрос и возвращает результат через **Gateway** обратно клиенту.

## Основные сущности и их связи

- **User** — пользователь системы.
- **Item** — предмет, который можно арендовать или запрашивать.
- **Request** — запрос пользователя на аренду предмета.
- **Booking** — бронирование предмета.
- **Comment** — отзыв или комментарий к предмету или запросу.

## Взаимосвязь

- **User** может создавать **Item**, отправлять **Request** и делать **Booking**.
- **Item** может быть забронирован другим пользователем через **Booking**.
- **Comment** может быть добавлен к **Item** или **Request**.

## Основные REST API эндпоинты

### 1. Пользователи (`/users`)

- `GET /users` — получить список всех пользователей.
- `GET /users/{userId}` — получить информацию о конкретном пользователе.
- `POST /users` — создать нового пользователя.
- `PUT /users/{userId}` — обновить информацию о пользователе.
- `DELETE /users/{userId}` — удалить пользователя.

### 2. Предметы (`/items`)

- `GET /items` — получить список всех предметов.
- `GET /items/{itemId}` — получить информацию о конкретном предмете.
- `POST /items` — создать новый предмет.
- `PUT /items/{itemId}` — обновить информацию о предмете.
- `DELETE /items/{itemId}` — удалить предмет.

### 3. Запросы (`/requests`)

- `GET /requests` — получить список всех запросов на аренду.
- `GET /requests/{requestId}` — получить информацию о конкретном запросе.
- `POST /requests` — создать новый запрос.
- `PUT /requests/{requestId}` — обновить запрос.
- `DELETE /requests/{requestId}` — удалить запрос.

### 4. Бронирования (`/bookings`)

- `GET /bookings` — получить список всех бронирований.
- `GET /bookings/{bookingId}` — получить информацию о конкретном бронировании.
- `POST /bookings` — создать новое бронирование.
- `PUT /bookings/{bookingId}` — обновить бронирование.
- `DELETE /bookings/{bookingId}` — удалить бронирование.

### 5. Комментарии (`/comments`)

- `GET /comments` — получить список всех комментариев.
- `GET /comments/{commentId}` — получить информацию о конкретном комментарии.
- `POST /comments` — добавить новый комментарий.
- `PUT /comments/{commentId}` — обновить комментарий.
- `DELETE /comments/{commentId}` — удалить комментарий.

## Пример взаимодействия модулей

### 1. Создание предмета:

- Клиент отправляет запрос `POST /items` на **Gateway**.
- **Gateway** валидирует запрос и передает его в **Main Service**.
- **Main Service** создает новый предмет в базе данных и возвращает результат через **Gateway** клиенту.

### 2. Создание бронирования:

- Клиент отправляет запрос `POST /bookings` на **Gateway**.
- **Gateway** проверяет права доступа и отправляет запрос в **Main Service**.
- **Main Service** создает новое бронирование и возвращает результат через **Gateway**.

### 3. Добавление комментария:

- Клиент отправляет запрос `POST /comments`.
- **Gateway** проверяет запрос и отправляет его в **Main Service** для сохранения комментария.
- Результат возвращается клиенту через **Gateway**.

---

# ShareHub (English Version)

**ShareHub** is a demo application designed to showcase the power of SQL and REST APIs through an item-sharing platform.

## Overview

**ShareHub** consists of two main modules:
1. **Gateway** — handles request processing, routing, and validation.
2. **Main Service** — manages business logic, including handling items, requests, and bookings.

## Module Interaction

1. The client sends a request to the **Gateway**.
2. **Gateway** validates the request and forwards it to the **Main Service**.
3. **Main Service** processes the request and returns the result through the **Gateway** back to the client.

## Main Entities and Relationships

- **User** — the system user.
- **Item** — an item that can be rented or requested by users.
- **Request** — a request from a user to rent an item.
- **Booking** — an item booking.
- **Comment** — a review or comment associated with an item or request.

## Relationships

- **User** can create an **Item**, submit a **Request**, and make a **Booking**.
- **Item** can be booked by other users via a **Booking**.
- **Comment** can be added to an **Item** or **Request**.

## Key REST API Endpoints

### 1. Users (`/users`)

- `GET /users` — fetch all users.
- `GET /users/{userId}` — get information about a specific user.
- `POST /users` — create a new user.
- `PUT /users/{userId}` — update user information.
- `DELETE /users/{userId}` — delete a user.

### 2. Items (`/items`)

- `GET /items` — fetch all items.
- `GET /items/{itemId}` — get information about a specific item.
- `POST /items` — create a new item.
- `PUT /items/{itemId}` — update item details.
- `DELETE /items/{itemId}` — delete an item.

### 3. Requests (`/requests`)

- `GET /requests` — fetch all requests.
- `GET /requests/{requestId}` — get information about a specific request.
- `POST /requests` — create a new request.
- `PUT /requests/{requestId}` — update a request.
- `DELETE /requests/{requestId}` — delete a request.

### 4. Bookings (`/bookings`)

- `GET /bookings` — fetch all bookings.
- `GET /bookings/{bookingId}` — get information about a specific booking.
- `POST /bookings` — create a new booking.
- `PUT /bookings/{bookingId}` — update booking details.
- `DELETE /bookings/{bookingId}` — delete a booking.

### 5. Comments (`/comments`)

- `GET /comments` — fetch all comments.
- `GET /comments/{commentId}` — get information about a specific comment.
- `POST /comments` — add a new comment.
- `PUT /comments/{commentId}` — update a comment.
- `DELETE /comments/{commentId}` — delete a comment.

## Module Interaction Examples

### 1. Creating an Item:

- The client sends a `POST /items` request to the **Gateway**.
- **Gateway** validates the request and forwards it to **Main Service**.
- **Main Service** creates a new item in the database and sends the result back to the client via **Gateway**.

### 2. Creating a Booking:

- The client sends a `POST /bookings` request to **Gateway**.
- **Gateway** checks permissions and forwards the request to **Main Service**.
- **Main Service** creates a new booking and sends the result back via **Gateway**.

### 3. Adding a Comment:

- The client sends a `POST /comments` request to the **Gateway**.
- **Gateway** validates the request and forwards it to **Main Service** to save the comment.
- The result is returned to the client through **Gateway**.

---

Обе версии описаны для использования в `README.md`, и их можно вставить в файл с минимальной модификацией для форматирования.