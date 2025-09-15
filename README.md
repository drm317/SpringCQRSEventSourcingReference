# Spring CQRS Event Sourcing Reference

A comprehensive reference implementation of Command Query Responsibility Segregation (CQRS) and Event Sourcing patterns using Spring Boot 3 and Java 21.

## Overview

This project demonstrates how to implement CQRS and Event Sourcing in a modern Spring Boot application. It includes:

- **Domain-Driven Design** with aggregates and domain events
- **Event Sourcing** for persisting state changes as events
- **CQRS** with separate command and query models
- **Event-driven architecture** with event handlers for projections
- **REST API** for commands and queries
- **Complete order management** example domain

## Architecture

### Key Components

1. **Domain Layer**
   - `AggregateRoot`: Base class for aggregates with event sourcing capabilities
   - `DomainEvent`: Base class for all domain events
   - `Order`: Example aggregate implementing business logic

2. **Command Side (Write Model)**
   - Commands: `CreateOrderCommand`, `AddOrderItemCommand`, etc.
   - Command Handlers: Process commands and interact with aggregates
   - Event Store: Persists domain events

3. **Query Side (Read Model)**
   - Projections: Denormalized views optimized for queries
   - Event Handlers: Update projections when events occur
   - Query Services: Provide read access to projections

4. **Infrastructure**
   - `EventStore`: Interface for event persistence
   - `JpaEventStore`: JPA implementation of event store
   - `AggregateRepository`: Repository for loading/saving aggregates

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Database Console

Access the H2 console at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:eventstore`
- Username: `sa`
- Password: (empty)

## API Documentation

### Command API (Write Operations)

#### Create Order
```http
POST /api/orders
Content-Type: application/json

{
  "customerName": "John Doe"
}
```

#### Add Order Item
```http
POST /api/orders/{orderId}/items
Content-Type: application/json

{
  "productName": "Laptop",
  "quantity": 2,
  "unitPrice": 999.99
}
```

#### Confirm Order
```http
POST /api/orders/{orderId}/confirm
```

#### Cancel Order
```http
POST /api/orders/{orderId}/cancel
```

### Query API (Read Operations)

#### Get Order by ID
```http
GET /api/orders/{orderId}
```

#### Get All Orders
```http
GET /api/orders
```

#### Filter Orders
```http
GET /api/orders?customerName=John&status=CONFIRMED
```

## Example Usage

1. **Create an order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe"}'
```

2. **Add items to the order:**
```bash
curl -X POST http://localhost:8080/api/orders/{orderId}/items \
  -H "Content-Type: application/json" \
  -d '{"productName": "Laptop", "quantity": 1, "unitPrice": 1200.00}'
```

3. **Confirm the order:**
```bash
curl -X POST http://localhost:8080/api/orders/{orderId}/confirm
```

4. **Query the order:**
```bash
curl http://localhost:8080/api/orders/{orderId}
```

## Testing

Run the tests with:
```bash
mvn test
```

The project includes:
- Unit tests for domain logic
- Integration tests for the complete flow
- API tests for REST endpoints

## Key Features

### Event Sourcing
- All state changes are stored as immutable events
- Aggregates can be reconstructed from their event history
- Complete audit trail of all changes
- Temporal queries and debugging capabilities

### CQRS
- Separate models for commands (writes) and queries (reads)
- Optimized read models for different query patterns
- Independent scaling of read and write sides
- Event-driven synchronization between models

### Domain-Driven Design
- Rich domain models with business logic
- Aggregates enforce consistency boundaries
- Domain events capture business-significant occurrences
- Clear separation of concerns

## Project Structure

```
src/main/java/com/example/cqrs/
├── api/                    # REST controllers and DTOs
├── command/                # Command side (write model)
│   └── order/              # Order commands and handlers
├── domain/                 # Domain layer
│   └── order/              # Order aggregate and events
├── infrastructure/         # Infrastructure concerns
├── query/                  # Query side (read model)
│   └── order/              # Order projections and queries
└── CqrsEventSourcingApplication.java
```

## Technologies Used

- **Spring Boot 3.2.0** - Framework
- **Java 21** - Programming language
- **Spring Data JPA** - Data access
- **H2 Database** - In-memory database
- **Jackson** - JSON serialization
- **Spring Boot Test** - Testing framework
- **Maven** - Build tool

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the Apache 2.0 License - see the LICENSE file for details.