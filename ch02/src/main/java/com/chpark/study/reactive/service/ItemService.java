package com.chpark.study.reactive.service;

import com.chpark.study.reactive.domain.Item;
import com.chpark.study.reactive.domain.ItemRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final ReactiveFluentMongoOperations fluentMongoOperations;

	public ItemService(ItemRepository itemRepository, ReactiveFluentMongoOperations fluentMongoOperations) {
		this.itemRepository = itemRepository;
		this.fluentMongoOperations = fluentMongoOperations;
	}

	public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
		// 검색어를 통해 Item 객체를 생성한다.
		Item item = new Item(name, description, 0.0);
		// useAnd == true 인 경우 AND조건이므로 모든 필드가 매칭이어야 하고, (matchingall)
		// useAnd == false 인 경우 OR조건이므로 하나 이상의 필드만 매칭되면 된다. (matchingAny)
		ExampleMatcher matcher = ( useAnd
			? ExampleMatcher.matchingAll()
			: ExampleMatcher.matchingAny())
			// 스트링이 부분적으로 일치해도 찾는다.
			.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
			// 대소문자 구분하지 않는다.
			.withIgnoreCase()
			// ExampleMatcher는 기본적으로 null 필드를 무시한다. 기본 타입 double은 not null이므로, 무시하는게 불가능하다.
			// 따라서 명시적으로 price 필드가 무시되도록 설정해준다.
			.withIgnorePaths("price");

		// 생성한 Item과 Matcher를 통해 Example 객체를 생성한다.
		Example<Item> probe = Example.of(item, matcher);
		// 검색 조건에 해당하는 모든 상품을 검색한다.
		return itemRepository.findAll(probe);
	}

	public Flux<Item> searchByFluentExample(String name, String description) {
		// 평문형 연산(Fluent operation)에서는 부분 일치 기능 및 null 필드에 대한 검색 기능은 사용할 수 없다.
		return fluentMongoOperations.query(Item.class)
			.matching(query(
				//where("Keyboard").is(name).and("Keychrone_bluetooth_keyboard").is(description)))
				where("Keyboard").is(name)))
			.all();
		/*  위 API를 MongoDB Query로 변환하면 아래와 같다.
			{ $and: [ { name: 'Keyboard' },
					  { description: 'Keychone bluetooth keyboard' }
					]
			};
		 */
	}

	public Flux<Item> searchByFluentExample(String name, String description, boolean useAnd) {
		Item item = new Item(name, description, 0.0);
		ExampleMatcher matcher = (useAnd
			? ExampleMatcher.matchingAll()
			: ExampleMatcher.matching())
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase()
				.withIgnorePaths("price");
		return fluentMongoOperations.query(Item.class)
			.matching(query(byExample(Example.of(item, matcher))))
			.all();
	}
}
