# HealthVerse AI — Discovery Server (Netflix Eureka)

The **Discovery Server** provides centralized service registration and dynamic lookup for all microservices in the HealthVerse AI ecosystem.

---

## Tech Stack
- **Java**: 21 (LTS)
- **Spring Boot**: 3.3.4
- **Spring Cloud**: 2023.0.3 (Netflix Eureka Server)
- **Spring Boot Actuator**: Health, Metrics & Monitoring
- **Build Tool**: Apache Maven (Maven Wrapper included)

---

## Configuration & Ports

| Property | Default Value | Environment Variable Override | Description |
|---|---|---|---|
| `server.port` | `8761` | `PORT` | HTTP Server port |
| `eureka.instance.hostname` | `localhost` | `EUREKA_HOSTNAME` | Hostname for Eureka registration |
| `management.endpoints.web.exposure.include` | `health,info,metrics` | - | Exposed Actuator endpoints |

---

## Running the Discovery Server

### Option 1: Using Maven Wrapper from `discovery-server` directory
```powershell
cd backend/discovery-server
.\mvnw.cmd spring-boot:run
```
*(On Linux/macOS: `./mvnw spring-boot:run`)*

### Option 2: Using Maven Wrapper from `backend` root
```powershell
cd backend
.\mvnw.cmd clean spring-boot:run -pl discovery-server
```

### Option 3: Building and Running the Packaged JAR
```powershell
cd backend/discovery-server
.\mvnw.cmd clean package -DskipTests=false
java -jar target/discovery-server-1.0.0-SNAPSHOT.jar
```

---

## Verification & URLs

Once the server is running, verify via your browser or `curl`:

1. **Eureka Dashboard UI**:
   - URL: [http://localhost:8761](http://localhost:8761)
   - Displays all registered microservices, instances, status, and system environment info.

2. **Spring Boot Actuator Health Check**:
   - URL: [http://localhost:8761/actuator/health](http://localhost:8761/actuator/health)
   - Expected Output:
     ```json
     {
       "status": "UP",
       "components": {
         "discoveryComposite": {
           "status": "UP",
           "components": {
             "eureka": {
               "status": "UP"
             }
           }
         },
         "ping": {
           "status": "UP"
         }
       }
     }
     ```

3. **Eureka Apps Registry Endpoint**:
   - URL: [http://localhost:8761/eureka/apps](http://localhost:8761/eureka/apps) (Accept header: `application/json`)

---

## Connecting Downstream Microservices

To register any other microservice (e.g., `api-gateway`, `auth-service`, `user-service`) with this Discovery Server, add the following dependency and configuration to the client microservice:

### Maven Dependency:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### Client `application.yml`:
```yaml
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka/}
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
```
