package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Post;
import net.skhu.mentoring.domain.Type;
import net.skhu.mentoring.model.NoticePagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.model.PostModel;
import net.skhu.mentoring.vo.NoticePostBriefVO;
import net.skhu.mentoring.vo.NoticePostMainVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeIntegrateService {
    List<OptionModel> fetchSearchBy();
    List<OptionModel> fetchOrderBy();
    List<Type> fetchNoticeTypes();
    Type fetchTypeById(final Long typeId);
    List<NoticePostBriefVO> fetchByPaginationModel(final NoticePagination noticePagination);
    NoticePostMainVO fetchPostById(final Long postId);
    PostModel fetchPostModelById(final Long postId);
    Post executeCreatingPost(final PostModel postModel, final String writer);
    Post executeUpdatingPost(final Long postId, final PostModel postModel);
    ResponseEntity<String> executeRemovingPost(final Long postId);
    ResponseEntity<String> executeRemovingMultiplePosts(final List<Long> ids);
    ResponseEntity<String> executeRemovingByUserId(final String userId);
}
