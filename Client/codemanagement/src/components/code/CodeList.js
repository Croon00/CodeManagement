import React, { useState, useEffect } from "react";
import {
  Container,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Box,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { selectedCodeState } from "../../state/selectedCodeState";
import { getAllCodes, searchCodes } from "../../api/codeApi";
import { getAuthenticatedUser } from "../../api/authApi";
import CodeSearchBox from "./common/CodeSearchBox";

function CodeManagement() {
  const [parentCode, setParentCode] = useState(null); // 선택된 부모 코드
  const [results, setResults] = useState([]); // 검색 결과
  const [isSearching, setIsSearching] = useState(false); // 검색 여부 상태
  const [userId, setUserId] = useState(null); // 로그인된 사용자 ID
  const [userRole, setUserRole] = useState(null); // 로그인된 사용자 권한
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const itemsPerPage = 10; // 페이지당 표시할 항목 수

  const navigate = useNavigate();
  const [, setSelectedCode] = useRecoilState(selectedCodeState); // 선택된 코드를 상태에 저장

  // 모든 코드 조회 및 사용자 정보 가져오기
  useEffect(() => {
    const fetchData = async () => {
      try {
        // 모든 코드 조회
        const codes = await getAllCodes();
        setResults(codes);

        // 로그인된 사용자 정보 가져오기
        try {
          const user = await getAuthenticatedUser(); // 인증된 사용자 정보 가져오기
          setUserId(user.userId);
          // authorities 배열에서 ROLE 값을 추출
          setUserRole(user.authorities?.[0]?.authority || null);
        } catch (authError) {
          if (authError.response?.status === 401) {
            console.warn("로그인되지 않은 상태입니다.");
          } else {
            console.error("사용자 정보를 가져오는 데 실패했습니다:", authError);
          }
        }
      } catch (error) {
        alert("코드 데이터를 가져오는 데 실패했습니다.");
      }
    };

    fetchData();
  }, []);

  const handleSearch = async (searchParams) => {
    try {
      setIsSearching(true); // 검색 상태 활성화
      const response = await searchCodes(searchParams);
      setResults(response);
      setCurrentPage(1); // 검색 시 페이지 초기화
    } catch (error) {
      alert("검색에 실패했습니다.");
    } finally {
      setIsSearching(false); // 검색 상태 비활성화
    }
  };

  const handleEdit = (code) => {
    if (!userId || userRole !== "ROLE_ADMIN") {
      alert("권한이 없습니다.");
      return;
    }
    setSelectedCode(code); // 선택된 코드 정보를 Recoil 상태에 저장
    navigate(`/update/${code.codeId}`); // 수정 페이지로 이동
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
    <Container maxWidth="lg" sx={{ marginTop: 4 }}>
      {/* 검색 박스 */}
      <CodeSearchBox
        onSearch={handleSearch}
        onParentCodeSearchClick={() => setParentCode(null)}
        parentCodeName={parentCode ? parentCode.codeName : ""}
      />

      {/* 검색 결과 */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead sx={{ backgroundColor: "#E3F2FD" }}>
            <TableRow>
              <TableCell>코드 ID</TableCell>
              <TableCell>코드 이름</TableCell>
              <TableCell>코드 값</TableCell>
              <TableCell>등록일</TableCell>
              <TableCell>부모 코드 이름</TableCell>
              <TableCell>상태</TableCell>
              {userRole === "ROLE_ADMIN" && <TableCell>액션</TableCell>}
            </TableRow>
          </TableHead>
          <TableBody>
            {paginatedResults.length > 0 ? (
              paginatedResults.map((row) => (
                <TableRow key={row.codeId}>
                  <TableCell>{row.codeId}</TableCell>
                  <TableCell>{row.codeName}</TableCell>
                  <TableCell>{row.codeValue}</TableCell>
                  <TableCell>{row.createdAt}</TableCell>
                  <TableCell>{row.parentCodeName || "없음"}</TableCell>
                  <TableCell>{row.activated ? "현존" : "폐지"}</TableCell>
                  {userRole === "ROLE_ADMIN" && (
                    <TableCell>
                      {userId === row.userId && (
                        <Button
                          variant="outlined"
                          color="primary"
                          onClick={() => handleEdit(row)} // 전체 row 정보를 전달
                        >
                          수정
                        </Button>
                      )}
                    </TableCell>
                  )}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  {isSearching ? "검색 중입니다..." : "결과가 없습니다."}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {/* 페이지네이션 */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
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
    </Container>
  );
}

export default CodeManagement;
