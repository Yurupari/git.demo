package com.redcare.git.demo.dto;

public record GitSearchRequest(
        String query,
        String sort,
        String order,
        Integer perPage,
        Integer page
) {
}
