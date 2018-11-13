package com.eurodyn.qlack.fuse.aaa.service;


public interface LdapUserService {

    /**
     * Check if the user can be authenticated with LDAP using 'simple' authentication (bind operation).
     *
     * @param username The LDAP username of the user.
     * @param password The LDAP password of the user.
     * @return The AAA ID of the user if authenticated, null otherwise.
     */
    String canAuthenticate(String username, String password);

    /**
     * Checks if the LDAP is enabled
     *
     * @return true if enabled else false
     */
    boolean isLdapEnabled();

}
