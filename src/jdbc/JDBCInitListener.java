package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * Application Lifecycle Listener implementation class JDBCInitListener
 *
 */
@WebListener
public class JDBCInitListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public JDBCInitListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
       ServletContext application = sce.getServletContext();
       
       String url = application.getInitParameter("jdbcUrl");
       String user = application.getInitParameter("jdbcUser");
       String password = application.getInitParameter("jdbcPassword");
       
       try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

//	try (
//			Connection con = DriverManager.getConnection(url, user, password);
//		) {
//		System.out.println("연결 잘 됨");
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
       
	ConnectionProvider.setUrl(url);
	ConnectionProvider.setUser(user);
	ConnectionProvider.setPassword(password);
	
	
	// context root 경로
	String contextPath = application.getContextPath();
	application.setAttribute("root", contextPath);
}
}
