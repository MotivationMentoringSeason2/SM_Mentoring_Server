package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.File;
import net.skhu.mentoring.domain.Image;
import net.skhu.mentoring.service.interfaces.NoticeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/NoticeAPI/notice")
public class FileRestController {
    @Autowired
    private NoticeFileService noticeFileService;

    private HttpHeaders generateImageHeader(Image image) {
        HttpHeaders header = new HttpHeaders();
        String fileName = image.getFileName();
        int infix = fileName.lastIndexOf('.');
        String fileSuffix = fileName.substring(infix + 1, fileName.length());
        switch (fileSuffix.toUpperCase()) {
            case "JPG":
            case "JPEG":
                header.setContentType(MediaType.IMAGE_JPEG);
                break;
            case "PNG":
                header.setContentType(MediaType.IMAGE_PNG);
                break;
            case "GIF":
                header.setContentType(MediaType.IMAGE_GIF);
                break;
        }
        return header;
    }

    @GetMapping("files/{postId}")
    public ResponseEntity<?> fetchFileListByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(noticeFileService.fetchByNoticePostIdFileList(postId));
    }

    @GetMapping("images/{postId}")
    public ResponseEntity<?> fetchImageListByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(noticeFileService.fetchByNoticePostIdImageIdList(postId));
    }

    @GetMapping("file/{fileId}")
    public void fetchFileByIdDownload(@PathVariable Long fileId, HttpServletResponse response) throws UnsupportedEncodingException {
        File file = noticeFileService.fetchByNoticeFileId(fileId);
        if(file != null) {
            String fileName = URLEncoder.encode(file.getFileName(), "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
        }
    }

    @GetMapping("image/{imageId}")
    public ResponseEntity<?> fetchImageByIdPreview(@PathVariable Long imageId) {
        Image image = noticeFileService.fetchByNoticeImageId(imageId);
        if (image != null) {
            HttpHeaders headers = this.generateImageHeader(image);
            return new ResponseEntity<>(image.getFileData(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "file/{postId}", consumes="multipart/form-data")
    public ResponseEntity<String> executeUploadingFilesByPostId(@PathVariable Long postId, @RequestPart("file") MultipartFile[] files){
        try {
            return noticeFileService.executeNoticeFileUploading(postId, files);
        } catch (IOException e) {
            return new ResponseEntity<>("게시글에 파일 올리는 중 오류가 발생했습니다.", HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping(value = "image/{postId}", consumes="multipart/form-data")
    public ResponseEntity<String> executeUploadingImagesByPostId(@PathVariable Long postId, @RequestPart("file") MultipartFile[] files){
        try {
            return noticeFileService.executeNoticeImageUploading(postId, files);
        } catch (IOException e) {
            return new ResponseEntity<>("게시글에 이미지를 포함 시키는 중 오류가 발생했습니다.", HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("file/{fileId}")
    public ResponseEntity<String> executeRemovingFileById(@PathVariable Long fileId){
        return noticeFileService.executeNoticeFileRemoving(fileId);
    }

    @DeleteMapping("files")
    public ResponseEntity<String> executeRemovingFileMultiple(@RequestBody List<Long> ids){
        return noticeFileService.executeNoticeFileRemovingMultiple(ids);
    }

    @DeleteMapping("file/post/{postId}")
    public ResponseEntity<String> executeRemovingFileByPostId(@PathVariable Long postId){
        return noticeFileService.executeNoticeFileRemovingByPostId(postId);
    }

    @DeleteMapping("images")
    public ResponseEntity<String> executeRemovingImageMultiple(@PathVariable List<Long> ids){
        return noticeFileService.executeNoticeImageRemovingMultiple(ids);
    }
}
