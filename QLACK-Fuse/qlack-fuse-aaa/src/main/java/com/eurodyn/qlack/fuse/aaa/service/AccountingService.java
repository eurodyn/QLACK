package com.eurodyn.qlack.fuse.aaa.service;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;

/**
 * Provides accounting information for the user. For details regarding the functionality offered see the respective interfaces.
 *
 * @author European Dynamics SA
 */
public interface AccountingService {

    String createSession(SessionDTO sessionDTO);

    void terminateSession(String sessionID);

    void terminateSessionByApplicationSessionId(String applicationSessionId);

    SessionDTO getSession(String sessionID);

    Long getUserLastLogIn(String userID);

    Long getUserLastLogOut(String userID);

    Long getUserLastLogInDuration(String userID);

    long getNoOfTimesUserLoggedIn(String userID);

    Set<String> filterOnlineUsers(Collection<String> userIDs);


    void updateAttribute(SessionAttributeDTO attribute,
        boolean createIfMissing);

    void updateAttributes(Collection<SessionAttributeDTO> attributes,
        boolean createIfMissing);

    void deleteAttribute(String sessionID, String attributeName);

    SessionAttributeDTO getAttribute(String sessionID, String attributeName);

    Set<String> getSessionIDsForAttribute(Collection<String> sessionIDs,
        String attributeName, String attributeValue);

    boolean isAttributeValueUnique(String userId, String attributeName,
        String attributeValue);

    void deleteSessionsBeforeDate(Date date);

    Page<SessionDTO> getSessions(String userId, Pageable pageable);


}
