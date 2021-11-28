FROM maven:3-jdk-11 as builder
# create app folder for sources
RUN mkdir -p /build
WORKDIR /build
COPY /ssbd02/pom.xml /build
#Download all required dependencies into one layer
RUN mvn -B dependency:resolve dependency:resolve-plugins
#Copy source code
COPY /ssbd02/src /build/src
# Build application
RUN mvn package
FROM bitnami/wildfly:24.0.1
WORKDIR /build
ADD /build/target/ssbd02-Kv1.0.0.war /opt/bitnami/wildfly/standalone/deployments
