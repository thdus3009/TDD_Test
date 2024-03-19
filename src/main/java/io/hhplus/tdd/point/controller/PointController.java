package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/point")
@RequiredArgsConstructor
@RestController
public class PointController {
    private final PointService pointService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public UserPoint points(@PathVariable Long id) { // point
        UserPoint up = pointService.getPoints(id);
        return up;
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요. (history)
     */
    @GetMapping("{id}/histories")
    public List<PointHistory> history(@PathVariable Long id) {
        List<PointHistory> phList = pointService.getHistory(id);
        return phList;
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    public UserPoint charge(@PathVariable Long id, @RequestBody Long amount) {
        UserPoint up = pointService.charge(id, amount);
        return up;
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public UserPoint use(@PathVariable Long id, @RequestBody Long amount) {
        UserPoint up = pointService.use(id, amount);
        return up;
    }
}
