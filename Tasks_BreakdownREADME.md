# CS-QuizGame

Group:

    1. Catherine Queenan
    2. Eugenie Kim
    3. Soomin Jeong
    4. Saba Karbakhsh
    5. Liam Janicke

Completed Tasks for 1(a):

    1. Catherine Queenan: Acted as project manager
        a. Created EERD for the database
        b. Built database creation SQL
        c. Created SQL statement to remove a category from the database when it is empty
        d. Drew wireframes for flow and style guide
        e. Researched colour palettes and fonts (for elderly vision) and selected accordingly
        f. Researched, curated, and wrote ten quizzes, each with ten questions with four answers
        g. Curated media, finding images, audio, and video (selecting start and end times where relevant)
        h. Coded the autoplay function for quizzes
        i. Populated database with hardcoded SQL for the literature quiz, and admin for others
        j. Debugged existing code and refactored
        h. *REDID: Built hardcoded sql for inputting Literature quiz (for testing)
    2. Eugenie Kim
        a. Created base web sockets
        b. Coded login/signup/logout functionalities with admin privileges
        c. Coded the loading of categories and quizzes dynamically
        d. Debugged existing code and refactored
        e. *REDID: The functionalities on the admin side to create quizzes so that they are functional
        f. Implemented the ability to upload media for questions
        g. *REDID: Delete functionality for quizzes and questions
    3. Soomin Jeong
        a. Styled all front-end with raw CSS
        b. *REDID: Functionality for adding questions to a quiz so that it is functional
        c. Debugged existing code and refactored
        d. Linked Liam's code to the rest of the web service so that it can be accessed
        e. Downloaded audio files
        f. Coded autoplay for videos and audio
    4. Saba Karbakhsh
        a. *Functionalties on the admin side to create quizzes
        b. *Functionality for adding questions to a quiz
    5. Liam Janicke
        a. *Delete functionality for quizzes and questions
        b. *Built hardcoded sql for inputting Literature quiz (for testing)

Completed Tasks for 1(b):

    1. Catherine Queenan:
        a. Coded errorMessage and ErrorServlet 
        b. Coded ability to add images to categories and quizzes themselves
        c. Refactored login, signup, and logout to use JSON and AJAX calls (introduced cookies, did not move into authentication tokens)
        d. Refactored errorMessage to load an error message from JSON when called by login, signup, and logout
        e. Refactored the Servlets for homepage and quizzes to be RESTful
        f. Refactored pages for homepage and quizzes to be RESTful
        g. Refactored admin delete of quizzes to be RESTful
        h. Debugged
    2. Eugenie Kim:
        a. Completed all components related to introducing MVC, Factory, DAO Patterns in the backend code
        b. Coded the ability to add media to answers (images AS answers, video and audio that plays after the correct answer is selected)
        c. Debugged
        c. *Refactored web sockets to function with existing code
        d. Debugged
    3. Soomin Jeong:
        a. Restyled and styled all css as changes were made (added feature for quizzes and categories to be scrolled through smoothly)
        b. Refactored quizzes to be RESTful
        c. Debugged
    4. Saba Karbakhsh
        a. *Created the websocket functionalities, minus the functionality to load media
    5. Liam Janicke
        a. Introduced AOP *ONLY WORKS IF YOU COMPILE WITH JDK 11* 
        b. Introduced FP *ONLY WORKS IF YOU COMPILE WITH JDK 11*

Completed Tasks for 1(c):

    1. Catherine Queenan:
        a. Refactored and modified answer video and audio to replace the question video and audio, where applicable
        b. Implemented GitHub actions to automatically compile the project on all major OS (MacOS, Linux, Windows)
        c. Created sessions for moderated mode
        d. Added the ability for regular users to join specific moderated sessions from the main page
        e. *Added base functionality for moderators to end a session
        f. *Began implementing the loading of audio on the websockets
        g. Debugged logout
    2. Eugenie Kim:
        a. Added the ability to edit quizzes and their media
        b. Added the ability to edit questions and their media
        c. Simplified the addition and removal of answers, questions, and quizzes
        d. Debugged editing functionalities
        e. Implemented MySQL database in a docker container with a compose file
        f. Combined the MySQL and Tomcat dockers
        g. Debugged logout
    3. Soomin Jeong:
        a. *Completed functionality for moderators to end a session
        b. Debugged moderated mode
        c. Styled all new features, and restyled existing features for better cohesion
        d. Implemented audio for the websocket questions
        e. Allowed users to join on the current question in moderated mode
        f. Implemented image answers for the websocket questions
        g. Debugged logout
    4. Saba Karbakhsh
    5. Liam Janicke
        a. Implemented Tomcat Docker container to run the webapp