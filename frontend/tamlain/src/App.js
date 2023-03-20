import { Route, Routes, Navigate } from "react-router-dom";
import { AnimatePresence } from "framer-motion";
import Loading from "./UI/Loading/Loading";
import Login from "./components/Login/Login";
import Navbar from "./UI/Navbar/Navbar";
import Frame from "./UI/Frame/Frame";

import SurveyMain from "./components/Survey/SurveyMain";
import SurveyCalendar from "./components/Survey/SurveyCalendar";
import SurveyGenderAndAge from "./components/Survey/SurveyGenderAndAge";
import SurveyWithWho from "./components/Survey/SurveyWithWho";
import SurveyCar from "./components/Survey/SurveyCar";
import SurveyTheme from "./components/Survey/SurveyTheme";
import SurveyFood from "./components/Survey/SurveyFood";
import SurveyCafe from "./components/Survey/SurveyCafe";
import SurveyActivity from "./components/Survey/SurveyActivity";
import SurveySport from "./components/Survey/SurveySport";
import SurveyExhibition from "./components/Survey/SurveyExhibition";
import SurveyRest from "./components/Survey/SurveyRest";

const App = () => {
  // const location = useLocation();
  return (
    <>
      <AnimatePresence>
        <Routes>
          <Route path="/login" element={<Login />} />;
          <Route path="/" element={<Navigate to="/surveyCalendar" />} />
          <Route path="/loading" element={<Loading />} />
          <Route element={<Navbar />}>
            {/**질문페이지 */}
            <Route element={<SurveyMain />}>
              <Route element={<Frame />}>
                <Route path="/surveyCalendar" element={<SurveyCalendar />} />
                <Route
                  path="/surveyGenderAndAge"
                  element={<SurveyGenderAndAge />}
                />
                <Route path="/surveyWithWho" element={<SurveyWithWho />} />
                <Route path="/surveyCar" element={<SurveyCar />} />
                <Route path="/surveyTheme" element={<SurveyTheme />} />
                <Route path="/surveyFood" element={<SurveyFood />} />
                <Route path="/surveyCafe" element={<SurveyCafe />} />
                <Route path="/surveyActivity" element={<SurveyActivity />} />
                <Route path="/surveySport" element={<SurveySport />} />
                <Route
                  path="/surveyExhibition"
                  element={<SurveyExhibition />}
                />
                <Route path="/surveyRest" element={<SurveyRest />} />
              </Route>
            </Route>
          </Route>
        </Routes>
      </AnimatePresence>
    </>
  );
};

export default App;
