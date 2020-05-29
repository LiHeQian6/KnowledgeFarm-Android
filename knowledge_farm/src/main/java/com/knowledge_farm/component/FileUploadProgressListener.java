package com.knowledge_farm.component;

import com.knowledge_farm.entity.Progress;
import org.apache.tomcat.util.http.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @ClassName FileUploadProgressListener
 * @Description
 * @Author 张帅华
 * @Date 2020-05-28 21:52
 */

@Component
public class FileUploadProgressListener implements ProgressListener {
    private HttpSession session;

    public void setSession(HttpSession session){
        this.session = session;
        Progress status = new Progress();
        session.setAttribute("status", status);
    }

    @Override
    public void update(long bytesRead, long contentLength, int items) {
        Progress status = (Progress) session.getAttribute("status");
        status.setBytesRead(bytesRead);
        status.setContentLength(contentLength);
        status.setItems(items);
    }

}
