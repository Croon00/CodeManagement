import { publicAxios, privateAxios } from "./axiosInstances";

// 로그인 (RefreshToken은 쿠키로, AccessToken은 로컬스토리지에 저장)
export const login = async (loginId, password) => {
  const response = await publicAxios.post("/api/auth/login", {
    loginId,
    password,
  });
  return response.data; // AccessToken 및 기타 데이터 반환
};

// 인증된 사용자 정보 가져오기 (AccessToken 필요)
export const getAuthenticatedUser = async () => {
  const response = await privateAxios.get("/api/auth/me");
  return response.data;
};

// RefreshToken으로 AccessToken 갱신
export const refreshAccessToken = async () => {
  const response = await publicAxios.post("/api/auth/refresh");
  return response.data; // 새 AccessToken 반환
};
