package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserPoint getPoints(Long id) {
        UserPoint up = userPointRepository.getPointsById(id);
        return up;
    }

    public List<PointHistory> getHistory(Long id) {
        return pointHistoryRepository.getHistoryById(id)
                .stream()
                .sorted(Comparator.comparing(PointHistory::timeMillis).reversed())
                .toList();
    }

    public UserPoint charge(Long id, Long amount) {
        UserPoint up = userPointRepository.getPointsById(id).chargePoint(amount);
        up = userPointRepository.charge(id, up.point());
        pointHistoryRepository.save(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return up;
    }

    public UserPoint use(Long id, Long amount) {
        UserPoint up = userPointRepository.getPointsById(id).usePoint(amount);
        up = userPointRepository.use(id, up.point());
        pointHistoryRepository.save(id, amount, TransactionType.USE, System.currentTimeMillis());
        return up;
    }


}
