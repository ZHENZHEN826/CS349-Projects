# CS349 A4
Student: jz7li
Marker: Bahar Sarrafzadeh


Total: 40 / 40 (100.00%)

Code: 
(CO: won't compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:   

## REQUIREMENTS

# Welcome Screen:

1. [2/2] A TextView to display a text (e.g., "Name") and and an EditText field to take text as an input.

2. [2/2] A button to navigate to the next screen. When the user clicks on this button, the app passes the user input from the EditText field to the next screen. Initially, the button should be disabled. When the user enters any letter in the EditText, it will be enabled.

3. [2/2] A TextView at the bottom-left corner showing your WatIAM User ID (i.e., the first part of your uwaterloo email account). You may include additional text (e.g., "copyright") as shown in the video.

# Topic Selection Screen:

4. [2/2] The quiz topic name with a TextView.

5. [2/2] A spinner to show values between 1 and 5.

6. [2/2] A button to navigate to the next screen (e.g., the "Load" button as you can see in the video). When the user clicks on this button, it passes the selected spinner value to the Questions Screen and opens the screen.

# Questions Screen:

7. [2/2] A TextView to show a question.

8. [2/2] An ImageView to show the image related to the question.

9. [2/2] Four options with radio buttons/checkboxes. Use radio buttons for questions with a single answer. Use checkboxes for questions with multiple answers.

10. [2/2] A button (e.g., "Previous") to access the previous question. This button should be disabled for the first question as we don't have any previous question to navigate.

11. [2/2] A button (e.g., "Next") to move to the next question. When the user reaches the last question, the button text should be changed to "Finish". Clicking on the "Finish" button opens the Results Screen.

12. [2/2] A TextView showing current question number and number of questions that the user selected in the topic selection page. For instance, if the user selected four questions for the quiz and is currently viewing the second question, the TextView will show "2/4".

13. [2/2] Responses to the questions should be recorded when the user select or deselects a radio button or a check box. This would allow users to go back to other questions and check their responses (see video 00:53s to 1:06s).

# Results Screen:

14. [2/2] A TextView showing the score. Each question is worth 1 mark. No mark for a partially correct answer.

15. [2/2] A button to go back to the Topic Selection Screen. Once in the Topic Selection Screen, the user starts the quiz from the beginning (i.e., from Question 1). The application shouldn't show any previously selected responses (e.g., response to question 1 in the previous attempt), if the user retakes a quiz.

# Common Requirements:

16. [2/2] For all application screens, use proper layout designs so that the app scales from small to large screens and supports different device orientations (e.g., landscape or portrait) properly.

17. [2/2] Except for the welcome screen, all screens should include the user's name (i.e., the name that the user entered in the Welcome screen). Feel free to re-use layouts with include.

18. [2/2] Except for the welcome screen, all screens should include a logout button that takes users to the welcome screen. Clicking on logout removes all user inserted/selected information (e.g. user name, answers, etc.).


## ENHANCEMENT [2/2]

## GENERAL [2/2] (can be negative)
