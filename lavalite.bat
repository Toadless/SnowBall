@rem Lavalite buildAndStart script

call gradlew build

echo Successfully Built!

pause

echo Starting Lavalite

java -jar build/libs/app-4.0.0-all.jar