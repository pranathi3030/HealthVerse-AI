from crewai import Crew, Process
from agents.planning_agent import create_planning_agent
from agents.health_agent import create_health_agent
from agents.nutrition_agent import create_nutrition_agent
from agents.fitness_agent import create_fitness_agent
from tasks.planning_tasks import orchestrate_wellness_plan_task

def run_planning_crew(goal: str, context_data: dict) -> dict:
    planner = create_planning_agent()
    health_expert = create_health_agent()
    nutrition_expert = create_nutrition_agent()
    fitness_expert = create_fitness_agent()
    
    plan_task = orchestrate_wellness_plan_task(planner, goal, context_data)
    
    crew = Crew(
        agents=[planner, health_expert, nutrition_expert, fitness_expert],
        tasks=[plan_task],
        process=Process.sequential,
        verbose=True
    )
    
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result

def run_orchestration(query: str, context_data: dict) -> dict:
    from agents.wellness_agent import create_wellness_agent
    from agents.report_agent import create_report_agent
    from tasks.planning_tasks import orchestrate_query_task
    from services.gemini_service import get_gemini_llm

    crew = Crew(
        agents=[
            create_health_agent(),
            create_nutrition_agent(),
            create_fitness_agent(),
            create_wellness_agent(),
            create_report_agent()
        ],
        tasks=[orchestrate_query_task(query, context_data)],
        process=Process.hierarchical,
        manager_llm=get_gemini_llm(),
        verbose=True
    )
    
    result = crew.kickoff()
    return {
        "response": result.raw if hasattr(result, 'raw') else str(result),
        "agents_involved": ["Planning Agent (Manager)", "Health Agent", "Nutrition Agent", "Fitness Agent", "Wellness Agent", "Report Agent"]
    }
