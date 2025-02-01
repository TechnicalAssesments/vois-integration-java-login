import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

export const signup = async (userData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/register`, userData);
    return response.data.message;
  } catch (error) {
    throw error.response.data; 
  }
};

export const login = async (credentials) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials);
    console.log(response);
    return response.data.message; 
  } catch (error) {
    throw error.response.data; 
  }
};  