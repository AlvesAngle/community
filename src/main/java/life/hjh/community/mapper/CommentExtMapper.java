package life.hjh.community.mapper;

import life.hjh.community.model.Comment;
import life.hjh.community.model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount( Comment record);
}