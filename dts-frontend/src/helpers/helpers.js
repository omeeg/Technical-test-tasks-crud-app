function formatDate(dateStr) {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleString(undefined, {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function formatStatus(status) {
  switch (status) {
    case 'TO_DO':
      return 'Not started';
    case 'IN_PROGRESS':
      return 'In progress';
    case 'DONE':
      return 'Completed';
    default:
      return status;
  }
}
export { formatDate, formatStatus };