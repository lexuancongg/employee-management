package com.xuancong.employee_management.dto.position;

import com.xuancong.employee_management.model.Position;

public record PositionGetResponse(
        Long id,
        String name
) {
    public static PositionGetResponse fromPosition(Position position) {
        return new PositionGetResponse(
                position.getId(), position.getName()
        );
    }

}
