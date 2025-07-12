package com.soosmart.facts.utils.pagination;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PageMapperUtils {

    public static Pageable createPageableWithoutSearch(PaginatedRequest paginatedRequest) {
        Sort sort = paginatedRequest.sort() == null ?
                Sort.by(Sort.Direction.DESC, "create_at") :
                Sort.by(Sort.Direction.ASC, paginatedRequest.sort());
        return PageRequest.of(paginatedRequest.page(), paginatedRequest.pagesize(), sort);
    }

    public static <T, R> CustomPageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {

        return new CustomPageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
