# ✅ OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-slim

# ✅ 작업 디렉토리 설정
WORKDIR /app

# ✅ JAR 파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar

# ✅ 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
