package com.javachen.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private Long total;
    private T data;

    public static PageResponse with(Page page) {
        return new PageResponse(page.getTotalElements(), page.getContent());
    }

    public static PageResponse with(Long total, Object data) {
        return new PageResponse(total, data);
    }
}
