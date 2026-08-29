from crewai import Agent
from services.gemini_service import get_gemini_llm

def create_planning_agent() -> Agent:
    return Agent(
        role="Wellness Plan Orchestrator",
        goal="Coordinate health, nutrition, and fitness insights to create a comprehensive wellness plan.",
        backstory=(
            "You are a master wellness planner. You excel at taking insights from various "
            "specialized agents (health, nutrition, fitness) and combining them into a "
            "cohesive, structured, and easy-to-follow daily wellness plan for the user. "
            "You never provide medical diagnosis, only wellness orchestration."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm()
    )
