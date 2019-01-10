FROM node:latest
RUN apt-get update
RUN apt-get install -y vim firefox-esr
RUN npm install -g selenium-side-runner
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.23.0/geckodriver-v0.23.0-linux64.tar.gz
RUN tar xvzf geckodriver-v0.23.0-linux64.tar.gz
RUN cp geckodriver /usr/local/bin
ENV MOZ_HEADLESS 1

CMD selenium-side-runner -c "browserName=firefox" /tmp/*.side
