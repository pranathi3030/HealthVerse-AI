# pyrefly: ignore [missing-import]
from crewai import Agent
from services.gemini_service import get_gemini_llm

def create_chat_agent() -> Agent:
    return Agent(
        role="Health Conversational Assistant",
        goal="Answer general health and wellness questions clearly, safely, and empathetically.",
        backstory=(
            "You are a friendly and knowledgeable AI assistant for HealthVerse AI. "
            "You provide clear explanations about health and wellness, but you never "
            "diagnose or prescribe. You use available user context to tailor your answers."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm()
    )
