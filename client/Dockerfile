FROM openjdk:8
ADD target/client.jar client.jar
ADD cli_net bin/cli_net
RUN chmod +x bin/cli_net
ENTRYPOINT ["bash"]