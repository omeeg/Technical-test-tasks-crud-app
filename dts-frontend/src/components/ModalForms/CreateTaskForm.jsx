import React, { useState } from "react";
import "./TaskForm.css";
function CreateTaskForm({onSubmit, message}) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [dueDate, setDueDate] = useState("");
  const [attemptedSubmit, setAttemptedSubmit] = useState(false); // track submission attempt

  //===========================================
  // Checking Valid Inputs - would probably abstract to separate validation module in larger app
  //===========================================

  const isTitleValid =  title.length <= 255;
  const isDescriptionValid = description.length <= 1000;
  const isDueDateValidPast = (() => {
    const due = new Date(dueDate);
    const now = new Date();
    // ≥1 hour in future 
    return (!isNaN(due.getTime()) && due.getTime() - now.getTime() >= 3600 * 1000);
  })();
  const isDueDateValidFuture =  (() => {
    const due = new Date(dueDate);
    const now = new Date();
    // ≤5 years in future
    return (!isNaN(due.getTime()) && due.getTime() - now.getTime() <= 5 * 365 * 24 * 3600 * 1000);
  })();

  const isFormValid = isTitleValid && isDescriptionValid && isDueDateValidPast && isDueDateValidFuture;

  const titleValidMessage = !isTitleValid ? "Title must be less than 255 characters." : "";
  const descriptionValidMessage = !isDescriptionValid ? "Description must be less than 1000 characters." : "";
  const dueDateValidPastMessage = !isDueDateValidPast ? "Tasks must have a due date at least 1 hour in the future." : "";  
  const dueDateValidFutureMessage = !isDueDateValidFuture ? "Tasks must have a due date not more than 5 years in the future." : "";
  
  
  //============================================
  // Handle form submission - from passed in onSubmit prop
  //===========================================
  const handleSubmit = (e) => {
    e.preventDefault();
    setAttemptedSubmit(true);
    if (!isFormValid) {
      console.log("Validation errors:", {titleValidMessage, descriptionValidMessage, dueDateValidPastMessage, dueDateValidFutureMessage});
      return; // prevent submission
    }
    setAttemptedSubmit(false);
    onSubmit({ title, description, dueDate });
  };


  //============================================
  // Render form
  //============================================
  return (
    <div className="create-task-container">
      <h2>Create New Task</h2>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Title</label>
          <input
            type="text"
            required
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        
        <div className="form-group">
          <label>Due Date</label>
          <input
            type="datetime-local"
            required
            value={dueDate} 
            onChange={(e) => setDueDate(e.target.value)}
          />
        </div>

        <button className="submit-btn" type="submit">Create Task</button>
      </form>

      {/* Show front end validation warnings */}
      {attemptedSubmit && !isTitleValid > 0 && <p style={{ marginTop: "10px" }}>{titleValidMessage}</p>}
      {attemptedSubmit && !isDescriptionValid > 0 && <p style={{ marginTop: "10px" }}>{descriptionValidMessage}</p>}
      {attemptedSubmit && !isDueDateValidPast > 0 && <p style={{ marginTop: "10px" }}>{dueDateValidPastMessage}</p>}
      {attemptedSubmit && !isDueDateValidFuture > 0 && <p style={{ marginTop: "10px" }}>{dueDateValidFutureMessage}</p>}

      {/* Show API warnings */}
      {message && <p style={{ marginTop: "10px" }}>{message}</p>}

    </div>
  );
}

export default CreateTaskForm;
