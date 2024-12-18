import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.nio.file.Paths;

import java.util.UUID;
import java.nio.ByteBuffer;
import jakarta.servlet.annotation.MultipartConfig;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONObject;


@MultipartConfig
public class AddQuestionServlet extends HttpServlet {

    private final IRepository repository = new Repository();
    private final AClassFactory factory = new AClassFactory();

    // Convert UUID to binary (byte array)
    public byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    // Convert binary (byte array) to UUID
    public UUID bytesToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            res.sendRedirect("login");
            return;
        }

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        String username = (String) session.getAttribute("USER_ID");
        String role = (String) session.getAttribute("USER_ROLE");
        // String role = getUserRoleFromDatabase(username);

        JSONObject jsonResponse = new JSONObject();

        if (!"a".equals(role)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 401
            req.setAttribute("errorMessage", "You are not authorized to access this page.");
            RequestDispatcher view = req.getRequestDispatcher("/views/401.jsp");
            view.forward(req, res);
            return;
        }

        jsonResponse.put("role", "admin");

       // Write the JSON responses
       out.write(jsonResponse.toString());
       out.flush();
       out.close();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Connection con = null;
        // PreparedStatement psQuestion = null;
        // PreparedStatement psAnswer = null;
        // PreparedStatement psMedia = null;
        // PreparedStatement psQuestionMedia = null;
        // PreparedStatement psAnswerMedia = null;

        try {
            // Load MySQL driver and establish connection
            // Class.forName("com.mysql.cj.jdbc.Driver");
            repository.init("com.mysql.cj.jdbc.Driver");
            // con = DatabaseUtil.getConnection();
            // Get form data from the request
            // Question info
            String quizName = req.getParameter("quizName");
            String questionText = req.getParameter("questionText");
            String questionType = req.getParameter("questionType");

            // Media Info
            Collection<Part> fileParts = req.getParts(); // Files
            String[] videoUrls = req.getParameterValues("videoUrl");

            // Media end and start times
            String[] videoStartsStr = req.getParameterValues("videoStart");
            String[] videoEndsStr = req.getParameterValues("videoEnd");
            String[] audioStartsStr = req.getParameterValues("audioStart");
            String[] audioEndsStr = req.getParameterValues("audioEnd");

            // Need to be ints but retrieved as strings
            int[] videoStarts = new int[videoStartsStr.length];
            int[] videoEnds = new int[videoEndsStr.length];
            int[] audioStarts = new int[audioStartsStr.length];
            int[] audioEnds = new int[audioEndsStr.length];

            // Convert to ints
            for (int i = 0; i < videoStarts.length; i++) {
                videoStarts[i] = Integer.parseInt(videoStartsStr[i]);
                videoEnds[i] = Integer.parseInt(videoEndsStr[i]);
            }
            for (int i = 0; i < audioStarts.length; i++) {
                audioStarts[i] = Integer.parseInt(audioStartsStr[i]);
                audioEnds[i] = Integer.parseInt(audioEndsStr[i]);
            }

            // Answer Info
            String[] answerTexts = req.getParameterValues("answerText");
            String correctAnswer = req.getParameter("correctAnswer");
            String addAnotherQuestion = req.getParameter("addQuestion");
            String answerType = req.getParameter("answerType");

            UUID[] mediaIds = new UUID[answerTexts.length + 1]; // There will at most be 1 media per answer and 1 media
                                                                // for the question itself (answers + 1)
            String[] mediaUrls = new String[answerTexts.length + 1];

            String[] mediaFileNames = new String[answerTexts.length + 1];

            // for (Part part : req.getParts()) {
            // System.out.println("Part Name: " + part.getName());
            // System.out.println("Submitted File Name: " + part.getSubmittedFileName());
            // }

            int filesProcessed = 0;
            if (questionType.equals("VID")) {
                mediaUrls[filesProcessed] = videoUrls[0];
                mediaFileNames[filesProcessed] = "N/A";
                filesProcessed++;
            }

            for (Part filePart : fileParts) {
                String fileName = filePart.getSubmittedFileName();

                // Generate a UUID for the media
                // UUID mediaUUID = UUID.randomUUID();
                // byte[] mediaIdBinary = uuidToBytes(mediaUUID);
                // mediaIds[filesProcessed] = mediaUUID;

                // Insert media information into the `media` table
                // String mediaUrl = null;
                if ((filePart.getName().equals("mediaFile") && fileName != null && !fileName.isEmpty())
                        && ((questionType.equals("IMG") || questionType.equals("AUD") && filesProcessed == 0)
                                || (questionType.equals("TEXT") && answerType.equals("IMG") || answerType.equals("AUD"))
                                || (filesProcessed > 0 && answerType.equals("IMG") || answerType.equals("AUD")))) {
                    File saveFile = new File(getServletContext().getRealPath("/public/media"));
                    System.out.println(saveFile + " " + fileName);
                    File file = new File(saveFile, fileName);
                    filePart.write(file.getAbsolutePath());
                    mediaUrls[filesProcessed] = "public/media/" + fileName;
                    filesProcessed++;
                }
            }

            if (answerType.equals("VID")) {
                System.out.println("THERE IS A VIDEO ANSWER");
                mediaUrls[filesProcessed] = videoUrls[1];
                System.out.println(mediaUrls[filesProcessed]);
                mediaFileNames[filesProcessed] = "N/A";
                filesProcessed++;
            }

            for (int i = 0; i < filesProcessed; i++) {
                System.out.println("Url count: " + mediaUrls.length);
                System.out.println("Index: " + i);
                UUID mediaUUID = UUID.randomUUID();
                byte[] mediaIdBinary = uuidToBytes(mediaUUID);
                mediaIds[i] = mediaUUID;

                String mediaType = (!questionType.equals("TEXT") && i == 0) ? questionType : answerType;
                StringBuilder criteria = new StringBuilder();
                criteria.append("id:==").append(new String(mediaIdBinary, StandardCharsets.UTF_8))
                        .append(",,,media_type:==").append(mediaType)
                        .append(",,,media_file_path:==").append(mediaUrls[i])
                        .append(",,,media_filename:==").append(mediaFileNames[i]);
                        System.out.println(criteria);

                // String insertMediaSql = "INSERT INTO media (id, media_type, media_file_path,
                // media_filename, media_start, media_end) VALUES (?, ?, ?, ?, ?, ?)";
                // psMedia = con.prepareStatement(insertMediaSql);
                // psMedia.setBytes(1, mediaIdBinary);
                // psMedia.setString(2, mediaType);
                // psMedia.setString(3, mediaUrls[i]);
                // psMedia.setString(4, mediaFileNames[i]);
                System.out.println("QUESTION ADDITION QUESTION TYPE: " + questionType);
                if (i == 0 && questionType.equals("VID")) {
                    // psMedia.setInt(5, videoStarts[i]);
                    // psMedia.setInt(6, videoEnds[i]);
                    criteria.append(",,,media_start:==").append(videoStarts[i])
                            .append(",,,media_end:==").append(videoEnds[i]);

                } else if (i == 0 && questionType.equals("AUD")) {
                    // psMedia.setInt(5, audioStarts[i]);
                    // psMedia.setInt(6, audioEnds[i]);
                    criteria.append(",,,media_start:==").append(audioStarts[i])
                            .append(",,,media_end:==").append(audioEnds[i]);
                } else if (answerType.equals("VID")) {
                    // psMedia.setInt(5, videoStarts[i]);
                    // psMedia.setInt(6, videoEnds[i]);
                    int indexVideoTimes = i == 0 ? 1 : i;
                    criteria.append(",,,media_start:==").append(videoStarts[indexVideoTimes])
                            .append(",,,media_end:==").append(videoEnds[indexVideoTimes]);
                } else {
                    int indexVideoTimes = i == 0 ? 1 : i;
                    criteria.append(",,,media_start:==").append(audioStarts[indexVideoTimes = i == 0 ? 1 : i])
                            .append(",,,media_end:==").append(audioEnds[indexVideoTimes = i == 0 ? 1 : i]);
                }
                System.out.println(criteria);
                repository.insert(factory.createAClass("media", criteria.toString()));
                // psMedia.executeUpdate();
            }

            System.out.println(mediaIds.length);
            // int filesProcessed = 0;

            // // Generate a UUID for the media
            // UUID mediaUUID = UUID.randomUUID();
            // byte[] mediaIdBinary = uuidToBytes(mediaUUID);
            // System.out.println("A");
            // // Insert media ionformation into the `media` table
            // String mediaUrl = null;
            // if (questionType.equals("VID")) {
            // mediaUrl = videoUrl;
            // } else if (!questionType.equals("TEXT")) {
            // filesProcessed++;
            // for(Part filePart : fileParts) {
            // System.out.println("Current folder: " + (new File(".")).getCanonicalPath());
            // File saveFile = new File(getServletContext().getRealPath("/public/media"));
            // File file = new File(saveFile, fileNames[0]);
            // filePart.write(file.getAbsolutePath());
            // mediaUrl = "public/media/" + fileNames[0];
            // videoStart = Integer.parseInt(req.getParameter("audioStart"));
            // videoEnd = Integer.parseInt(req.getParameter("audioEnd"));

            // break;
            // }

            // }
            // System.out.println("A");
            // String insertMediaSql = "INSERT INTO media (id, media_type, media_file_path,
            // media_filename, media_start, media_end) VALUES (?, ?, ?, ?, ?, ?)";
            // psMedia = con.prepareStatement(insertMediaSql);
            // psMedia.setBytes(1, mediaIdBinary);
            // psMedia.setString(2, questionType);
            // psMedia.setString(3, mediaUrl);
            // psMedia.setString(4, fileNames[0]);
            // psMedia.setInt(5, videoStart);
            // psMedia.setInt(6, videoEnd);
            // psMedia.executeUpdate();

            // Generate a UUID for the question
            UUID questionUUID = UUID.randomUUID();
            byte[] questionIdBinary = uuidToBytes(questionUUID);

            // // Insert the new question into the `questions` table
            // String insertQuestionSql = "INSERT INTO questions (id, quiz_name,
            // question_text, question_type) VALUES (?, ?, ?, ?)";
            // psQuestion = con.prepareStatement(insertQuestionSql);
            // psQuestion.setBytes(1, questionIdBinary);
            // psQuestion.setString(2, quizName);
            // psQuestion.setString(3, questionText);
            // psQuestion.setString(4, questionType);
            // psQuestion.executeUpdate();

            StringBuilder newQuestion = new StringBuilder();
            newQuestion.append("id:==").append(new String(questionIdBinary, StandardCharsets.UTF_8))
                    .append(",,,quiz_name:==").append(quizName)
                    .append(",,,question_text:==").append(questionText)
                    .append(",,,question_type:==").append(questionType);

            // // Insert into `question_media` table to link the question and the media
            if (!questionType.equals("TEXT")) {
            System.out.println(mediaIds[0]);
            // String insertQuestionMediaSql = "INSERT INTO question_media (question_id,
            // media_id) VALUES (?, ?)";
            // psQuestionMedia = con.prepareStatement(insertQuestionMediaSql);
            // psQuestionMedia.setBytes(1, questionIdBinary);
            // psQuestionMedia.setBytes(2, uuidToBytes(mediaIds[0]));
            // psQuestionMedia.executeUpdate();

            newQuestion.append(",,,media_id:==").append(new String(uuidToBytes(mediaIds[0]),
            StandardCharsets.UTF_8));
            }
            repository.insert(factory.createAClass("question", newQuestion.toString()));

            int indexOfCorrect =0;
            // Insert answers into the `answers` table

            for (int i = 0; i < answerTexts.length; i++) {
                UUID answerUUID = UUID.randomUUID();
                byte[] answerIdBinary = uuidToBytes(answerUUID);
                boolean isCorrect = (correctAnswer != null && Integer.parseInt(correctAnswer) == i + 1);
                if(isCorrect){
                    indexOfCorrect = i;
                }
                // String insertAnswerSql = "INSERT INTO answers (id, question_id, answer_text, is_correct, answer_type) VALUES (?, ?, ?, ?, ?)";
                // psAnswer = con.prepareStatement(insertAnswerSql);
                // psAnswer.setBytes(1, answerIdBinary);
                // psAnswer.setBytes(2, questionIdBinary);
                // psAnswer.setString(3, answerTexts[i]);
                // psAnswer.setBoolean(4, isCorrect);
                // psAnswer.setString(5, "text");  // Assuming answer_type is 'text' for now
                // psAnswer.executeUpdate();

                StringBuilder newAnswer = new StringBuilder();
                newAnswer.append("id:==").append(new String(answerIdBinary, StandardCharsets.UTF_8))
                        .append(",,,question_id:==").append(new String(questionIdBinary, StandardCharsets.UTF_8))
                        .append(",,,answer_text:==").append(answerTexts[i])
                        .append(",,,is_correct:==").append((isCorrect ? 1 : 0))
                        .append(",,,answer_type:==").append(answerType);

                // String insertAnswerSql = "INSERT INTO answers (id, question_id, answer_text,
                // is_correct, answer_type) VALUES (?, ?, ?, ?, ?)";
                // psAnswer = con.prepareStatement(insertAnswerSql);
                // psAnswer.setBytes(1, answerIdBinary);
                // psAnswer.setBytes(2, questionIdBinary);
                // psAnswer.setString(3, answerTexts[i]);
                // psAnswer.setBoolean(4, isCorrect);
                // psAnswer.setString(5, answerType);
                // psAnswer.executeUpdate();

                // String insertAnswerMediaSql = "INSERT INTO answer_media (answer_id, media_id) VALUES (?, ?)";
                if (answerType.equals("IMG")) {
                    // System.out.println(mediaIds[i]);
                    // psAnswerMedia = con.prepareStatement(insertAnswerMediaSql);
                    // psAnswerMedia.setBytes(1, answerIdBinary);
                    byte[] mediaId = (questionType.equals("TEXT")) ? uuidToBytes(mediaIds[i])
                            : uuidToBytes(mediaIds[i + 1]);
                    // psAnswerMedia.setBytes(2, mediaId);
                    // psAnswerMedia.executeUpdate();

                    newAnswer.append(",,,media_id:==").append(new String (mediaId, StandardCharsets.UTF_8));
                } else if (!answerType.equals("TEXT") && isCorrect) {
                    // System.out.println(mediaIds[i]);
                    // psAnswerMedia = con.prepareStatement(insertAnswerMediaSql);
                    // psAnswerMedia.setBytes(1, answerIdBinary);
                    byte[] mediaId = (questionType.equals("TEXT")) ? uuidToBytes(mediaIds[i])
                            : uuidToBytes(mediaIds[i + 1]);
                    // psAnswerMedia.setBytes(2, mediaId);
                    // psAnswerMedia.executeUpdate();

                    newAnswer.append(",,,media_id:==").append(new String (mediaId, StandardCharsets.UTF_8));
                }

                repository.insert(factory.createAClass("answer", newAnswer.toString()));
            }


            JSONArray jsonArray = new JSONArray(answerTexts);
            String answerTexts2 = jsonArray.toString();  // Converts the array to a JSON string
            // String insertQuestionWithAnswersSql = "INSERT INTO QuestionsWithAnswers (questionText, answers, indexOfCorrect,quizName) VALUES (?, ?, ?,?)";
            // psQuestionWithAnswers = con.prepareStatement(insertQuestionWithAnswersSql);
            // psQuestionWithAnswers.setString(1, questionText);
            // psQuestionWithAnswers.setString(2, answerTexts2);
            // psQuestionWithAnswers.setInt(3, indexOfCorrect);
            // psQuestionWithAnswers.setString(4, quizName);
            // psQuestionWithAnswers.executeUpdate();

//             StringBuilder newQuestionWithAnswers = new StringBuilder();
// String answerTextsFinal = answerTexts2.replace(",", ",,,");  // Converts the array to a JSON string
// newQuestionWithAnswers.append("questionText:==").append(questionText)
//                       .append(",answers:==").append(answerTextsFinal)
//                       .append(",indexOfCorrect:==").append(indexOfCorrect)
//                       .append(",quizName:==").append(quizName);
//             repository.insert(factory.createAClass("websocket", newQuestionWithAnswers.toString()));



            // Redirect back to the quiz creation page or show success message
            res.setStatus(200); 
            res.getWriter().write("{\"message\": \"Quiz edited successfully!\"}");

        } catch (Exception e) {
            throw new ServletException("Error processing question addition", e);
        } 
        // finally {
        //     try {
        //         if (psQuestion != null)
        //             psQuestion.close();
        //         if (psAnswer != null)
        //             psAnswer.close();
        //         if (psMedia != null)
        //             psMedia.close();
        //         if (psQuestionMedia != null)
        //             psQuestionMedia.close();
        //         if (con != null)
        //             con.close();
        //     } catch (SQLException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    // private String getUserRoleFromDatabase(String username) {
    //     Connection con = null;
    //     PreparedStatement ps = null;
    //     ResultSet rs = null;
    //     String role = null;

    //     try {
    //         // Load MySQL driver
    //         Class.forName("com.mysql.cj.jdbc.Driver");

    //         // Database connection
    //         con = DatabaseUtil.getConnection();

    //         // Query to get the user's role
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
    //         try {
    //             if (rs != null)
    //                 rs.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //         try {
    //             if (ps != null)
    //                 ps.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //         try {
    //             if (con != null)
    //                 con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }

    //     return role;
    // }
}
