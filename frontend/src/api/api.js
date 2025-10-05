// src/api/api.js
import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE || "/api";

const client = axios.create({
  baseURL: API_BASE,
  timeout: 15000,
});

// Sentiment
export async function fetchSentiment(ticker) {
  const res = await client.get(`/sentiment/${ticker}`);
  return res.data;
}

export async function refreshSentiment(ticker) {
  const res = await client.get(`/sentiment/${ticker}/refresh`);
  return res.data;
}

// Backtest - request body should be { ticker, strategyType, parameters, startDate, endDate }
export async function runBacktest(payload) {
  const res = await client.post("/backtest/run", payload);
  return res.data;
}

// Get historical backtests for a strategy (or optionally for strategy+ticker)
export async function fetchBacktestsByStrategy(strategyType) {
  const res = await client.get(`/backtest/strategy/${strategyType}`);
  return res.data;
}

export async function fetchBacktestsByStrategyAndTicker(strategyType, ticker) {
  const res = await client.get(`/backtest/strategy/${strategyType}/ticker/${ticker}`);
  return res.data;
}
