package br.com.exemplo.roteador;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface QueueGateway {
	@Gateway(requestChannel = IntegrationFlowDefinitions.HANDLER_FLOW, replyChannel = GatewayChannels.REPLY,replyTimeout = 10000)
	byte[] handle(@Payload Request payload, @Header("partitionKey") String a);
}