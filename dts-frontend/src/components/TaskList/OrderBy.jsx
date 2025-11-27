// src/components/TaskList/OrderBy.js
import React, { useState } from "react";

function OrderBy({ onChange }) {
  const [field, setField] = useState("createdAt"); //Default ordered by createdAt
  const [direction, setDirection] = useState("asc");

  //These handlers update local state and notify parent of changes
  const handleFieldChange = (e) => {
    const newField = e.target.value;
    setField(newField);
    onChange({ orderBy: newField, direction });
  };

  const handleDirectionChange = (e) => {
    const newDirection = e.target.value;
    setDirection(newDirection);
    onChange({ orderBy: field, direction: newDirection });
  };
  
  return (
    <div className="order-by-container">
      <label>
        Order by:{" "}
        <select value={field} onChange={handleFieldChange}>
          <option value="createdAt">Created At</option>
          <option value="dueDate">Due At</option>
        </select>
      </label>
      <label>
        <select value={direction} onChange={handleDirectionChange}>
          <option value="asc">Ascending</option>
          <option value="desc">Descending</option>
        </select>
      </label>
    </div>
  );
}

export default OrderBy;
