**Katalon automatic systemtests**
_____________________________

Due to the fact, that the feedback tool concentrates on a website as an UI it is possible to use Website testing mechanisms like Selenium or (Katalon in our case)

How to run this Tests:

Prequisites: Firefox Browser (alternatively Chrome), IDE with JUnit Runtime

These tests are captured with Katalon, a popular browser extension for Google Chrome and exported in Java Source Files.
All Tests are compatible to IDEs with a JUnit Runtime Environment installed (e.g. STS, Eclipse...).

Starting them seperately (like every other JUnit Test) will cause the Google Chrome Browser/ Firefox Browser installed to your computer to pop-up and cycle through the tests.

For all tests (except the LoginLogoff Test) it is needed to be logged in to the https://feedback.hschat.app/#/ site e.g. with the following User Data:

Email: admin@hschat.app
Password: Admin123
