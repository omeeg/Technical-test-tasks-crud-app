import { useEffect, useState } from 'react';
import './App.css';
import CreateTaskForm from './components/ModalForms/CreateTaskForm';
import Modal from './components/ModalForms/Modal';
import taskService from './services/taskServices';
import TaskForm from './components/ModalForms/TaskForm';
import TaskList from './components/TaskList/TaskList';
import Pagination from './components/TaskList/Pagination';
import { DEFAULT_PAGE_SIZE, DEFAULT_ORDER_BY } from './helpers/config';
import OrderBy from './components/TaskList/OrderBy';

function App() {
  //Modal visibility states - would refactor to different approach in larger app
  //e.g. enums instead
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);

  //============================================
  // Task Data states
  //============================================
  const [currentTask, setCurrentTask] = useState(null);  
  const [createFormMessage, setCreateFormMessage] = useState(null);
  const [viewFormMessage, setViewFormMessage] = useState(null);
  
  //============================================
  // Task List states
  //============================================
  const [tasks, setTasks] = useState([]);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [orderBy, setOrderBy] = useState(DEFAULT_ORDER_BY);
  const [ascending, setAscending] = useState(false);

  const pageSize = DEFAULT_PAGE_SIZE; //import from config default
  
  //============================================
  // Fetch tasks with pagination and ordering
  //============================================
  const fetchTasks = async (page = 0, fetchOrderBy = orderBy, fetchAscending = ascending) => {
    try {
      const data = await taskService.getTasks(page, pageSize, fetchOrderBy, fetchAscending);
      setTasks(data.content || []);
      setTotalPages(data.totalPages || 1);
      setCurrentPage(page);
    } catch (err) {
      console.error("Error fetching tasks:", err);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  //============================================
  // Handle creating a new task - may again, abstract to separate handler module in larger app
  //============================================

  const handleCreateTask = async (taskData) => {
    try {
      const newTask = await taskService.createTask(taskData);

      //Close create modal
      setShowCreateModal(false);

      //Set the created task and open view modal
      setCurrentTask(newTask);
      setViewFormMessage("Task created successfully!");
      setShowViewModal(true);
      //Refresh tasks list
      fetchTasks();
    } catch (err) {
      setCreateFormMessage(err?.message || JSON.stringify(err));
      console.error("Error creating task:", err);
    }
  };

  //============================================
  // Render App
  //============================================
  return (
    <div className="App">
      <header className="App-header">
        <h1 className="Title-header">Task Management System</h1>
        <button className="create-button" onClick={() => setShowCreateModal(true)}>
          Create New Task
        </button>

      </header>
      <div>

        {showCreateModal && (
          <Modal onClose={() => {setShowCreateModal(false); setCreateFormMessage(null);}}>
            <CreateTaskForm onSubmit={handleCreateTask} message={createFormMessage}/>
          </Modal>
        )}

        {/* View Modal */}
        {showViewModal && currentTask && (
          <Modal onClose={() => {setShowViewModal(false); setCurrentTask(null); setViewFormMessage(null);}}>
            <TaskForm task={currentTask} message={viewFormMessage}/>
          </Modal>
        )}      

        <TaskList tasks={tasks} onView={(task) => {
          setCurrentTask(task);
          setViewFormMessage(null);
          setShowViewModal(true);
        }}/>
        
        {tasks.length === 0 && <p style={{ marginTop: "20px" }}>No tasks available. Please create a new task.</p>}
        
        <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={fetchTasks}
        />
        <OrderBy
          onChange={({ orderBy: newOrderBy, direction }) => {
            setOrderBy(newOrderBy);
            setAscending(direction === "asc");
            fetchTasks(0, newOrderBy, direction === "asc");
          }}
        />
      </div>
    </div>
  );
}

export default App;
