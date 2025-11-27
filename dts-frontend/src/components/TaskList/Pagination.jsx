import "./TaskList.css";

function Pagination({ currentPage, totalPages, onPageChange }) {
  //These two functions handle page changes up to parent component
  const handlePrev = () => {
    if (currentPage > 0) onPageChange(currentPage - 1);
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1) onPageChange(currentPage + 1);
  };
  //If only one page, don't show pagination
  if (totalPages <= 1) return null;


  return (
    <div className="pagination">
      <button onClick={handlePrev} disabled={currentPage === 0}>
        Prev
      </button>
      <span>
        Page {currentPage + 1} of {totalPages}
      </span>
      <button onClick={handleNext} disabled={currentPage === totalPages - 1}>
        Next
      </button>
    </div>
  );
}

export default Pagination;
