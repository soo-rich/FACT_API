package com.soosmart.facts.dto.pagination;



import jakarta.validation.constraints.Null;


public record PaginatedRequest(
        int page,
        int pagesize,
        String search,
        @Null String sort
) {
    public PaginatedRequest(int page, int size, String sort) {
        this(page, size, sort, null); // valeur par défaut pour `search`
    }
}
