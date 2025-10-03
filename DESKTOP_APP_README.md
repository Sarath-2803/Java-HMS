# Hotel Management System - Desktop Application

## Overview
This is a Java desktop application for hotel management with restaurant reservation features. The application connects directly to a PostgreSQL database without requiring a separate backend server.

## Architecture
- **Type:** Single Desktop Application (not client-server)
- **Frontend:** Java Swing GUI
- **Backend Logic:** Services and DAOs (directly integrated)
- **Database:** PostgreSQL

## Prerequisites
1. **Java Development Kit (JDK)** - Java 8 or higher
2. **PostgreSQL 16** - Running on `localhost:5432`
3. **Database Setup:**
   - Database name: `test`
   - Username: `postgres`
   - Password: `postgres`

## Running the Application

### Windows (PowerShell)
```powershell
cd frontend
.\run_app.ps1
```

### Windows (Command Prompt)
```cmd
cd frontend
run_app.bat
```

### Linux/Mac
```bash
cd frontend
chmod +x run.sh
./run.sh
```

## Features

### Restaurant Management
1. **Restaurant Reservation** - Make table reservations
   - Select date, time, and number of guests
   - Automatically finds available tables based on capacity
   - 2-hour reservation duration
   - Direct database access (no HTTP server needed)

2. **Restaurant Chart** - View table availability
   - Real-time table status (Available/Reserved)
   - Shows reservation end times
   - Refresh button to update status
   - Direct database access

### Other Features
- Room booking
- Payment processing
- User management
- Dashboard with quick access to all features

## Technical Details

### Classpath Configuration
The application requires the following in its classpath:
- `frontend/frames` - Frontend compiled classes
- `backend/build` - Backend compiled classes (Services, DAOs, Models)
- `backend/lib/postgresql-42.7.1.jar` - PostgreSQL JDBC driver

### Direct Database Access
Unlike traditional client-server applications, this desktop app:
- ✅ Calls service methods directly (no HTTP/REST API)
- ✅ Connects directly to PostgreSQL database
- ✅ No need to start a backend server
- ✅ All logic runs in a single JVM process

### Code Flow
```
Frontend (Swing GUI) 
    → Services (Business Logic)
        → DAOs (Database Access)
            → PostgreSQL Database
```

## Development

### Compiling Backend Classes
```powershell
cd backend
javac -cp "lib\postgresql-42.7.1.jar;src\main\java" -d build src\main\java\hotel\util\*.java src\main\java\hotel\dto\*.java src\main\java\hotel\model\*.java src\main\java\hotel\dao\*.java src\main\java\hotel\service\*.java
```

### Compiling Frontend Classes
```powershell
cd frontend\frames
javac -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" *.java
```

### Running Individual Frames
```powershell
# From frontend/frames directory
java -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" login
java -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" restaurant_reservation
java -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" restaurant_chart
```

## Database Tables
The application uses the following tables (auto-created on first run):
- `users` - User accounts
- `payments` - Payment records
- `amenity` - Restaurant tables (6 tables with varying capacities: 2, 4, 6, 8)
- `amenity_booking` - Table reservations

## Troubleshooting

### "Cannot connect to database"
- Ensure PostgreSQL is running: `Get-Process postgres`
- Check database exists: `psql -U postgres -l | Select-String "test"`
- Verify credentials in `backend/src/main/java/hotel/util/Utils.java`

### "Class not found" errors
- Make sure backend classes are compiled in `backend/build` directory
- Verify classpath includes all three paths (frames, build, postgresql jar)

### "No tables found"
- Database migrations should run automatically on first startup
- Check `backend/src/main/resources/db/migrations/` folder exists
- Manually run migrations if needed

## Notes

### What Changed from Client-Server Version
This application was refactored from a client-server architecture to a desktop application:

**Removed:**
- ❌ HTTP Server (port 8080)
- ❌ Controllers (AmenityController, AmenityBookingController)
- ❌ HttpClient utility
- ❌ JSON parsing (JSONObject, JSONArray)
- ❌ REST API endpoints

**Kept:**
- ✅ Services (business logic)
- ✅ DAOs (database access)
- ✅ Models (data classes)
- ✅ Utils (database connection)
- ✅ All frontend frames

### Migration Files
SQL migration files are located in `backend/src/main/resources/db/migrations/`:
- V1_Schema_migration.sql
- V2_User_table_created.sql
- V3_Payment_table_created.sql
- V4_Amenity_table_created.sql (creates 6 restaurant tables)
- V5_AmenityBooking_table_created.sql

## License
[Your License Here]

## Contributors
- Joseph Savio Kav
