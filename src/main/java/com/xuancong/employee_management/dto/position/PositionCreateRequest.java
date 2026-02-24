package com.xuancong.employee_management.dto.position;

import com.xuancong.employee_management.model.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PositionCreateRequest(
        @NotBlank(message ="Position name must not be  blank" )
                @Size(max = 100,message = "too long")
        String name

) {
    public Position toPosition(){
        return Position.builder()
                .name(this.name)
                .build();
    }
}
