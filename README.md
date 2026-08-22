# MarketPulse

MarketPulse is a Spring Boot backend application for monitoring marketplace products and identifying changes in their popularity, price, and search position over time.

The application integrates with the eBay API, stores product search results in PostgreSQL, periodically collects product snapshots, and calculates product trends based on historical data.

The project was built as a backend portfolio project demonstrating REST API development, external API integration, scheduled jobs, persistence, validation, testing, and deployment.

## Features

- Search products using the eBay API
- Store marketplace products in PostgreSQL
- Track product price, popularity, and search position
- Create historical product snapshots
- Calculate product popularity scores
- Detect product trends over time
- Track selected search keywords automatically
- Periodically refresh tracked keywords using scheduled jobs
- Enable or disable individual tracked keywords
- View analytics summaries for tracked keywords
- REST API with request validation
- Centralized exception handling
- Interactive Swagger / OpenAPI documentation
- Unit and controller tests

## Tech Stack

### Backend

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Bean Validation
- Maven

### Database

- PostgreSQL
- Neon PostgreSQL in production

### External Integration

- eBay Browse API
- eBay OAuth authentication

### Mapping and Utilities

- MapStruct
- Lombok

### API Documentation

- OpenAPI 3
- Swagger UI
- Springdoc OpenAPI

### Testing

- JUnit 5
- Mockito
- MockMvc

### Infrastructure

- Docker
- Docker Compose
- Render
- Neon

## Architecture

MarketPulse follows a conventional layered Spring architecture:

```text
Controller
    |
    v
Service
    |
    +--------> eBay API
    |
    v
Repository
    |
    v
PostgreSQL
```

The application is divided into several main layers:

```text
client/          External eBay API integration
configuration/   Spring and OpenAPI configuration
controller/      REST API controllers
dto/             API request and response objects
entity/          JPA entities
exception/       Application exceptions and global error handling
mapper/          MapStruct entity/DTO mapping
model/           Domain models and enums
repository/      Spring Data JPA repositories
scheduler/       Scheduled marketplace monitoring
service/         Business logic
```

## How Market Monitoring Works

A user can add a keyword such as:

```text
wireless headphones
```

MarketPulse stores the keyword as a tracked search query.

The scheduler periodically processes active tracked keywords:

```text
Tracked Keyword
      |
      v
Scheduled Job
      |
      v
eBay Browse API
      |
      v
Product Results
      |
      +----> Product
      |
      +----> Product Snapshot
                    |
                    v
              Trend Analysis
```

Each search creates historical snapshots containing marketplace information such as:

- price
- search position
- popularity score
- capture time

These snapshots allow MarketPulse to compare product performance between different points in time.

## Product Trend Analysis

MarketPulse analyzes changes between recent product snapshots.

The trend calculation considers:

- change in search position
- change in popularity score
- percentage price change

The resulting trend score is used to classify a product as:

```text
RISING
STABLE
FALLING
NOT_ENOUGH_DATA
```

For example, a product that moves higher in search results while its popularity score increases may be classified as `RISING`.

Historical snapshots make the calculation independent from the product's current database state.

## Tracked Keywords

Tracked keywords allow searches to be monitored automatically.

Example:

```json
{
  "keyword": "wireless headphones",
  "marketplace": "EBAY",
  "searchLimit": 20
}
```

A tracked keyword can be:

- created
- listed
- enabled or disabled
- analyzed through a summary endpoint
- deleted

Only active keywords are processed by the scheduler.

## REST API

The main API is divided into two groups.

### Products

Product endpoints provide:

- stored products
- eBay product search
- popularity data
- product history
- individual product trend analysis
- trending product discovery

### Tracked Keywords

Tracked keyword endpoints provide:

- keyword creation
- keyword listing
- monitoring activation/deactivation
- analytics summary
- keyword deletion

The complete and current endpoint specification is available through Swagger UI.

## API Documentation

When the application is running locally:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI allows requests to be executed directly from the browser using **Try it out**.

## Validation and Error Handling

Incoming requests are validated using Jakarta Bean Validation.

Examples include:

- required keyword values
- valid search limits
- valid request parameters

The application uses centralized exception handling through `@RestControllerAdvice`.

API errors are returned using a consistent response structure and appropriate HTTP status codes, including:

```text
400 Bad Request
404 Not Found
409 Conflict
500 Internal Server Error
```

## Testing

The project contains unit and controller tests covering important business and HTTP behavior.

The current test suite includes coverage for:

- tracked keyword creation
- duplicate keyword handling
- missing entities
- default keyword configuration
- rising product trends
- falling product trends
- stable product trends
- insufficient historical data
- REST request validation
- HTTP error responses
- controller behavior

Run the tests with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Running Locally

### Requirements

Install:

- Java 21
- Docker
- Docker Compose

Clone the repository:

```bash
git clone https://github.com/NikolasArro/MarketPulse.git
cd MarketPulse
```

Start PostgreSQL:

```bash
docker compose up -d
```

Configure the required application environment variables for the database and eBay API credentials.

Then start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

or on Linux/macOS:

```bash
./mvnw spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Database

The application stores marketplace data in PostgreSQL.

The main domain tables include:

```text
product
product_snapshot
search_query
tracked_keyword
```

`product` stores the current product information.

`product_snapshot` stores historical observations used for trend analysis.

`search_query` associates collected marketplace data with the search that produced it.

`tracked_keyword` stores keywords processed automatically by the scheduler.

## Production

The application is deployed using:

```text
Render
   |
   v
Spring Boot Application
   |
   v
Neon PostgreSQL
```

Production configuration is provided through environment variables rather than hard-coded credentials.

## Project Goals

MarketPulse was created to explore how marketplace data can be transformed into useful trend information instead of simply returning search results.

The project focuses on several practical backend engineering concepts:

- third-party REST API integration
- OAuth authentication
- relational data modeling
- historical data collection
- scheduled background processing
- business-rule implementation
- REST API design
- DTO mapping
- input validation
- centralized exception handling
- automated testing
- API documentation
- containerized local development
- cloud deployment

## Future Improvements

Possible future development includes:

- support for additional marketplaces
- configurable monitoring intervals
- more advanced trend scoring
- category-based analytics
- product alerts
- historical charts
- pagination and filtering
- caching
- authentication and authorization
- frontend dashboard
- CI/CD pipeline

## Author

**Nikolas Arro**

Java Backend / Full-Stack Developer

GitHub: https://github.com/NikolasArro