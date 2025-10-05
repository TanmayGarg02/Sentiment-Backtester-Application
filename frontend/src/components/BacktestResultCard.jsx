// src/components/BacktestResultCard.jsx
import React from "react";
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";

function buildEquity(avgDaily, days) {
  const data = [];
  let nav = 1.0;
  for (let i = 0; i < days; i++) {
    nav *= (1 + avgDaily);
    data.push({ day: i+1, value: Number(nav.toFixed(4)) });
  }
  return data;
}

export default function BacktestResultCard({ result }) {
  if (!result) return null;
  const { totalReturn, sharpeRatio, maxDrawdown, avgDailyReturn, days, createdAt } = result;
  const equityData = buildEquity(avgDailyReturn, Math.max(1, days));

  return (
    <div className="mt-6 p-4 border rounded bg-white shadow-sm grid md:grid-cols-2 gap-4">
      <div>
        <h3 className="text-lg font-semibold">{result.ticker} — {result.strategyType}</h3>
        <p className="text-sm text-gray-600">Run at: {createdAt ? new Date(createdAt).toLocaleString() : "N/A"}</p>

        <div className="mt-4 space-y-2">
          <div><strong>Total return:</strong> {(totalReturn*100).toFixed(2)}%</div>
          <div><strong>Sharpe ratio:</strong> {sharpeRatio.toFixed(2)}</div>
          <div><strong>Max drawdown:</strong> {(maxDrawdown*100).toFixed(2)}%</div>
          <div><strong>Avg daily:</strong> {(avgDailyReturn*100).toFixed(3)}%</div>
          <div><strong>Days:</strong> {days}</div>
        </div>
      </div>

      <div className="h-48">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={equityData}>
            <CartesianGrid strokeDasharray="3 3"/>
            <XAxis dataKey="day" />
            <YAxis domain={['dataMin', 'dataMax']} />
            <Tooltip />
            <Line type="monotone" dataKey="value" stroke="#10B981" dot={false} />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
