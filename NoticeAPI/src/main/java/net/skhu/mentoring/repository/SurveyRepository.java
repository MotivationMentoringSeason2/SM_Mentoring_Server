package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Survey;
import net.skhu.mentoring.enumeration.SurveyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByTypeIn(List<SurveyType> type);
    void deleteByIdIn(List<Long> id);
}
