import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Sentiment from "./pages/Sentiment";
import Backtest from "./pages/Backtest";
import History from "./pages/History";

export default function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/sentiment" element={<Sentiment />} />
        <Route path="/backtest" element={<Backtest />} />
        <Route path="/history" element={<History />} />
      </Routes>
    </Router>
  );
}
