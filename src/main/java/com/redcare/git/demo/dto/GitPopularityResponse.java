package com.redcare.git.demo.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GitPopularityResponse {
    Integer totalCount;
    Boolean incompleteResults;
    List<ObjectNode> items;
}
