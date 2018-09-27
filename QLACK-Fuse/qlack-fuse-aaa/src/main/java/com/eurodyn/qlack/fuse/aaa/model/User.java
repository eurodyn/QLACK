package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The persistent class for the aaa_user database table.
 */
@Entity
@Table(name = "aaa_user")
@Getter
@Setter
public class User extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  @Column(name = "pswd")
  private String password;

  private String salt;

  private byte status;

  private String username;

  private boolean superadmin;

  /**
   * An indicator that this user's password is not held in the database of AAA.
   */
  private Boolean external = false;

  //bi-directional many-to-one association to UserHasOperation
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserHasOperation> userHasOperations;

  //bi-directional many-to-one association to Session
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Session> sessions;

  //bi-directional many-to-many association to Group
  @ManyToMany(mappedBy = "users")
  private List<Group> groups;

  //bi-directional many-to-one association to UserAttribute
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserAttribute> userAttributes;

  // bi-directional many-to-one association to VerificationToken.
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<VerificationToken> verificationTokens;

  public User() {
    setId(UUID.randomUUID().toString());
  }

//  public static User find(String userID, EntityManager em) {
//    return em.find(User.class, userID);
//  }

//  public static User findByUsername(String username, EntityManager em) {
//    Query query = em.createQuery(
//        "SELECT u FROM com.eurodyn.qlack.fuse.aaa.model.User u WHERE u.username = :username");
//    query.setParameter("username", username);
//    List<User> resultList = query.getResultList();
//
//    return resultList.isEmpty() ? null : resultList.get(0);
//  }

//  public static UserAttribute findAttribute(String userId, String attributeName,
//      EntityManager em) {
//    UserAttribute retVal = null;
//    Query query = em.createQuery("SELECT a FROM com.eurodyn.qlack.fuse.aaa.model.UserAttribute a "
//        + "WHERE a.user.id = :id AND a.name = :name");
//    query.setParameter("id", userId);
//    query.setParameter("name", attributeName);
//    List<UserAttribute> l = query.getResultList();
//    if (!l.isEmpty()) {
//      retVal = l.get(0);
//    }
//
//    return retVal;
//  }

//  public static Set<String> getAllUserIds(EntityManager em) {
//    Set<String> retVal = new HashSet<>();
//    Query query = em.createQuery("SELECT u.id FROM com.eurodyn.qlack.fuse.aaa.model.User u");
//    retVal.addAll(query.getResultList());
//    return retVal;
//  }

//  public static Set<String> getNormalUserIds(EntityManager em) {
//    Set<String> retVal = new HashSet<>();
//    Query query = em.createQuery("SELECT u.id FROM com.eurodyn.qlack.fuse.aaa.model.User u WHERE u.superadmin = false");
//    retVal.addAll(query.getResultList());
//    return retVal;
//  }

//  public static Set<String> getSuperadminUserIds(EntityManager em) {
//    Set<String> retVal = new HashSet<>();
//    Query query = em.createQuery("SELECT u.id FROM com.eurodyn.qlack.fuse.aaa.model.User u WHERE u.superadmin = true");
//    retVal.addAll(query.getResultList());
//    return retVal;
//  }

  public UserHasOperation addUserHasOperation(UserHasOperation userHasOperations) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<UserHasOperation>());
    }
    getUserHasOperations().add(userHasOperations);
    userHasOperations.setUser(this);

    return userHasOperations;
  }

  public UserHasOperation removeUserHasOperation(UserHasOperation userHasOperations) {
    getUserHasOperations().remove(userHasOperations);
    userHasOperations.setUser(null);

    return userHasOperations;
  }

  public Session addSession(Session session) {
    getSessions().add(session);
    session.setUser(this);

    return session;
  }

  public Session removeSession(Session session) {
    getSessions().remove(session);
    session.setUser(null);

    return session;
  }

  public UserAttribute addUserAttribute(UserAttribute userAttribute) {
    getUserAttributes().add(userAttribute);
    userAttribute.setUser(this);

    return userAttribute;
  }

  public UserAttribute removeUserAttribute(UserAttribute userAttribute) {
    getUserAttributes().remove(userAttribute);
    userAttribute.setUser(null);

    return userAttribute;
  }

}