package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.BusinessException;
import io.hhplus.tdd.exception.ErrorMessages;

public record UserPoint(
        Long id,
        Long point,
        Long updateMillis
) {
    public UserPoint chargePoint(long amount) {
        long totalPoint = this.point + amount;
        // 충전 포인트가 양수인지 확인
        amountCheck(amount);

        // 충전 최소 금액(1,000), 최대 금액(50,000,000)
        if (1000L > amount || 50000000L < amount) {
            throw new BusinessException(ErrorMessages.POINT_NOT_APPLY);
        }

        // 충전시 포인트 최대치(50,000,000) 넘는지 확인
        if (totalPoint > 50000000L) {
            throw new BusinessException(ErrorMessages.POINT_MAXIUM);
        }
        return new UserPoint(this.id, totalPoint, this.updateMillis);
    }

    public UserPoint usePoint(long amount) {
        long totalPoint = this.point - amount;
        // 사용 포인트가 양수인지 확인
        amountCheck(amount);

        // 보유 포인트-사용 포인트가 음수가 아닌지 확인
        if (totalPoint < 0) {
            throw new BusinessException(ErrorMessages.POINT_NOT_ENOUGH);
        }
        return new UserPoint(this.id, totalPoint, this.updateMillis);
    }

    public void amountCheck(Long amount) {
        if (amount <= 0) {
            throw new RuntimeException(ErrorMessages.POINT_ONLY_POSITIVE);
        }
    }

}
