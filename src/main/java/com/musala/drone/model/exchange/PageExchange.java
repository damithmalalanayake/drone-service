package com.musala.drone.model.exchange;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageExchange<T> {
    List<T> items;
    Integer page;
    Integer pageSize;
    Long totalItems;
    Integer totalPages;
}
