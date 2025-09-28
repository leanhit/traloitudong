package com.chatbot.botmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkspaceResponse {
    private String id;
    private String name;
    private String audience;
    private List<String> bots;
    private List<Role> roles;
    private String defaultRole;
    private String adminRole;
    private List<Pipeline> pipeline;
    private String rolloutStrategy;
    private List<String> authStrategies;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Role {
        private String id;
        private String name;
        private String description;
        private List<Rule> rules;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rule {
        private String res;
        private String op;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pipeline {
        private String id;
        private String label;
        private String action;
        private List<String> reviewers;
        private int minimumApprovals;
        private String reviewSequence;
    }
}
