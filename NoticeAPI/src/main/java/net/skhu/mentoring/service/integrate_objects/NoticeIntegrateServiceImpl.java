package net.skhu.mentoring.service.integrate_objects;

import net.skhu.mentoring.domain.Post;
import net.skhu.mentoring.domain.Type;
import net.skhu.mentoring.model.NoticePagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.model.PostModel;
import net.skhu.mentoring.repository.PostRepository;
import net.skhu.mentoring.repository.TypeRepository;
import net.skhu.mentoring.service.interfaces.NoticeIntegrateService;
import net.skhu.mentoring.vo.NoticePostBriefVO;
import net.skhu.mentoring.vo.NoticePostMainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeIntegrateServiceImpl implements NoticeIntegrateService {
    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<OptionModel> fetchSearchBy(){
        return postRepository.searchBy;
    }

    @Override
    public List<OptionModel> fetchOrderBy(){
        return postRepository.orderBy;
    }

    @Override
    public List<Type> fetchNoticeTypes() {
        return typeRepository.findAll();
    }

    @Override
    public Type fetchTypeById(final Long typeId) {
        Optional<Type> type = typeRepository.findById(typeId);
        System.out.println(type.get());
        if(type.isPresent())
            return type.get();
        else return null;
    }

    @Override
    public List<NoticePostBriefVO> fetchByPaginationModel(final NoticePagination noticePagination) {
        return postRepository.findAll(noticePagination).stream()
                .map(post -> NoticePostBriefVO.builtToVO(post))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public NoticePostMainVO fetchPostById(final Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Post viewPost = post.get();
            viewPost.setViews(viewPost.getViews() + 1);
            postRepository.save(viewPost);
            return NoticePostMainVO.builtToVO(viewPost);
        }
        else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeCreatingPost(final PostModel postModel, final String writer) {
        Optional<Type> type = typeRepository.findById(postModel.getTypeId());
        if(type.isPresent()) {
            Post createPost = new Post();
            createPost.setId(0L);
            createPost.setTitle(postModel.getTitle());
            createPost.setContext(postModel.getContext());
            createPost.setWriter(writer);
            createPost.setType(type.get());
            createPost.setWrittenDate(LocalDateTime.now());
            createPost.setViews(0);
            postRepository.save(createPost);
            return ResponseEntity.ok("새로운 게시글이 저장 되었습니다.");
        } else {
            return new ResponseEntity<>("게시글을 저장하기 위한 게시판 타입이 불분명합니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeUpdatingPost(final Long postId, final PostModel postModel) {
        if(postRepository.existsById(postId)){
            Post updatePost = postRepository.getOne(postId);
            updatePost.setTitle(postModel.getTitle());
            updatePost.setContext(postModel.getContext());
            updatePost.setWrittenDate(LocalDateTime.now());
            postRepository.save(updatePost);
            return ResponseEntity.ok("게시글 내용 일부가 수정 되었습니다.");
        } else {
            return new ResponseEntity<>("게시글이 존재하지 않아 수정 작업이 진행되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemovingPost(final Long postId) {
        if(postRepository.existsById(postId)){
            postRepository.deleteById(postId);
            return ResponseEntity.ok("선택하신 게시글이 삭제 되었습니다.");
        } else {
            return new ResponseEntity<>("게시글이 존재하지 않아 삭제 작업이 진행되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemovingMultiplePosts(final List<Long> ids) {
        if(postRepository.existsByIdIn(ids)){
            postRepository.deleteByIdIn(ids);
            return ResponseEntity.ok("선택하신 게시글이 삭제 되었습니다.");
        } else {
            return new ResponseEntity<>("게시글이 존재하지 않아 삭제 작업이 진행되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemovingByUserId(final String userId) {
        if(postRepository.existsByWriter(userId)){
            postRepository.deleteByWriter(userId);
            return ResponseEntity.ok(String.format("삭제 하려는 회원 %s의 게시글이 모두 삭제 되었습니다.", userId));
        } else return ResponseEntity.ok(String.format("삭제 하려는 회원 %s의 게시글이 존재하지 않습니다.", userId));
    }
}