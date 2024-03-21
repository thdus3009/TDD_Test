package io.hhplus.tdd;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.BusinessException;
import io.hhplus.tdd.exception.ErrorMessages;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service에 대한 단위 테스트
 */
@SpringBootTest(classes = TddApplicationTests.class)
@ExtendWith(MockitoExtension.class)
class TddApplicationTests {
	private UserPoint userPoint;
	private List<PointHistory> pointHistories;

	// 데이터를 바로 넣지말고 Repository > Mockito를 이용
	@Mock(lenient = true)
	private UserPointRepository userPointRepository;
	@Mock(lenient = true)
	private PointHistoryRepository pointHistoryRepository;

	@InjectMocks
	private PointService pointService;


	@BeforeEach
	void setUp(){
		userPoint = new UserPoint(1L, 10000L, System.currentTimeMillis());
		pointHistories = null;

		// 의존성 주입
		UserPointRepository userPointRepository = new UserPointRepository(
				new UserPointTable()
		);
		PointHistoryRepository pointHistoryRepository = new PointHistoryRepository(
				new PointHistoryTable()
		);
		pointService = new PointService(userPointRepository, pointHistoryRepository);

	}
	// 포인트 조회
	@ParameterizedTest
	@ValueSource(longs = {1000L, 2500L})
	@DisplayName("포인트 조회 - 포인트 최종 조회값 확인")
	void 포인트_조회(long amount) {
		// given (기본값 설정)
		UserPoint userPoint1 = new UserPoint(1L, amount, System.currentTimeMillis());
		Mockito.when(userPointRepository.charge(userPoint.id(), amount)).thenReturn(userPoint1);
		UserPoint userPoint2 = pointService.charge(userPoint.id(), amount);

		// when

		// then
		Assertions.assertThat(userPoint1.point())
				.isEqualTo(userPoint2.point());
	}

	static Stream<Arguments> generateData() {
		return Stream.of(
				Arguments.of(Arrays.asList(5000L, 1000L, 2000L, 4000L))
		);
	}
	// 포인트 이력 조회
	@ParameterizedTest
	@MethodSource("generateData")
	@DisplayName("포인트 조회 - 포인트 이력 확인")
	void 포인트_이력_조회(List<Long> amounts) {
		// given (기본값 설정)
		Mockito.when(userPointRepository.charge(userPoint.id(), amounts.get(0))).thenReturn(userPoint);
		pointService.charge(userPoint.id(), 5000L);
		Mockito.when(userPointRepository.use(userPoint.id(), amounts.get(1))).thenReturn(userPoint);
		pointService.use(userPoint.id(), 1000L);
		Mockito.when(userPointRepository.charge(userPoint.id(), amounts.get(2))).thenReturn(userPoint);
		pointService.charge(userPoint.id(), 2000L);
		Mockito.when(userPointRepository.use(userPoint.id(), amounts.get(3))).thenReturn(userPoint);
		pointService.use(userPoint.id(), 4000L);

		// when
		Mockito.when(pointHistoryRepository.getHistoryById(userPoint.id())).thenReturn(pointHistories);
		pointHistories = pointService.getHistory(userPoint.id());

		for (int i = amounts.size()-1; i>=0; i--){
			Assertions.assertThat(pointHistories.get(3-i).amount()).isEqualTo(amounts.get(i));
		}
		// then

	}

	// 포인트 사용
	@ParameterizedTest
	@ValueSource(longs = {1000L, 2500L})
	@DisplayName("포인트 사용 - 포인트 사용시 변경되는 값 확인")
	void 사용시_포인트_변경(long amount) {
		// given (기본값 설정)
		Mockito.when(userPointRepository.charge(userPoint.id(), userPoint.point())).thenReturn(userPoint);
		Mockito.when(userPointRepository.use(userPoint.id(), amount)).thenReturn(userPoint);
		pointService.charge(userPoint.id(), userPoint.point());

		// when
		UserPoint up = pointService.use(userPoint.id(), amount);

		// then
		Assertions.assertThat(up.point())
				.isEqualTo(userPoint.point() - amount);
	}


	@ParameterizedTest
	@ValueSource(longs = {11000L})
	@DisplayName("포인트 사용 - 보유 포인트보다 사용 포인트가 초과인지 확인")
	void 사용할_포인트_여부확인(long amount) {
		// given
		Mockito.when(userPointRepository.charge(userPoint.id(), userPoint.point())).thenReturn(userPoint);
		pointService.charge(userPoint.id(), userPoint.point());

		// when

		// then
		Assertions.assertThatThrownBy(()-> pointService.use(userPoint.id(), amount))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorMessages.POINT_NOT_ENOUGH);

	}

	// 포인트 충전
	@ParameterizedTest
	@ValueSource(longs = {10000L, 50000L})
	@DisplayName("포인트 충전 - 포인트 충전시 변경되는 값 확인")
	void 충전시_포인트_변경(long amount) {
		// given
		Mockito.when(userPointRepository.charge(userPoint.id(), userPoint.point())).thenReturn(userPoint);
		pointService.charge(userPoint.id(), userPoint.point());

		// when
		UserPoint up = pointService.charge(userPoint.id(), amount);

		// then
		Assertions.assertThat(up.point())
				.isEqualTo(userPoint.point() + amount);
	}

	@ParameterizedTest
	@ValueSource(longs = {100L, 50000001L})
	@DisplayName("포인트 충전 - 충전 최소(1,000) & 최대금액(50,000,000) 확인")
	void 포인트충전금액_미달_초과(long amount) {
		// given
		Mockito.when(userPointRepository.charge(userPoint.id(), userPoint.point())).thenReturn(userPoint);
		pointService.charge(userPoint.id(), userPoint.point());

		// when

		// then
		Assertions.assertThatThrownBy(() -> pointService.charge(userPoint.id(), amount))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorMessages.POINT_NOT_APPLY);
	}

	@ParameterizedTest
	@ValueSource(longs = {49999000L})
	@DisplayName("포인트 충전 - 충전시 포인트 최대치(50,000,000) 넘는지 확인")
	void 기존포인트금액_최대치(long amount) {
		Mockito.when(userPointRepository.charge(userPoint.id(), userPoint.point())).thenReturn(userPoint);

		// when
		pointService.charge(userPoint.id(), userPoint.point());

		// then
		Assertions.assertThatThrownBy(() -> pointService.charge(userPoint.id(), amount))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorMessages.POINT_MAXIUM);
	}


}
