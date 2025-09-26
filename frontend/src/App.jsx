import React from "react";
import SearchBar from "./components/SearchBar";
import SentimentResults from "./components/SearchResult";

export default function App() {
  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <h1 className="text-3xl font-bold text-center mb-6 ">
        📊 Stock Sentiment Dashboard
      </h1>
      <div className="max-w-3xl mx-auto bg-white p-6 rounded-xl shadow-md">
        <SearchBar />
        <SentimentResults />
      </div>
    </div>
  );
}
