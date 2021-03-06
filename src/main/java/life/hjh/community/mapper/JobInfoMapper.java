package life.hjh.community.mapper;

import java.util.List;
import life.hjh.community.model.JobInfo;
import life.hjh.community.model.JobInfoExample;
import life.hjh.community.model.JobInfoWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface JobInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    long countByExample(JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int deleteByExample(JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int insert(JobInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int insertSelective(JobInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    List<JobInfoWithBLOBs> selectByExampleWithBLOBsWithRowbounds(JobInfoExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    List<JobInfoWithBLOBs> selectByExampleWithBLOBs(JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    List<JobInfo> selectByExampleWithRowbounds(JobInfoExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    List<JobInfo> selectByExample(JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    JobInfoWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int updateByExampleSelective(@Param("record") JobInfoWithBLOBs record, @Param("example") JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int updateByExampleWithBLOBs(@Param("record") JobInfoWithBLOBs record, @Param("example") JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int updateByExample(@Param("record") JobInfo record, @Param("example") JobInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int updateByPrimaryKeySelective(JobInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(JobInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table job_info
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    int updateByPrimaryKey(JobInfo record);
}