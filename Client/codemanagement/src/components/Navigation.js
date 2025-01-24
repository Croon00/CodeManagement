import React, { useEffect, useState } from "react";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { authState } from "../state/authState";
import { getAuthenticatedUser } from "../api/authApi"; // 인증된 유저 정보 가져오기

function Navigation() {
  const [isLoggedIn, setIsLoggedIn] = useRecoilState(authState);
  const [authorities, setAuthorities] = useState([]); // 유저의 권한 리스트 초기화
  const navigate = useNavigate();

  // 브라우저 새로고침 후 상태 동기화 및 권한 주기
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    setIsLoggedIn(!!token);

    const fetchUserAuthorities = async () => {
      if (token) {
        try {
          const user = await getAuthenticatedUser(); // 유저 정보 가져오기
          setAuthorities(user.authorities || []); // authorities 배열 설정
        } catch (error) {
          console.error("유저 정보를 가져오지 못했습니다:", error);
          setAuthorities([]); // 에러 발생 시 빈 배열로 초기화
        }
      }
    };

    fetchUserAuthorities();
  }, [isLoggedIn, setIsLoggedIn]);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setIsLoggedIn(false); // 상태 업데이트
    setAuthorities([]); // 로그아웃 시 권한 초기화
    alert("로그아웃되었습니다.");
    navigate("/"); // 메인 페이지로 이동
  };

  const isAdmin = authorities.some((auth) => auth.authority === "ROLE_ADMIN");

  return (
    <AppBar
      position="static"
      sx={{
        backgroundColor: "white",
        color: "black",
        borderBottom: "2px solid #e0e0e0",
        boxShadow: "none",
      }}
    >
      <Toolbar>
        <Typography
          variant="h6"
          component={Link}
          to="/"
          sx={{
            textDecoration: "none",
            color: "black",
            marginRight: 2,
          }}
        >
          코드 관리 시스템
        </Typography>

        <Box sx={{ display: "flex", gap: 2 }}>
          <Button sx={{ color: "black" }} component={Link} to="/create">
            코드등록
          </Button>
          <Button sx={{ color: "black" }} component={Link} to="/list">
            코드리스트
          </Button>
          {/* ADMIN 권한인 경우에만 유저 등록 버튼 표시 */}
          {isAdmin && (
            <Button
              sx={{ color: "black" }}
              component={Link}
              to="/user/register"
            >
              유저등록
            </Button>
          )}
        </Box>

        <Box sx={{ marginLeft: "auto" }}>
          {isLoggedIn ? (
            <Button sx={{ color: "black" }} onClick={handleLogout}>
              로그아웃
            </Button>
          ) : (
            <Button sx={{ color: "black" }} component={Link} to="/login">
              로그인
            </Button>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}

export default Navigation;
