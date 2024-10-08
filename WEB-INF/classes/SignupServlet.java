import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.sql.*;
import java.io.*;
import java.util.UUID;
import java.nio.*;


public class SignupServlet extends HttpServlet {
    //Creating unique user Ids
    public static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
    public static UUID asUuid(byte[] bytes) {
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
    }

    //Dispaly html page
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("/views/signup.html");
        view.forward(req, res);
    }

    //Send new sign up info to database 
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String errMsg = "";
        Connection con = null;

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            try {
                //DATABASE CONNECTION LINE
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Class.forName("oracle.jdbc.OracleDriver");
            } catch (Exception ex) {}

            //DATABASE CONNECTION LINE 
            con = DatabaseUtil.getConnection();
             Statement statement = con.createStatement();

            //Query database for the user name
            ResultSet rs = statement.executeQuery("SELECT password FROM users WHERE username  =\"" + username + "\"");

            if (rs.next()) { //if something is returned get the password
                username = null;
            } else {
                //Create insert statement for database
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO users (ID, username, password, role) VALUES (?, ?, ?, ?)");
                UUID userId = UUID.randomUUID();
                preparedStatement.setBytes(1, asBytes(userId));

                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, "g");
                
                int row = preparedStatement.executeUpdate();

                preparedStatement.close();
            }

            
        } catch(SQLException ex) {
            
            System.out.println("FAILED ON SIGNUP");
            while (ex != null) { 
                System.out.println("Message: " + ex.getMessage ()); 
                System.out.println("SQLState: " + ex.getSQLState ()); 
                System.out.println("ErrorCode: " + ex.getErrorCode ()); 
                ex = ex.getNextException(); 
                System.out.println("");
            } 

        }

        
        if(username != null){
            //Session creation
            HttpSession session = req.getSession(true);
            session.setAttribute("USER_ID", username);
            res.setStatus(302);

            res.sendRedirect("home");
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 401
            req.setAttribute("errorMessage", "This username is already in use.");
            RequestDispatcher view = req.getRequestDispatcher("/views/401.jsp");
            view.forward(req, res);
            return;
        }
        

    }
}