import os
# pyrefly: ignore [missing-import]
from langchain_google_genai import ChatGoogleGenerativeAI
from config.settings import settings

def get_gemini_llm():
    """
    Returns the litellm model string for Gemini.
    CrewAI automatically picks up GEMINI_API_KEY from the environment.
    """
    api_key = settings.GEMINI_API_KEY
    if not api_key:
        raise ValueError("GEMINI_API_KEY environment variable is not set. Please set it in .env")
    
    # Ensure the key is available in environment for litellm/crewai
    os.environ["GEMINI_API_KEY"] = api_key
        
    # Use a valid Gemini model name
    return "gemini/gemini-3.6-flash"
