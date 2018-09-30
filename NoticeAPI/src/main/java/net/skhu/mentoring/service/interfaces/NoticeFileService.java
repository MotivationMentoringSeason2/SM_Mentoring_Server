package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.File;
import net.skhu.mentoring.domain.Image;
import net.skhu.mentoring.vo.NoticeFileBriefVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoticeFileService {
    List<NoticeFileBriefVO> fetchByNoticePostIdFileList(final Long postId);
    List<Long> fetchByNoticePostIdImageIdList(final Long postId);
    File fetchByNoticeFileId(final Long fileId);
    Image fetchByNoticeImageId(final Long imageId);
    ResponseEntity<String> executeNoticeFileUploading(final Long postId, final List<MultipartFile> files) throws IOException;
    ResponseEntity<String> executeNoticeImageUploading(final Long postId, final List<MultipartFile> images) throws IOException;
    ResponseEntity<String> executeNoticeFileRemoving(final Long fileId);
    ResponseEntity<String> executeNoticeFileRemovingMultiple(final List<Long> ids);
    ResponseEntity<String> executeNoticeFileRemovingByPostId(final Long postId);
    ResponseEntity<String> executeNoticeImageRemovingByPost(final Long postId);
}
