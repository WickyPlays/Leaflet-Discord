# 🌿 Leaflet-Discord

Welcome to **Leaflet-Discord**, a **demo Discord bot** created for learning, testing, and showcasing basic bot structure and hosting environment.

> [!CAUTION]
> This project is **not production-ready**. It is intended purely as a reference or starter template.

## What is this?
Leaflet-Discord is a simple, open-source Discord bot meant to:
- Demonstrate a basic Discord bot setup
- Serve as a learning resource
- Act as a starting point for experimentation

## Open-source nature
This project is meant to be open source so anyone can:
- Learn from real project structure
- Fork and customize it
- Build their own Discord bot faster

## 🚀 Build & Run

### Requirements
- Java (JVM)
- Gradle (included via `gradlew`)
- Discord bot token. Go to [here](https://discord.com/developers/home) and generate one

### Environment Variable
Provide your bot token using an environment variable (either .env file or system environment):

```env
DISCORD_TOKEN=your_discord_bot_token_here
```

### Execute

#### With Gradle
Command approach (terminal or IDE):
```bash
./gradlew clean build
./gradlew run
```
JAR approach
```bash
./gradlew clean shadowJar
java -jar build/libs/app.jar
```

#### With Dockerfile 🐳
You can run Leaflet-Discord using Docker instead of Gradle.

Command approach:
```bash
docker build -t leaflet-discord .
docker run -e DISCORD_TOKEN=your_discord_bot_token_here leaflet-discord
```
> [!TIP]
> You can add `ENV DISCORD_TOKEN="<your token here>"` inside Dockerfile, however it is discouraged

### Hosting outside local
I recommend some good option for hosting, such as Railway, Koyeb. Render, Fly.io, Oracle Cloud, etc.

## Notes
This bot is intentionally minimal and may lack security, scalability, and advanced error handling.

## Contributing
Contributions are welcome since it is open-source. Keep changes simple and aligned with the demo purpose.

## A word of appreciation
A big thanks to everyone who explores, forks, or contributes to this project.
Special thanks to:
+ JDA (https://github.com/discord-jda/JDA)