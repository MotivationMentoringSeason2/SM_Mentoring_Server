package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.File;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFileBriefVO {
    private Long id;
    private String fileName;
    private int fileSize;
    private LocalDateTime uploadDate;
    public static NoticeFileBriefVO builtToVO(File file){
        return new NoticeFileBriefVO(file.getId(), file.getFileName(), file.getFileSize(), file.getUploadDate());
    }
}
