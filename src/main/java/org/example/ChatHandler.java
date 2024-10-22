package org.example;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final Set<WebSocketSession> readyPlayers = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Nueva conexión: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Manejar el mensaje de "LISTO"
        String payload = message.getPayload();
        if (payload.equals("LISTO")) {
            readyPlayers.add(session);
            System.out.println("Jugador " + session.getId() + " está listo.");
            System.out.println("Jugadores listos: " + readyPlayers.size() + "/" + sessions.size());

            // Verifica si todos los jugadores están listos
            if (readyPlayers.size() == sessions.size()) {
                System.out.println("Todos los jugadores están listos. Iniciando cuenta regresiva...");
                startCountdown(); // Iniciar la cuenta regresiva si todos están listos
            }
        }

        // Reenvía el mensaje a todos los clientes conectados
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(message); // Reenviar el mensaje a todos los clientes
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        readyPlayers.remove(session);
        System.out.println("Conexión cerrada: " + session.getId());
    }

    private void startCountdown() {
        final int[] countdownTime = {10}; // 10 segundos de cuenta regresiva

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (countdownTime[0] >= 0) {
                    broadcastMessage(Integer.toString(countdownTime[0])); // Enviar el tiempo restante
                    countdownTime[0]--; // Decrementar el tiempo de cuenta regresiva
                } else {
                    broadcastMessage("¡Comienza el juego!");
                    timer.cancel(); // Detener el temporizador
                }
            }
        }, 0, 1000); // Ejecutar cada segundo
    }

    private void broadcastMessage(String message) {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message)); // Enviar mensaje a todos los clientes
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
