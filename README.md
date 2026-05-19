<div align="center">

# 📈 QuantDash

**AI-Powered Sentiment Analysis & Algorithmic Trading Backtester**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Python](https://img.shields.io/badge/Python-3.11+-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://python.org/)
[![Tailwind](https://img.shields.io/badge/Tailwind_CSS-4-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://rabbitmq.com/)

A full-stack, microservice-oriented platform that combines **real-time NLP sentiment analysis** from financial news and social media with **multi-strategy algorithmic backtesting** — enabling traders and quant researchers to test sentiment-driven strategies against historical data.

[Getting Started](#-getting-started) · [Architecture](#-architecture) · [API Reference](#-api-reference) · [Roadmap](#-roadmap)

</div>

---

## 🎯 Overview

Sentiment Backtester bridges the gap between **market sentiment intelligence** and **quantitative strategy validation**. The platform ingests live financial text data from multiple sources, processes it through domain-specific NLP models (FinBERT), and feeds the resulting sentiment signals into a backtesting engine where users can evaluate various trading strategies.

### Key Capabilities

- **Multi-Source Sentiment Ingestion** — Aggregates financial text from NewsAPI and Reddit (r/stocks) in real-time
- **FinBERT-Powered NLP** — Uses ProsusAI/FinBERT via Hugging Face Inference API for finance-domain sentiment classification (positive / negative / neutral)
- **Multi-Strategy Backtesting** — Supports Sentiment Threshold, Mean Reversion, Momentum, and RSI strategies with configurable parameters
- **Real-Time Dashboard** — Interactive React UI with equity curves, performance metrics (Sharpe, drawdown), and backtest history
- **Event-Driven Architecture** — RabbitMQ message broker for asynchronous NLP processing pipelines
- **High-Performance Caching** — Redis-backed sentiment score caching for sub-millisecond strategy lookups
- **Persistent Storage** — PostgreSQL with Flyway migrations for backtest results and market data

---

## 💡 Motivation

Traditional backtesting platforms treat price action in isolation, ignoring the **information layer** that drives markets — news headlines, social media buzz, and analyst sentiment. Sentiment Backtester was built to answer a simple question:

> *"What if you could systematically test whether public sentiment actually predicts stock movement?"*

This project combines three engineering disciplines into a single cohesive platform:

1. **NLP Engineering** — Domain-specific transformer models (FinBERT) applied to financial text, with multi-source data aggregation and confidence scoring
2. **Quantitative Finance** — Multi-strategy backtesting engine with industry-standard metrics (Sharpe ratio, max drawdown, equity curves)
3. **Distributed Systems** — Event-driven microservice architecture with async messaging (RabbitMQ), caching (Redis), and persistent storage (PostgreSQL)

The result is a platform where a user can type a stock ticker, instantly see AI-generated sentiment analysis from news and Reddit, then run that sentiment signal through configurable trading strategies — all visualized in a real-time dashboard.

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        FRONTEND (React 19 + Vite)                   │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐               │
│  │   Home   │ │Sentiment │ │ Backtest │ │ History  │               │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘               │
│       └─────────────┴────────────┴─────────────┘                    │
│            Redux Toolkit (State) + Axios (HTTP)                     │
└──────────────────────────────┬──────────────────────────────────────┘
                               │ REST API
┌──────────────────────────────▼──────────────────────────────────────┐
│                   BACKEND (Spring Boot 3.5 / Java 21)               │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                     Controller Layer                        │    │
│  │  SentimentController · BacktestController · MarketData      │    │
│  │  IngestionController                                        │    │
│  └──────────────────────────┬──────────────────────────────────┘    │
│  ┌──────────────────────────▼──────────────────────────────────┐    │
│  │                      Service Layer                          │    │
│  │  SentimentService · BacktestService · MarketDataService     │    │
│  │  IngestionService · MessagingService                        │    │
│  └─────┬──────────────────────────────────────────┬────────────┘    │
│        │                                          │                 │
│  ┌─────▼─────┐  ┌──────────────┐  ┌──────────────▼────────────┐    │
│  │ PostgreSQL │  │    Redis     │  │       RabbitMQ            │    │
│  │  (JPA +    │  │  (Caching)  │  │  (Async Messaging)       │    │
│  │  Flyway)   │  │             │  │                           │    │
│  └───────────┘  └──────────────┘  └──────────────┬────────────┘    │
└──────────────────────────────────────────────────┼──────────────────┘
                                                   │ AMQP
┌──────────────────────────────────────────────────▼──────────────────┐
│                   NLP WORKER (Python / FastAPI)                      │
│                                                                     │
│  ┌───────────────┐  ┌───────────────┐  ┌───────────────────────┐    │
│  │  News Client  │  │ Reddit Client │  │  Sentiment Analyzer   │    │
│  │  (NewsAPI)    │  │   (PRAW)      │  │  (FinBERT via HF API) │    │
│  └───────────────┘  └───────────────┘  └───────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
```

### Service Breakdown

| Service | Tech | Port | Purpose |
|---------|------|------|---------|
| **Frontend** | React 19, Vite 7, TailwindCSS 4, Redux Toolkit, Recharts | `5173` | Interactive dashboard & strategy configuration |
| **Backend** | Spring Boot 3.5, Java 21, JPA, WebFlux | `8080` | REST API, backtesting engine, data persistence |
| **NLP Worker** | Python 3.11+, FastAPI, FinBERT, PRAW | `8000` | Sentiment analysis & data ingestion pipeline |
| **PostgreSQL** | PostgreSQL 15 | `5432` | Backtest results, market data, sentiment records |
| **Redis** | Redis 7 | `6379` | Sentiment score caching, session data |
| **RabbitMQ** | RabbitMQ 3 (Management) | `5672` / `15672` | Async message broker for ingestion pipeline |

---

## 🛠 Tech Stack

### Backend
- **Java 21** with Spring Boot 3.5
- **Spring Data JPA** + PostgreSQL (Flyway migrations)
- **Spring AMQP** (RabbitMQ integration)
- **Spring Data Redis** (caching layer)
- **Spring WebFlux** (reactive HTTP client for NLP worker calls)
- **Spring Actuator** (health, metrics, info endpoints)
- **Lombok** (boilerplate reduction)
- **Jackson** (JSON serialization with JSR-310 support)

### NLP Worker
- **Python 3.11+** with FastAPI
- **ProsusAI/FinBERT** via Hugging Face Inference API
- **PRAW** (Python Reddit API Wrapper)
- **NewsAPI** client for financial news
- **yfinance** for ticker-to-company-name resolution

### Frontend
- **React 19** with Vite 7
- **Redux Toolkit** + React-Redux for state management
- **TailwindCSS 4** + Flowbite components
- **Recharts** for equity curve & performance visualizations
- **Framer Motion** for animations
- **Lucide React** for icons
- **React Router DOM 7** for SPA routing
- **Axios** for API communication

### Infrastructure
- **Docker Compose** for local development orchestration
- **RabbitMQ 3** with management UI
- **Redis 7** for high-performance caching
- **PostgreSQL 15** for persistent storage

---

## 🔄 Data Flow

The platform follows a multi-stage pipeline from raw text to actionable trading signals:

```
  User enters ticker (e.g. "AAPL")
           │
           ▼
  ┌─────────────────────┐
  │ Frontend (React)    │── GET /api/sentiment/AAPL ──▶ Spring Boot Backend
  └─────────────────────┘                                     │
                                                              ▼
                                              ┌──────────────────────────┐
                                              │ SentimentService         │
                                              │ 1. Check Redis cache     │
                                              │ 2. If miss → call Python │
                                              │ 3. If fail → fallback DB │
                                              └────────────┬─────────────┘
                                                           │
                                              WebClient GET /sentiment/AAPL
                                                           │
                                                           ▼
                                              ┌──────────────────────────┐
                                              │ NLP Worker (FastAPI)     │
                                              │ 1. Resolve ticker → name │
                                              │    via yfinance          │
                                              │ 2. Fetch NewsAPI articles│
                                              │ 3. Fetch Reddit posts    │
                                              │ 4. Batch FinBERT analysis│
                                              │ 5. Return best sentiment │
                                              └────────────┬─────────────┘
                                                           │
                                                    Response flows back
                                                           │
                                                           ▼
                                              ┌──────────────────────────┐
                                              │ Backend persists:        │
                                              │ • Redis (3h TTL cache)   │
                                              │ • PostgreSQL (permanent) │
                                              └──────────────────────────┘
```

### Sentiment Analysis Pipeline

1. **Ticker Resolution** — `yfinance` resolves the ticker symbol to a company name (e.g., `AAPL` → `Apple Inc.`) for broader search coverage
2. **Multi-Source Fetch** — Concurrently queries NewsAPI (financial headlines) and Reddit (r/stocks subreddit) using the combined query `"AAPL OR Apple Inc."`
3. **FinBERT Classification** — Each text (title + description/body) is truncated to 500 chars and sent to the ProsusAI/FinBERT model via Hugging Face Inference API for tri-class sentiment classification: `positive`, `negative`, or `neutral`
4. **Confidence Filtering** — Results are filtered by a configurable confidence threshold (default 0.70). The highest-confidence prediction across all sources is returned
5. **Caching & Persistence** — The winning sentiment score is cached in Redis (3-hour TTL) and persisted to PostgreSQL for historical analysis

### Backtesting Pipeline

1. **Strategy Selection** — User selects from 4 strategies (Sentiment, Mean Reversion, Momentum, RSI) and configures parameters
2. **Signal Generation** — The selected strategy generates daily position signals based on its logic (e.g., sentiment threshold crossing, z-score deviation)
3. **PnL Simulation** — Daily returns are computed based on position and simulated price movements
4. **Metrics Calculation** — Total return, Sharpe ratio (annualized √252), and maximum drawdown are computed from the return series
5. **Persistence** — Results are saved to PostgreSQL with full parameter JSON for reproducibility and historical comparison

---

## 📊 Strategy Deep Dive

### Sentiment Threshold
The core strategy that makes this platform unique. It uses real-time NLP sentiment scores as trading signals.

- **Long signal**: sentiment score > threshold → buy
- **Short signal**: sentiment score < -threshold → sell
- **Neutral**: sentiment within threshold → hold flat
- **Key parameter**: `sentimentThreshold` (0.0–1.0, default 0.5)

### Mean Reversion (Z-Score)
Classic statistical arbitrage strategy that bets on prices reverting to their historical mean.

- **Short signal**: z-score > entryZ (price is unusually high) → sell
- **Long signal**: z-score < -entryZ (price is unusually low) → buy
- **Key parameters**: `lookback` (rolling window, default 5 days), `entryZ` (z-score threshold, default 1.0)

### Momentum (MA Crossover)
Trend-following strategy that rides the direction of price momentum.

- **Long signal**: positive momentum (uptrend detected)
- **Short signal**: negative momentum (downtrend detected)
- **Key parameters**: `fast` (fast MA period, default 10), `slow` (slow MA period, default 50)

### RSI Strategy
Oscillator-based strategy that identifies overbought and oversold conditions.

- **Long signal**: RSI < oversold threshold (market is oversold) → buy
- **Short signal**: RSI > overbought threshold (market is overbought) → sell
- **Key parameters**: `period` (RSI period, default 14), `oversold` (default 30), `overbought` (default 70)

### Performance Metrics Explained

| Metric | Formula | Interpretation |
|--------|---------|----------------|
| **Total Return** | `NAV_final / NAV_initial - 1` | Overall profit/loss as a percentage |
| **Sharpe Ratio** | `(avg_return / std_return) × √252` | Risk-adjusted return; >1 is good, >2 is excellent |
| **Max Drawdown** | `max((NAV - peak) / peak)` | Worst peak-to-trough decline; lower magnitude is better |
| **Avg Daily Return** | `mean(daily_returns)` | Expected daily return of the strategy |

---

## 🗄 Database Schema

The application uses three core tables managed by JPA with Flyway migrations:

### `sentiment_data`
| Column | Type | Description |
|--------|------|-------------|
| `id` | `BIGINT (PK)` | Auto-generated ID |
| `ticker` | `VARCHAR` | Stock symbol (e.g., AAPL) |
| `sentiment_score` | `DOUBLE` | Confidence score from FinBERT (0.0–1.0) |
| `sentiment_label` | `VARCHAR` | Classification: positive, negative, neutral |
| `source` | `VARCHAR` | Data source: news, reddit, cache, fallback |
| `url` | `VARCHAR` | Link to source article/post |
| `date` | `TIMESTAMP` | When the sentiment was recorded |

### `backtest_results`
| Column | Type | Description |
|--------|------|-------------|
| `id` | `BIGINT (PK)` | Auto-generated ID |
| `strategy_name` | `VARCHAR` | Strategy type used |
| `ticker` | `VARCHAR` | Stock symbol tested |
| `start_date` | `DATE` | Backtest start date |
| `end_date` | `DATE` | Backtest end date |
| `total_return` | `DOUBLE` | Strategy total return |
| `sharpe_ratio` | `DOUBLE` | Annualized Sharpe ratio |
| `max_drawdown` | `DOUBLE` | Maximum drawdown percentage |
| `parameters_json` | `TEXT (LOB)` | Strategy parameters as JSON |
| `created_at` | `TIMESTAMP` | When the backtest was run |

### `market_data`
| Column | Type | Constraint | Description |
|--------|------|------------|-------------|
| `id` | `BIGINT (PK)` | | Auto-generated ID |
| `ticker` | `VARCHAR` | `UNIQUE(ticker, date)` | Stock symbol |
| `date` | `DATE` | `UNIQUE(ticker, date)` | Trading date |
| `open` | `DOUBLE` | | Opening price |
| `high` | `DOUBLE` | | Highest price |
| `low` | `DOUBLE` | | Lowest price |
| `close` | `DOUBLE` | | Closing price |
| `volume` | `BIGINT` | | Trading volume |

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|------|---------|
| Java | 21+ |
| Maven | 3.9+ |
| Node.js | 20+ |
| Python | 3.11+ |
| Docker & Docker Compose | Latest |

### 1. Clone the Repository

```bash
git clone https://github.com/TanmayGarg02/Sentiment-Backtester-Application.git
cd Sentiment-Backtester-Application
```

### 2. Environment Configuration

Create a `.env` file in the project root with the following variables:

| Variable | Required | Where to Get It | Description |
|----------|----------|----------------|-------------|
| `HF_API_KEY` | ✅ | [huggingface.co/settings/tokens](https://huggingface.co/settings/tokens) | Hugging Face API token for FinBERT inference |
| `NEWS_API_KEY` | ✅ | [newsapi.org/register](https://newsapi.org/register) | NewsAPI key for financial news fetching |
| `REDDIT_CLIENT_ID` | ✅ | [reddit.com/prefs/apps](https://www.reddit.com/prefs/apps) | Reddit OAuth app client ID |
| `REDDIT_CLIENT_SECRET` | ✅ | Same as above | Reddit OAuth app client secret |
| `REDDIT_USER_AGENT` | ⬜ | — | Custom user-agent string (default: `nlp-worker/0.1`) |

```env
# Hugging Face
HF_API_KEY=hf_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# NewsAPI
NEWS_API_KEY=your_newsapi_key_here

# Reddit (PRAW)
REDDIT_CLIENT_ID=your_reddit_client_id
REDDIT_CLIENT_SECRET=your_reddit_client_secret
REDDIT_USER_AGENT=nlp-worker/0.1
```

> **Note:** The backend's database, Redis, and RabbitMQ credentials are pre-configured in `backend/src/main/resources/application.yml` and `infra/docker-compose.yml` for local development. Modify these for production deployments.

### 3. Start Infrastructure Services

```bash
docker-compose -f infra/docker-compose.yml up -d
```

This starts PostgreSQL, Redis, and RabbitMQ. Verify services:
- RabbitMQ Management UI: [http://localhost:15672](http://localhost:15672) (guest/guest)
- PostgreSQL: `localhost:5432` (postgres/postgres, db: sentimentdb)
- Redis: `localhost:6379`

### 4. Start the Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

The API will be available at [http://localhost:8080](http://localhost:8080).

### 5. Start the NLP Worker (FastAPI)

```bash
# from project root
pip install -r nlp_worker/requirements.txt
pip install fastapi uvicorn yfinance transformers

uvicorn app:app --reload --port 8000
```

API available at [http://localhost:8000](http://localhost:8000).

### 6. Start the Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

Dashboard available at [http://localhost:5173](http://localhost:5173).

---

## 📡 API Reference

> **Base URLs:** Backend → `http://localhost:8080` | NLP Worker → `http://localhost:8000`

### NLP Worker Endpoints

#### `GET /`
Health check.

```json
{ "message": "NLP Worker API is running 🚀" }
```

#### `GET /sentiment/{ticker}`
Fetch and analyze sentiment for a stock ticker.

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `ticker` | path | — | Stock symbol (e.g., `AAPL`) |
| `limit` | query | `5` | Max articles/posts per source |
| `days` | query | `30` | Lookback period in days |
| `threshold` | query | `0.70` | Min confidence score (0.0–1.0) |

**Response:**
```json
{
  "ticker": "AAPL",
  "score": 0.9234,
  "label": "positive",
  "source": "news",
  "url": "https://example.com/article",
  "date": "2026-05-19T10:30:00"
}
```

### Backend Endpoints

#### Sentiment API

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/sentiment/{ticker}` | Get sentiment (cache → Python → DB fallback) |
| `GET` | `/api/sentiment/{ticker}/refresh` | Force refresh from Python NLP worker |
| `GET` | `/api/sentiment/{ticker}/latest` | Get latest raw DB record |
| `GET` | `/api/sentiment/{ticker}/history?start=...&end=...` | Get sentiment history in date range |
| `POST` | `/api/sentiment` | Save a single sentiment record |
| `POST` | `/api/sentiment/batch` | Batch save sentiment records |
| `DELETE` | `/api/sentiment/{id}` | Delete a sentiment record |

#### Backtest API

#### `POST /api/backtest/run`
Execute a strategy backtest.

**Request Body:**
```json
{
  "ticker": "AAPL",
  "strategyType": "SENTIMENT",
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "parameters": {
    "sentimentThreshold": 0.5
  }
}
```

**Response:**
```json
{
  "ticker": "AAPL",
  "strategyType": "SENTIMENT",
  "parameters": { "sentimentThreshold": 0.5 },
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "totalReturn": 0.0423,
  "sharpeRatio": 1.87,
  "maxDrawdown": -0.032,
  "avgDailyReturn": 0.00012,
  "days": 365,
  "createdAt": "2026-05-19T10:45:00"
}
```

#### Available Strategy Types

| Strategy | Key Parameters | Description |
|----------|---------------|-------------|
| `SENTIMENT` | `sentimentThreshold` | Long/short based on sentiment score vs threshold |
| `MEAN_REVERSION` | `lookback`, `entryZ` | Trade against z-score deviations from mean |
| `MOMENTUM` | `lookback` | Follow price momentum signals |
| `RSI` | `period`, `oversold`, `overbought` | Trade based on RSI overbought/oversold zones |

#### Backtest History & Query Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/backtest/history` | Get all saved backtest results |
| `GET` | `/api/backtest/strategy/{type}` | Filter results by strategy type |
| `GET` | `/api/backtest/strategy/{type}/ticker/{ticker}` | Filter by strategy + ticker |
| `POST` | `/api/backtest/result` | Save a single backtest result |
| `POST` | `/api/backtest/results/batch` | Batch save backtest results |

#### Health & Observability

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status with component details |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Runtime metrics (JVM, HTTP, cache stats) |

---

## 📁 Project Structure

```
sentiment-backtester/
├── app.py                          # FastAPI entry point (NLP Worker API)
├── nlp_worker/                     # Python NLP microservice
│   ├── main.py                     # CLI entry point for testing
│   ├── config.py                   # Environment configuration
│   ├── requirements.txt            # Python dependencies
│   ├── apis/
│   │   ├── news_client.py          # NewsAPI integration
│   │   └── reddit_client.py        # Reddit/PRAW integration
│   └── utils/
│       ├── hf_client.py            # Hugging Face Inference API client
│       └── sentiment_utils.py      # Batch sentiment processing & parsing
│
├── backend/                        # Spring Boot backend
│   ├── pom.xml                     # Maven dependencies
│   └── src/main/
│       ├── java/com/example/backend/
│       │   ├── controller/         # REST controllers
│       │   │   ├── SentimentController.java
│       │   │   ├── BacktestController.java
│       │   │   ├── MarketDataController.java
│       │   │   └── IngestionController.java
│       │   ├── service/            # Business logic
│       │   │   ├── BacktestService.java
│       │   │   ├── SentimentService.java
│       │   │   ├── MarketDataService.java
│       │   │   ├── IngestionService.java
│       │   │   └── MessagingService.java
│       │   ├── model/              # JPA entities
│       │   ├── dto/                # Request/Response DTOs
│       │   ├── repository/         # Spring Data repositories
│       │   ├── cache/              # Redis caching service
│       │   ├── config/             # WebClient, CORS config
│       │   ├── messaging/          # RabbitMQ listener & config
│       │   └── util/               # Sentiment calculation helpers
│       └── resources/
│           ├── application.yml     # Spring Boot configuration
│           └── db/migration/       # Flyway SQL migrations
│
├── frontend/                       # React SPA
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── App.jsx                 # Router setup
│       ├── main.jsx                # React entry point
│       ├── components/             # Reusable UI components
│       │   ├── Navbar.jsx
│       │   ├── BacktestForm.jsx
│       │   ├── BacktestResultCard.jsx
│       │   ├── HistoryTable.jsx
│       │   ├── SearchBar.jsx
│       │   ├── SearchResult.jsx
│       │   └── Loader.jsx
│       ├── pages/                  # Route pages
│       │   ├── Home.jsx
│       │   ├── Sentiment.jsx
│       │   ├── Backtest.jsx
│       │   └── History.jsx
│       ├── redux/                  # State management
│       │   ├── store.js
│       │   ├── backtestSlice.js
│       │   └── sentimentSlice.js
│       └── api/                    # API client layer
│
└── infra/
    └── docker-compose.yml          # PostgreSQL, Redis, RabbitMQ
```

---

## 🗺 Roadmap

### 🔜 Coming Soon

#### 🧠 RAG (Retrieval-Augmented Generation) Pipeline
- **Vector Store Integration** — Embed and index historical financial articles, SEC filings, and earnings transcripts using pgvector or Pinecone
- **Context-Aware Sentiment** — Augment FinBERT predictions with retrieved context from similar historical events for more nuanced sentiment scoring
- **LLM-Powered Summarization** — Use GPT-4 / Claude to generate human-readable market sentiment summaries grounded in retrieved documents
- **Semantic Search** — Enable natural language queries like _"How did AAPL react to supply chain disruptions?"_ across the entire document corpus

#### 🤖 Multi-Agent System (Agentic AI)
- **Orchestrator Agent** — Central coordinator that decomposes complex trading research tasks into sub-tasks and delegates to specialized agents
- **Sentiment Agent** — Dedicated agent for real-time sentiment monitoring, alerting, and trend detection across multiple tickers
- **Research Agent** — Autonomous agent that scrapes, summarizes, and synthesizes financial research from SEC EDGAR, analyst reports, and macro data
- **Strategy Agent** — AI agent that proposes, tunes, and evaluates backtesting parameters based on market regime detection
- **Risk Agent** — Monitors portfolio risk metrics in real-time, flags anomalies, and suggests position sizing adjustments
- **Agent Communication Protocol** — LangGraph-based agent orchestration with shared memory and tool-use capabilities

#### 📊 Enhanced Backtesting Engine
- **Live Market Data Integration** — Replace synthetic data with real OHLCV feeds from Yahoo Finance / Alpha Vantage / Polygon.io
- **Walk-Forward Optimization** — Rolling window strategy optimization to prevent overfitting
- **Monte Carlo Simulation** — Statistical validation of strategy robustness through randomized scenario testing
- **Multi-Asset Backtesting** — Test strategies across portfolios of correlated assets simultaneously
- **Transaction Cost Modeling** — Realistic slippage, commission, and market impact simulation

#### 🔗 LangChain Integration
- **Tool-Augmented LLM** — LangChain agent with access to backtesting, sentiment analysis, and market data tools
- **Conversational Interface** — Natural language interface for running backtests: _"Backtest momentum strategy on TSLA for the last 6 months with a 10-day fast MA"_
- **Chain-of-Thought Reasoning** — Transparent, step-by-step reasoning for strategy recommendations
- **Memory & Context** — Persistent conversation memory that builds a user's research history and preferences

### 🔮 Future Vision

#### 📡 Real-Time Streaming
- WebSocket-based live sentiment feeds with push notifications
- Kafka integration for high-throughput event streaming
- Real-time equity curve updates during live trading sessions

#### 🧪 MLOps & Model Management
- Custom FinBERT fine-tuning pipeline on domain-specific financial data
- Model versioning and A/B testing for sentiment classifiers
- Drift detection and automated model retraining triggers

#### 🔐 Production Hardening
- JWT authentication with role-based access control
- Rate limiting and API key management
- Kubernetes deployment manifests with horizontal pod autoscaling
- Comprehensive observability stack (Prometheus + Grafana + distributed tracing)

#### 📱 Platform Expansion
- Mobile-responsive progressive web app (PWA)
- Telegram / Slack bot for sentiment alerts
- Webhook integrations with brokerage APIs for semi-automated trading

---

## 🐛 Troubleshooting

<details>
<summary><strong>Hugging Face API returns 503 (Model Loading)</strong></summary>

FinBERT needs to be loaded into memory on the first request. The HF Inference API may return a 503 status with an estimated load time. **Wait 20–30 seconds and retry.** The model stays warm for subsequent requests.
</details>

<details>
<summary><strong>Reddit API returns empty results</strong></summary>

Ensure your Reddit app is set to **"script"** type (not "web" or "installed"). Verify credentials in `.env`. Reddit rate limits to ~60 requests/minute — the app handles this gracefully by returning empty results.
</details>

<details>
<summary><strong>Backend fails to start — PostgreSQL connection refused</strong></summary>

Make sure Docker containers are running: `docker ps`. If PostgreSQL isn't listed, run `docker-compose -f infra/docker-compose.yml up -d`. Verify the port isn't occupied by another Postgres instance: `lsof -i :5432`.
</details>

<details>
<summary><strong>CORS errors in browser console</strong></summary>

The backend includes a `WebConfig` for CORS. Ensure the frontend dev server URL (`http://localhost:5173`) is allowed. Check `backend/src/main/java/com/example/backend/config/WebConfig.java`.
</details>

<details>
<summary><strong>Redis connection refused</strong></summary>

Redis must be running via Docker. Verify with `docker ps | grep redis`. If using a custom Redis port, update `application.yml` under `spring.data.redis.port`.
</details>

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- **Backend**: Follow Spring Boot conventions. Use Lombok annotations. Add Flyway migrations for schema changes.
- **NLP Worker**: Keep API endpoints thin; business logic goes in `utils/`. Truncate inputs to 500 chars for FinBERT.
- **Frontend**: Use Redux Toolkit for all async state. Component naming follows `PascalCase`. Pages go in `pages/`, reusable elements in `components/`.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">

**Built with ☕ and 🐍 by [Tanmay Garg](https://github.com/TanmayGarg02)**

*If this project helped you, consider giving it a ⭐*

</div>
