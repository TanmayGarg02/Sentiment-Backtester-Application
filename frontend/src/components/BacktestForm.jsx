// src/components/BacktestForm.jsx
import React, { useState } from "react";

const defaultParams = {
  SENTIMENT: { sentimentThreshold: 0.5 },
  MEAN_REVERSION: { lookback: 14, entryZ: 1.5 },
  MOMENTUM: { lookback: 20 }
};

export default function BacktestForm({ onRun }) {
  const [ticker, setTicker] = useState("");
  const [strategy, setStrategy] = useState("SENTIMENT");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [params, setParams] = useState(defaultParams[strategy]);

  const changeStrategy = (s) => {
    setStrategy(s);
    setParams(defaultParams[s]);
  };

  const handleParamChange = (key, value) => {
    setParams(prev => ({ ...prev, [key]: Number(value) }));
  };

  const run = () => {
    if (!ticker || !startDate || !endDate) {
      alert("Provide ticker, start and end dates.");
      return;
    }
    const payload = {
      ticker: ticker.toUpperCase(),
      strategyType: strategy,
      parameters: params,
      startDate,
      endDate
    };
    onRun(payload);
  };

  return (
    <div className="p-4 border rounded bg-white shadow-sm">
      <div className="grid md:grid-cols-4 gap-3">
        <input className="border rounded px-2 py-1" placeholder="Ticker" value={ticker} onChange={(e)=>setTicker(e.target.value.toUpperCase())} />
        <select className="border rounded px-2 py-1" value={strategy} onChange={(e)=>changeStrategy(e.target.value)}>
          <option value="SENTIMENT">Sentiment Threshold</option>
          <option value="MEAN_REVERSION">Mean Reversion</option>
          <option value="MOMENTUM">Momentum (MA)</option>
        </select>
        <input className="border rounded px-2 py-1" type="date" value={startDate} onChange={(e)=>setStartDate(e.target.value)} />
        <input className="border rounded px-2 py-1" type="date" value={endDate} onChange={(e)=>setEndDate(e.target.value)} />
      </div>

      {/* dynamic parameter UI */}
      <div className="mt-3 space-y-2">
        {strategy === "SENTIMENT" && (
          <div>
            <label className="block text-sm">Sentiment Threshold</label>
            <input type="number" step="0.01" min="0" max="1"
                   value={params.sentimentThreshold}
                   onChange={(e)=>handleParamChange("sentimentThreshold", e.target.value)}
                   className="border rounded px-2 py-1 w-32" />
          </div>
        )}

        {strategy === "MEAN_REVERSION" && (
          <>
            <div>
              <label className="block text-sm">Lookback (days)</label>
              <input type="number" value={params.lookback} onChange={(e)=>handleParamChange("lookback", e.target.value)} className="border rounded px-2 py-1 w-32" />
            </div>
            <div>
              <label className="block text-sm">Entry Z-score</label>
              <input type="number" step="0.1" value={params.entryZ} onChange={(e)=>handleParamChange("entryZ", e.target.value)} className="border rounded px-2 py-1 w-32" />
            </div>
          </>
        )}

        {strategy === "MOMENTUM" && (
          <div>
            <label className="block text-sm">Lookback (days)</label>
            <input type="number" value={params.lookback} onChange={(e)=>handleParamChange("lookback", e.target.value)} className="border rounded px-2 py-1 w-32" />
          </div>
        )}
      </div>

      <div className="mt-4">
        <button onClick={run} className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Run Backtest</button>
      </div>
    </div>
  );
}
