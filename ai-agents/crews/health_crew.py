from crewai import Crew, Process
from agents.health_agent import create_health_agent
from tasks.health_tasks import analyze_symptoms_task

def run_health_crew(context_data: dict) -> dict:
    agent = create_health_agent()
    task = analyze_symptoms_task(agent, context_data)
    
    crew = Crew(
        agents=[agent],
        tasks=[task],
        process=Process.sequential,
        verbose=True
    )
    
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result
