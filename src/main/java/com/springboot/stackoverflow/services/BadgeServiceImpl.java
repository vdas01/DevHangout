package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Badge;
import com.springboot.stackoverflow.repository.BadgeRepository;
import org.springframework.stereotype.Service;

@Service
public class BadgeServiceImpl implements BadgeService{
    BadgeRepository badgeRepository;
    BadgeServiceImpl(BadgeRepository badgeRepository){
        this.badgeRepository = badgeRepository;
    }

    @Override
    public void createBadge(String badgeName, String badgeDescription, int reputationRequired) {
        Badge badge=new Badge();
        badge.setName(badgeName);
        badge.setDescription(badgeDescription);
        badge.setReputationRequired(reputationRequired);
        badgeRepository.save(badge);
    }
}
