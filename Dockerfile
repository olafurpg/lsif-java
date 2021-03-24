FROM openjdk:8-jdk-alpine
RUN apk add --no-cache git curl \
    && git config --global user.email "you@example.com" \
    && git config --global user.name "Your Name" \
    && curl -L https://sourcegraph.com/.api/src-cli/src_linux_amd64 -o /src \
    && chmod +x /src
COPY bin/coursier coursier
ENTRYPOINT /coursier launch -r sonatype:snapshots com.sourcegraph:lsif-java-server_2.13:0.2.0-47-a019e8ae-SNAPSHOT -M com.sourcegraph.package_server.PackageServer -- --host 0.0.0.0 --port $PORT --src /src

