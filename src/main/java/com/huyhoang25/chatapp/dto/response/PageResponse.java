package com.huyhoang25.chatapp.dto.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageResponse<T> implements Serializable {
    private int currentPage;
    private int pageSize;
    private int totalPage;
    private long totalElement;

    @Builder.Default
    private List<T> content = Collections.emptyList();
}
