import { publicAxios, privateAxios } from "./axiosInstances";

// 모든 코드 조회 (인증 불필요)
export const getAllCodes = async () => {
  const response = await publicAxios.get("/api/codes");
  return response.data;
};

// 코드 조회 (인증 불필요)
export const getCodeById = async (codeId) => {
  const response = await publicAxios.get("/api/codes", {
    params: { codeId },
  });
  return response.data;
};

// 코드 검색 API 호출
export const searchCodes = async (searchParams) => {
  const response = await privateAxios.get("/api/codes/search", {
    params: searchParams,
  });
  return response.data;
};

// 부모 코드 검색
export const searchParentCodes = async (query) => {
  const response = await publicAxios.get("/api/codes/search-by-name", {
    params: { codeName: query },
  });
  return response.data;
};

// 코드 등록 (ADMIN 권한 필요)
export const createCode = async (codeData) => {
  const response = await privateAxios.post("/api/codes", codeData);
  return response.data;
};

// 코드 수정 (ADMIN 권한 필요)
export const updateCode = async (codeId, codeData) => {
  const response = await privateAxios.put(`/api/codes/${codeId}`, codeData);
  return response.data;
};

// Top 10 검색된 코드 가져오기
export const getPopularCodes = async () => {
  const response = await publicAxios.get("/api/code-search-log/popular");
  return response.data;
};
