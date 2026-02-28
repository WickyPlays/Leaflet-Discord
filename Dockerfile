FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/WickyPlays/Leaflet-Discord .

RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

# This should not be used unless it is your last resort. Use docker run -e DISCORD_TOKEN="" is better
#ENV DISCORD_TOKEN=""

CMD ["sh","-c","java -jar build/libs/*.jar"]