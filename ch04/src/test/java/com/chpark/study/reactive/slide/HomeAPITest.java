package com.chpark.study.reactive.slide;

import com.chpark.study.reactive.domain.Cart;
import com.chpark.study.reactive.domain.Item;
import com.chpark.study.reactive.service.CartService;
import com.chpark.study.reactive.service.ItemService;
import com.chpark.study.reactive.web.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(HomeController.class)
class HomeAPITest {
	@Autowired
	private WebTestClient webTestClient;

	// ItemService 의존주입을 위해 MockBean 생성
	// WebFluxTest는 통합 테스트가 아니므로 ApplicationContext에 @Service는 포함되지 않는다.
	@MockBean
	private ItemService itemService;

	@MockBean
	private CartService cartService;

	@Test
	@DisplayName("/ 요청시 home화면으로 이동한다")
	void home() {
		when(cartService.findCart("My Cart")).thenReturn(Mono.just(
			new Cart("My Cart")
		));

		when(cartService.findAllItems()).thenReturn(Flux.just(
			new Item("Item1_Id", "Item1_Name", "item1_Description", 1500.0),
			new Item("Item2_Id", "Item2_Name", "item2_Description", 4500.0)
		));

		webTestClient.get().uri("/").exchange()
			//.expectStatus().isOk()
			.expectBody(String.class)
			.consumeWith(exchangeResult -> {
				assertThat(exchangeResult.getResponseBody()).contains("action=\"/add/Item1_Id\"");
				assertThat(exchangeResult.getResponseBody()).contains("action=\"/add/Item2_Id\"");
			});
	}
}
