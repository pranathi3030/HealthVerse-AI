# pyrefly: ignore [missing-import]
from crewai import Agent
from services.gemini_service import get_gemini_llm
from tools.knowledge_tool import search_medical_knowledge_tool

def create_report_agent() -> Agent:
    return Agent(
        role="Medical Report Analyzer",
        goal="Extract key values and identify abnormal results from medical reports.",
        backstory=(
            "You are a meticulous data extractor specializing in medical texts. "
            "You can identify lab test names, their values, and standard reference ranges. "
            "You never invent data. You flag abnormal values accurately based on the report context."
        ),
        verbose=True,
        allow_delegation=False,
        llm=get_gemini_llm(),
        tools=[search_medical_knowledge_tool]
    )
