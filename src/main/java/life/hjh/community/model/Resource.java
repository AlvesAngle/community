package life.hjh.community.model;

public class Resource {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.id
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.title
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.description
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.user_id
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.resource_name
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private String resourceName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.resource_url
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private String resourceUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.gmt_create
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.gmt_modify
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private Long gmtModify;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column resource.tag
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    private String tag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.id
     *
     * @return the value of resource.id
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.id
     *
     * @param id the value for resource.id
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.title
     *
     * @return the value of resource.title
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.title
     *
     * @param title the value for resource.title
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.description
     *
     * @return the value of resource.description
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.description
     *
     * @param description the value for resource.description
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.user_id
     *
     * @return the value of resource.user_id
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.user_id
     *
     * @param userId the value for resource.user_id
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.resource_name
     *
     * @return the value of resource.resource_name
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.resource_name
     *
     * @param resourceName the value for resource.resource_name
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName == null ? null : resourceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.resource_url
     *
     * @return the value of resource.resource_url
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public String getResourceUrl() {
        return resourceUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.resource_url
     *
     * @param resourceUrl the value for resource.resource_url
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl == null ? null : resourceUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.gmt_create
     *
     * @return the value of resource.gmt_create
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.gmt_create
     *
     * @param gmtCreate the value for resource.gmt_create
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.gmt_modify
     *
     * @return the value of resource.gmt_modify
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public Long getGmtModify() {
        return gmtModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.gmt_modify
     *
     * @param gmtModify the value for resource.gmt_modify
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setGmtModify(Long gmtModify) {
        this.gmtModify = gmtModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column resource.tag
     *
     * @return the value of resource.tag
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public String getTag() {
        return tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column resource.tag
     *
     * @param tag the value for resource.tag
     *
     * @mbg.generated Sun May 17 01:31:17 CST 2020
     */
    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }
}