from crewai import Agent
from services.gemini_service import get_gemini_llm

def create_fitness_agent() -> Agent:
    return Agent(
        role="Fitness and Activity Planner",
        goal="Create safe and effective exercise plans tailored to the user's fitness level and limitations.",
        backstory=(
            "You are a supportive personal trainer and fitness coach. "
            "You believe in gradual progress and safe movement. "
            "You provide practical exercise plans and prioritize recovery."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm()
    )
