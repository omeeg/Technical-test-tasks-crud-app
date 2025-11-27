//Pop out modal - will popout in front of the current page
import './Modal.css';
function Modal({ children, onClose }) {
  return (
    <div className="modal-overlay">
      <div className="modal-box">
        <button onClick={onClose} className="close-button">X</button>
        {children}
      </div>
    </div>
  );
}

export default Modal;