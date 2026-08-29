from crewai import Crew, Process
from agents.report_agent import create_report_agent
from agents.health_agent import create_health_agent
from agents.wellness_agent import create_wellness_agent
from tasks.report_tasks import extract_report_data_task, interpret_health_context_task, generate_report_wellness_task

def run_report_crew(report_text: str) -> dict:
    report_agent = create_report_agent()
    health_agent = create_health_agent()
    wellness_agent = create_wellness_agent()
    
    extract_task = extract_report_data_task(report_agent, report_text)
    interpret_task = interpret_health_context_task(health_agent)
    wellness_task = generate_report_wellness_task(wellness_agent)
    
    crew = Crew(
        agents=[report_agent, health_agent, wellness_agent],
        tasks=[extract_task, interpret_task, wellness_task],
        process=Process.sequential,
        verbose=True
    )
    
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result
