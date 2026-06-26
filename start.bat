@echo off
cd /d "%~dp0"

echo Smart Tourism Platform
echo ======================
echo Frontend: http://localhost:5173
echo Backend : http://localhost:8080
echo Admin   : admin / admin123
echo.

echo Stopping old backend...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080.*LISTENING"') do taskkill /F /PID %%a 2>nul

start "Tourism-Backend" cmd /k "cd tourism-backend && mvnw.cmd spring-boot:run"
start "Tourism-Frontend" cmd /k "cd tourism-frontend && npm run dev"

echo Both services starting...
echo Wait ~30 seconds for backend to compile.
pause
start http://localhost:5173
