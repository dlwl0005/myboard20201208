package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import mvc.command.NullHandler;

/**
 * Servlet implementation class ControllerUsingURI
 */

public class ControllerUsingURI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String prefix = "/WEB-INF/view/";
	private String suffix = ".jsp";
	private Map<String, CommandHandler> map;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerUsingURI() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	map = new HashMap<>();
    	
    	ServletConfig config = getServletConfig();
    	String configFilePath 
    		= config.getInitParameter("configFile").trim();
    	
    	System.out.println(config);
    	System.out.println(configFilePath);
    	ServletContext application = getServletContext();
  		String filePath = application
  				.getRealPath(configFilePath);
  		System.out.println(filePath);
  		
  		try (FileReader fr = new FileReader(filePath);) {
  			Properties properties = new Properties();
  			properties.load(fr);
  			
  			Set<Object> keys = properties.keySet();
  			
  			for (Object key : keys) {
  				Object value = properties.get(key);
  				String className = (String) value;
  				System.out.println("value:" + value);
  				try {
  					Class c = Class.forName(className);
  					Object o = c.newInstance();
  					System.out.println("o:"+o);
  					CommandHandler handler = (CommandHandler) o;
  					map.put((String) key, handler);
  				} catch (Exception e) {
  					e.printStackTrace();
  				}
  			}
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	private void process(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();
		String root = request.getContextPath();
		String command = "";
		if (uri.startsWith(root)) {
			command = uri.substring(root.length());
		}
		System.out.println(uri);
		System.out.println(root);
		System.out.println(command);
		CommandHandler handler = map.get(command);
		System.out.println(handler);
		if (handler == null) {
			handler = new NullHandler();
		}
		
		String view = null;
		try {
			view = handler.process(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (view != null) {
		request.getRequestDispatcher(prefix + view + suffix)
			.forward(request, response);
		}
		}

}





