import os
from huggingface_hub import InferenceClient
from transformers import pipeline
from nlp_worker import sentiment_utils
from .. import config

class HFClient:
    def __init__(self):
        self.mode = config.HF_MODE
        self.model_id = config.HF_MODEL_ID
        self.api_token = config.HF_API_TOKEN

        if self.mode == "hf_api":
            self.client = InferenceClient(model=self.model_id, token=self.api_token)
        elif self.mode == "local":
            self.pipeline = pipeline("sentiment-analysis", model=self.model_id)
        else:
            raise ValueError(f"Unsupported HF_MODE: {self.mode}")

    def get_sentiment(self, text: str):
        if self.mode == "hf_api":
            result = self.client.text_classification(text)
        else:
            result = self.pipeline(text)

        # Result example: [{'label': 'positive', 'score': 0.97}]
        return sentiment_utils.parse_result(result[0])
