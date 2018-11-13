package com.eurodyn.qlack.fuse.aaa.service;

public interface VerificationService {

    String createVerificationToken(String userId, long expiresOn);

    String createVerificationToken(String userId, long expiresOn, String data);

    String verifyToken(String tokenID);

    void deleteExpired();

    void deleteToken(String tokenID);

    String getTokenPayload(String tokenID);

    String getTokenUser(String tokenID);

}
