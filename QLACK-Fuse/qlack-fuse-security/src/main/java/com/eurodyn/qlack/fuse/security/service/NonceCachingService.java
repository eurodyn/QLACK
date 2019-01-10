package com.eurodyn.qlack.fuse.security.service;

/**
 * A service that manage the nonce cache operations.
 *
 * @author EUROPEAN DYNAMICS SA
 */
public interface NonceCachingService {

    /**
     * Returns a value stored to a user cache for a given nonce key.
     *
     * Creates the user cache if it doesn't exist.
     *
     * @param username Authenticated user name
     * @param nonce Nonce value to lookup
     * @param type Value type class
     * @param <T> Value type
     * @return Value mapped to nonce key or null if key doesn't exist
     */
    <T> T getValueForUser(String username, String nonce, Class<T> type);

    /**
     * Puts a new pair <nonce, value> to a user cache.
     *
     * Creates the user cache if it doesn't exist.
     *
     * @param username Authenticated user name
     * @param nonce Nonce key
     * @param value Value that should be mapped to the nonce key
     */
    void putForUser(String username, String nonce, Object value);

    /**
     * Clears a user cache from all mappings.
     *
     * @param username Authenticated user name
     */
    void clear(String username);

}
