package life.hjh.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/*
* 分页
* Ctrl + w  不断扩充代码块
* */

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;//判断是否有前一页
    private boolean showFirstPage; //判断是否为第一页
    private boolean showNext; //判断是否有下一页
    private boolean showEndPage; //判断是否为尾页
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage; //总页数

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;
        /*向前向后 展示3个页码*/
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
