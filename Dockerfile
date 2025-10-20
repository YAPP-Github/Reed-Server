# Build stage
FROM gradle:8.7-jdk21 AS build
# MODULE: 빌드할 모듈 (apis, admin, batch) - 빌드 시 --build-arg MODULE=xxx 필수
ARG MODULE
WORKDIR /app

# 의존성 캐싱 최적화를 위한 단계별 복사
# 1. 의존성 캐싱 최적화를 위해 Gradle Wrapper 및 의존성 관련 파일만 먼저 복사
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat ./
COPY gradle/wrapper/ ./gradle/wrapper/
COPY buildSrc/ ./buildSrc/
COPY ${MODULE}/build.gradle.kts ./${MODULE}/

# 2. Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# 3. 소스코드 없이 의존성만 다운로드
RUN ./gradlew :${MODULE}:dependencies --no-daemon

# 4. 소스코드 전체 복사
COPY . .

# 5. 실제 애플리케이션 빌드
RUN ./gradlew :${MODULE}:bootJar --parallel --no-daemon

# Run stage
FROM openjdk:21-slim
# MODULE: 빌드할 모듈 (apis, admin, batch) - 빌드 시 --build-arg MODULE=xxx 필수
ARG MODULE
WORKDIR /app

# 멀티스테이지 빌드로 최종 이미지 크기 최소화
COPY --from=build /app/${MODULE}/build/libs/${MODULE}-*.jar app.jar

# 런타임에 필요한 secret 폴더 복사
COPY --from=build /app/secret ./secret/

# TimeZone KST 설정
ENV TZ=Asia/Seoul

# JVM 실행 설정
# - Xms512m: 초기 힙 메모리 512MB
# - Xmx1g: 최대 힙 메모리 1GB
# - server.port: Spring Boot 서버 포트 (컨테이너 실행 시 -e SERVER_PORT=xxxx로 주입, 기본값: 8080)
# - exec: shell 프로세스를 java 프로세스로 대체하여 graceful shutdown 지원
ENTRYPOINT ["sh", "-c", "exec java -Xms512m -Xmx1g -Duser.timezone=Asia/Seoul -Dserver.port=${SERVER_PORT:-8080} -jar app.jar"]
