FROM openjdk:11 as builder

COPY . .

RUN ./gradlew shadowJar

FROM openjdk:11

COPY --from=builder /build/libs/drinks-1.0-SNAPSHOT-all.jar ./drinks-web.jar

CMD ["java", "-jar", "drinks-web.jar"]