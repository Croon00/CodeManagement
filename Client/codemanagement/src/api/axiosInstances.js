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

// 공통 에러 핸들러
const handleApiError = (error) => {
  if (!error.response) {
    console.error("네트워크 오류 또는 서버 응답 없음");
    alert("네트워크 오류가 발생했습니다. 다시 시도해주세요.");
    return Promise.reject(error);
  }

  const status = error.response.status;

  if (status === 401) {
    console.warn("401 Unauthorized: 인증이 필요합니다.");
    alert("세션이 만료되었습니다. 다시 로그인해주세요.");
    localStorage.removeItem("accessToken");
    window.location.href = "/login";
  } else if (status === 403) {
    console.warn("403 Forbidden: 권한이 없습니다.");
    alert("이 작업을 수행할 권한이 없습니다.");
  } else if (status === 500) {
    console.error("500 Internal Server Error: 서버 오류 발생");
    alert("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
  }

  return Promise.reject(error);
};

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

    return handleApiError(error); // ✅ 공통 에러 핸들러 적용
  }
);
