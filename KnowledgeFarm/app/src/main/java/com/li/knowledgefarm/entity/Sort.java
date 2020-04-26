package com.li.knowledgefarm.entity;

import java.io.Serializable;

public class Sort implements Serializable {
    private boolean unsorted;
    private boolean sorted;
    private boolean empty;
    public void setUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
    }
    public boolean getUnsorted() {
        return unsorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }
    public boolean getSorted() {
        return sorted;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    public boolean getEmpty() {
        return empty;
    }
}
