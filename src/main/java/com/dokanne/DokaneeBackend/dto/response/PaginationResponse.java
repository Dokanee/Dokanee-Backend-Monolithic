package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaginationResponse<T> {
    int pageNo;

    int pageSize;

    Long totalContent;

    int totalPages;

    boolean isLastPage;

    T data;
}
