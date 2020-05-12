package com.li.knowledgefarm.entity.QuestionEntity;

import com.li.knowledgefarm.entity.Pageable;
import com.li.knowledgefarm.entity.Sort;

import java.io.Serializable;
import java.util.List;

public class QuestionPage<T> implements Serializable {
    private List<T> content;
    private Pageable pageable;
    private int totalPages;
    private boolean last;
    private int totalElements;
    private Sort sort;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    private boolean empty;
    public void setContent(List<T> content) {
        this.content = (List<T>) content;
    }
    public List<T> getContent() {
        return (List<T>) content;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
    public Pageable getPageable() {
        return pageable;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getTotalPages() {
        return totalPages;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
    public boolean getLast() {
        return last;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
    public int getTotalElements() {
        return totalElements;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
    public Sort getSort() {
        return sort;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
    public boolean getFirst() {
        return first;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    public boolean getEmpty() {
        return empty;
    }
}
