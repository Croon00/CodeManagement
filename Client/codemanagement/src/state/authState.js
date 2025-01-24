import { atom } from "recoil";

export const authState = atom({
  key: "authState",
  default: {
    isLoggedIn: !!localStorage.getItem("accessToken"),
    authorities: [],
  },
});
