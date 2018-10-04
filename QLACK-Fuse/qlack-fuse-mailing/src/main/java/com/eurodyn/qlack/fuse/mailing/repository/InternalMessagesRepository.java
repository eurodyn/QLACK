package com.eurodyn.qlack.fuse.mailing.repository;

import com.eurodyn.qlack.fuse.mailing.model.InternalMessages;

public interface InternalMessagesRepository extends MailingRepository<InternalMessages, String>{

	// findUserInbox
//	 List<InternalMessages> findbyUserid(String userId);
//	 
//	 List<InternalMessages> findUserSent(String userId); 
	
//	 long countByUserAndStatus(String userId, String status);
	 /**
	  * public static long countByUserAndStatus(EntityManager em, String userId, String status) {
    String select = "SELECT count(m) FROM InternalMessages m";

    List<String> predicates = new ArrayList<>(2);
    if (userId != null) {
      predicates.add("(m.mailTo = :userId AND m.deleteType <> 'I')");
    }
    if (status != null) {
      predicates.add("(UPPER(m.status) = :status)");
    }

    // open-coded join()
    StringBuilder sb = new StringBuilder(select);
    Iterator<String> iter = predicates.iterator();
    if (iter.hasNext()) {
      sb.append(" WHERE ").append(iter.next());
      while (iter.hasNext()) {
        sb.append(" AND ").append(iter.next());
      }
    }
    String jpql = sb.toString();

    TypedQuery<Long> q = em.createQuery(jpql, Long.class);
    if (userId != null) {
      q.setParameter("mailTo", userId);
    }
    if (status != null) {
      q.setParameter("status", status.toUpperCase());
    }

    return q.getSingleResult();
  }
	  */
	 
	 
	 
	 
}
