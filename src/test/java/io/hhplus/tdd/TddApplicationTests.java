package io.hhplus.tdd;

import io.hhplus.tdd.exception.BusinessException;
import io.hhplus.tdd.exception.ErrorMessages;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TddApplicationTests {
	private UserPoint userPoint;
	private PointHistory pointHistory;

	@BeforeEach
	void setUp(){
		userPoint = new UserPoint(1L, 10000L, System.currentTimeMillis());
		pointHistory = new PointHistory(1L, 1L, TransactionType.CHARGE, 10000L, System.currentTimeMillis());

	}
	// 포인트 사용
	@ParameterizedTest
	@ValueSource(longs = {1000L, 2500L})
	@DisplayName("포인트 사용 - 포인트 사용시 변경되는 값 확인")
	void 사용시_포인트_변경(long amount) {
		Assertions.assertThat(userPoint.usePoint(amount).point())
				.isEqualTo(10000L-amount);
	}

	@ParameterizedTest
	@ValueSource(longs = {11000L})
	@DisplayName("포인트 사용 - 보유 포인트보다 사용 포인트가 초과인지 확인")
	void 사용할_포인트_여부확인(long amount) {
		Assertions.assertThatThrownBy(()-> userPoint.usePoint(amount))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorMessages.POINT_NOT_ENOUGH);

	}

	// 포인트 충전
	@ParameterizedTest
	@ValueSource(longs = {10000L, 50000L})
	@DisplayName("포인트 충전 - 포인트 충전시 변경되는 값 확인")
	void 충전시_포인트_변경(long amount) {
		Assertions.assertThat(userPoint.chargePoint(amount).point())
				.isEqualTo(10000L+amount);
	}

	@ParameterizedTest
	@ValueSource(longs = {100L, 50000001L})
	@DisplayName("포인트 충전 - 충전 최소(1,000) & 최대금액(50,000,000) 확인")
	void 포인트충전금액_미달_초과(long amount) {
		Assertions.assertThatThrownBy(() -> userPoint.chargePoint(amount))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorMessages.POINT_NOT_APPLY);
	}

	@ParameterizedTest
	@ValueSource(longs = {49999000L})
	@DisplayName("포인트 충전 - 충전시 포인트 최대치(50,000,000) 넘는지 확인")
	void 기존포인트금액_최대치(long amount) {
		Assertions.assertThatThrownBy(() -> userPoint.chargePoint(amount))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorMessages.POINT_MAXIUM);
	}


}
