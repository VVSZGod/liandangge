package com.jiamian.translation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjunyu
 * @date 2019/11/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<E> implements Serializable {
    // 结果集
    private List<E> list;

    // 查询记录总数
    private int totalRecords;

    // 每页多少条记录
    private int pageSize;

    // 第几页
    private int pageNo;

    private int totalPages;

    /**
     * @return 总页数
     */
    public void countTotalPages() {
        this.totalPages = (totalRecords + pageSize - 1) / pageSize;
    }

    /**
     * 计算当前页开始记录
     *
     * @return 当前页开始记录号
     */
    public int countOffset() {
        int offset = pageSize * (pageNo - 1);
        return offset;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}