package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.position.PositionCreateRequest;
import com.xuancong.employee_management.dto.position.PositionGetResponse;
import com.xuancong.employee_management.dto.position.PositionPagingGetResponse;
import com.xuancong.employee_management.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api")
public class PositionController {
    private final PositionService positionService;

    @PostMapping("/positions")
    public ResponseEntity<PositionGetResponse> createPosition(@Valid @RequestBody PositionCreateRequest positionCreateRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(positionService.createPosition(positionCreateRequest));
    }

    @GetMapping("/positions")
    public ResponseEntity<PositionPagingGetResponse> getPositions(
            @RequestParam(name = "keyword",defaultValue = "" ,required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "pageIndex",defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false) int pageIndex

    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(positionService.getPositions(pageIndex, pageSize, keyword));

    }


    @GetMapping("/positions/{id}")
    public ResponseEntity<PositionGetResponse> getPosition(@PathVariable long id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }



    @PutMapping("/positions/{id}")
    public ResponseEntity<Void> updatePosition(@PathVariable long id,
                                               @RequestBody PositionCreateRequest positionCreateRequest) {
        positionService.updatePosition(id, positionCreateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @DeleteMapping("/positions/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable long id) {
        positionService.deletePosition(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }




}
