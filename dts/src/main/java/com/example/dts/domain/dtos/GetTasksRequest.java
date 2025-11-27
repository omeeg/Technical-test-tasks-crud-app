package com.example.dts.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object passed down to services from GET query endpoint
 */
public class GetTasksRequest {

    @Min(value = 0, message = "Page must be at least 0")
    private int page = 0; // default page

    @Min(value = 1, message = "Size must be at least 1")
    private int size = 10; // default page size

    @NotBlank(message = "Order by field can't be blank")
    private String orderBy = "dueDate"; // default ordering field

    private boolean ascending = true; // default order

    public GetTasksRequest() {
    }

    public GetTasksRequest(int page, int size, String orderBy, boolean ascending) {
        this.page = page;
        this.size = size;
        this.orderBy = orderBy;
        this.ascending = ascending;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}