import React, { useEffect, useState } from "react";
import {
  Box,
  Container,
  Typography,
  List,
  ListItem,
  ListItemText,
  Divider,
} from "@mui/material";
import CodeSearchBox from "./code/common/CodeSearchBox";
import { getPopularCodes } from "../api/codeApi"; // API 호출 함수 가져오기

function Home() {
  const [popularCodes, setPopularCodes] = useState([]); // 인기 코드를 저장할 상태

  // 인기 코드 가져오기
  useEffect(() => {
    const fetchPopularCodes = async () => {
      try {
        const topCodes = await getPopularCodes(); // API 호출
        setPopularCodes(topCodes);
      } catch (error) {
        console.error("인기 코드를 가져오는 데 실패했습니다:", error);
      }
    };

    fetchPopularCodes();
  }, []);

  return (
    <Container maxWidth="lg" sx={{ marginTop: 4 }}>
      <Box
        sx={{
          display: "flex",
          flexDirection: { xs: "column", md: "row" },
          gap: 4,
        }}
      >
        {/* 검색 박스 */}
        <Box
          sx={{
            flex: 2,
            border: "1px solid black", // 검은 테두리
            borderRadius: 2,
            padding: 2,
          }}
        >
          <CodeSearchBox />
        </Box>

        {/* 실시간 검색 영역 */}
        <Box
          sx={{
            flex: 0.7,
            backgroundColor: "#f5f5f5",
            border: "1px solid black", // 검은 테두리
            borderRadius: 2,
            padding: 2,
            height: "100%",
            overflowY: "auto",
          }}
        >
          <Typography variant="h6" gutterBottom>
            자주 이용하는 코드
          </Typography>
          <List>
            {popularCodes.length > 0 ? (
              popularCodes.map((code, index) => (
                <React.Fragment key={code.codeId}>
                  <ListItem>
                    <ListItemText primary={`${index + 1}. ${code.codeName}`} />
                  </ListItem>
                  {index < popularCodes.length - 1 && <Divider />}{" "}
                  {/* 구분선 */}
                </React.Fragment>
              ))
            ) : (
              <Typography variant="body2" color="textSecondary" align="center">
                데이터를 가져오는 중이거나 검색 기록이 없습니다.
              </Typography>
            )}
          </List>
        </Box>
      </Box>
    </Container>
  );
}

export default Home;
