import { Outlet } from "react-router-dom";
import * as S from "./Frame.styled";
const Frame = () => {
  return (
    <S.Frame>
      <S.FrameContainer>
        <S.FrameIcon></S.FrameIcon>
        <Outlet />
      </S.FrameContainer>
    </S.Frame>
  );
};
export default Frame;
