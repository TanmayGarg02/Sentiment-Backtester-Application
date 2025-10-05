// src/pages/Backtest.jsx
import React, { useState, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { runBacktest } from "../redux/backtestSlice"; // keep as your thunk name
import Loader from "../components/Loader";
import { Play } from "lucide-react";
import {
  LineChart,
  Line,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  AreaChart,
  Area
} from "recharts";

/**
 * Supported strategies and their parameter schemas.
 * key = strategyType sent to backend.
 * label = human readable.
 * params = object where each key maps to { type, label, default, min, max, step, options }
 */
const STRATEGIES = {
  SENTIMENT_THRESHOLD: {
    label: "Sentiment Threshold",
    params: {
      sentimentThreshold: { type: "number", label: "Sentiment Threshold", default: 0.5, min: 0, max: 1, step: 0.01 }
    }
  },
  MEAN_REVERSION: {
    label: "Mean Reversion (z-score)",
    params: {
      lookback: { type: "number", label: "Lookback (days)", default: 20, min: 2, step: 1 },
      entryZ: { type: "number", label: "Entry Z-score", default: 1.5, step: 0.1 },
      exitZ: { type: "number", label: "Exit Z-score", default: 0.5, step: 0.1 }
    }
  },
  MOMENTUM: {
    label: "Momentum (MA crossover)",
    params: {
      fast: { type: "number", label: "Fast MA (days)", default: 10, min: 1, step: 1 },
      slow: { type: "number", label: "Slow MA (days)", default: 50, min: 1, step: 1 }
    }
  },
  RSI: {
    label: "RSI Strategy",
    params: {
      period: { type: "number", label: "RSI Period", default: 14, min: 2, step: 1 },
      oversold: { type: "number", label: "Oversold Threshold", default: 30, min: 1, max: 49, step: 1 },
      overbought: { type: "number", label: "Overbought Threshold", default: 70, min: 51, max: 99, step: 1 }
    }
  }
};

/** small helper: create an equity curve if backend doesn't provide series */
function synthesizeEquity(avgDailyReturn, days) {
  const arr = [];
  let nav = 1.0;
  for (let i = 0; i < Math.max(1, days); i++) {
    nav = nav * (1 + (avgDailyReturn ?? 0));
    arr.push({ x: i + 1, nav: +nav.toFixed(6) });
  }
  return arr;
}

export default function Backtest() {
  const dispatch = useDispatch();
  const { latest, loading, error } = useSelector((s) => s.backtest);

  const [ticker, setTicker] = useState("");
  const [strategy, setStrategy] = useState("SENTIMENT_THRESHOLD");
  const [params, setParams] = useState(() => {
    // initialize with defaults for the initial strategy
    const p = {};
    Object.entries(STRATEGIES[strategy].params).forEach(([k, v]) => (p[k] = v.default));
    return p;
  });
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  // When user changes strategy, reset params to defaults for that strategy
  const onStrategyChange = (newStrategy) => {
    setStrategy(newStrategy);
    const p = {};
    Object.entries(STRATEGIES[newStrategy].params).forEach(([k, v]) => (p[k] = v.default));
    setParams(p);
  };

  const onParamChange = (key, value) => {
    setParams((prev) => ({ ...prev, [key]: value }));
  };

  const handleRun = async (e) => {
    e.preventDefault();
    if (!ticker) {
      alert("Enter ticker symbol (e.g. AAPL)");
      return;
    }
    if (!startDate || !endDate) {
      if (!confirm("No date range specified — run with default historical window?")) {
        return;
      }
    }

    // Build payload consistent with backend expectation:
    const payload = {
      ticker: ticker.toUpperCase(),
      strategyType: strategy,
      parameters: params,
      startDate: startDate || null,
      endDate: endDate || null
    };

    // dispatch thunk
    dispatch(runBacktest(payload));
  };

  // derive equityData: either backend provides `equitySeries` or synthesize
  const equityData = useMemo(() => {
    if (!latest) return [];
    if (latest.equitySeries && Array.isArray(latest.equitySeries)) {
      // normalize expected structure
      return latest.equitySeries.map((p, i) => {
        // accept {date, nav} or {x, value}
        if (p.date && p.nav != null) return { x: p.date, nav: p.nav };
        if (p.x !== undefined && p.nav !== undefined) return { x: p.x, nav: p.nav };
        if (p.value !== undefined) return { x: i + 1, nav: p.value };
        return { x: i + 1, nav: Object.values(p)[0] };
      });
    }
    // fallback: use avgDailyReturn and days
    return synthesizeEquity(latest.avgDailyReturn ?? 0, latest.days ?? 30);
  }, [latest]);

  return (
    <div className="min-h-screen p-8 bg-gradient-to-br from-[#f5f1e6] to-[#efe7d9] text-[#3a2e25]">
      <div className="max-w-5xl mx-auto">
        <header className="flex items-center justify-between mb-6">
          <h1 className="text-4xl font-bold">⚡ Backtest</h1>
          <div className="text-sm text-gray-600">QuantDash — run strategies on historical data</div>
        </header>

        <form className="bg-white rounded-xl shadow-md p-6 mb-6" onSubmit={handleRun}>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="text-sm font-medium">Ticker</label>
              <input
                value={ticker}
                onChange={(e) => setTicker(e.target.value)}
                placeholder="e.g. AAPL"
                className="mt-1 block w-full border rounded-md px-3 py-2 focus:ring focus:ring-green-200"
              />
            </div>

            <div>
              <label className="text-sm font-medium">Strategy</label>
              <select
                value={strategy}
                onChange={(e) => onStrategyChange(e.target.value)}
                className="mt-1 block w-full border rounded-md px-3 py-2"
              >
                {Object.entries(STRATEGIES).map(([key, def]) => (
                  <option key={key} value={key}>
                    {def.label}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="text-sm font-medium">Date range</label>
              <div className="flex gap-2 mt-1">
                <input
                  type="date"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  className="w-1/2 border rounded-md px-3 py-2"
                />
                <input
                  type="date"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  className="w-1/2 border rounded-md px-3 py-2"
                />
              </div>
            </div>
          </div>

          {/* dynamic params */}
          <div className="mt-6">
            <h3 className="font-semibold mb-2">Strategy parameters</h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {Object.entries(STRATEGIES[strategy].params).map(([k, meta]) => (
                <div key={k}>
                  <label className="text-sm">{meta.label}</label>
                  {meta.type === "number" && (
                    <input
                      type="number"
                      value={params[k]}
                      min={meta.min}
                      max={meta.max}
                      step={meta.step ?? "any"}
                      onChange={(e) => onParamChange(k, Number(e.target.value))}
                      className="mt-1 block w-full border rounded-md px-3 py-2"
                    />
                  )}
                  {meta.type === "select" && (
                    <select
                      value={params[k]}
                      onChange={(e) => onParamChange(k, e.target.value)}
                      className="mt-1 block w-full border rounded-md px-3 py-2"
                    >
                      {meta.options.map((o) => (
                        <option key={o.value} value={o.value}>
                          {o.label}
                        </option>
                      ))}
                    </select>
                  )}
                  {meta.type === "text" && (
                    <input
                      type="text"
                      value={params[k]}
                      onChange={(e) => onParamChange(k, e.target.value)}
                      className="mt-1 block w-full border rounded-md px-3 py-2"
                    />
                  )}
                </div>
              ))}
            </div>
          </div>

          <div className="mt-6 flex items-center justify-between">
            <div className="text-sm text-gray-600">Tip: run short range tests first to iterate fast.</div>
            <button
              type="submit"
              className="inline-flex items-center gap-2 bg-green-700 hover:bg-green-800 text-white px-4 py-2 rounded-md"
              disabled={loading}
            >
              <Play size={16} /> {loading ? "Running…" : "Run Backtest"}
            </button>
          </div>
        </form>

        {/* result */}
        {loading && <Loader />}

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-md mb-4">
            Error: {String(error)}
          </div>
        )}

        {latest && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* metrics card */}
            <div className="bg-white p-6 rounded-xl shadow-md">
              <h2 className="text-2xl font-semibold mb-3">{latest.ticker} — {latest.strategyName ?? latest.strategyType}</h2>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <div className="text-sm text-gray-500">Total return</div>
                  <div className="text-2xl font-bold">{((latest.totalReturn ?? 0) * 100).toFixed(2)}%</div>
                </div>
                <div>
                  <div className="text-sm text-gray-500">Sharpe</div>
                  <div className="text-2xl font-bold">{(latest.sharpeRatio ?? 0).toFixed(2)}</div>
                </div>
                <div>
                  <div className="text-sm text-gray-500">Max drawdown</div>
                  <div className="text-2xl font-bold">{((latest.maxDrawdown ?? 0) * 100).toFixed(2)}%</div>
                </div>
                <div>
                  <div className="text-sm text-gray-500">Days</div>
                  <div className="text-2xl font-bold">{latest.days ?? "-"}</div>
                </div>
              </div>

              <div className="mt-4 text-xs text-gray-600">
                Parameters: <pre className="inline">{JSON.stringify(latest.parameters ?? latest.params ?? params)}</pre>
              </div>

              <div className="mt-4 text-sm text-gray-500">
                Run at: {latest.createdAt ? new Date(latest.createdAt).toLocaleString() : "N/A"}
              </div>
            </div>

            {/* chart */}
            <div className="bg-white p-6 rounded-xl shadow-md">
              <h3 className="text-lg font-semibold mb-3">Equity curve</h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={equityData}>
                    <defs>
                      <linearGradient id="colorNav" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#16a34a" stopOpacity={0.8} />
                        <stop offset="95%" stopColor="#16a34a" stopOpacity={0.05} />
                      </linearGradient>
                    </defs>
                    <XAxis dataKey="x" />
                    <YAxis />
                    <CartesianGrid strokeDasharray="3 3" />
                    <Tooltip />
                    <Area type="monotone" dataKey="nav" stroke="#16a34a" fillOpacity={1} fill="url(#colorNav)" />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
