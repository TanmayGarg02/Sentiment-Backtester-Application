import { BarChart3, Home, History, Activity } from "lucide-react";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="bg-gray-900 text-white px-6 py-4 flex items-center justify-between shadow-md">
      <div className="flex items-center gap-2 text-xl font-bold">
        <BarChart3 size={28} className="text-blue-400" />
        <span>QuantDash</span>
      </div>
      <div className="flex gap-6 text-lg">
        <Link to="/" className="hover:text-blue-400 flex items-center gap-1">
          <Home size={18} /> Home
        </Link>
        <Link to="/sentiment" className="hover:text-blue-400 flex items-center gap-1">
          <Activity size={18} /> Sentiment
        </Link>
        <Link to="/backtest" className="hover:text-blue-400 flex items-center gap-1">
          <BarChart3 size={18} /> Backtest
        </Link>
        <Link to="/history" className="hover:text-blue-400 flex items-center gap-1">
          <History size={18} /> History
        </Link>
      </div>
    </nav>
  );
}
