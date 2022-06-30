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
    public final static String CLIENTS_INFO= FULL_SERVER_PATH + "/AdminClientInfo";
    public final static String INCREASE_YAZ= FULL_SERVER_PATH + "/increaseYaz";
    public final static String REWIND_TIME= FULL_SERVER_PATH + "/rewindTime";
    public final static String LOAD_NORMAL_DATA= FULL_SERVER_PATH + "/loadNormal";


    public final static String ADMIN_LOAN_LIST = FULL_SERVER_PATH + "/AdminLoanList";
    public final static String GET_INNER_TABLE_OBJ = FULL_SERVER_PATH + "/innerTable";


}
