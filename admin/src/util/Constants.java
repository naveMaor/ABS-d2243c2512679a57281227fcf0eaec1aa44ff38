package util;

import com.google.gson.Gson;

public class Constants {

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();


    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/absWebApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/AdminLoginResponse";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/UsersList";


    public final static String ADMIN_LOAN_LIST = FULL_SERVER_PATH + "/AdminLoanList";


}
