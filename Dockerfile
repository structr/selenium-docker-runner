FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y vim openjdk-8-jdk firefox wget
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.23.0/geckodriver-v0.23.0-linux64.tar.gz
RUN tar xvzf geckodriver-v0.23.0-linux64.tar.gz
RUN cp geckodriver /usr/local/bin
COPY structr-selenium-dsl-0.1-SNAPSHOT.jar /root
ENV MOZ_HEADLESS 1
WORKDIR /root
CMD java -jar structr-selenium-dsl-0.1-SNAPSHOT.jar -e firefox /tmp/testsuite -u "http://structr:8082" -s /tmp/selenium-test-summary.xml
