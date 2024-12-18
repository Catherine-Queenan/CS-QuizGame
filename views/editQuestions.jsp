<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Questions</title>
    <link rel="stylesheet" href="../public/css/reset.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>

        .wrap {
            padding: 80px 0;
            justify-content: unset;
            overflow-y: scroll;
            -ms-overflow-style: none;
            /* Internet Explorer 10+ */
            scrollbar-width: none;
            /* Firefox */
            -webkit-scrollbar: none;
        }

        @media screen and (max-width: 500px) {
            .wrap {
                padding: 50px 0;
            }
        }

        .title {
            font-size: 40px;
            margin: 0;
        }

        .questionsWrap {
            width: 65%;
            margin-top: 50px;
            border-radius: 15px;
            font-size: 18px;
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .emptyMsg {
            text-align: center;
            margin-bottom: 20px;
        }

        .question {
            width: 100%;
            background-color: #45425A;
            border-radius: 15px;
            padding: 40px 40px 30px 40px;
            display: flex;
            flex-direction: column;
        }

        .buttonWrap {
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }

        .addQuestionBtn,
        .deleteBtn,
        .question a {
            all: unset;
            width: fit-content;;
            display: inline-block;
            margin-top: 20px;
            padding: 15px 40px;
            border-radius: 15px;
            font-size: 18px;
            /* color: rgb(244, 244, 244); */
            color: #0C1B33;
            background-color: #FF4B3E;
            cursor: pointer;
            transition-duration: 0.3s;
        }

        .addQuestionBtn {
            margin-top: 20px;
            background-color: #D7E8BA;
            font-size: 20px;
        }

        .question a {
            background-color: #99c252;
        }

        .question a:hover,
        .deleteBtn:hover {
            box-shadow: inset 5px 5px 5px rgba(1, 1, 1, 0.5);
        }

        .addQuestionBtn:hover {
            transform: scale(1.03);
            box-shadow: 5px 5px 5px rgba(1, 1, 1, 0.4);
        }

        .emptyMsg {
            width: 100%;
            padding: 15px 10% 0 10%;
            font-size: 18px;
            text-align: center;
            margin-bottom: 20px;
        }

    </style>
</head>

<body>
    <header>
        <form id="homeForm" action="home">
            <button class="homeBtn" type="Submit">Home</button>
        </form>
        <button id="logoutButton" class="logoutBtn">Log Out</button>
    </header>

    <div class="wrap">
        <div class="title cherry-cream-soda">
            Questions for <span id="quizTitle" class="cherry-cream-soda"></span>
            <!-- Questions for quizName -->
        </div>

        <div id="questionDiv" class="questionsWrap">
            <!-- <div class="question">
            <p class="questionTitle">Question 1</p>
            <a class="deleteBtn" href="deleteQuestion?id=0&amp;quizName=QuizName">Delete Question</a>
        </div>
        <div class="question">
            <p class="questionTitle">Question 1</p>
            <a class="deleteBtn" href="deleteQuestion?id=1&amp;quizName=QuizName">Delete Question</a>
        </div> -->
        </div>

        <!-- Add Question button -->
        <a id="addQuestionsId" class="addQuestionBtn">Add Question</a>
        <!-- <a href="addQuestion?quizName=quizName" class="addQuestionBtn">Add Question</a> -->
    </div>

</body>
<script src="..\scripts\logout.js"></script>
<script>

    document.addEventListener('DOMContentLoaded', function () {
        const questionsContainer = document.getElementById('questionDiv');
        const homeButtonForm = document.getElementById('homeForm');
        const addQuestionButton = document.getElementById('addQuestionsId');
        const quizTitle = document.getElementById('quizTitle');

        const currentPath = window.location.pathname;
        const pathSegments = currentPath.split('/');

        // Extract the category name from URL parameters
        const quizName = pathSegments[3];
        const quizNameSpace = quizName.replace(/%20/g, ' ');
        quizTitle.innerHTML = quizNameSpace;
        console.log(quizName);

        // Extract the base path dynamically (remove last segment if it's quiz-related)
        pathSegments.pop();
        pathSegments.pop();

        // Construct the new path dynamically
        const newPath = pathSegments.join('/') + `/editQuestions-json?quizName=${quizName}`;
        const postPath = pathSegments.join('/') + `/editQuestions-json`;
        const homePath = pathSegments.join('/') + `/home`;
        const addQuestionPath = pathSegments.join('/') + `/addQuestion/${quizName}`;
        const editQuestionPathSeg = pathSegments.join('/') + `/editQuestion/${quizName}/`;
        console.log(newPath)

        fetch(newPath, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        }).then(response => {
            if (!response.ok) {
                console.error('Response status:', response.status);
                throw new Error('Failed to fetch quiz');
            }
            return response.json(); // Change this temporarily to text() instead of json()
        }).then(data => {
            console.log(data)
            homeButtonForm.action = homePath;
            addQuestionButton.href = addQuestionPath;

            if (data.questions.length === 0) {
                questionsContainer.innerHTML = '<p class="emptyMsg">There are currently no questions for this quiz</p>';
                return;
            }

            data.questions.forEach(question => {
                const questionDiv = document.createElement('div');
                const questionText = document.createElement('div');
                const buttonWrap = document.createElement('div');
                const editButton = document.createElement('a');
                const deleteButton = document.createElement('button');

                questionDiv.className = 'question';
                questionText.className = 'questionTitle';
                questionText.innerHTML = question.question_text;

                buttonWrap.className = 'buttonWrap';

                editButton.className = 'editBtn';
                editButton.href = editQuestionPathSeg + question.questionNum;
                editButton.innerHTML = "Edit Question";

                deleteButton.className = 'deleteBtn';
                deleteButton.innerHTML = "Delete Question";
                deleteButton.addEventListener('click', function () {

                    // Show a confirmation dialog before proceeding
                    const isConfirmed = confirm(`Are you sure you want to delete this question?`);
                    if (isConfirmed) {
                        const deleteURL = pathSegments.join('/') + `/deleteQuestion?id=${question.questionNum}&quizName=${quizName}`;

                        console.log("delete: " + deleteURL)
                        // Send a DELETE request to the server
                        fetch(deleteURL, {
                            method: 'DELETE'
                        })
                        .then(response => {
                            if (!response.ok) {
                                console.error('Response status:', response.status);
                                throw new Error('Failed to delete question');
                            }
                            return response.json();
                        })
                        .then(data => {
                            window.location.reload();
                        })
                        .catch(error => {
                            console.error('Error deleting quiz:', error);
                        });
                    }
                });

                buttonWrap.appendChild(editButton);
                buttonWrap.appendChild(deleteButton);

                questionDiv.appendChild(questionText);
                questionDiv.appendChild(buttonWrap)
                questionsContainer.appendChild(questionDiv);
            });

            // Changing the admin buttons for responsive 
            document.querySelectorAll(".buttonWrap").forEach(function(buttonWrap) {
                if (buttonWrap.offsetWidth < 430) {
                    document.querySelectorAll(".buttonWrap .editBtn").forEach(function(editButton) {
                        editButton.innerHTML = `<i class="fa-solid fa-pen"></i>`
                    });
                    document.querySelectorAll(".deleteBtn").forEach(function(deleteButton) {
                        deleteButton.innerHTML = `<i class="fa-solid fa-trash"></i>`;
                    });
                }
            })


        }).catch(error => {
            console.error('Error fetching categories:', error);
            document.getElementById('categories').innerHTML = '<p class="emptyMsg">There was an error loading the categories. Please try again later.</p>';
        });

    });

</script>
</html>