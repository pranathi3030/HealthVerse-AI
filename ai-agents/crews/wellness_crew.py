from crewai import Crew, Process
from agents.nutrition_agent import create_nutrition_agent
from agents.fitness_agent import create_fitness_agent
from agents.wellness_agent import create_wellness_agent
from tasks.wellness_tasks import generate_nutrition_plan_task, generate_fitness_plan_task, generate_wellness_plan_task

def run_nutrition_crew(context_data: dict) -> dict:
    agent = create_nutrition_agent()
    task = generate_nutrition_plan_task(agent, context_data)
    crew = Crew(agents=[agent], tasks=[task], process=Process.sequential, verbose=True)
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result

def run_fitness_crew(context_data: dict) -> dict:
    agent = create_fitness_agent()
    task = generate_fitness_plan_task(agent, context_data)
    crew = Crew(agents=[agent], tasks=[task], process=Process.sequential, verbose=True)
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result

def run_wellness_crew(context_data: dict) -> dict:
    agent = create_wellness_agent()
    task = generate_wellness_plan_task(agent, context_data)
    crew = Crew(agents=[agent], tasks=[task], process=Process.sequential, verbose=True)
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result

def run_predictive_wellness_crew(history: list) -> dict:
    from tasks.wellness_tasks import predict_wellness_task
    from models.schemas import PredictiveWellnessResponse
    agent = create_wellness_agent()
    task = predict_wellness_task(agent, history)
    crew = Crew(agents=[agent], tasks=[task], process=Process.sequential, verbose=True)
    result = crew.kickoff()
    return result.json_dict if hasattr(result, 'json_dict') else result
