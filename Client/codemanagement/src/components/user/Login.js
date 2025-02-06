import React, { useState } from "react";
import { login, getAuthenticatedUser } from "../../api/authApi";
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
import { authState } from "../../state/authState";

function Login() {
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const setIsLoggedIn = useSetRecoilState(authState);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault(); // 기본 form 제출 동작 방지
    try {
      const { accessToken } = await login(loginId, password);
      localStorage.setItem("accessToken", accessToken);
      setIsLoggedIn(true);

      try {
        const user = await getAuthenticatedUser();
        localStorage.setItem(
          "userAuthorities",
          JSON.stringify(user.authorities)
        );
      } catch (error) {
        console.error("권한 정보를 가져오는 데 실패했습니다:", error);
        localStorage.removeItem("userAuthorities");
      }

      alert("로그인 성공!");
      navigate("/");
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
        <form onSubmit={handleLogin}>
          {" "}
          {/* ✅ form 태그 사용 */}
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
              type="submit" // ✅ form 안에서 type="submit" 설정
              variant="contained"
              color="primary"
              fullWidth
              sx={{ maxWidth: 400 }}
            >
              로그인
            </Button>
            {errorMessage && (
              <Typography variant="body2" color="error">
                {errorMessage}
              </Typography>
            )}
          </Box>
        </form>
      </Paper>
    </Container>
  );
}

export default Login;
