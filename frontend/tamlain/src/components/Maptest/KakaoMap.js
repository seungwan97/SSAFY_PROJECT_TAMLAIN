import React, { useEffect } from "react";

const { kakao } = window;

const KakaoMap = () => {
  useEffect(() => {
    const container = document.getElementById("map"); // 지도 담을 영역
    const options = {
      center: new kakao.maps.LatLng(33.490365, 126.496376),
      level: 3,
    };
    const map = new kakao.maps.Map(container, options); // 지도 생성
  }, []);

  return <div id="map" style={{ width: "500px", height: "500px" }}></div>;
};

export default KakaoMap;
