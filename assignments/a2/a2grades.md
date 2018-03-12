# CS349 A2
Student: jz7li
Marker: Gregory Philbrick


Total: 21 / 60 (35.00%)

Code: 
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:   

## REQUIREMENTS

1. [3/3] The main user interface follows the specifications regarding size and look and feel.

2. [6/8] In the drawing (insert) mode, a mouse drag event draws a shape on the canvas.

-1 When drawing rectangles, it isn't possible to go from bottom-right to top-left.
-1 When drawing ellipses, it isn't possible to go from bottom-right to top-left.

3. [0/8] In the selection mode, the user can click on an existing shape to select it for modification.

-8 This part isn't done.

4. [0/2] Only one shape can be selected at once.

5. [0/2] When a shape is selected, the application must provide a visual indication of selection.

6. [2/2] Shape: the toolbar must have a a drop-down menu that allows the user to select the type of shape to draw next (freeform, straight line, rectangle, or ellipse).

7. [0/2] Menu File -> New will allow users to draw on a blank canvas, i.e., it will clear the current contents of the canvas.

-2 Not done.

8. [2/2] Menu File -> Exit will close the application.

9. [0/2] Menu Edit -> Selection Mode (and similar tool bar button) should change the editor into the Selection mode.

10. [0/2] Menu Edit -> Drawing Mode (and similar tool bar button) should change the editor into the Drawing mode.

11. [0/2] Menu Edit -> Delete should delete the currently selected shape.

12. [0/6] Menu Edit -> Transform will show a dialog window that allows the user to transform the currently selected shape (translation, scaling, and rotation).

13. [1/3] Menu Format -> Stroke width (and similar tool bar widget) is used to set/change the stroke of a shape.

-1 Stroke width does not change for the currently selected shape
-1 There is no indication of current stroke width in the Format menu.

14. [2/3] Menu Format -> Fill color (and similar tool bar widget) is used to set/change the fill color of a closed shape (rectangle/ellipse).

-1 There is no indication of current fill color.

15. [1/3] Menu Format -> Stroke color (and similar tool bar widget) is used to set/change the stroke color of a shape.

-1 Stroke color does not change for the currently selected shape
-1 There is no indication of current stroke color.

## MVC REQUIREMENTS [2/5]

-2 data about the drawn shapes is not stored in the Model
-1 View does not properly respond to a notification from the Model (you have update methods, but they don't do anything).

## ENHANCEMENT [0/3]

## GENERAL [2/2] (can be negative)
