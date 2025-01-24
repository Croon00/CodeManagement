import React, { useState, useEffect } from "react";
import {
  Box,
  Container,
  TextField,
  Button,
  Typography,
  Select,
  MenuItem,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../../api/userApi";
import { getAuthenticatedUser } from "../../api/authApi";

function UserRegister() {
  const [loginId, setLoginId] = useState(""); // 사용자 ID
  const [userName, setUserName] = useState(""); // 사용자 이름
  const [password, setPassword] = useState(""); // 비밀번호
  const [role, setRole] = useState("USER"); // 사용자 역할
  const [activated, setActivated] = useState(true); // 활성화 여부
  const [authorities, setAuthorities] = useState([]); // 유저 권한
  const navigate = useNavigate();

  useEffect(() => {
    const checkAuthentication = async () => {
      try {
        const user = await getAuthenticatedUser();
        setAuthorities(user.authorities || []); // 유저 권한 설정

        // ADMIN 권한 확인
        const isAdmin = user.authorities.some(
          (auth) => auth.authority === "ROLE_ADMIN"
        );
        if (!isAdmin) {
          alert("관리자만 사용자 등록이 가능합니다.");
          navigate("/");
        }
      } catch (error) {
        alert("로그인이 필요합니다.");
        navigate("/login");
      }
    };
    checkAuthentication();
  }, [navigate]);

  const handleRegister = async () => {
    try {
      await registerUser({
        loginId,
        userName,
        password,
        role,
        activated,
      });
      alert("사용자가 성공적으로 등록되었습니다!");
      navigate("/"); // 등록 후 메인 페이지로 이동
    } catch (error) {
      alert(error.response?.data?.message || "사용자 등록에 실패했습니다.");
    }
  };

  return (
    <Container maxWidth="lg" sx={{ marginTop: 4 }}>
      <Box
        sx={{
          border: "1px solid black",
          borderRadius: 2,
          padding: 4,
          display: "flex",
          justifyContent: "center",
          backgroundColor: "#f5f5f5",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
            backgroundColor: "#ffffff",
            color: "#1976d2",
            borderRadius: 2,
            padding: 4,
            width: "100%",
            maxWidth: 600,
            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.2)",
          }}
        >
          <Typography variant="h5" align="center" gutterBottom>
            사용자 등록
          </Typography>
          <TextField
            label="사용자 ID"
            variant="outlined"
            fullWidth
            value={loginId}
            onChange={(e) => setLoginId(e.target.value)}
          />
          <TextField
            label="사용자 이름"
            variant="outlined"
            fullWidth
            value={userName}
            onChange={(e) => setUserName(e.target.value)}
          />
          <TextField
            label="비밀번호"
            type="password"
            variant="outlined"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Box sx={{ width: "100%" }}>
            <Typography variant="body1" sx={{ marginTop: 2 }}>
              사용자 역할
            </Typography>
            <Select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              fullWidth
            >
              <MenuItem value="USER">USER</MenuItem>
              <MenuItem value="OPERATOR">OPERATOR</MenuItem>
              <MenuItem value="ADMIN">ADMIN</MenuItem>
            </Select>
          </Box>
          <Box sx={{ width: "100%" }}>
            <Typography variant="body1" sx={{ marginTop: 2 }}>
              활성화 여부
            </Typography>
            <Select
              value={activated}
              onChange={(e) => setActivated(e.target.value === "true")}
              fullWidth
            >
              <MenuItem value="true">활성화</MenuItem>
              <MenuItem value="false">비활성화</MenuItem>
            </Select>
          </Box>
          <Button
            variant="contained"
            color="primary"
            sx={{ marginTop: 3 }}
            onClick={handleRegister}
          >
            등록
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default UserRegister;
