package com.chpark.study.reactive.blockhound;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

class BlockHoundTest {
	@Test
	@DisplayName("리액터 스레드 내에서도 발생한 Blocking 호출을 blockhound가 잡아낸다")
	void threadSleepIsBlockingCall() {
		// Mono.delay()는 전체 플로우를 Reactor 스레드에서 실행되도록 구성한다.
		// Blockhound는 Reactor 스레드 내에서 호출되는 Blocking Code를 검출 가능하다.
		Mono.delay(Duration.ofSeconds(1))
			.flatMap(tick -> {
				try {
					Thread.sleep(10);		// Blocking !!
					return Mono.just(true);
				} catch (InterruptedException e) {
					return Mono.error(e);
				}
			})
			.as(StepVerifier::create)
			// Blocking Code가 검출되었으므로 테스트가 실패한다.
			// 아래 코드는 예외가 발생하는것을 의도했으므로 테스트가 통과된다.
			.verifyErrorMatches(throwable -> {
				//Assertions.assertThat(throwable.getMessage()).contains("Blocking call!");
				Assertions.assertThat(throwable.getClass()).isEqualTo(BlockingOperationError.class);
				return true;
			});

		/* Blockhound가 검출해내는 Blocing Code
			1. java.lnag.Thread#sleep()
			2. Socket, 네트워크 연산
			3. 파일 접근 메서드 일부
			=> BlockHound 클래스 안에 있는 Builder 클래스의 blockingModes hashMap을 통해 상세하게 알 수 있다.
		 */
	}
}

