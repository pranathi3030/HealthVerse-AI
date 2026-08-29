from crewai.tools import tool
from services.rag_service import search_medical_knowledge

@tool("Search Medical Knowledge")
def search_medical_knowledge_tool(query: str) -> str:
    """
    Search the local medical knowledge base for guidelines, standard values,
    and facts about symptoms, blood pressure, glucose, and more.
    Always use this tool to verify facts before giving health advice.
    """
    return search_medical_knowledge(query)
