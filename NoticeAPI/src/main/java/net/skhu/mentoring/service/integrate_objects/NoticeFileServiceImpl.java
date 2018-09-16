package net.skhu.mentoring.service.integrate_objects;

import net.skhu.mentoring.domain.File;
import net.skhu.mentoring.domain.Image;
import net.skhu.mentoring.domain.Post;
import net.skhu.mentoring.repository.FileRepository;
import net.skhu.mentoring.repository.ImageRepository;
import net.skhu.mentoring.repository.PostRepository;
import net.skhu.mentoring.service.interfaces.NoticeFileService;
import net.skhu.mentoring.vo.NoticeFileBriefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeFileServiceImpl implements NoticeFileService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<NoticeFileBriefVO> fetchByNoticePostIdFileList(final Long postId) {
        return fileRepository.findByPostId(postId).stream()
                .map(file -> NoticeFileBriefVO.builtToVO(file))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> fetchByNoticePostIdImageIdList(final Long postId) {
        return fileRepository.findByPostId(postId).stream()
                .map(image -> image.getId())
                .collect(Collectors.toList());
    }

    @Override
    public File fetchByNoticeFileId(final Long fileId) {
        Optional<File> file = fileRepository.findById(fileId);
        if(file.isPresent()) return file.get();
        else return null;
    }

    @Override
    public Image fetchByNoticeImageId(final Long imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        if(image.isPresent()) return image.get();
        else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeNoticeFileUploading(final Long postId, final MultipartFile[] files) throws IOException {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            boolean beUploaded = true;
            for (MultipartFile file : files) {
                if(file.getSize() <= 0) {
                    beUploaded = false;
                    break;
                }
                Optional<File> tmpFile = fileRepository.findByFileNameAndPost(file.getOriginalFilename(), post.get());
                if(!tmpFile.isPresent()){
                    if(!Arrays.equals(tmpFile.get().getFileData(), file.getBytes())){
                        File updateFile = tmpFile.get();
                        updateFile.setFileSize(file.getBytes().length);
                        updateFile.setFileData(file.getBytes());
                        updateFile.setUploadDate(LocalDateTime.now());
                        fileRepository.save(updateFile);
                    }
                } else {
                    File createFile = new File(0L, post.get(), file.getOriginalFilename(), file.getBytes().length, file.getBytes(), LocalDateTime.now());
                    fileRepository.save(createFile);
                }
            }
            return beUploaded ? new ResponseEntity<>("게시물의 첨부파일들이 정상적으로 올라갔습니다.", HttpStatus.OK) : new ResponseEntity<>("게시물 등록 도중 알 수 없는 파일이 있습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else {
            return new ResponseEntity<>("게시물이 존재하지 않아서 첨부파일이 올라가지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeNoticeImageUploading(final Long postId, final MultipartFile[] images) throws IOException {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            boolean beUploaded = true;
            for (MultipartFile image : images) {
                if(image.getSize() <= 0) {
                    beUploaded = false;
                    break;
                }
                Optional<Image> tmpImage = imageRepository.findByFileNameAndPost(image.getOriginalFilename(), post.get());
                if(!tmpImage.isPresent()){
                    if(!Arrays.equals(tmpImage.get().getFileData(), image.getBytes())){
                        Image updateImage = tmpImage.get();
                        updateImage.setFileSize(image.getBytes().length);
                        updateImage.setFileData(image.getBytes());
                        updateImage.setUploadDate(LocalDateTime.now());
                        imageRepository.save(updateImage);
                    }
                } else {
                    Image createImage = new Image(0L, post.get(), image.getOriginalFilename(), image.getBytes().length, image.getBytes(), LocalDateTime.now());
                    imageRepository.save(createImage);
                }
            }
            return beUploaded ? new ResponseEntity<>("게시물에서 추가한 이미지들이 정상적으로 올라갔습니다.", HttpStatus.OK) : new ResponseEntity<>("게시물 등록 도중 알 수 없는 이미지 파일이 있습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else {
            return new ResponseEntity<>("게시물이 존재하지 않아서 첨부파일이 올라가지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeNoticeFileRemoving(final Long fileId) {
        if(fileRepository.existsById(fileId)){
            fileRepository.deleteById(fileId);
            return ResponseEntity.ok("현재 선택하신 파일이 삭제 되었습니다.");
        } else return new ResponseEntity<>("선택하신 파일이 없어서 삭제 작업을 진행하지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    public ResponseEntity<String> executeNoticeFileRemovingMultiple(final List<Long> ids) {
        fileRepository.deleteByIdIn(ids);
        return ResponseEntity.ok("게시물에서 선택하신 파일이 삭제 되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeNoticeFileRemovingByPostId(final Long postId) {
        if(fileRepository.existsByPostId(postId)){
            fileRepository.deleteByPostId(postId);
            return ResponseEntity.ok("현재 선택한 게시물의 파일들이 삭제 되었습니다.");
        } else {
            return new ResponseEntity<>("현재 선택한 게시물에 파일이 존재하지 않습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    public ResponseEntity<String> executeNoticeImageRemovingMultiple(final List<Long> ids) {
        imageRepository.deleteByIdIn(ids);
        return ResponseEntity.ok("현재 선택하신 이미지들이 삭제 되었습니다.");
    }
}
