# DZ_7 — Smart Home

This project includes:
- Android UI for a smart-home air conditioner screen
- Backend webhook in Java that accepts commands from Yandex Alice
- Local integration over HTTP for the app and server

## Run the backend

From the project root:

```bash
javac server/SmartHomeServer.java
java -cp server SmartHomeServer
```

Then check:

- http://localhost:8080/health
- POST to http://localhost:8080/alice with JSON payload:

```json
{"request":{"command":"включи кондиционер и установи температуру 22"}}
```

## Android app connection

The app sends requests to:

```text
http://10.0.2.2:8080/alice
```

This address is for Android emulator. On a real device, replace it with your server IP or public HTTPS endpoint.

## Real Yandex Alice integration

A real production integration requires:
- public HTTPS endpoint
- Yandex Skill / Dialogs webhook
- valid SSL certificate
- backend that handles commands and returns Alice response JSON

This project implements the correct architecture pattern for that integration: Android app -> backend webhook -> Yandex Alice.
