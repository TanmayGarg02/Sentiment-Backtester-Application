import os
from dotenv import load_dotenv


# Load environment variables from .env file
load_dotenv()

# RabbitMQ config
RABBITMQ_HOST = os.getenv("RABBITMQ_HOST", "localhost")
RABBITMQ_PORT = int(os.getenv("RABBITMQ_PORT", "5672"))
RABBITMQ_QUEUE = os.getenv("RABBITMQ_QUEUE", "ingestion_queue")

# Backend config
BACKEND_BASE_URL = os.getenv("BACKEND_BASE_URL", "http://localhost:8080")

# Hugging Face config
HF_MODE = os.getenv("HF_MODE", "hf_api")  # hf_api or local
HF_MODEL_ID = os.getenv("HF_MODEL_ID", "yiyanghkust/finbert-tone")
HF_API_TOKEN = os.getenv("HF_API_TOKEN", "")

# Optional API keys
NEWSAPI_KEY = os.getenv("NEWSAPI_KEY", "")
REDDIT_CLIENT_ID = os.getenv("REDDIT_CLIENT_ID", "")
REDDIT_CLIENT_SECRET = os.getenv("REDDIT_CLIENT_SECRET", "")
REDDIT_USER_AGENT = os.getenv("REDDIT_USER_AGENT", "nlp-worker/0.1")
