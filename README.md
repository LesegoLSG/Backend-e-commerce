# E-Commerce Application

This project is an e-commerce RESTful API built with Spring Boot. The API provides functionalities such as product management, user authentication, cart management, order handling, and more. The application employs JWT for secure user authentication and authorization.

## Table of Contents
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Environment Variables](#environment-variables)
- [Installation](#installation)
- [Endpoints](#endpoints)
- [Authentication and Authorization](#authentication-and-authorization)
- [Contributing](#contributing)
- [License](#license)
---

## Getting Started

### Prerequisites
- **Java 17**
- **Maven 3.x**
- **MySQL** (or any other relational database)
- **Postman** (optional, for testing)

### Project Structure
The project follows a layered architecture with three main layers: **Controller**, **Service**, and **Repository**.

- `controller/`: Handles incoming HTTP requests and routes them to the appropriate service.
- `service/`: Contains business logic and manages interactions between controllers and repositories.
- `repository/`: Interfaces with the database using Spring Data JPA.
- `security/`: Handles security configurations and JWT authentication.
- `model/`: Contains entities representing database tables.

### Environment Variables
To configure the application, create an `.env` file or add the following properties to `application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your-username
spring.datasource.password=your-password

# JWT configuration
auth.token.jwtSecret=your-jwt-secret
auth.token.expirationInMils=3600000
auth.token.refreshExpirationInMils=86400000
```

### Installation (bash)
1. Clone the repository:
```
git clone https://github.com/LesegoLSG/Backend-e-commerce.git
cd e-commerce
```
2. Build the project:
```
mvn clean install
```

3. Run the application:
```
mvn spring-boot:run
```

### Endpoints

| **Method** | **Endpoint**                    | **Description**                |
|------------|---------------------------------|--------------------------------|
| **POST**   | `/user/register`                | Register a new user            |
| **POST**   | `/auth/login`                   | Login for registered users     |
| **POST**   | `/auth/refresh-token`           |Refreshes the access token      |
| **PUT**    | `/users/{userId}/update`        | Updates an existing user by id |
| **DELETE** | `/users/{userId}/delete`        | Delete an existing user by id  |
| **GET**    | `users/{userId}/user`           | Retrieve existing user by id    |
| **POST**   | `/products/add`                 | Create a new product     |
| **PUT**   | `/products/product/{productId}/update`| Updates existing product     |
| **POST**   | `/products/product/{productId}/delete` |Removes existing product     |
| **GET**    | `/products/all`                     | Retrieve a list of products    |
| **GET**    | `/products/product/by-brand`    | Retrieve a product by brand name    |
| **GET**    | `/products/products/{category}/all/products`| Retrieve a product by brand name |
| **GET**    | `/products/product/{ProductId}/product`| Retrieve details of a product  |
| **POST**   | `/cartItems/add`                     | Add an item to the cart        |
| **PUT**    | `/cartItems/cart/{cardId}/item/{itemId}` | Retrieve cart details          |
| **DELETE** | `/cartItems/cart{cardId}/item/{itemId}/delete`| Remove an item from the cart   |
| **GET**   | `/carts/{cardId}/getCart`           | Retrieve card by id             |
| **GET**   | `/carts/{cardId}/getTotalPrice`   | Retrieve total price for the cart |
| **POST**   | `/orders/order/add`                | Create a new order             |
| **GET**    | `/orders/{orderId}`             | Retrieve order details         |
| **POST**   | `/review/add/{productId}`        | Add a review to a product      |
| **GET**    | `/reviews/{productId}`          | Retrieve reviews for a product |
| **DELETE**   | `/reviews/delete/{reviewId}`   | Delete review by id             |
| **POST**   | `/billing/add`                 | Save billing information to user      |
| **PUT**   | `/billing/update/{billingId}`           | Update a billing by id       |
| **POST**   | `/shippingInfo/add`                | Save shipping information      |
| **PUT**   | `/shippingInfo/update/{shippingId}`  | Save shipping information      |
| **GET**   | `/shippingInfo/getShippingAddress/{shippingId}`| Save shipping information      |
| **DELETE**   | `/shippingInfo/delete/{shippingId}`   | Save shipping information      |
| **POST**   | `/images/upload`                | Save image to product      |
| **GET**   | `/images/image/download/{ImageId}` | Save image to product      |


### Authentication and Authorization

#### Overview
The application secures endpoints using JWT (JSON Web Token), which provides stateless authentication and enhances security for the application.

#### JWT Authentication Flow

### 1. **User Login**
   - **Endpoint**: `/auth/login`
   - **Description**: Users authenticate by submitting their credentials at this endpoint. A valid login returns:
      - **Access Token**: Used for authenticated requests with a short expiration time.
      - **Refresh Token**: Allows obtaining a new access token after expiration without requiring re-authentication.

### 2. **Access Token**
   - Used to make authenticated requests to protected endpoints within a short time window.

### 3. **Refresh Token**
   - Used to obtain a new access token after the previous one expires, allowing users to maintain their session without re-authentication.

## Protected Endpoints
Endpoints that require JWT authentication include:
- **POST** `/cart/add` - Add an item to the cart.
- **POST** `/order/create` - Create a new order.

Unauthorized requests to these endpoints return a **401 Unauthorized** status.

## JWT Token Structure

The JWT token returned to the client has the following structure:

```json
{
  "id": "<user_id>",
  "roles": ["ROLE_USER", "ROLE_ADMIN"],
  "exp": "<expiration_date>",
  "sub": "<user_email>"
}
```

### contributions

**1. Fork the repository.**

**2. Create a new feature branch:**
```
git checkout -b feature/new-feature
```
**3. Commit your changes:**
```
git commit -m "Add new feature"
```
**4. Push to the branch:**
```
git push origin feature/new-feature
```
**5. Submit a pull request.**

### License
This project is a personal portfolio project and is not intended for commercial use or distribution. All rights reserved by the author.
