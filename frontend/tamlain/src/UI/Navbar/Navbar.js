import { Outlet } from "react-router-dom";
import * as S from "./Navbar.styled";

const Navbar = () => {
  return (
    <>
      <S.NavbarDiv>
        <img
          src="assets/MainLogo.png"
          style={{ width: "120px", height: "40px" }}
          alt="메인로고"
        />
      </S.NavbarDiv>
      <Outlet />
    </>
  );
};
export default Navbar;
