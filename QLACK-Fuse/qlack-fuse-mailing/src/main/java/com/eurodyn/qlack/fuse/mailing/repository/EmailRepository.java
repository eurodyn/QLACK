package com.eurodyn.qlack.fuse.mailing.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.util.MailConstants.EMAIL_STATUS;

public interface EmailRepository extends MailingRepository<Email, String>{


	
	 List<Email> findByAddedOnDateAndStatus(Long date, EMAIL_STATUS... statuses);

	
	
//	 List<Email> findQueued(byte maxTries);
	 /**
	  *  
	  *   public static List<Email> findQueued(EntityManager em, byte maxTries) {
    String jpql =
        "SELECT m FROM Email m " +
            "WHERE m.status = :status AND m.tries < :tries";

    return em.createQuery(jpql, Email.class)
        .setParameter("status", EMAIL_STATUS.QUEUED.toString())
        .setParameter("tries", maxTries)
        .getResultList();
  }

	  */
	
	 
	 /**
	  *  public static List<Email> findByDateAndStatus(EntityManager em, Long date,
      EMAIL_STATUS... statuses) {
    String select = "SELECT m FROM Email m ";

    List<String> predicates = new ArrayList<>(2);
    if (date != null) {
      predicates.add("(addedOnDate <= " + date.longValue() + ")");
    }
    if (statuses != null && statuses.length > 0) {
      // open-coded join()
      StringBuilder sb = new StringBuilder("(status IN ('");
      sb.append(statuses[0].toString());
      for (int i = 1; i < statuses.length; i++) {
        sb.append("',' ").append(statuses[i].toString());
      }
      sb.append("'))");
      predicates.add(sb.toString());
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

    return em.createQuery(jpql, Email.class).getResultList();
  }
	  */
	 
	
}
