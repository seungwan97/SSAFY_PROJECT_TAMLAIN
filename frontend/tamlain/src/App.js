import { Route, Routes, Navigate } from "react-router-dom";
import { AnimatePresence } from "framer-motion";

// import VideoRoomComponent from "./components/Openvidu/components/VideoRoomComponent";
const App = () => {
  // const location = useLocation();
  return (
    <AnimatePresence>
      <Routes>
        <Route path="/login" element={<Login />} />;
        <Route path="/" element={<Navigate to="/login" />} />
      </Routes>
    </AnimatePresence>
  );
};

export default App;
