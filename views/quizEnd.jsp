<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>You Won!</title>
    <link rel="stylesheet" href="public/css/reset.css">
    <!-- <link rel="stylesheet" href="../public/css/reset.css"> -->
    <style>

        .btnWrap {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
        }

        .btns {
            all: unset;
            margin-top: 20px;
            padding: 20px 40px;
            border-radius: 12px;
            font-size: 20px;
            font-weight: 700;
            color: #0C1B33;
            transition-duration: 0.3s;
            cursor: pointer;
        }

        .btns:hover {
            transform: scale(1.04);
            box-shadow: 5px 5px 5px rgba(1, 1, 1, 0.4);
        }

        .wrap .homeBtn {
            background-color: #99c252;
        }

        .playAgainBtn {
            background-color: #6e6ba6;
        }
        
        .endSession {
            display: none;
            background-color: #FF4B32;
        }

        @media screen and (max-width: 450px) {
            .btnWrap {
                margin-top: 20px;
                flex-direction: column;
            }

            .btnWrap button {
                margin-top: 0;
                padding: 15px 30px;
                font-size: 18px;
            }
        }

    </style>
</head>
<body>
    <header>
        <form action="home">
            <button class="homeBtn" type="Submit">Home</button>
        </form>
        <button id="logoutButton" class="logoutBtn">Log Out</button>
    </header>
    
    <div class="wrap">
        <div class="title cherry-cream-soda">
            You Completed the Quiz !
        </div>

        <p id="role" style="display: none;">
            <%=request.getAttribute("role")%>
        </p>
        <p id="sessionToEnd" style="display: none;">
            <%=request.getAttribute("sessionToEnd")%>
        </p>

        <div class="btnWrap">
            <button class="btns endSession" id="endSession">End Session</button>
            <form action="home"><button class="btns homeBtn" id="homeButton">Home</button></form>
            <form method="post"><button class="btns playAgainBtn">Play Again</button></form>
        </div>
    </div>
</body>
<script src="scripts\logout.js"></script>
<script>

    window.onload = function () {
        let sessionId = document.getElementById("sessionToEnd").textContent.trim()
        let role = document.getElementById("role").textContent.trim();
        console.log(sessionId)
        console.log(role)
        if (sessionId != "null" && sessionId != null) {
            // Display the thing
            document.querySelector(".playAgainBtn").style.display = "none";
            // Only admin can end session
            if (role == "a") { 
                document.getElementById("endSession").style.display = "block";
                document.getElementById("homeButton").style.display = "none";
            }
            // Add onclick to buttons
            document.querySelector(".endSession").onclick = function () {
                endModeration(sessionId);
            };
        } else {
            document.getElementById("endSession").style.display = "none";
            document.querySelector(".playAgainBtn").style.display = "block";
        }
    }
    
    function endModeration(modSessionId) {
        const currentSessionPath = window.location.pathname;
        const pathSegments = currentSessionPath.split('/');
        pathSegments.pop();
        const endSessionPath = pathSegments.join('/') + `/getActiveSessions?action=endModeratedSession&sessionId=${encodeURIComponent(modSessionId)}`;

        console.log(endSessionPath)
        window.location.href = endSessionPath;
    }

</script>
</html>