FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/WickyPlays/Leaflet-Discord .

RUN chmod +x gradlew

RUN ./gradlew clean shadowJar --no-daemon

CMD ["java","-jar","build/libs/LeafletDiscord-1.0.0-all.jar"]