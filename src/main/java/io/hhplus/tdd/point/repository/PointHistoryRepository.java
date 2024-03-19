package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.exception.DataBaseException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepository {
    private final PointHistoryTable pointHistoryTable;

    public List<PointHistory> getHistoryById(Long id){
        return pointHistoryTable.selectAllByUserId(id);
    }

    public PointHistory save(Long id, Long amount, TransactionType transactionType, Long updateMillis){
        try {
            return pointHistoryTable.insert(id, amount, transactionType, updateMillis);
        } catch (InterruptedException e) {
            throw new DataBaseException("포인트 히스토리 추가");
        }
    }
}
