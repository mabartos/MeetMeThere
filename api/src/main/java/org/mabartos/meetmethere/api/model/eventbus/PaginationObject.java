package org.mabartos.meetmethere.api.model.eventbus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationObject {
    private Integer firstResult;
    private Integer maxResult;
}
