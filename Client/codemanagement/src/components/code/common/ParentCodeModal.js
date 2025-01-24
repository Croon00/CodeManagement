import React, { useState, useRef, useEffect } from "react";
import {
  Box,
  Modal,
  TextField,
  Select,
  MenuItem,
  Button,
  Typography,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import { useSetRecoilState } from "recoil";
import { parentCodeState } from "../../../state/parentCodeState";
import { searchCodes } from "../../../api/codeApi"; // searchCodes API 가져오기

function ParentCodeModal({ open, onClose }) {
  const [searchParams, setSearchParams] = useState({
    searchType: "codeName", // 초기 검색 타입
    query: "", // 초기 검색어
  });
  const [results, setResults] = useState([]); // 전체 검색 결과 상태
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const itemsPerPage = 5; // 페이지당 표시할 항목 수
  const setParentCode = useSetRecoilState(parentCodeState); // Recoil 상태 업데이트 함수
  const listRef = useRef(null); // 리스트 참조용 ref

  // 검색 API 호출
  const handleSearch = async () => {
    try {
      const response = await searchCodes({
        searchType: searchParams.searchType,
        query: searchParams.query,
      }); // searchCodes API 호출
      setResults(response); // 전체 결과 저장
      setCurrentPage(1); // 검색 시 페이지 초기화
    } catch (error) {
      alert("부모 코드 검색에 실패했습니다.");
    }
  };

  // 검색 후 리스트 맨 위로 스크롤
  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = 0; // 리스트 상단으로 스크롤 이동
    }
  }, [results]);

  // 부모 코드 선택
  const handleSelect = (code) => {
    if (code === null) {
      // "없음" 선택 시 처리
      setParentCode(null);
    } else {
      setParentCode({ codeId: code.codeId, codeName: code.codeName }); // Recoil에 ID와 이름 저장
    }
    onClose(); // 모달 닫기
  };

  // 페이지네이션 계산
  const totalPages = Math.ceil(results.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const paginatedResults = results.slice(startIndex, endIndex);

  // 페이지네이션 버튼 생성
  const renderPageButtons = () => {
    const pageButtons = [];
    for (let i = 1; i <= totalPages; i++) {
      pageButtons.push(
        <Button
          key={i}
          variant={i === currentPage ? "contained" : "outlined"}
          onClick={() => setCurrentPage(i)}
        >
          {i}
        </Button>
      );
    }
    return pageButtons;
  };

  // 페이지 이동 핸들러
  const handleFirstPage = () => setCurrentPage(1);
  const handlePrevPage = () => setCurrentPage((prev) => Math.max(prev - 1, 1));
  const handleNextPage = () =>
    setCurrentPage((prev) => Math.min(prev + 1, totalPages));
  const handleLastPage = () => setCurrentPage(totalPages);

  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: 500,
          bgcolor: "background.paper",
          boxShadow: 24,
          p: 4,
          borderRadius: 2,
        }}
      >
        <Typography variant="h6" gutterBottom>
          부모 코드 검색
        </Typography>

        <Select
          value={searchParams.searchType}
          onChange={(e) =>
            setSearchParams((prev) => ({
              ...prev,
              searchType: e.target.value,
            }))
          }
          fullWidth
          sx={{ marginBottom: 2 }}
        >
          <MenuItem value="codeName">코드 이름</MenuItem>
          <MenuItem value="codeValue">코드 값</MenuItem>
        </Select>

        <TextField
          label="검색어 입력"
          variant="outlined"
          fullWidth
          value={searchParams.query}
          onChange={(e) =>
            setSearchParams((prev) => ({
              ...prev,
              query: e.target.value,
            }))
          }
          sx={{ marginBottom: 2 }}
        />

        <Button
          variant="contained"
          fullWidth
          onClick={handleSearch}
          sx={{ marginBottom: 2 }}
        >
          검색
        </Button>

        <List ref={listRef}>
          {/* "없음" 선택 항목 추가 */}
          <ListItem button onClick={() => handleSelect(null)}>
            <ListItemText primary="없음" secondary="부모 코드 없이 설정" />
          </ListItem>
          {paginatedResults.map((code) => (
            <ListItem
              key={code.codeId}
              button
              onClick={() => handleSelect(code)}
            >
              <ListItemText
                primary={code.codeName}
                secondary={`코드 값: ${code.codeValue}`}
              />
            </ListItem>
          ))}
        </List>

        {/* 페이지네이션 */}
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            marginTop: 2,
            gap: 1,
          }}
        >
          <Button
            variant="outlined"
            onClick={handleFirstPage}
            disabled={currentPage === 1}
          >
            {"<<"}
          </Button>
          <Button
            variant="outlined"
            onClick={handlePrevPage}
            disabled={currentPage === 1}
          >
            {"<"}
          </Button>
          {renderPageButtons()}
          <Button
            variant="outlined"
            onClick={handleNextPage}
            disabled={currentPage === totalPages}
          >
            {">"}
          </Button>
          <Button
            variant="outlined"
            onClick={handleLastPage}
            disabled={currentPage === totalPages}
          >
            {">>"}
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}

export default ParentCodeModal;
