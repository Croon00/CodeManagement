import React, { useState } from "react";
import {
  Box,
  TextField,
  MenuItem,
  Button,
  Typography,
  Select,
  RadioGroup,
  Radio,
  FormControlLabel,
} from "@mui/material";
import { useRecoilValue } from "recoil";
import { parentCodeState } from "../../../state/parentCodeState";
import ParentCodeModal from "./ParentCodeModal";

function CodeSearchBox({ onSearch }) {
  const parentCode = useRecoilValue(parentCodeState); // 부모 코드 상태 읽기
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열기 상태
  const [searchParams, setSearchParams] = useState({
    searchType: "codeName",
    searchQuery: "",
    startDate: "",
    endDate: "",
    codeStatus: "all", // 추가: 활성 상태 (all, active, inactive)
  });

  const handleSearch = () => {
    const transformedSearchParams = {
      searchType: searchParams.searchType,
      query: searchParams.searchQuery,
      startDate: searchParams.startDate
        ? new Date(searchParams.startDate).toISOString()
        : null,
      endDate: searchParams.endDate
        ? new Date(searchParams.endDate).toISOString()
        : null,
      activated:
        searchParams.codeStatus === "all"
          ? null
          : searchParams.codeStatus === "active",
      parentCodeId: parentCode ? parentCode.codeId : null,
    };
    console.log(transformedSearchParams);

    // 부모 컴포넌트로 변환된 검색 조건 전달
    onSearch(transformedSearchParams);
  };

  return (
    <Box
      sx={{
        backgroundColor: "#E3F2FD",
        padding: 4,
        borderRadius: 2,
        border: "1px solid #0D47A1",
        marginBottom: 4,
      }}
    >
      <Typography variant="h5" align="center" gutterBottom>
        코드 검색
      </Typography>
      <Box
        sx={{
          display: "flex",
          flexDirection: { xs: "column", md: "row" },
          gap: 2,
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Select
          value={searchParams.searchType}
          onChange={(e) =>
            setSearchParams((prev) => ({
              ...prev,
              searchType: e.target.value,
            }))
          }
          sx={{
            backgroundColor: "white",
            borderRadius: 1,
            minWidth: 200,
          }}
        >
          <MenuItem value="codeName">코드 이름</MenuItem>
          <MenuItem value="codeValue">코드 값</MenuItem>
        </Select>

        <TextField
          label="검색어 입력"
          variant="outlined"
          sx={{
            backgroundColor: "white",
            borderRadius: 1,
            flex: 1,
          }}
          value={searchParams.searchQuery}
          onChange={(e) =>
            setSearchParams((prev) => ({
              ...prev,
              searchQuery: e.target.value,
            }))
          }
        />

        <Button
          variant="contained"
          color="primary"
          sx={{ height: "56px" }}
          onClick={handleSearch}
        >
          검색
        </Button>
      </Box>

      <Box
        sx={{
          marginTop: 3,
          display: "flex",
          flexDirection: { xs: "column", md: "row" },
          gap: 2,
        }}
      >
        <TextField
          label="선택된 부모 코드"
          variant="outlined"
          sx={{
            backgroundColor: "white",
            borderRadius: 1,
            flex: 1,
          }}
          value={parentCode ? parentCode.codeName : ""}
          InputProps={{ readOnly: true }}
          onClick={() => setIsModalOpen(true)} // 모달 열기
        />
        <Button
          variant="contained"
          color="secondary"
          onClick={() => setIsModalOpen(true)}
        >
          부모 코드 검색
        </Button>
      </Box>

      {/* 활성 상태 선택 */}
      <Box sx={{ marginTop: 3 }}>
        <Typography variant="h6">활성 상태</Typography>
        <RadioGroup
          row
          value={searchParams.codeStatus}
          onChange={(e) =>
            setSearchParams((prev) => ({
              ...prev,
              codeStatus: e.target.value,
            }))
          }
        >
          <FormControlLabel value="all" control={<Radio />} label="전체" />
          <FormControlLabel value="active" control={<Radio />} label="현존" />
          <FormControlLabel value="inactive" control={<Radio />} label="폐지" />
        </RadioGroup>
      </Box>

      {/* 부모 코드 검색 모달 */}
      <ParentCodeModal
        open={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </Box>
  );
}

export default CodeSearchBox;
