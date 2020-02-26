package com.javachen.web.auth;

public class AuthConstant {

    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_USER_ID = "user";

    public static final String COOKIE_NAME = "app-cookie";
    // header set for internal user id
    public static final String CURRENT_USER_HEADER = "current-user-id";
    // AUTHORIZATION_HEADER is the http request header
    // key used for accessing the internal authorization.
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // AUTH ERROR Messages
    public static final String ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service";
    public static final String ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header";
}
