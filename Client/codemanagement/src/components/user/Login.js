import React, { useState } from "react";
import { login, getAuthenticatedUser } from "../../api/authApi"; // 인증된 사용자 정보 가져오기
import {
  Box,
  Container,
  TextField,
  Button,
  Typography,
  Paper,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import { authState } from "../../state/authState"; // authState 가져오기

function Login() {
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지
  const setIsLoggedIn = useSetRecoilState(authState); // Recoil 상태 업데이트 함수
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const { accessToken } = await login(loginId, password); // 로그인 요청
      localStorage.setItem("accessToken", accessToken);
      setIsLoggedIn(true); // 로그인 상태 업데이트

      // 로그인 성공 후 권한 정보 가져오기
      try {
        const user = await getAuthenticatedUser(); // 유저 정보 가져오기
        localStorage.setItem(
          "userAuthorities",
          JSON.stringify(user.authorities)
        ); // 권한 정보를 로컬스토리지에 저장
      } catch (error) {
        console.error("권한 정보를 가져오는 데 실패했습니다:", error);
        localStorage.removeItem("userAuthorities"); // 실패 시 초기화
      }

      alert("로그인 성공!");
      navigate("/"); // Home.js로 이동
    } catch (error) {
      console.error("로그인 실패:", error);
      setErrorMessage(
        "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요."
      );
    }
  };

  return (
    <Container maxWidth="sm" sx={{ marginTop: 8 }}>
      <Paper
        elevation={3}
        sx={{ padding: 4, backgroundColor: "#E3F2FD", borderRadius: 2 }}
      >
        <Typography variant="h5" align="center" gutterBottom>
          로그인
        </Typography>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
            marginTop: 2,
          }}
        >
          <TextField
            label="사용자 ID"
            variant="outlined"
            fullWidth
            sx={{ maxWidth: 400, backgroundColor: "white" }}
            value={loginId}
            onChange={(e) => setLoginId(e.target.value)}
          />
          <TextField
            label="비밀번호"
            variant="outlined"
            type="password"
            fullWidth
            sx={{ maxWidth: 400, backgroundColor: "white" }}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            variant="contained"
            color="primary"
            fullWidth
            sx={{ maxWidth: 400 }}
            onClick={handleLogin}
          >
            로그인
          </Button>
          {errorMessage && (
            <Typography variant="body2" color="error">
              {errorMessage}
            </Typography>
          )}
        </Box>
      </Paper>
    </Container>
  );
}

export default Login;
