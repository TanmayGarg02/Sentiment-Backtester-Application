// src/components/SearchResult.jsx
import React from "react";

export default function SearchResult({ data, onRefresh }) {
  if (!data) return null;

  return (
    <div className="mt-6 p-4 border rounded shadow-sm bg-white">
      <div className="flex justify-between items-start">
        <div>
          <h2 className="text-lg font-semibold">{data.ticker} — {data.label}</h2>
          <p className="text-sm text-gray-600">Source: {data.source}</p>
        </div>
        <div>
          <button
            onClick={() => onRefresh(data.ticker)}
            className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
          >
            Force refresh
          </button>
        </div>
      </div>

      <div className="mt-3 grid grid-cols-2 gap-4">
        <div>
          <p><strong>Score</strong></p>
          <p className="text-2xl">{(data.score ?? 0).toFixed(3)}</p>
        </div>
        <div>
          <p><strong>Last updated</strong></p>
          <p className="text-sm text-gray-600">{data.lastUpdatedIso ?? "N/A"}</p>
        </div>
      </div>

      {data.url && (
        <div className="mt-3">
          <a href={data.url} target="_blank" rel="noreferrer" className="text-blue-600 underline">
            Source link
          </a>
        </div>
      )}
    </div>
  );
}
