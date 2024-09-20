<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Question</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 600px;
            margin: auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 10px;
        }
        input[type="text"], input[type="radio"] {
            width: calc(100% - 22px);
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="radio"] {
            width: auto;
            margin-right: 10px;
        }
        .answer {
            margin-bottom: 10px;
        }
        button {
            padding: 10px 20px;
            background-color: #5cb85c;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #4cae4c;
        }
        .button-link {
            display: inline-block;
            padding: 10px 20px;
            background-color: #0275d8;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .button-link:hover {
            background-color: #025aa5;
        }
    </style>
    <script>
        function addAnswer() {
            const answerDiv = document.createElement('div');
            answerDiv.classList.add('answer');
            answerDiv.innerHTML = `
                <input type="text" name="answerText" placeholder="Answer" required>
                <input type="radio" name="correctAnswer" value="${document.querySelectorAll('input[name="answerText"]').length + 1}"> Correct
            `;
            document.getElementById('answersContainer').appendChild(answerDiv);
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Add Question to <%= request.getAttribute("quizName") %></h1>

        <form method="POST" action="addQuestion" enctype="multipart/form-data">
            <input type="hidden" name="quizName" value="${quizName}">
        
            <!-- Question text -->
            <label for="questionText">Question:</label>
            <input type="text" name="questionText" required><br>
        
            <!-- Question Type (for example, 'multiple-choice') -->
            <label for="questionType">Question Type:</label>
            <input type="text" name="questionType" required><br>
        
            <!-- Media type (if any) -->
            <label for="mediaType">Media Type:</label>
            <select name="mediaType">
                <option value="">None</option>
                <option value="image">Image</option>
                <option value="video">Video</option>
            </select><br>
        
            <!-- Media file upload -->
            <label for="mediaFile">Upload Media:</label>
            <input type="file" name="mediaFile" accept="image/*,video/*"><br>
        
            <!-- Answers -->
            <label for="answerText">Answers:</label><br>
            <input type="text" name="answerText" required><br>
            <input type="text" name="answerText"><br>
            <input type="text" name="answerText"><br>
            <input type="text" name="answerText"><br>
        
            <!-- Correct answer -->
            <label for="correctAnswer">Correct Answer (index):</label>
            <input type="number" name="correctAnswer" min="0" required><br>
        
            <button type="submit">Submit</button>
        </form>
        
    </div>
</body>
</html>
