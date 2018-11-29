package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    String createUser(UserDTO dto);

    void updateUser(UserDTO dto, boolean updatePassword);

    void deleteUser(String userID);

    UserDTO getUserById(String userID);

    Set<UserDTO> getUsersById(Collection<String> userIDs);

    Map<String, UserDTO> getUsersByIdAsHash(Collection<String> userIDs);

    UserDTO getUserByName(String userName);

    void updateUserStatus(String userID, byte status);

    byte getUserStatus(String userID);

    boolean isSuperadmin(String userID);

    boolean isExternal(String userID);

    String canAuthenticate(String username, String password);

    UserDTO login(String userID, String applicationSessionID, boolean terminateOtherSessions);

    void logout(String userID, String applicationSessionID);

    void logoutAll();

    List<SessionDTO> isUserAlreadyLoggedIn(String userID);

    String updatePassword(String username, String newPassword);

    boolean updatePassword(String username, String oldPassword, String newPassword);

    boolean belongsToGroupByName(String userID, String groupName, boolean includeChildren);

    void updateAttributes(Collection<UserAttributeDTO> attributes, boolean createIfMissing);

    void updateAttribute(UserAttributeDTO attributeDTO, boolean createIfMissing);

    void deleteAttribute(String userID, String attributeName);

    UserAttributeDTO getAttribute(String userID, String attributeName);

    Set<String> getUserIDsForAttribute(Collection<String> userIDs, String attributeName, String attributeValue);

    Iterable<UserDTO> findUsers(UserSearchCriteria criteria);

    long findUserCount(UserSearchCriteria criteria);

    boolean isAttributeValueUnique(String attributeValue, String attributeName, String userID);

}
