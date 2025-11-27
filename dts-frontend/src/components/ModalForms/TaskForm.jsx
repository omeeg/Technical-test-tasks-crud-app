import "./TaskForm.css";
import { formatStatus, formatDate } from "../../helpers/helpers";

function TaskForm({ task, message }) {
  if (!task) return null;

  return (
    <div className="view-task-container">
      {message && <p style={{ marginTop: "10px" }}>{message}</p>}
      <h2>{task.title}</h2>
      <div className="form-group">
        <label>Description</label>
        <p>{task.description || "-"}</p>
      </div>

      <div className="form-group">
        <label>Status</label>
        <p>{(formatStatus(task.status))}</p>
      </div>

      <div className="form-group">
        <label>Date History</label>
        <p>Created: {formatDate(task.createdAt)} 
        <br />
          Due: {formatDate(task.dueDate)}
        </p>
      </div>

    </div>
  );
}

export default TaskForm;
