FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/test-app.jar /test-app/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/test-app/app.jar"]
