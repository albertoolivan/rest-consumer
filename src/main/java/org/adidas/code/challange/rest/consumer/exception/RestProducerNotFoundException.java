package org.adidas.code.challange.rest.consumer.exception;

public class RestProducerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1396819548717421855L;

	public RestProducerNotFoundException() {
		super();
	}

	public RestProducerNotFoundException(Exception e) {
		super(e);
	}

	public RestProducerNotFoundException(String message) {
		super(message);
	}

	public RestProducerNotFoundException(String message, Exception cause) {
		super(message, cause);
	}

}
