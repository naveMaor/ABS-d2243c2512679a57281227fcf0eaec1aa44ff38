package utils;

import customes.Account;
import data.Database;
import jakarta.servlet.ServletContext;
import engine.Engine;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();

    public static Database getSystemDataBase(ServletContext servletContext) {
        synchronized (userManagerLock) {
            Engine engine = new Engine();
            //            Engine engine = Engine.getInstance();
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, engine.getDatabase());
            }
        }

        return (Database) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

}
