package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.DataBaseException;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepository {
    private final UserPointTable userPointTable;

    public UserPoint getPointsById(Long id) {
        try {
            return userPointTable.selectById(id);
        } catch (InterruptedException e) {
            throw new DataBaseException("포인트 조회");
        }
    }

    public UserPoint use(Long id, Long amount) {
        try {
            return userPointTable.insertOrUpdate(id, amount);
        } catch (InterruptedException e) {
            throw new DataBaseException("포인트 사용");
        }
    }

    public UserPoint charge(Long id, Long amount) {
        try {
            return userPointTable.insertOrUpdate(id, amount);
        } catch (InterruptedException e) {
            throw new DataBaseException("포인트 충전");
        }
    }

}
