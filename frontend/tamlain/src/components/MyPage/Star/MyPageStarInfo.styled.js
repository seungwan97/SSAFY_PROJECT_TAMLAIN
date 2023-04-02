import styled from "styled-components";

export const Container = styled.div`
  width:100%;
  height:110px;
  // background-color:red;
`;

export const Wrap = styled.div`
  margin-top: 30%;
  margin-left:5%;
  color:#3A3A3A;
  height:100%;
  // background-color:blue;
`;


export const RegistBtn = styled.button`

position: absolute;

color: #ffffff;
width: 90%;
height: 70px;
box-shadow: 0 0 0 3px #ffffff;
border-radius: 20px;
border: none;
background-color: #FFC08E;
text-align: center;
font-size: 30px;
font-weight: bold;
margin: 20px 0 0 20px;
cursor: pointer;

:hover {
  background-color: #FC872A;
  transition: 0.5s;
  color: #ffffff;
}
`;

