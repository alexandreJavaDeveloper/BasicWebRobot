# BasicWebRobot

## Description
- There is the source 'https://www.openanswers.co.uk/careers/join-us' where is necessary to calculate so fast the operation that comes, but is impossible to answer this operation without a software to execute it.
- The site expect, I believe, unless 1 second to solve this.
- That's why this project exists, to provide requests using the HTTP protocol (GET and POST) to access this site.
- Basically, it collects the operation to be executed via HTTP GET, extract the string value, execute the correct operation  and send a HTTP POST to the form with the response in the body request.

## Instructions to execute
- git clone https://github.com/alexandreJavaDeveloper/BasicWebRobot.git
- cd BasicWebRobot/
- mvn clean package
- At this stage, will be executed all tests that execute the Robot. This uses all structure of the code of this project.