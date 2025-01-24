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
import { createCode } from "../../api/codeApi";
import { getAuthenticatedUser } from "../../api/authApi";
import { useRecoilValue } from "recoil";
import { parentCodeState } from "../../state/parentCodeState"; // Recoil 상태
import ParentCodeModal from "./common/ParentCodeModal";

function CodeCreate() {
  const parentCode = useRecoilValue(parentCodeState); // Recoil에서 부모 코드 상태 읽기
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열기 상태
  const [codeName, setCodeName] = useState(""); // 코드 이름
  const [codeValue, setCodeValue] = useState(""); // 코드 값
  const [codeMean, setCodeMean] = useState(""); // 코드 의미
  const [activated, setActivated] = useState(true); // 활성화 여부

  const navigate = useNavigate();

  useEffect(() => {
    const checkAuthentication = async () => {
      try {
        await getAuthenticatedUser(); // 인증된 사용자 확인
      } catch (error) {
        alert("로그인이 필요합니다.");
        navigate("/login");
      }
    };
    checkAuthentication();
  }, [navigate]);

  const handleCreate = async () => {
    try {
      await createCode({
        codeName,
        codeValue,
        codeMean,
        parentCodeId: parentCode ? parentCode.codeId : null,
        activated,
      });
      alert("코드가 성공적으로 생성되었습니다!");
      navigate("/list"); // 생성 후 리스트 페이지로 이동
    } catch (error) {
      alert(error.response?.data?.message || "코드 생성에 실패했습니다.");
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
            코드 생성
          </Typography>
          <TextField
            label="코드 이름"
            variant="outlined"
            fullWidth
            value={codeName}
            onChange={(e) => setCodeName(e.target.value)}
          />
          <TextField
            label="코드 값"
            variant="outlined"
            fullWidth
            value={codeValue}
            onChange={(e) => setCodeValue(e.target.value)}
          />
          <TextField
            label="코드 의미"
            variant="outlined"
            fullWidth
            value={codeMean}
            onChange={(e) => setCodeMean(e.target.value)}
          />
          <TextField
            label="부모 코드"
            variant="outlined"
            fullWidth
            value={parentCode ? parentCode.codeName : ""}
            InputProps={{
              readOnly: true,
            }}
            onClick={() => setIsModalOpen(true)} // 모달 열기
          />
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
            onClick={handleCreate}
          >
            생성
          </Button>
        </Box>
      </Box>

      {/* 부모 코드 검색 모달 */}
      <ParentCodeModal
        open={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </Container>
  );
}

export default CodeCreate;
