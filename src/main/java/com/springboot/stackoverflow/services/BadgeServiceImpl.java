package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Badge;
import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.repository.BadgeRepository;
import com.springboot.stackoverflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeServiceImpl implements BadgeService{
    BadgeRepository badgeRepository;
    UserRepository userRepository;
    BadgeServiceImpl(BadgeRepository badgeRepository,UserRepository userRepository){
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createBadge(String badgeName, String badgeDescription, int reputationRequired) {
        Badge badge=new Badge();
        badge.setName(badgeName);
        badge.setDescription(badgeDescription);
        badge.setReputationRequired(reputationRequired);
        badgeRepository.save(badge);
    }
    @Override
    public void checkAndAssignBadges(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Badge> badges = badgeRepository.findByReputationRequiredLessThanEqual(user.getReputation());
            user.setUserBadges(badges);
            userRepository.save(user);
        }
    }

    @Override
    public List<Badge> getBadgeList() {
        return badgeRepository.findAll();
    }
}
