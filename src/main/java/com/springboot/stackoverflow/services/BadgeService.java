package com.springboot.stackoverflow.services;

public interface BadgeService {
    void createBadge(String badgeName, String badgeDescription, int reputationRequired);
}
