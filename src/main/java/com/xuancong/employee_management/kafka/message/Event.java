package com.xuancong.employee_management.kafka.message;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event<T> {

    private String eventType;
    private T data;

    public Event(String eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }
}
EntityManager
