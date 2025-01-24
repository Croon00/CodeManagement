import axios from "axios";

// API URL 설정
const baseURL = "http://localhost:8080";

// Public API 전용 Axios 인스턴스 (GET 요청만 처리)
export const publicAxios = axios.create({
  baseURL: baseURL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // 쿠키 전송 허용
});

// Private API 전용 Axios 인스턴스 (POST, PUT 요청 처리 및 Access Token 필요)
export const privateAxios = axios.create({
  baseURL: baseURL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // 쿠키 전송 허용
});

// 요청 인터셉터: Access Token 추가
privateAxios.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
  }
  return config;
});

// 응답 인터셉터: Access Token 갱신 로직 포함
privateAxios.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const skipRedirectUrls = ["/api/auth/me"];
    const publicUrls = [
      "/api/codes",
      "/api/codes/search",
      "/api/codes/search-by-name",
    ];

    if (
      skipRedirectUrls.some((url) => originalRequest.url.startsWith(url)) &&
      error.response?.status === 401
    ) {
      console.warn("401 에러 발생: 인증되지 않은 요청입니다.");
      return Promise.reject(error); // 에러 반환만 수행
    }

    if (
      publicUrls.some((url) => originalRequest.url.startsWith(url)) &&
      error.response?.status === 401
    ) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshResponse = await publicAxios.post("/api/auth/refresh");
        const newAccessToken = refreshResponse.data.accessToken;

        localStorage.setItem("accessToken", newAccessToken);
        originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
        return privateAxios(originalRequest);
      } catch (refreshError) {
        console.error("Access Token 갱신 실패:", refreshError);
        localStorage.removeItem("accessToken");
        window.location.href = "/login";
      }
    }

    return Promise.reject(error);
  }
);
