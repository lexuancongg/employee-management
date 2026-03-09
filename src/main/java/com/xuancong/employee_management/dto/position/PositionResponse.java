package com.xuancong.employee_management.dto.position;

import com.xuancong.employee_management.model.Position;

public record PositionResponse(
        Long id,
        String name
) {
    public static PositionResponse fromPosition(Position position) {
        return new PositionResponse(
                position.getId(), position.getName()
        );
    }

}
