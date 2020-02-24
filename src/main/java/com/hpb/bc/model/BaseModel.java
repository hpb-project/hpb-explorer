package com.hpb.bc.model;

import java.util.LinkedList;

public class BaseModel {

    public int currentPage = 1;
    public int pageSize = 20;
    private LinkedList<String> orderByList;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public LinkedList<String> getOrderByList() {
        return orderByList;
    }

    public void setOrderByList(LinkedList<String> orderByList) {
        this.orderByList = orderByList;
    }
}
