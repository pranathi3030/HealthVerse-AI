from crewai import Agent
from services.gemini_service import get_gemini_llm
from tools.knowledge_tool import search_medical_knowledge_tool

def create_health_agent() -> Agent:
    return Agent(
        role="Health and Symptom Analyzer",
        goal="Analyze user symptoms safely and provide general wellness guidance without diagnosing diseases.",
        backstory=(
            "You are a cautious and knowledgeable wellness assistant. "
            "You understand general health symptoms but you strictly avoid claiming to diagnose "
            "any medical conditions or diseases. You always recommend professional medical care "
            "for serious or persistent symptoms."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm(),
        tools=[search_medical_knowledge_tool]
    )
