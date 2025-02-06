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
  const [parentCode, setParentCode] = useState(null);
  const [results, setResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);
  const [userId, setUserId] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const navigate = useNavigate();
  const [, setSelectedCode] = useRecoilState(selectedCodeState);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const codes = await getAllCodes();
        setResults(codes);

        try {
          const user = await getAuthenticatedUser();
          setUserId(user.userId);
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
      setIsSearching(true);
      const response = await searchCodes(searchParams);
      setResults(response);
      setCurrentPage(1);
    } catch (error) {
      alert("검색에 실패했습니다.");
    } finally {
      setIsSearching(false);
    }
  };

  const handleEdit = (code) => {
    if (!userId || userRole !== "ROLE_ADMIN") {
      alert("권한이 없습니다.");
      return;
    }
    setSelectedCode(code);
    navigate(`/update/${code.codeId}`);
  };

  const totalPages = Math.ceil(results.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const paginatedResults = results.slice(startIndex, endIndex);

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

  const handleFirstPage = () => setCurrentPage(1);
  const handlePrevPage = () => setCurrentPage((prev) => Math.max(prev - 1, 1));
  const handleNextPage = () =>
    setCurrentPage((prev) => Math.min(prev + 1, totalPages));
  const handleLastPage = () => setCurrentPage(totalPages);

  // 날짜 변환 함수: "yyyy-MM-dd HH:mm" 형식으로 변환
  const formatDateTime = (dateString) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toISOString().slice(0, 16).replace("T", " ");
  };

  return (
    <Container maxWidth="lg" sx={{ marginTop: 4 }}>
      <CodeSearchBox
        onSearch={handleSearch}
        onParentCodeSearchClick={() => setParentCode(null)}
        parentCodeName={parentCode ? parentCode.codeName : ""}
      />

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
                  <TableCell>{formatDateTime(row.createdAt)}</TableCell>
                  <TableCell>{row.parentCodeName || "없음"}</TableCell>
                  <TableCell>{row.activated ? "현존" : "폐지"}</TableCell>
                  {userRole === "ROLE_ADMIN" && (
                    <TableCell>
                      {userId === row.userId && (
                        <Button
                          variant="outlined"
                          color="primary"
                          onClick={() => handleEdit(row)}
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
