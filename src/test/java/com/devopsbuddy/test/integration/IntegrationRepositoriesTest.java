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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
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
    public void testCreateNewRole()throws Exception{
        Role userRole=createRole(RolesEnum.BASIC);
        roleRepository.save(userRole);
        Optional<Role> reterivedRole=roleRepository.findById(RolesEnum.BASIC.getId());
        Assert.assertNotNull(reterivedRole);

    }

    private Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }
    
    @Test
    public void testDeleteUser() throws Exception {
    	User basicUser=createUser();
    	userRepository.deleteById(basicUser.getId());
    }
    
    @Test
    public void testCreateNewUser() throws Exception{
       User basicUser=createUser();

        Optional<User> newlyCreatedUser=userRepository.findById(basicUser.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.get().getId() !=0);
        Assert.assertNotNull(newlyCreatedUser.get().getPlan());
        Assert.assertNotNull(newlyCreatedUser.get().getPlan().getId());
        Set<UserRole> newlyCreateduserRoles=newlyCreatedUser.get().getUserRoles();
        for (UserRole ur:newlyCreateduserRoles){
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }

    }

    private User createUser(){
        Plan basicPlan=createPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser=UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole=createRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles=new HashSet<>();
        UserRole userRole=new UserRole(basicUser,basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        basicUser=userRepository.save(basicUser);
        return basicUser;

    }
}
