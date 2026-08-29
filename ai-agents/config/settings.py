from pydantic_settings import BaseSettings, SettingsConfigDict
import os
from pathlib import Path
from dotenv import load_dotenv

# Resolve the absolute path to the project root (ai-agents) and the .env file
BASE_DIR = Path(__file__).resolve().parent.parent
ENV_PATH = BASE_DIR / ".env"

# Explicitly load into os.environ for other libraries (like CrewAI/litellm)
load_dotenv(dotenv_path=ENV_PATH)

class Settings(BaseSettings):
    # Pydantic will automatically map variables from the env file or os.environ
    GEMINI_API_KEY: str = ""
    PORT: int = 8000
    HOST: str = "0.0.0.0"

    model_config = SettingsConfigDict(
        env_file=str(ENV_PATH),
        env_file_encoding="utf-8",
        extra="ignore"
    )

settings = Settings()
