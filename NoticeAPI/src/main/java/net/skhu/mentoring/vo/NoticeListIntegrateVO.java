package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.model.NoticePagination;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListIntegrateVO {
    private List<NoticePostBriefVO> posts;
    private NoticePagination pagination;

    public static NoticeListIntegrateVO buildToVO(List<NoticePostBriefVO> posts, NoticePagination pagination){
        return new NoticeListIntegrateVO(posts, pagination);
    }
}
