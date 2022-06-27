package utils;

import jakarta.servlet.ServletContext;
import engine.Engine;

public class ServletUtils {



    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();

    public static Engine getSystemEngine(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new Engine());
            }
        }
        return (Engine) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }


    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */

    private static final String ADMIN_LOGGED_IN_ATTRIBUTE_NAME = "admin_logged_in";

    private static final Object adminManagerLock = new Object();

    public static boolean getAdminManger(ServletContext servletContext) {
        synchronized (adminManagerLock) {
            if (servletContext.getAttribute(ADMIN_LOGGED_IN_ATTRIBUTE_NAME)==null) {
                servletContext.setAttribute(ADMIN_LOGGED_IN_ATTRIBUTE_NAME, true);
                return (boolean) servletContext.getAttribute(ADMIN_LOGGED_IN_ATTRIBUTE_NAME);
            }
            else
            {
                return false;
            }
        }
    }


}
