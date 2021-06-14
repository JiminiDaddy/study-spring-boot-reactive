package com.chpark.study.reactive;

import com.chpark.study.reactive.domain.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemTest {
	@Test
	@DisplayName("Item의 기본기능 확인")
	void itemBasicsWork() {
		Item item = new Item("item01", "Notebook", "LG Gram", 2_000_000);

		Assertions.assertThat(item.getId()).isEqualTo("item01");
		Assertions.assertThat(item.getName()).isEqualTo("Notebook");
		Assertions.assertThat(item.getDescription()).isEqualTo("LG Gram");
		Assertions.assertThat(item.getPrice()).isEqualTo(2 * 1000 * 1000);

		Item item2 = new Item("item01", "Notebook", "LG Gram", 2_000_000);

		Assertions.assertThat(item).isEqualTo(item2);
		Assertions.assertThat(item).isNotSameAs(item2);
	}
}
