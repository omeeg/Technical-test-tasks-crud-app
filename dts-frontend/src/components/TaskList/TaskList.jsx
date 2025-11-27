import React from "react";
import TaskRow from "./TaskRow";
import "./TaskList.css";

function TaskList({ tasks, onView }) {
  return (
    <div className="table-container">
      <table className="list-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Status</th>
            <th>Created</th>
            <th>Due</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task) => (
            <TaskRow key={task.id} task={task} onView={onView} />
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default TaskList;
