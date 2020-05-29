package com.knowledge_farm.entity;

/**
 * @ClassName Progress
 * @Description
 * @Author 张帅华
 * @Date 2020-05-28 21:53
 */
public class Progress {
    private long bytesRead;
    private long contentLength;
    private long items;

    public long getBytesRead() {
        return bytesRead;
    }
    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }
    public long getContentLength() {
        return contentLength;
    }
    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
    public long getItems() {
        return items;
    }
    public void setItems(long items) {
        this.items = items;
    }
    @Override
    public String toString() {
        return "Progress [bytesRead=" + bytesRead + ", contentLength=" + contentLength + ", items=" + items + "]";
    }

}
