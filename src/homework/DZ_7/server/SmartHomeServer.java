import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartHomeServer {
    private static final SmartHomeState STATE = new SmartHomeState();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/health", exchange -> {
            String response = "OK";
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        });

        server.createContext("/alice", exchange -> {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String command = extractCommand(requestBody);
            String responseText = handleCommand(command);
            String json = "{\"response\":{\"text\":\"" + escapeJson(responseText)
                    + "\",\"end_session\":false},\"version\":\"1.0\"}";

            byte[] payload = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, payload.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(payload);
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Smart home Alice webhook started on http://localhost:8080/alice");
    }

    private static String extractCommand(String body) {
        Pattern pattern = Pattern.compile("\"command\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String handleCommand(String rawCommand) {
        String command = rawCommand == null ? "" : rawCommand.toLowerCase(Locale.ROOT).trim();
        if (command.isEmpty()) {
            return "Команда не распознана. Попробуйте: включи кондиционер, установи температуру 22, включи турбо режим.";
        }

        if (command.contains("выключ") || command.contains("off")) {
            STATE.setOn(false);
            return "Кондиционер выключен.";
        }

        if (command.contains("включ") || command.contains("on")) {
            STATE.setOn(true);
        }

        if (command.contains("турбо")) {
            STATE.setTurbo(true);
        }

        if (command.contains("обычный") || command.contains("eco") || command.contains("эконом")) {
            STATE.setTurbo(false);
        }

        if (command.contains("таймер") || command.contains("timer")) {
            STATE.setTimerMinutes(20);
        }

        Matcher matcher = Pattern.compile("(\\d{2})").matcher(command);
        if (matcher.find()) {
            int parsed = Integer.parseInt(matcher.group(1));
            if (parsed >= 16 && parsed <= 30) {
                STATE.setTemperature(parsed);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(STATE.isOn() ? "Кондиционер включён" : "Кондиционер выключен");
        sb.append(". Температура ").append(STATE.getTemperature()).append("°C");
        if (STATE.isTurbo()) {
            sb.append(". Турбо режим активирован.");
        } else {
            sb.append(". Режим эконом.");
        }
        if (STATE.getTimerMinutes() > 0) {
            sb.append(" Таймер на ").append(STATE.getTimerMinutes()).append(" минут.");
        }
        return sb.toString();
    }

    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static final class SmartHomeState {
        private boolean on = true;
        private int temperature = 25;
        private boolean turbo = false;
        private int timerMinutes = 0;

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public boolean isTurbo() {
            return turbo;
        }

        public void setTurbo(boolean turbo) {
            this.turbo = turbo;
        }

        public int getTimerMinutes() {
            return timerMinutes;
        }

        public void setTimerMinutes(int timerMinutes) {
            this.timerMinutes = timerMinutes;
        }
    }
}
