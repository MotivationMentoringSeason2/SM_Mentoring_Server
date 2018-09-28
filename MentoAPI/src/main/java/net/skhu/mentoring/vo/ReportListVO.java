package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportListVO {
    List<ReportBriefVO> reports;
    TotalActivityVO activity;

    public static ReportListVO builtToVO(List<ReportBriefVO> reports, TotalActivityVO activity){
        return new ReportListVO(reports, activity);
    }
}
