import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchSentiment } from "../redux/sentimentSlice";
import Loader from "../components/Loader";
import { LineChart, Line, CartesianGrid, XAxis, YAxis, Tooltip } from "recharts";

export default function Sentiment() {
  const [ticker, setTicker] = useState("");
  const dispatch = useDispatch();
  const { sentiment, loading } = useSelector((state) => state.sentiment);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (ticker.trim()) dispatch(fetchSentiment(ticker));
  };

  return (
    <div className="min-h-screen bg-[#f5f1e6] text-[#3a2e25] p-10">
      <h2 className="text-4xl font-bold text-center mb-8">📊 Sentiment Analysis</h2>

      <form onSubmit={handleSubmit} className="flex justify-center gap-4 mb-6">
        <input
          value={ticker}
          onChange={(e) => setTicker(e.target.value)}
          className="px-4 py-2 border rounded-lg w-64 focus:ring focus:ring-green-400"
          placeholder="Enter stock ticker"
        />
        <button
          type="submit"
          className="px-6 py-2 bg-green-700 text-white rounded-lg hover:bg-green-800"
        >
          Search
        </button>
      </form>

      {loading && <Loader />}
      {sentiment && (
        <div className="max-w-2xl mx-auto bg-white rounded-lg shadow p-6">
          <p><b>Ticker:</b> {sentiment.ticker}</p>
          <p><b>Score:</b> {sentiment.score.toFixed(2)}</p>

          {/* Chart example */}
          <LineChart width={500} height={300} data={[{ day: 1, score: sentiment.score }]}>
            <Line type="monotone" dataKey="score" stroke="#16a34a" />
            <CartesianGrid stroke="#ccc" />
            <XAxis dataKey="day" />
            <YAxis />
            <Tooltip />
          </LineChart>
        </div>
      )}
    </div>
  );
}
