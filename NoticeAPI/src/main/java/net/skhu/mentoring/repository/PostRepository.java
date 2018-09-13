package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Post;
import net.skhu.mentoring.model.NoticePagination;
import net.skhu.mentoring.model.OptionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<OptionModel> searchBy = Arrays.asList(new OptionModel(1, "제목 포함 검색"), new OptionModel(2, "내용 포함 검색"), new OptionModel(3, "회원 아이디 검색"));
    List<OptionModel> orderBy = Arrays.asList(new OptionModel(1, "최신 순서"), new OptionModel(2, "오래된 순서"), new OptionModel(3, "제목 오름차순"), new OptionModel(4, "제목 내림차순"), new OptionModel(5, "조회수 오름차순"), new OptionModel(6, "조회수 내림차순"));
    Sort[] sort = {
        new Sort(Sort.Direction.DESC, "writtenDate"),
        new Sort(Sort.Direction.ASC, "id"),
        new Sort(Sort.Direction.ASC, "title"),
        new Sort(Sort.Direction.DESC, "title"),
        new Sort(Sort.Direction.ASC, "views"),
        new Sort(Sort.Direction.DESC, "views")
    };
    default List<Post> findAll(NoticePagination noticePagination){
        Pageable pageable = new PageRequest(noticePagination.getPg() - 1, noticePagination.getSz(), sort[(noticePagination.getOb() != 0) ? noticePagination.getOb() - 1 : noticePagination.getOb()]);
        Page<Post> page;
        String searchText = noticePagination.getSt();
        switch(noticePagination.getSb()){
            case 1 :
                page = this.findByTypeIdAndTitleContains(noticePagination.getTid(), searchText, pageable);
                break;
            case 2 :
                page = this.findByTypeIdAndContextContains(noticePagination.getTid(), searchText, pageable);
                break;
            case 3 :
                page = this.findByTypeIdAndUserId(noticePagination.getTid(), searchText, pageable);
                break;
            default :
                page = this.findByTypeId(noticePagination.getTid(), pageable);
                break;
        }
        noticePagination.setRequestCount(page.getTotalElements());
        return page.getContent();
    }
    Page<Post> findByTypeId(Long typeId, Pageable pageable);
    Page<Post> findByTypeIdAndTitleContains(Long typeId, String title, Pageable pageable);
    Page<Post> findByTypeIdAndContextContains(Long typeId, String context, Pageable pageable);
    Page<Post> findByTypeIdAndUserId(Long typeId, String userId, Pageable pageable);
}
