# Quick Start Guide - Hotel Management System

## Step 1: Start PostgreSQL
Make sure PostgreSQL is running on your system.

**Check if running (Windows):**
```powershell
Get-Process postgres -ErrorAction SilentlyContinue
```

**Start if not running:**
- Open Services (services.msc)
- Find "postgresql-x64-16"
- Click "Start"

## Step 2: Run the Application

**Option A - Double-click the script:**
- Navigate to `frontend` folder
- Double-click `run_app.ps1` (PowerShell) or `run_app.bat` (Command Prompt)

**Option B - From terminal:**
```powershell
cd F:\joseph\Java\Java-HMS\frontend
.\run_app.ps1
```

## Step 3: Use the Application

1. **Login Screen** appears first
2. **Dashboard** - Click buttons to access features:
   - "Restaurant Reservation" - Make new table reservations
   - "View Chart" - See table availability
   - "Room Booking" - Book hotel rooms
   - "Room Status" - Check room availability

### Making a Restaurant Reservation

1. Click "Restaurant Reservation" from dashboard
2. Fill in the form:
   - **Guest Name:** Your name
   - **Date:** Format DD/MM/YYYY (e.g., 03/10/2025)
   - **Time:** Format HH:MM (e.g., 19:30)
   - **Number of Guests:** Use spinner (1-8)
3. Click "Confirm Reservation"
4. System will automatically:
   - Find a table with sufficient capacity
   - Create 2-hour reservation
   - Show confirmation with Table ID and Booking ID

### Viewing Restaurant Chart

1. Click "Restaurant Reservation" from dashboard
2. Click "View Reservation Chart" button
3. See real-time table status:
   - **Available** - Table is free
   - **Reserved** - Shows reservation end time (MM/DD HH:MM)
4. Click "Refresh" to update status

## Common Issues

### Application won't start
```powershell
# Recompile everything:
cd F:\joseph\Java\Java-HMS\backend
javac -cp "lib\postgresql-42.7.1.jar;src\main\java" -d build src\main\java\hotel\util\*.java src\main\java\hotel\dto\*.java src\main\java\hotel\model\*.java src\main\java\hotel\dao\*.java src\main\java\hotel\service\*.java

cd ..\frontend\frames
javac -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" *.java
```

### Database connection error
- Verify PostgreSQL is running
- Check database "test" exists
- Default credentials: postgres/postgres

### No tables available
- Database was automatically populated with 6 tables on first run
- Tables have capacities: 2, 4, 4, 6, 2, 8 guests
- Check if reservations overlap with your desired time

## Tips

- **Reservation Duration:** All reservations are 2 hours long
- **Table Assignment:** System automatically picks the best table for your party size
- **Refresh Chart:** Click refresh to see latest availability
- **No Server Needed:** This is a desktop app - no need to start any server!

## Need Help?

Check the full documentation in `DESKTOP_APP_README.md`
