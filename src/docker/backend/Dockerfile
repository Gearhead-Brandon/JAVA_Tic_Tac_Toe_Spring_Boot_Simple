FROM openjdk:23-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY app/app.jar app.jar

# Запускаем JAR-файл
ENTRYPOINT ["java","-jar","/app/app.jar"]