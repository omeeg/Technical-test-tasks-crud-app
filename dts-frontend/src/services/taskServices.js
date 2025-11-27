import axios from 'axios';

// Would typically be in a config file - for simplicity is here
const API_BASE_URL = 'http://localhost:8080/api/tasks';

const taskService = {
  //POST new task
  createTask: async (taskData) => {
    try {
      const response = await axios.post(API_BASE_URL, taskData);
      return response.data;
    } catch (error) {
      //catching of different errors.
      if (error.response) {
        //Error if we get a response e.g. error on backend or something
        throw new Error(`Error creating new task: ${error.response.status}`);
      } 
      else if (error.request) {
        //Error if our requuest was made but never reaches the server
        throw new Error(`No response from server. Network error or server down. Details: ${error.message}`);
      } 
      else {
        //Error some other issue setting up the request
        throw new Error(`Unexpected New Task Request setup error: ${error.message}`);
      }    
    }
  },


  //Front page view - no search bar so only get service currently
  getTasks: async (page = 0, size = 5, orderBy = 'createdAt', ascending = false) => {
    try {
      const response = await axios.get(API_BASE_URL, {
        params: { page, size, orderBy, ascending }
      });
      return response.data;
    } catch (error) {
        //single error message for simplicity
      throw error.response?.data || error.message;
    }
  },

};

export default taskService;