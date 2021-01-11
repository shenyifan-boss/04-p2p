package com.bjpowernode.vo;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/27
 **/
//分页使用的类
public class page {
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalRows;
    private Integer totalPage;

    public page(Integer pageNo, Integer pageSize, Integer totalRows) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRows = totalRows;
    }

    public page() {
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalPage() {
        int pageSize=9;
        if(totalRows%pageSize==0){
            totalPage=totalRows/pageSize;
        }else {
            totalPage=totalRows/pageSize+1;
        }
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

}
