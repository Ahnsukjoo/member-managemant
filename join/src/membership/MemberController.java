package membership;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
    //컨트롤러의 역할: 브라우저에서 요청한 경로에 따른 서비스 제공

    MemberDAO dao;

    @Override
    public void init() {
        dao = new MemberDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doHandle(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doHandle(request, response);
    }

    protected void doHandle(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        String action = request.getPathInfo();
        String nextPage = null;

        if (action == null || action.equals("/listShow.do") || action.equals("/")) {
            List<MemberVO> membersList = dao.readDB();
            System.out.println(membersList);
            request.setAttribute("membersList", membersList);
            nextPage = "/views/listMembers.jsp";
        } else if (action.equals("/addList.do")) {
            String id = request.getParameter("id");
            String pwd = request.getParameter("pwd");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            dao.addDB(id, pwd, name, email);
            nextPage = "/member/listShow.do";
        } else if (action.equals("/memberForm.do")) {
            nextPage = "/views/memberForm.jsp";
        } else if (action.equals("/modMember.do")) {
            MemberVO vo = new MemberVO();
            String id = request.getParameter("id");
            String pwd = request.getParameter("pwd");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            vo.setId(id);
            vo.setPwd(pwd);
            vo.setName(name);
            vo.setEmail(email);
            dao.updateDB(vo);
            nextPage = "/member/listShow.do";
        } else if (action.equals("/modMemberForm.do")) {
            String id = request.getParameter("id");
            MemberVO memInfo = dao.readModDB(id);
            request.setAttribute("memInfo", memInfo);
            nextPage = "/views/modMemberForm.jsp";
        } else {
            nextPage = "error";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
        dispatcher.forward(request, response);
    }


}
