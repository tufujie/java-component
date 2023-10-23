package com.jef.entity;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * @author Jef
 * @date 2020/4/4
 */
public class MailAuthenticator extends Authenticator {

    public static String USERNAME;
    public static String PASSWORD;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
    }

}