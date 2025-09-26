import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { fetchSentiment } from "../features/sentimentSlice";
import { Search } from "lucide-react";

export default function SearchBar() {
  const [ticker, setTicker] = useState("");
  const dispatch = useDispatch();

  const handleSearch = () => {
    if (ticker.trim()) {
      dispatch(fetchSentiment(ticker.trim().toUpperCase()));
    }
  };

  return (
    <div className="flex gap-2 mb-4">
      <input
        type="text"
        placeholder="Enter stock ticker (e.g. AAPL)"
        value={ticker}
        onChange={(e) => setTicker(e.target.value)}
        className="flex-grow border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        onClick={handleSearch}
        className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
      >
        <Search className="w-4 h-4" />
        Search
      </button>
    </div>
  );
}
