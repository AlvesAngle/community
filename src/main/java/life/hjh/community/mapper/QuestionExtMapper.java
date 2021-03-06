package life.hjh.community.mapper;

import life.hjh.community.dto.QuestionQueryDTO;
import life.hjh.community.model.Question;
import life.hjh.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Tue Mar 03 16:17:39 CST 2020
     */
    int incView(@Param("record") Question record);
    int incCommentCount(@Param("record") Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO); //通过 关键搜索词查询 记录数目

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO); // 通过 关键词 实现分页查询
}