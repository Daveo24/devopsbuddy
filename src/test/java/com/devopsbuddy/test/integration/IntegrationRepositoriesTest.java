package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class IntegrationRepositoriesTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Optional<Plan> retrievePlan = planRepository.findById(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievePlan);
    }

    private Plan createPlan(PlansEnum plansEnum) {
        return new Plan(plansEnum);
    }

    @Test
    public void testCreateNewRole() throws Exception {
        Role userRole = createRole(RolesEnum.BASIC);
        roleRepository.save(userRole);

        Optional<Role> retrieveRole = roleRepository.findById(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrieveRole);
    }

    private Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

    @Test
    public void createNewUser() throws Exception {

        Plan basicPlan = createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createRole(RolesEnum.BASIC);
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);

        for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
        }

        basicUser = userRepository.save(basicUser);
        Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.get().getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.get().getPlan());
        Assert.assertNotNull(newlyCreatedUser.get().getPlan().getId());
        Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.get().getUserRoles();
        for (UserRole ur : newlyCreatedUserRoles
             ) {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }
    }
}
