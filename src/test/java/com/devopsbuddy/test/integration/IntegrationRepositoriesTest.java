package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IntegrationRepositoriesTest {

    private static final int BASIC_PLAN_ID = 1;
    private static final int BASIC_ROLE_ID = 1;

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
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);
        Optional<Plan> retrievePlan = planRepository.findById(BASIC_PLAN_ID);
        Assert.assertNotNull(retrievePlan);
    }

    private Plan createBasicPlan() {
        Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("Basic");
        return plan;
    }

    @Test
    public void testCreateNewRole() throws Exception {
        Role userRole = createBasicRole();
        roleRepository.save(userRole);

        Optional<Role> retrieveRole = roleRepository.findById(BASIC_ROLE_ID);
        Assert.assertNotNull(retrieveRole);
    }

    private Role createBasicRole() {
        Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("ROLE_USER");
        return role;
    }

//    @Test
//    public void createNewUser() throws Exception {
//
//        Plan basicPlan = createBasicPlan();
//        planRepository.save(basicPlan);
//
//        User basicUser = createBasicUser();
//        basicUser.setPlan(basicPlan);
//
//        Role basicRole = createBasicRole();
//        Set<UserRole> userRoles = new HashSet<>();
//        UserRole userRole = new UserRole();
//        userRole.setUser(basicUser);
//        userRole.setRole(basicRole);
//        userRoles.add(userRole);
//
//        basicUser.getUserRoles().addAll(userRoles);
//
//        for (UserRole ur : userRoles) {
//            roleRepository.save(ur.getRole());
//        }
//
//        basicUser = userRepository.save(basicUser);
//        Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
//        Assert.assertNotNull(newlyCreatedUser);
//        Assert.assertTrue(newlyCreatedUser.getId() != 0);
//        Assert.assertNotNull(newlyCreatedUser.getPlan());
//        Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
//        Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.getUserRoles();
//        for (UserRole ur : newlyCreatedUserRoles
//             ) {
//            Assert.assertNotNull(ur.getRole());
//            Assert.assertNotNull(ur.getRole().getId());
//        }
//    }

    private User createBasicUser() {
        User user = new User();
        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("sa@test.com");
        user.setFirstName("Joe");
        user.setLastName("Bloggs");
        user.setEnabled(true);
        user.setDescription("This is a basic user");
        user.setCountry("GB");
        user.setPhoneNumber("07792329001");
        user.setProfileImageUrl("url.url");

        return user;
    }

}
