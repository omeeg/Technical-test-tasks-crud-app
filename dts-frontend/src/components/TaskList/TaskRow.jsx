import { formatStatus, formatDate } from "../../helpers/helpers";
function TaskRow({ task, onView }) {
  return (
    <tr>
      <td>{task.title}</td>
      <td>{formatStatus(task.status)}</td>
      <td>{formatDate(task.createdAt)}</td>
      <td>{formatDate(task.dueDate)}</td>
      <td>
        <button onClick={() => onView(task)}>View</button>
      </td>
    </tr>
  );
}

export default TaskRow;