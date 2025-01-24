import { atom } from "recoil";

// 검색 조건 상태
export const searchParamsState = atom({
  key: "searchParamsState",
  default: {
    searchType: "codeName", // 검색 기준
    searchQuery: "", // 검색어
    startDate: "", // 시작일
    endDate: "", // 종료일
    codeStatus: "all", // 코드 상태 (all, active, inactive)
  },
});

// 검색 결과 상태
export const searchResultsState = atom({
  key: "searchResultsState",
  default: [],
});
