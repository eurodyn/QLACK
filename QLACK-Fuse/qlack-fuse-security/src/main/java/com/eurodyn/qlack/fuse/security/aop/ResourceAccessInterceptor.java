package com.eurodyn.qlack.fuse.aaa.aop;

import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceOperation;
import com.eurodyn.qlack.fuse.aaa.exception.AuthorizationException;
import com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import com.eurodyn.qlack.fuse.aaa.service.OperationService;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author European Dynamics
 */
@Aspect
@Component
@Log
public class ResourceAccessInterceptor {

    @Autowired
    UserService userService;

    @Autowired
    OperationService operationService;

    @Pointcut("execution(@com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess * *(..))")
    public void annotation() {
    }

    //Check if user group is a match
    //If not (?) then if user group operations match with provided operations
    //If not, check if user operations match with provided operations
    //If any operation matches and resourceId is present match resource?
    @Before("annotation() && @annotation(resourceAccess)")
    public void secured(JoinPoint joinPoint, ResourceAccess resourceAccess) throws AuthorizationException {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        boolean isUserSuperAdmin = false;

        //First check if user is a super admin
        if (!isUserSuperAdmin) {
            String[] groups = resourceAccess.roleAccess();

            //dummy data - will be replaced by Groups from the UserDetails object
            List<String> dummygroupsOfUser = new ArrayList<>();
            dummygroupsOfUser.add("Administrator");
            dummygroupsOfUser.add("User");

            // boolean authorizesOnRole = Arrays.stream(groups).anyMatch(new HashSet<>(dummygroupsOfUser)::contains);
            // List<ResourceOperationDTO> ops = new ArrayList<>();
            // If not authorized on role level, check specific operation permissions
            // if (!authorizesOnRole) {
            //     log.info("The groups this user is assigned to are not authorized to access this resource.");
            //     ResourceOperation[] operations = resourceAccess.operations();

            // OPTION - A - list of DTOs
            // List<ResourceOperationDTO> resourceOperations = Arrays.stream(operations)
            //     .map(o -> new ResourceOperationDTO(o.operation(), o.resourceId()))
            //     .collect(Collectors.toList());

            //OPTION - B - separate String lists
            // List<String> resourceOperations = Arrays.stream(operations)
            //             //     .map(o -> o.operation())
            //             //     .collect(Collectors.toList());

            for (ResourceOperation ro : resourceAccess.operations()) {
                if (ro.resourceId() != null) {

                } else {

                }

            }
            //Provided lists from UserDetailsDTO
            List<GroupHasOperation> gho = new ArrayList<>();
            List<UserHasOperation> uho = new ArrayList<>();

            //dummy data
            List<String> dummyOperationsOfUser = new ArrayList<>();
            dummyOperationsOfUser.add("READ_USER");
            dummyOperationsOfUser.add("READ_COMPANY_USER");

            boolean groupHasOperation = false;
            boolean userHasOperation;

            userHasOperation = uho.stream().anyMatch(new HashSet<>(dummyOperationsOfUser)::contains);

            //TODO - For each operationResource pair
            //TODO - if a resourceId is defined search within to find operation and resource match

            if (userHasOperation) { //then there is a match - next step is to check if the foundUho for resourceId match
                // List<UserHasOperation> foundUho = uho.stream().anyMatch(dummyOperationsOfUser).findFirst().orElseThrow(() -> new AuthorizationException(""));


            } else {
                groupHasOperation = gho.stream().anyMatch(new HashSet<>(dummyOperationsOfUser)::contains);
            }

            // TODO - An issue arises here. I should find out how the ResourceOperation array will be mapped in order to be compared
            // TODO - to the list from UserDetailsDTO. If mapping is not viable then a simple array iteration will suffice;
            boolean authorizesOnOperation = userHasOperation || groupHasOperation;

            if (!authorizesOnOperation) {
                throw new AuthorizationException("403 - Unauthorized Access. This user is not authorized for the specific operation.");
            } else {
                //If operation is found check for resource

            }

            // TODO - should also check for resource permissions by getting the resourceId from the ws, and then get it from the db
            // TODO - then it should be compared to the ones on groupHasOperation and userHasOperation objects
        }
    }
}
// return operationService.isPermittedForGroupNameByResource(resourceAccess.roleAccess()[0], resourceAccess.operations()[0]);
// }
    // }
