# AuditProWeb

AuditProWeb is a Spring Boot web application designed to manage and audit employee documents such as payslips, payroll reports, and vacation receipts across multiple companies.

## 🔧 Features

- Company and employee registration
- PDF document upload and organization
- Document listing by company and employee
- Built-in web interface using Thymeleaf
- PostgreSQL database integration
- Accessible on the local network (LAN)

## 📚 Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven

## ⚙️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/auditproweb.git
cd auditproweb
```

### 2. Configure the database

Rename the example configuration file:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Update the file with your local PostgreSQL credentials:

```properties
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Make sure the PostgreSQL service is running, and create the database if it doesn’t exist:

```sql
CREATE DATABASE auditoria_documentos;
```

### 3. Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or run the `AuditprowebApplication` class in your IDE.

### 4. Access the system

Once the application is running, access it at:

```
http://localhost:8080
```

You can also access it from another machine on the same network using your computer’s IP address.

## 🔐 Security Notice

> ⚠️ Do not commit your actual `application.properties` file to version control.  
> The file contains sensitive information such as database credentials.  
> Use the provided `application.properties.example` as a template.

## 📁 Folder Structure

```
auditproweb/
├── src/
│   ├── main/
│   │   ├── java/com/eleetricz/auditproweb/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── templates/
│   │       └── application.properties.example
│   └── test/
├── pom.xml
└── README.md
```

## 📄 License

All rights reserved.  
This project is proprietary and maintained by the original author.  
Unauthorized distribution or commercial use is not permitted without explicit permission.



