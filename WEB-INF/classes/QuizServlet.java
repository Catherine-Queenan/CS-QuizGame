import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONObject;
import org.json.JSONArray;

public class QuizServlet extends HttpServlet {

    private final IRepository repository = new Repository();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("USER_ID") == null) {
            res.sendRedirect("login");
            return;
        }

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        // Initialize JSON object to store the response
        JSONObject jsonResponse = new JSONObject();  
        // if (session == null) {
        //     res.setStatus(302);
        //     res.sendRedirect("login");
        //     return;
        // }

        // String username = (String) session.getAttribute("USER_ID");
        // String role = getUserRoleFromDatabase(username); // Fetch the role from DB
        String role = (String) session.getAttribute("USER_ROLE"); // Fetch the role from DB
        String category = req.getParameter("categoryName");
        System.out.println("category: "+category);
        
        if ("a".equals(role)) {
            jsonResponse.put("role", "admin");
        }

        // Connection con = null;
        // PreparedStatement psQuiz = null;
        // ResultSet rsQuiz = null;
        // PreparedStatement psMedia = null;
        // PreparedStatement psQuizMedia = null;
        StringBuilder quizzesHtml = new StringBuilder();
        StringBuilder mediaHtml = new StringBuilder();
        // ResultSet rsMedia = null;
        // ResultSet rsQuizMedia = null;

        JSONArray quizzesArray = new JSONArray();

        try {
            repository.init("com.mysql.cj.jdbc.Driver");
            String criteria = "category_name = \"" + category + "\"";
            ArrayList<AClass> quizzes = repository.select("quiz", criteria);

            // con = DatabaseUtil.getConnection();

            // psQuiz = con.prepareStatement("SELECT name, description FROM quizzes WHERE category_name = \"" + category + "\";");
            // rsQuiz = psQuiz.executeQuery();

            // psMedia = con.prepareStatement("SELECT media_id FROM quiz_media WHERE quiz_name = ?");
            // psQuizMedia = con.prepareStatement("SELECT media_file_path FROM media WHERE id = ?");


            for(AClass quiz : quizzes){
                JSONObject quizJSON = quiz.serialize();
                // String quizName = quizJSON.getString("name");
                // String quizDescription = quizJSON.getString("description");
                
                if(!quizJSON.isNull("media_id")){
                    String media_id = quizJSON.getString("media_id");
                    ArrayList<AClass> quizMedia = repository.select("media", media_id);
                    JSONObject quizMediaJSON = quizMedia.get(0).serialize();
                    
                    //String mediaFilePath = quizMediaJSON.getString("media_file_path");
                    //mediaHtml.append("<img src=\"").append(mediaFilePath).append("\" alt=\"").append(quizName).append("\" class=\"categoryImg\">");
                    quizJSON.put("media", quizMediaJSON);
                }

                quizzesArray.put(quizJSON);

        //         quizzesHtml.append("<div class=\"quiz\">\n")
        //                 .append("       <form method=\"post\">\n")
        //                 .append("           <input type=\"hidden\" name=\"quizName\" value=\"").append(quizName).append("\" />\n")
        //                 .append("           <input type=\"submit\" value=\"").append(quizName).append("\" />\n")
        //                 // .append("    <label for=\"").append(quizName).append("\">").append(quizName).append("</label>")
        //                 .append("           <p class=\"quiz-description\">").append(quizDescription).append("</p>\n")
        //                 .append("               <div class=\"img\">").append(mediaHtml.toString()).append("</div>\n")
        //                 .append("       </form>\n");
                
        //         // Show "Add Question" and "Delete Quiz" buttons only for admin users
        //         if ("a".equals(role)) {
        //             quizzesHtml.append("<div class=\"adminBtnWrap\">")
        //                     // .append("    <button type=\"button\" onclick=\"window.location.href='addQuestion?quizName=")
        //                     // .append(quizName).append("'\">Add Question</button>\n")
        //                     .append("    <button type=\"button\" onclick=\"window.location.href='deleteQuiz?quizName=")
        //                     .append(quizName).append("'\">Delete Quiz</button>\n")
        //                     .append("    <button type=\"button\" onclick=\"window.location.href='edit?quizName=")
        //                     .append(quizName).append("'\">Edit Quiz</button>\n</div>");
        //         }

                //quizzesHtml.append("</div>\n");
                
            }
            
            // while (rsQuiz.next()) {
            //     String quizName = rsQuiz.getString("name");
            //     String quizDescription = rsQuiz.getString("description");

            //     psMedia.setString(1, quizName);
            //     rsMedia = psMedia.executeQuery();

            //     while(rsMedia.next()) {

            //         InputStream mediaId = rsMedia.getBinaryStream("media_id");

            //         if(mediaId != null) {
            //             psQuizMedia.setBinaryStream(1, mediaId);
            //             rsQuizMedia = psQuizMedia.executeQuery();
            //             if(rsQuizMedia.next()) {
            //                 String mediaFilePath = rsQuizMedia.getString("media_file_path");
            //                 mediaHtml.append("<img src=\"").append(mediaFilePath).append("\" alt=\"").append(quizName).append("\" class=\"categoryImg\">");
            //             }
            //         }
            //     }

            //     quizzesHtml.append("<div class=\"quiz\">\n")
            //             .append("<form method=\"post\">\n")
            //             .append("    <input type=\"hidden\" name=\"quizName\" value=\"").append(quizName).append("\" />\n")
            //             .append("    <input type=\"submit\" value=\"").append(quizName).append("\" />\n")
            //             // .append("    <label for=\"").append(quizName).append("\">").append(quizName).append("</label>")
            //             .append("<p class=\"quiz-description\">").append(quizDescription).append("</p>\n")
            //             .append("  <div class=\"img\">").append(mediaHtml.toString()).append("</div>\n")
            //             .append("</div>\n")
            //             .append("</form>\n");
                
            //     // Show "Add Question" and "Delete Quiz" buttons only for admin users
            //     if ("a".equals(role)) {
            //         quizzesHtml.append("<div class=\"adminBtnWrap\">")
            //                 // .append("    <button type=\"button\" onclick=\"window.location.href='addQuestion?quizName=")
            //                 // .append(quizName).append("'\">Add Question</button>\n")
            //                 .append("    <button type=\"button\" onclick=\"window.location.href='deleteQuiz?quizName=")
            //                 .append(quizName).append("'\">Delete Quiz</button>\n")
            //                 .append("    <button type=\"button\" onclick=\"window.location.href='edit?quizName=")
            //                 .append(quizName).append("'\">Edit Quiz</button>\n</div>");
            //     }

            //     quizzesHtml.append("</div>\n");
            // }

            jsonResponse.put("quizzes", quizzesArray);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        // finally {
        //     try { if (rsQuiz != null) rsQuiz.close(); } catch (Exception e) { e.printStackTrace(); }
        //     try { if (rsMedia != null) rsMedia.close(); } catch (Exception e) { e.printStackTrace(); }
        //     try { if (rsQuizMedia != null) rsQuizMedia.close(); } catch (Exception e) { e.printStackTrace(); }
        //     try { if (psQuiz != null) psQuiz.close(); } catch (Exception e) { e.printStackTrace(); }
        //     try { if (psMedia != null) psMedia.close(); } catch (Exception e) { e.printStackTrace(); }
        //     try { if (psQuizMedia != null) psQuizMedia.close(); } catch (Exception e) { e.printStackTrace(); }
        //     try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        // }

        // req.setAttribute("quizzesHtml", quizzesHtml.toString());
        // // Forward the request to the quiz.jsp
        // RequestDispatcher view = req.getRequestDispatcher("/views/quiz.jsp");
        // view.forward(req, res);

        res.getWriter().write(jsonResponse.toString());
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String quizName = req.getParameter("quizName");

        // Connection con = null;
        // Statement stmnt = null;
        // ResultSet rs = null;

        try {
            // Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL Driver
            repository.init("com.mysql.cj.jdbc.Driver");

            // con = DatabaseUtil.getConnection();
            // stmnt = con.createStatement();
            // rs = stmnt.executeQuery("SELECT id FROM questions WHERE quiz_name = \"" + quizName + "\" ORDER BY rand();");
            String criteria = "quiz_name = \"" + quizName + "\" ORDER BY rand()";
            ArrayList<AClass> questions = repository.select("question", criteria);

            // ArrayList<InputStream> qIDs = new ArrayList<>();
            // while (rs.next()) {
            //     InputStream qID = rs.getBinaryStream("id");
            //     qIDs.add(qID);
            // }

            HttpSession session = req.getSession(false);
            if(session != null){
                session.setAttribute("quiz", quizName);
                session.setAttribute("questions", questions);
                session.setAttribute("currQuestion", 0);
                
                res.setStatus(302);
                res.sendRedirect("questions");
            } else {
                res.setStatus(302);
                res.sendRedirect("login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
        // finally {
        //     try {
        //         if (rs != null) rs.close();
        //     } catch (SQLException e) { e.printStackTrace(); }
        //     try {
        //         if (stmnt != null) stmnt.close();
        //     } catch (SQLException e) { e.printStackTrace(); }
        //     try {
        //         if (con != null) con.close();
        //     } catch (SQLException e) { e.printStackTrace(); }
        // }
    }

    // private String getUserRoleFromDatabase(String username) {
    //     Connection con = null;
    //     PreparedStatement ps = null;
    //     ResultSet rs = null;
    //     String role = null;

    //     try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");

    //         con = DatabaseUtil.getConnection();
            
    //         String sql = "SELECT role FROM users WHERE username = ?";
    //         ps = con.prepareStatement(sql);
    //         ps.setString(1, username);
    //         rs = ps.executeQuery();

    //         if (rs.next()) {
    //             role = rs.getString("role");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
    //         try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
    //         try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
    //     }

    //     return role;
    // }
}