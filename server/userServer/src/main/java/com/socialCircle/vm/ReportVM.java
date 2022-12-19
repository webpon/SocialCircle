package com.socialCircle.vm;

import com.socialCircle.entity.Image;
import com.socialCircle.entity.Report;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReportVM implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer userId;
    /**
     * 被封号人
     */
    private Integer reportUserId;
    private String content;
    private List<Image> images;

    public ReportVM() {
    }

    public ReportVM(Report report) {
        this.id = report.getId();
        this.userId = report.getUserId();
        this.reportUserId = report.getReportUserId();
        this.content = report.getContent();
    }
}