# pyrefly: ignore [missing-import]
from crewai import Crew, Process
from agents.chat_agent import create_chat_agent
from tasks.chat_tasks import chat_task

def run_chat_crew(query: str, context: dict = None) -> dict:
    agent = create_chat_agent()
    task = chat_task(agent, query, context)
    crew = Crew(agents=[agent], tasks=[task], process=Process.sequential, verbose=True)
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result
