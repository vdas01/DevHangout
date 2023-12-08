package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Badge;

import java.util.List;

public interface BadgeService {
    void createBadge(String badgeName, String badgeDescription, int reputationRequired);
    public void checkAndAssignBadges(Integer userId);

    List<Badge> getBadgeList();
}
