// src/components/SearchBar.jsx
import React, { useState } from "react";

export default function SearchBar({ onSearch, placeholder = "Enter ticker (e.g. AAPL)" }) {
  const [value, setValue] = useState("");

  const submit = () => {
    if (!value.trim()) return;
    onSearch(value.toUpperCase());
  };

  return (
    <div className="flex gap-2">
      <input
        className="flex-1 border rounded px-3 py-2"
        placeholder={placeholder}
        value={value}
        onChange={(e) => setValue(e.target.value)}
        onKeyDown={(e) => e.key === "Enter" && submit()}
      />
      <button
        onClick={submit}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        Search
      </button>
    </div>
  );
}
