package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public class UserIntegrationTest extends AbstractIntegrationTest {
    
    @Rule public TestName testName = new TestName();

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



    @Test
    public void testCreateNewRole()throws Exception{
        Role userRole=createRole(RolesEnum.BASIC);
        roleRepository.save(userRole);
        Optional<Role> reterivedRole=roleRepository.findById(RolesEnum.BASIC.getId());
        Assert.assertNotNull(reterivedRole);

    }
    
    @Test
    public void testDeleteUser() throws Exception {
    	String username = testName.getMethodName();
    	String email = testName.getMethodName() + "@me.com";
    	
    	User basicUser=createUser(username, email);
    	userRepository.deleteById(basicUser.getId());
    }
    
    @Test
    public void testCreateNewUser() throws Exception{
    	
    	String username = testName.getMethodName();
    	String email = testName.getMethodName() + "@me.com";
    	
    	User basicUser=createUser(username, email);

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
}
