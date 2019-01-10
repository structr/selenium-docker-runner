# selenium-docker-runner

### Prerequisites
- Selenium IDE
- Docker
- curl

### Usage

    test.sh <Structr deployment repository> <testsuite.side>
    
### Note
In order for an exported .side file to be usable with the automatic test setup, a small change must be made to the exported file:

    {
      "id": "b10bb8b4-d9a2-48ca-9212-1060ae91649e",
      "version": "1.1",
      "name": "demo1",
      "url": "http://localhost:8082",
          "tests": [{...

becomes

    {
      "id": "b10bb8b4-d9a2-48ca-9212-1060ae91649e",
      "version": "1.1",
      "name": "demo1",
      "url": "http://structr:8082",
          "tests": [{...
          

### Selenium IDE Download
https://www.seleniumhq.org/selenium-ide/
