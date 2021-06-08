package com.chpark.study.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

public class ReactorExample {
	public static void main(String[] args) {
		// 예외가 발생한 지점과 예외의 원인이 된 스레드가 서로 다르더라도 Hooks는 StackStrace에 의미 있는 정보를 담을 수 있다.
		// 단, 스택 세부정보를 스레드 경계를 넘어 전달하는 과정에서 성능 문제가 발생하므로, 운영 환경에서는 절대로 사용하지 않는다.
		Hooks.onOperatorDebug();

		Mono<Integer> source;
		if (new Random().nextBoolean()) {
			source = Flux.range(1, 10).elementAt(5);
		} else {
			source = Flux.just(1, 2, 3, 4).elementAt(5);
		}

		source.subscribeOn(Schedulers.parallel()).block();
	}
}
