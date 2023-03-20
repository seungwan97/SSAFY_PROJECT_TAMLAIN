import React from "react";
import * as S from "./Login.styled";
import { KAKAO_AUTH_URL } from "./OAuth";

const Login = () => {
  const kakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  return (
    <S.Container>
      <S.Filter />
      <S.KakaoBtnContainer onClick={kakaoLogin}>
        <S.KakaoImg src="./assets/Icon/kakao_login_medium_wide.png" />
      </S.KakaoBtnContainer>
    </S.Container>
  );
};

export default Login;
