import * as S from "./Navbar.styled";
import { useState } from "react";

const Navbar = (props) => {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <S.NavFrame>
      <S.NavContainer>
        <S.MainLogoImg src="assets/MainLogo.png" alt=" 메인 로고 " />
        <S.RightContainer>
          {!isLogin && <S.RightItem>로그인 </S.RightItem>}
          {isLogin && <S.RightItem>마이페이지 </S.RightItem>}
          {isLogin && <S.RightItem>|</S.RightItem>}
          {isLogin && <S.RightItem>로그아웃 </S.RightItem>}
          <S.ExitLogoImg
            src="assets/Icon/ExitLogo.png"
            alt="logIn/Out 로고"
          ></S.ExitLogoImg>
        </S.RightContainer>
      </S.NavContainer>
    </S.NavFrame>
  );
};

export default Navbar;
