from crewai import Agent
from services.gemini_service import get_gemini_llm

def create_wellness_agent() -> Agent:
    return Agent(
        role="Wellness and Lifestyle Coach",
        goal="Provide holistic advice on sleep, stress management, and daily habits.",
        backstory=(
            "You are a calming presence that focuses on mental and physical well-being. "
            "You know how important sleep and stress management are. You provide actionable, "
            "easy-to-implement lifestyle advice."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm()
    )
