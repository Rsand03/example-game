# example-game

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

An example 2D game for a course in TalTech. Project is intended for newcomers to take inspiration from. The example project is a multiplayer game utilizing LibGDX library for the game and Kryonet for server-side networking.

[Excalidraw diagram about the structure of this game](https://excalidraw.com/#json=kq2idkeEMGFr-LRx4AVKx,Qv7z8Ks417BKfwIkaTcV5A)


## Gameplay description

Currently, the server is configured to run only one game instance at a time, with a maximum capacity of two players.

- On the title screen, press "Start" to begin a new game or "Exit" to close the application.
- Wait for the second player to join—the game will begin automatically.
- Both players can move using the WASD keys and shoot bullets using the arrow keys.
- Each bullet hit decreases the opponent’s lives by 1. The game ends when one player’s lives reach 0.
- Once the game is over, players can return to the title screen and start a new match.

## How to run

1. Open the project in an IDE, preferably IntelliJ. While it's possible to run the game from the command line, running multiple clients simultaneously can be tricky.
2. Launch the server by running `ServerLauncher.java` from the `server` package.
3. Launch two clients by running `Lwjgl3Launcher.java` from the `lwjgl3` package.
   - It’s recommended to start the clients by pressing the green triangle at the top of the screen in IntelliJ.
   - To avoid issues when running multiple clients, select "Current file" in the Run Configuration settings.
4. Start playing!

## Lombok library

This project uses Lombok, a Java library that helps reduce boilerplate code by automatically generating getters, setters (and even constructors, but not in this project) for any class.
For example look at `...Message` classes that use @Data annotation, which generates both getters and setters.
This makes the code cleaner and more readable.

Learn more about Lombok here: [Introduction to Lombok on GeeksforGeeks](https://www.geeksforgeeks.org/introduction-to-project-lombok-in-java-and-how-to-get-started/)

## Platforms / main modules

- `core`: Main module with the application logic shared by all platforms. Contains mainly client-side logic.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs. For running client side of the game.
- `server`: A separate application without access to the `core` module.
- `shared`: A common module shared by `core` (client) and `server` platforms. Contains shared constants, messages, etc.

## Other files and directories

- `assets`: Contains game resources like sprites, images, fonts, and sounds used by the client.
- `gradle/wrapper`: No need to modify. Contains files for the Gradle wrapper, ensuring consistent Gradle version across different environments.
- `.editorconfig`: No need to modify. Defines coding style rules, such as indentation and line endings in the whole project.
- `.gitattributes`: No need to modify. This .gitattributes file ensures consistent line endings for .bat files.
- `.gitignore`: Tells Git which files and directories to exclude from version control.
- `build.gradle`: No need to modify. Defines the build configuration for a Gradle project, including dependencies, plugins, and tasks.
- `gradle.properties`: No need to modify. Contains project-specific properties and configuration settings, such as version numbers and JVM options.
- `gradlew`: No need to modify. Script that runs Gradle tasks with a specific version.
- `gradlew.bat`: No need to modify. Script that runs Gradle tasks on Windows without a pre-installed Gradle.
- `settings.gradle`: No need to modify. Configures the structure of a multi-project Gradle build, specifying which subprojects are included.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `server:run`: runs the server application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
