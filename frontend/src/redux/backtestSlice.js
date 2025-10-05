import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const runBacktest = createAsyncThunk(
  "backtest/run",
  async (params, thunkAPI) => {
    try {
      const response = await axios.post("http://localhost:8080/api/backtest/run", params);
      return response.data;
    } catch (err) {
      return thunkAPI.rejectWithValue(err.response?.data || "Error running backtest");
    }
  }
);

export const fetchHistory = createAsyncThunk(
  "backtest/history",
  async (_, thunkAPI) => {
    try {
      const response = await axios.get("http://localhost:8080/api/backtest/history");
      return response.data;
    } catch (err) {
      return thunkAPI.rejectWithValue(err.response?.data || "Error fetching history");
    }
  }
);

const backtestSlice = createSlice({
  name: "backtest",
  initialState: {
    history: [],
    latest: null,
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(runBacktest.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(runBacktest.fulfilled, (state, action) => {
        state.loading = false;
        state.latest = action.payload;
      })
      .addCase(runBacktest.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(fetchHistory.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchHistory.fulfilled, (state, action) => {
        state.loading = false;
        state.history = action.payload;
      })
      .addCase(fetchHistory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export default backtestSlice.reducer;
