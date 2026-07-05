# Getting Started

Follow these steps to set up and run the Lobby backend application on your local machine.

---

## 1. Prerequisites

Ensure you have the following installed:
- **Docker & Docker Compose**
- **Java 17**

If you do not have Java 17 installed, we recommend installing it using **SDKMAN!**:
1. Install SDKMAN!:
   ```bash
   curl -s "https://get.sdkman.io" | bash
   ```
2. Open a new terminal window and install Java 17 (Eclipse Temurin distribution):
   ```bash
   sdk install java 17.0.19-tem
   ```

---

## 2. Set Up the Database

Start the PostgreSQL database container in the background using the provided Docker compose and environment file:

```bash
docker compose --env-file docker/db-local.env -f docker/db.docker-compose.yml up -d
```

To stop the database container when you are done, run:
```bash
docker compose --env-file docker/db-local.env -f docker/db.docker-compose.yml down
```

---

## 3. Configure Application Keys

The application requires API keys that are excluded from version control for security.

1. Duplicate the template file:
   [Key_template.java](./src/main/java/com/lobby/app/config/Key_template.java)
2. Rename the duplicate to `Key.java` in the same directory:
   `src/main/java/com/lobby/app/config/Key.java`
3. Update the class name to `Key` inside the new file:
   ```java
   package com.lobby.app.config;

   public class Key {
       public static String steamApiKey = "steam-key";
       public static String clientId = "client-id";
       public static String secretId = "secret-id";
       public static String accessToken = "access-token";
   }
   ```

---

## 4. Run the Application

### Option A: Via VS Code
1. Go to the **Run and Debug** view (`Cmd+Shift+D` on Mac).
2. Select **Lobby App (Spring Boot)**.
3. Click the **Play** button or press **`F5`** to start debugging.

> [!NOTE]
> A sample VS Code environment configuration is provided in the [launch_config](./launch_config) directory. You can copy these files into a `.vscode` folder at the project root to set up the editor's debug and build environment.

### Option B: Via Command Line
Run the following Maven wrapper command from the project root:
```bash
./mvnw spring-boot:run
```

---

## 5. Verify the Installation

Once the application starts, check that the REST API is running by visiting:
[http://localhost:8080/auth/ping](http://localhost:8080/auth/ping)

It should respond with:
```text
pong
```
