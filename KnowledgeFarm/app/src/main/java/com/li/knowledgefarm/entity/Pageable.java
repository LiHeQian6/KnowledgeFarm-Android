package com.li.knowledgefarm.entity;

import java.io.Serializable;

public class Pageable implements Serializable {
    private Sort sort;
    private int offset;
    private int pageNumber;
    private int pageSize;
    private boolean paged;
    private boolean unpaged;
    public void setSort(Sort sort) {
        this.sort = sort;
    }
    public Sort getSort() {
        return sort;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    public int getOffset() {
        return offset;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }
    public boolean getPaged() {
        return paged;
    }

    public void setUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
    }
    public boolean getUnpaged() {
        return unpaged;
    }
}
