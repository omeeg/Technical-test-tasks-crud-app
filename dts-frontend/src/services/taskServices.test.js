import axios from "axios";
import taskService from "./taskServices";

jest.mock("axios");

describe("createTask", () => {
  it("should return data when task is created successfully", async () => {
    const mockTask = { title: "Test task" };
    const mockResponse = { data: { id: 1, ...mockTask } };

    axios.post.mockResolvedValue(mockResponse);

    const result = await taskService.createTask(mockTask);

    expect(axios.post).toHaveBeenCalledWith(
      "http://localhost:8080/api/tasks",
      mockTask
    );
    expect(result).toEqual(mockResponse.data);
  });

  it("throw error with status code when backend returns error response", async () => {
    axios.post.mockRejectedValue({
      response: { status: 500 },
    });

    await expect(taskService.createTask({})).rejects.toThrow(
      "Error creating new task: 500"
    );
  });
});

describe("getTasks", () => {
  it("should return data when tasks fetched successfully", async () => {
    const mockResponse = { data: { content: [1, 2], totalPages: 1 } };

    axios.get.mockResolvedValue(mockResponse);

    const result = await taskService.getTasks();

    expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/api/tasks", {
      params: { page: 0, size: 5, orderBy: "createdAt", ascending: false },
    });
    expect(result).toEqual(mockResponse.data);
  });

  it("should throw general error message if no backend data", async () => {
    axios.get.mockRejectedValue(new Error("Something failed"));

    await expect(taskService.getTasks()).rejects.toBe("Something failed");
  });
});
