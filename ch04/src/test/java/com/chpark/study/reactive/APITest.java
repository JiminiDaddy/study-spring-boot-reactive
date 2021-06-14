package com.chpark.study.reactive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// Reactive에서 WebClient 인스턴스를 생성하기 위해 사용
@AutoConfigureWebTestClient
class APITest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	@DisplayName("/ API가 정상적으로 동작하는지 확인")
	void main() {
		webTestClient.get().uri("/")	.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.TEXT_HTML)
			.expectBody(String.class)
			.consumeWith(exchangeResult ->
				assertThat(exchangeResult.getResponseBody().contains("<a href=\"add"))
			);
	}
}
