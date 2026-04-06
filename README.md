1. Install MAMP and start MySQL on port 8889
2. Create a database named inventory_control

    CREATE DATABASE inventory_control;
    
3. Confirm application.properties uses port 8889, username root, password root
4. Run: mvn spring-boot:run
5. Spring will execute schema.sql and create the required tables

Updated Logging