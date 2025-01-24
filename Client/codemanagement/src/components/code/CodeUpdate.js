import React, { useState, useEffect } from "react";
import {
  Box,
  Container,
  TextField,
  MenuItem,
  Button,
  Typography,
  List,
  ListItem,
  ListItemText,
  Select,
} from "@mui/material";
import { useParams, useNavigate } from "react-router-dom";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { searchParentCodes } from "../../api/codeApi";
import { selectedCodeState } from "../../state/selectedCodeState"; // Recoil 상태
import { updateCode, getCodeById } from "../../api/codeApi"; // API 함수

function CodeUpdate() {
  const { codeId } = useParams(); // URL에서 codeId 가져오기
  const navigate = useNavigate();
  const selectedCode = useRecoilValue(selectedCodeState); // Recoil 상태 값 읽기
  const setSelectedCode = useSetRecoilState(selectedCodeState); // 상태 업데이트

  const [parentCode, setParentCode] = useState(
    selectedCode?.parentCode || null
  );
  const [codeName, setCodeName] = useState(selectedCode?.codeName || "");
  const [codeValue, setCodeValue] = useState(selectedCode?.codeValue || "");
  const [codeMean, setCodeMean] = useState(selectedCode?.codeMean || "");
  const [activated, setActivated] = useState(selectedCode?.activated || true);
  const [searchQuery, setSearchQuery] = useState(""); // 부모 코드 검색어
  const [searchResults, setSearchResults] = useState([]); // 검색 결과

  // 초기 데이터 로드
  useEffect(() => {
    if (!selectedCode) {
      // Recoil에 데이터가 없으면 API 호출로 가져오기
      const fetchCodeData = async () => {
        try {
          const data = await getCodeById(codeId);
          setSelectedCode(data); // 상태에 저장
          setParentCode(data.parentCode || null);
          setCodeName(data.codeName || "");
          setCodeValue(data.codeValue || "");
          setCodeMean(data.codeMean || "");
          setActivated(data.activated || true);
        } catch (error) {
          alert("코드 정보를 가져오는 데 실패했습니다.");
        }
      };
      fetchCodeData();
    }
  }, [codeId, selectedCode, setSelectedCode]);

  const handleSearch = async () => {
    try {
      const response = await searchParentCodes(searchQuery);
      setSearchResults(response); // 검색 결과 업데이트
    } catch (error) {
      alert("부모 코드 검색에 실패했습니다.");
    }
  };

  const handleSelectParentCode = (code) => {
    setParentCode(code); // 부모 코드 설정
    setSearchResults([]); // 검색 결과 초기화
    setSearchQuery(""); // 검색어 초기화
  };

  const handleUpdate = async () => {
    try {
      const updatedData = {
        codeName,
        codeValue,
        codeMean,
        parentCodeId: parentCode ? parentCode.codeId : null,
        activated,
      };
      await updateCode(codeId, updatedData); // 수정 API 호출
      alert("코드가 성공적으로 수정되었습니다!");
      navigate("/list"); // 수정 후 리스트 페이지로 이동
    } catch (error) {
      alert("코드 수정에 실패했습니다.");
    }
  };

  return (
    <Container maxWidth="lg" sx={{ marginTop: 4 }}>
      <Box
        sx={{
          border: "1px solid black", // 외부 테두리
          borderRadius: 2,
          padding: 4,
          display: "flex",
          justifyContent: "center", // 전체 컨텐츠 가운데 정렬
          backgroundColor: "#f5f5f5",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 3,
            backgroundColor: "#ffffff", // 내부 배경
            color: "#1976d2",
            borderRadius: 2,
            padding: 4,
            width: "100%",
            maxWidth: 600, // 내부 컨텐츠의 너비 제한
            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.2)", // 그림자 효과
          }}
        >
          <Typography variant="h5" align="center" gutterBottom>
            코드 수정
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
          <Box sx={{ width: "100%" }}>
            <Typography variant="body1" sx={{ marginBottom: 1 }}>
              부모 코드 검색
            </Typography>
            <TextField
              label="부모 코드 검색"
              variant="outlined"
              fullWidth
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <Button
              variant="contained"
              color="secondary"
              sx={{ marginTop: 2 }}
              onClick={handleSearch}
            >
              검색
            </Button>
            {searchResults.length > 0 && (
              <Box
                sx={{
                  backgroundColor: "#f9f9f9",
                  borderRadius: 1,
                  padding: 2,
                  marginTop: 2,
                }}
              >
                <Typography variant="body2">검색 결과:</Typography>
                <List>
                  {searchResults.map((code) => (
                    <ListItem
                      button
                      key={code.codeId}
                      onClick={() => handleSelectParentCode(code)}
                    >
                      <ListItemText primary={code.codeName} />
                    </ListItem>
                  ))}
                </List>
              </Box>
            )}
            {parentCode && (
              <Typography variant="body2" sx={{ marginTop: 2 }}>
                선택된 부모 코드: {parentCode.codeName}
              </Typography>
            )}
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
            onClick={handleUpdate}
          >
            수정
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default CodeUpdate;
