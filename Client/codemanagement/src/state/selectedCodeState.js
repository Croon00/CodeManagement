// src/state/store.js
import { atom } from "recoil";

// 선택된 코드 정보를 관리하는 Recoil 상태
export const selectedCodeState = atom({
  key: "selectedCodeState", // 상태의 고유 키 (필수)
  default: null, // 초기값 (선택된 코드 없음)
});

// 모든 코드 리스트를 관리하는 Recoil 상태
export const allCodesState = atom({
  key: "allCodesState",
  default: [], // 초기값은 빈 배열
});
