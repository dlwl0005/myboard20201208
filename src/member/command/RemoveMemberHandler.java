package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.User;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import member.service.RemoveMemberService;
import mvc.command.CommandHandler;

public class RemoveMemberHandler implements CommandHandler {
   private static final String FORM_VIEW = "removeMemberForm";
   private RemoveMemberService removeMemberSvc = new RemoveMemberService();
   
   @Override
   public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
      if (req.getMethod().equalsIgnoreCase("GET")) {
         return processForm(req, res);
      } else if (req.getMethod().equalsIgnoreCase("POST")) {
         return processSubmit(req, res);
      } else {
         res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
         return null;
      }
   }
   
   public String processForm(HttpServletRequest req, HttpServletResponse res) {
      return FORM_VIEW;
   }
   
   private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
      
      
      Map<String, Boolean> errors = new HashMap<>();
      req.setAttribute("errors", errors);

      String pwd = req.getParameter("password");
      
      if(pwd == null || pwd.isEmpty()) {
         errors.put("p", true);
      }
      
      if (!errors.isEmpty()) {
         return FORM_VIEW;
      }
      User user = (User)req.getSession().getAttribute("authUser");
      
      
      //서비스에게 일 시키기
      try {
         removeMemberSvc.removeMember(user, pwd);
         req.getSession().invalidate();
         return "removeMemberSuccess";
      } catch (InvalidPasswordException e) {
         errors.put("bad", true);
         return FORM_VIEW;
      } catch (MemberNotFoundException e) {
         res.sendError(HttpServletResponse.SC_BAD_REQUEST);
         return null;
      }

   }
}