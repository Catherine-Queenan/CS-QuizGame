import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;

import java.util.ArrayList;

import org.json.JSONObject;

public class DeleteQuestionServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            res.sendRedirect("login");
            return;
        }

        String username = (String) session.getAttribute("USER_ID");
        String role = (String) session.getAttribute("USER_ROLE"); 
        // getUserRoleFromDatabase(username);

        if (!"a".equals(role)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 401
            req.setAttribute("errorMessage", "You are not authorized to access this page.");
            RequestDispatcher view = req.getRequestDispatcher("/views/401.jsp");
            view.forward(req, res);
            return;
        }
        int idIndex = Integer.parseInt(req.getParameter("id"));
        String quizName = req.getParameter("quizName");
        ArrayList<AClass> questions = (ArrayList<AClass>)session.getAttribute("questions");
        
        System.out.println(quizName);
        for(AClass q: questions){
            System.out.println(q.serialize());
        }

        session.removeAttribute("questions");
        // Connection con = null;
        // PreparedStatement ps = null;

        try {
            // DATABASE CONNECTION LINE
            // Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL Driver
            // Class.forName("oracle.jdbc.OracleDriver"); // Oracle Driver

            
            IRepository repository = new Repository();
            repository.init("com.mysql.cj.jdbc.Driver");
            JSONObject deleteQuestion = questions.get(idIndex).serialize();
            System.out.println("HEREEEE  " + deleteQuestion);
            repository.delete("question", deleteQuestion.getString("id"));
            
            // DATABASE CONNECTION LINE
            // con = DatabaseUtil.getConnection();
            // Delete the question
            // String deleteQuestionSql = "DELETE FROM questions WHERE id = ?";
            // ps = con.prepareStatement(deleteQuestionSql);
            // ps.setBinaryStream(1, qIDs.get(idIndex));
            // ps.executeUpdate();

            // Redirect back to the edit questions page after successful deletion
            res.sendRedirect("editQuestions?quizName=" + quizName);

        } catch (Exception e) {
            e.printStackTrace();
        } 
        // finally {
        //     try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        //     try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        // }
    }
    //     private String getUserRoleFromDatabase(String username) {
    //         Connection con = null;
    //         PreparedStatement ps = null;
    //         ResultSet rs = null;
    //         String role = null;
    
    //         try {
    //             Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL Driver

    //             con = DatabaseUtil.getConnection();

    //             // Query to get the user's role
    //             String sql = "SELECT role FROM users WHERE username = ?";
    //             ps = con.prepareStatement(sql);
    //             ps.setString(1, username);
    //             rs = ps.executeQuery();
    
    //             if (rs.next()) {
    //                 role = rs.getString("role");
    //             }
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         } finally {
    //             try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
    //             try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
    //             try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
    //         }
    
    //         return role;
        
    // }
}
