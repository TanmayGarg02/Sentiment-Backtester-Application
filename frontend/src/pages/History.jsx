import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchHistory } from "../redux/backtestSlice"; // ensure your thunk name matches
import Loader from "../components/Loader";

export default function History() {
  const dispatch = useDispatch();
  const { history, loading, error } = useSelector((s) => s.backtest);

  useEffect(() => {
    dispatch(fetchHistory());
  }, [dispatch]);

  return (
    <div className="min-h-screen p-8 bg-gradient-to-br from-[#f5f1e6] to-[#efe7d9] text-[#3a2e25]">
      <div className="max-w-6xl mx-auto">
        <header className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold">📜 Backtest History</h1>
          <div className="text-sm text-gray-600">All saved backtests</div>
        </header>

        {loading && <Loader />}

        {error && <div className="bg-red-50 border border-red-200 text-red-700 p-3 rounded">{String(error)}</div>}

        {!loading && (!history || history.length === 0) && (
          <div className="text-gray-600 p-6 bg-white rounded shadow">No backtests found yet.</div>
        )}

        {!loading && history && history.length > 0 && (
          <div className="bg-white rounded-xl shadow p-4 overflow-auto">
            <table className="min-w-full table-auto text-left">
              <thead className="bg-[#e6dfcf]">
                <tr>
                  <th className="p-3">When</th>
                  <th className="p-3">Ticker</th>
                  <th className="p-3">Strategy</th>
                  <th className="p-3">Parameters</th>
                  <th className="p-3">Return</th>
                  <th className="p-3">Sharpe</th>
                </tr>
              </thead>
              <tbody>
                {history.map((h, i) => (
                  <tr key={i} className="border-t hover:bg-[#fbfaf7]">
                    <td className="p-3 align-top">{h.createdAt ? new Date(h.createdAt).toLocaleString() : "-"}</td>
                    <td className="p-3 align-top font-semibold">{h.ticker}</td>
                    <td className="p-3 align-top">{h.strategyName ?? h.strategyType}</td>
                    <td className="p-3 align-top text-sm">
                      <pre className="whitespace-pre-wrap">{JSON.stringify(h.parameters ?? h.params ?? h.parametersJson ?? {}, null, 0)}</pre>
                    </td>
                    <td className="p-3 align-top">{((h.totalReturn ?? 0) * 100).toFixed(2)}%</td>
                    <td className="p-3 align-top">{(h.sharpeRatio ?? 0).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
