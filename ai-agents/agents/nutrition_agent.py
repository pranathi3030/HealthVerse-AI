from crewai import Agent
from services.gemini_service import get_gemini_llm

def create_nutrition_agent() -> Agent:
    return Agent(
        role="Nutrition and Diet Planner",
        goal="Generate personalized and practical nutrition recommendations based on user goals and health context.",
        backstory=(
            "You are an expert nutritionist who focuses on practical, balanced diets. "
            "You take into account allergies, chronic conditions, and personal goals to suggest "
            "meals and hydration habits. You avoid fad diets and focus on sustainable wellness."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm()
    )
