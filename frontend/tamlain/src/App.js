import { Route, Routes } from "react-router-dom";
import { AnimatePresence } from "framer-motion";
import Login from "./components/Login/Login";
import MainPage from "./components/MainPage/MainPage";
import Navbar from "./UI/Navbar/Navbar";
import OAuthRedirectPage from "./components/Login/OAuthRedirectPage";
import Loading from "./UI/Loading/Loading";
import Login from "./components/Login/Login";

const App = () => {
  return (
    // <div>
    //   <Login />
    // </div>
    <AnimatePresence>
      <Routes>
        {/* <Route path="/" element={<Navbar />} /> */}
        <Route path="/" element={<MainPage />} />
        <Route path="oauth/callback/kakao" element={<OAuthRedirectPage />} />
        <Route path="/login" element={<Login />} />;
        <Route path="/" element={<Navigate to="/loading" />} />
        <Route path="/loading" element={<Loading />} />
      </Routes>
    </AnimatePresence>
  );
};

export default App;
