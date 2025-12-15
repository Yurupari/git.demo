package com.redcare.git.demo.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public record GitPopularityResponse(
    Integer totalCount,
    Boolean incompleteResults,
    List<ObjectNode> items
) {}
