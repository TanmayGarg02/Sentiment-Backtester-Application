// src/components/HistoryTable.jsx
import React from "react";

export default function HistoryTable({ rows, onSelect }) {
  if (!rows || rows.length === 0) {
    return <div className="p-4 text-gray-600">No past runs.</div>;
  }

  return (
    <div className="overflow-auto mt-4">
      <table className="min-w-full bg-white border">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 text-left">When</th>
            <th className="p-2 text-left">Ticker</th>
            <th className="p-2 text-left">Strategy</th>
            <th className="p-2 text-left">Return</th>
            <th className="p-2 text-left">Sharpe</th>
            <th className="p-2 text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((r, idx) => (
            <tr key={idx} className="border-t">
              <td className="p-2">{r.createdAt ? new Date(r.createdAt).toLocaleString() : "-"}</td>
              <td className="p-2">{r.ticker}</td>
              <td className="p-2">{r.strategyType}</td>
              <td className="p-2">{(r.totalReturn*100).toFixed(2)}%</td>
              <td className="p-2">{r.sharpeRatio.toFixed(2)}</td>
              <td className="p-2">
                <button onClick={()=>onSelect(r)} className="bg-blue-600 text-white px-3 py-1 rounded">View</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
