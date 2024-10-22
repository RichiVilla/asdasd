import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@RestController
@EnableWebSocketMessageBroker
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat") // Endpoint para recibir mensajes
    public void sendMessage(Message message) {
        // Enviar el mensaje a todos los clientes conectados
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
