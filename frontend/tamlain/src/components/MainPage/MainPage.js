import { Link } from "react-router-dom";
import * as S from "./MainPage.styled";

const MainPage = () => {
  return (
    <S.Container>
      <Link to="/login">카카오 로그인</Link>
    </S.Container>
  );
};

export default MainPage;
