package br.com.exemplo.roteador;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class Request {
	private String id;
	private long origin;
	private int messageIndex;

}
