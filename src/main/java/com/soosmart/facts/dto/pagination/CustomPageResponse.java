package com.soosmart.facts.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomPageResponse<T>{
    private List<T> content;
    private long totalElements;
    private int totalPages;
}
