package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.position.PositionCreateRequest;
import com.xuancong.employee_management.dto.position.PositionResponse;
import com.xuancong.employee_management.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PositionController {   
    private final PositionService positionService;

    @PostMapping("/management/positions")
    public ResponseEntity<PositionResponse> createPosition(@Valid @RequestBody PositionCreateRequest positionCreateRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(positionService.createPosition(positionCreateRequest));
    }

    @GetMapping("/management/positions")
    public ResponseEntity<PageResponse<PositionResponse>> getPositions(
            @RequestParam(name = "keyword",defaultValue = "" ,required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "pageIndex",defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false) int pageIndex

    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(positionService.getPositions(pageIndex, pageSize, keyword));

    }


    @GetMapping("management/positions/{id}")
    public ResponseEntity<PositionResponse> getPosition(@PathVariable long id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }



    @PutMapping("management/positions/{id}")
    public ResponseEntity<Void> updatePosition(@PathVariable long id,
                                               @RequestBody PositionCreateRequest positionCreateRequest) {
        positionService.updatePosition(id, positionCreateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @DeleteMapping("management/positions/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable long id) {
        positionService.deletePosition(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }




}
