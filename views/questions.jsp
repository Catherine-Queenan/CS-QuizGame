
<!DOCTYPE html>
<html>
<head>
    <title>Questions</title>
</head>
<body>
    <form action="home"><button type="Submit">Home</button></form>
    <form method="post">
        <input type="hidden" value="true" name="restart">
        <button type="Submit">Restart</button>
    </form>
    <h1>Question <%=request.getAttribute("qNumber")%> out of <%=request.getAttribute("quizSize")%></h1>
    <div id="questions"><%=request.getAttribute("questionsHtml")%>
    </div>
    
    
    <%= request.getAttribute("mediaHtml") %>

</body>
<script>
    let body = document.getElementsByTagName("body");
    let wrongAnswers = documnet.getElementsByClassName("wrongPlayAnswer");
    let rightAnswer = document.getElementById("rightPlayAnswer");

    wrongAnswers.array.forEach(element => {
        element.addEventListener('click', () => {
            body.style.background = "red";
            setTimeout(() => {
                body.style.background = "white";
            }, 5000);
        })
    });

    rightAnswer.addEventListener('click', () =>{
        body.style.background = "green";
            setTimeout(() => {
                document.getElementById("questionForm").submit();
            }, 5000);
    });

</script>
</html>
