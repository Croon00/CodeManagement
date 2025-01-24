import React from "react";
import { Routes, Route } from "react-router-dom";
import Home from "./components/Home";
import { RecoilRoot } from "recoil";
import CodeCreate from "./components/code/CodeCreate";
import CodeUpdate from "./components/code/CodeUpdate";
import CodeList from "./components/code/CodeList";
import Navigation from "./components/Navigation";
import Login from "./components/user/Login";
import UserRegister from "./components/user/UserRegister"; // 유저 등록 페이지

function App() {
  return (
    <div>
      <RecoilRoot>
        {/* Navigation을 공통으로 렌더링 */}
        <Navigation />
        {/* 페이지별 라우팅 */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/create" element={<CodeCreate />} />
          <Route path="/update/:codeId" element={<CodeUpdate />} />
          <Route path="/list" element={<CodeList />} />
          <Route path="/login" element={<Login />} />
          <Route path="/user/register" element={<UserRegister />} />{" "}
          {/* 유저 등록 */}
        </Routes>
      </RecoilRoot>
    </div>
  );
}

export default App;
