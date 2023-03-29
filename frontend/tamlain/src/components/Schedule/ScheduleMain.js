import * as S from "./ScheduleMain.styled";
import ScheduleCarousel from "./ScheduleCarousel";
import Modal from "../../UI/Modal/Modal";
import { useState } from "react";
import ModalRegist from "../../UI/Modal/ModalRegist";

const ScheduleMain = () => {
  const [modaltest, setModaltest] = useState(false);
  const [modaltest2, setModaltest2] = useState(false);
  const ModalTest = () => {
    setModaltest((modaltest) => !modaltest);
  };
  const ModalTest2 = () => {
    setModaltest2((modaltest2) => !modaltest2);
  };

  return (
    <S.Container>
      <button onClick={ModalTest}> 나가기 모달 테스트 버튼 </button>
      {modaltest && (
        <Modal
          name="정말 일정 등록을 그만하시겠습니까?"
          name2="(작성중인 일정은 저장되지 않습니다.)"
        ></Modal>
      )}
      <button onClick={ModalTest2}> 일정 등록 모달 테스트 버튼 </button>
      {modaltest2 && <ModalRegist></ModalRegist>}
      <ScheduleCarousel />
    </S.Container>
  );
};

export default ScheduleMain;
