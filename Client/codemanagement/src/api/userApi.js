import { privateAxios } from "./axiosInstances";

// 사용자 등록 (ADMIN 권한 필요)
export const registerUser = async (userData) => {
  const response = await privateAxios.post("/api/user", userData);
  return response.data;
};

// 모든 사용자 조회 (ADMIN 권한 필요)
export const getAllUsers = async () => {
  const response = await privateAxios.get("/api/users");
  return response.data;
};

// 특정 사용자 조회
export const getUserById = async (userId) => {
  const response = await privateAxios.get(`/api/users/${userId}`);
  return response.data;
};

// 사용자 수정 (ADMIN 권한 필요)
export const updateUser = async (userId, userData) => {
  const response = await privateAxios.put(`/api/users/${userId}`, userData);
  return response.data;
};

// 사용자 삭제 (ADMIN 권한 필요)
export const deleteUser = async (userId) => {
  const response = await privateAxios.delete(`/api/users/${userId}`);
  return response.data;
};
