// src/state/parentCodeState.js
import { atom } from "recoil";

export const parentCodeState = atom({
  key: "parentCodeState", // 상태 고유 키
  default: null, // 초기값은 부모 코드 없음
});

export const selectedParentCodeState = atom({
  key: "selectedParentCodeState", // 고유 키
  default: null, // 초기값
});
