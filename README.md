# Mini Project 1 - Spring Boot E-Commerce Application

## 📌 Overview
This project is a basic e-commerce application built using **Spring Boot**. It consists of four main models: **User**, **Product**, **Cart**, and **Order**, each with corresponding repositories, services, and controllers. The application allows users to manage products, add items to carts, and place orders. Data is persisted in JSON files (`users.json`, `products.json`, `carts.json`, and `orders.json`).

---

## 📂 Project Structure

The project follows a standard Spring Boot architecture with the following structure:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── MiniProject1/
│   │               ├── MiniProject1Application.java
│   │               ├── data/
│   │               │   ├── users.json
│   │               │   ├── products.json
│   │               │   ├── carts.json
│   │               │   └── orders.json
│   │               ├── model/
│   │               │   ├── User.java
│   │               │   ├── Product.java
│   │               │   ├── Cart.java
│   │               │   └── Order.java
│   │               ├── repository/
│   │               │   ├── UserRepository.java
│   │               │   ├── ProductRepository.java
│   │               │   ├── CartRepository.java
│   │               │   └── OrderRepository.java
│   │               ├── service/
│   │               │   ├── UserService.java
│   │               │   ├── ProductService.java
│   │               │   ├── CartService.java
│   │               │   └── OrderService.java
│   │               └── controller/
│   │                   ├── UserController.java
│   │                   ├── ProductController.java
│   │                   ├── CartController.java
│   │                   └── OrderController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── example/
                └── MiniProject1/
                    └── MiniProject1ApplicationTests.java
```

### Key Notes:
- **JSON Files**: Persist data in `src/main/data/` (users, products, carts, orders)
- **Layered Architecture**:
  - `model/`: Entity classes
  - `repository/`: CRUD operations
  - `service/`: Business logic
  - `controller/`: REST endpoints
- **Tests**: Unit tests under `src/test/`


---

## 📋 Models
### 1. **User**
- Represents individual customers.
- Attributes: `id` (UUID), `name` (String), `orders` (List of `Order`).

### 2. **Product**
- Defines items available for purchase.
- Attributes: `id` (UUID), `name` (String), `price` (double).

### 3. **Cart**
- Temporary storage for products a user intends to buy.
- Attributes: `id` (UUID), `userId` (UUID), `products` (List of `Product`).

### 4. **Order**
- Captures finalized orders.
- Attributes: `id` (UUID), `userId` (UUID), `totalPrice` (double), `products` (List of `Product`).

---

## 🚀 API Endpoints
### **User Controller (`/user`)**
| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | `/`                          | Add a new user.                      |
| GET    | `/`                          | Get all users.                       |
| GET    | `/{userId}`                  | Get a specific user by ID.           |
| GET    | `/{userId}/orders`           | Get all orders of a user.            |
| POST   | `/{userId}/checkout`         | Create a new order for the user.     |
| POST   | `/{userId}/removeOrder`      | Remove an order from the user.       |
| DELETE | `/{userId}/emptyCart`        | Empty the user's cart.               |
| PUT    | `/addProductToCart`          | Add a product to the user's cart.    |
| PUT    | `/deleteProductFromCart`     | Remove a product from the user's cart. |
| DELETE | `/delete/{userId}`           | Delete a user by ID.                 |

### **Product Controller (`/product`)**
| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | `/`                          | Add a new product.                   |
| GET    | `/`                          | Get all products.                    |
| GET    | `/{productId}`               | Get a specific product by ID.        |
| PUT    | `/update/{productId}`        | Update a product.                    |
| PUT    | `/applyDiscount`             | Apply discount to selected products. |
| DELETE | `/delete/{productId}`        | Delete a product by ID.              |

### **Cart Controller (`/cart`)**
| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | `/`                          | Add a new cart.                      |
| GET    | `/`                          | Get all carts.                       |
| GET    | `/{cartId}`                  | Get a specific cart by ID.           |
| PUT    | `/addProduct/{cartId}`       | Add a product to a cart.             |
| DELETE | `/delete/{cartId}`           | Delete a cart by ID.                 |

### **Order Controller (`/order`)**
| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | `/`                          | Add a new order.                     |
| GET    | `/`                          | Get all orders.                      |
| GET    | `/{orderId}`                 | Get a specific order by ID.          |
| DELETE | `/delete/{orderId}`          | Delete an order by ID.               |

---

## 🐳 Docker Setup

### Dockerfile Configuration
```dockerfile
# Use OpenJDK 25 as the base image
FROM openjdk:25-ea-4-jdk-oraclelinux9

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
COPY ./ /app

# Set environment variable for JSON data path
ENV DATA_JSON_PATH=/app/data

# Ensure the data directory exists
RUN mkdir -p /app/data

# Expose port 8080 for the application
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/target/mini1.jar"]
```

### Building and Running the Container

1. First build the Spring Boot application:
```bash
mvn clean package
```

2. Build the Docker image:
```bash
docker build -t mini-project1 .
```

3. Run the container with volume mounting for persistent data:
```bash
docker run -p 8080:8080 -v "$(pwd)/src/main/data:/app/data" mini-project1
```

### Important Notes:
- The `DATA_JSON_PATH` environment variable is set to `/app/data` inside the container
- Volume mounting (`-v` flag) ensures your JSON data persists between container restarts
- The application will be available at `http://localhost:8080`
- Make sure to build the project with `mvn package` before building the Docker image

### Docker Commands Cheat Sheet
```bash
# List running containers
docker ps

# Stop a container
docker stop <container-id>

# Remove a container
docker rm <container-id>

# Remove an image
docker rmi mini-project1
```
---

## 🧪 Testing
- Write **3 unit test cases** for each service (total of 75 tests).  
- Uncomment the provided test cases in `MiniProject1ApplicationTests.java` and run:  
  ```bash
  mvn test
  ```
## 💻 How to Run

### Prerequisites:
- Java 17+
- Maven 3.8+
- (Optional) Docker for containerization

### Steps:

1. **Clone the repository**:
```bash
git clone https://github.com/Scalable2025/Mini-Project1-Base.git
cd Mini-Project1-Base
```

2. **Build and run with Maven**:
```bash
mvn clean install
mvn spring-boot:run
```

3. **(Optional) Docker setup**:
```bash
# Build the image
docker build -t mini-project1 .

# Run the container with volume mounting for JSON data
docker run -p 8080:8080 -v "$(pwd)/src/main/data:/app/data" mini-project1
```

4. **Access the API**:
```
http://localhost:8080
```

5. **To stop**:
- For Maven: Press `Ctrl+C` in terminal
- For Docker: Run `docker stop <container-id>`
