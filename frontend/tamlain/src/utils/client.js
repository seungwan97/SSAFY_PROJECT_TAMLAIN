import axios from "axios";

const client = axios.create();

// 로컬 테스트 용
client.defaults.baseURL = "http://localhost:8080";
<<<<<<< HEAD
// client.defaults.baseURL = "";
// 배포 서버 용`
=======

client.defaults.url = "http://localhost:3000";

// // 배포 서버 용`
>>>>>>> 55b9ec1cbff4114981f8463814f2c264a2529352
// client.defaults.baseURL = "https://j8b204.p.ssafy.io/api";

// client.defaults.url = "https://j8b204.p.ssafy.io";

export default client;
